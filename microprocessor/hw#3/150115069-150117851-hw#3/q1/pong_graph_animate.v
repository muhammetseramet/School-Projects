// -------------------------------------------------------------------
// => a color value is obtained according to the current coordinate of 
// the pixel
// -------------------------------------------------------------------

module pong_graph_animate
(
input wire clk, reset,
input wire video_on,
input wire [1:0] btn,
input wire [9:0] pix_x, pix_y,
output reg [2:0] graph_rgb
);


reg [25:0] clk1;
reg [4:0] counter;
reg resetcik;
always @(posedge clk)
	clk1 <= clk1 + 1;
	
	
always @(posedge clk1[25])
begin
	if (counter == 5'b11000)
	begin
		counter<=5'b00001;
	end
	counter <= counter + 1;
end

localparam MAX_X = 640;
localparam MAX_Y = 480;
wire refr_tick;

// -----------------------------------------------------------
// vertical stripe as a wall bunu siktir etcez
// -----------------------------------------------------------
// wall left, right boundary
// a four-pixel-wide vertical stripe between columns 32 and 35
// WALL_X_L, and WALL_X_R, representing the left and right x-coordinates of the wall
// The object has two output signals, wall-on and wall-rgb
localparam WALL_X_L = 0;
localparam WALL_X_R = 1;

// -----------------------------------------------------------
// right vertical bar
// -----------------------------------------------------------
// bar left, right boundary
localparam BAR_X_L = 600;
localparam BAR_X_R = 603;

// bar top, bottom boundary
wire [9:0] bar_y_t, bar_y_b;
//vertical length of the bar is 72 pixels
localparam BAR_Y_SIZE = 72;

// register to track top boundary (x position is fixed)
reg [9:0] bar_y_reg, bar_y_next;

// bar moving velocity when a button is pressed
localparam BAR_V = 4;

// -----------------------------------------------------------
// square ball
// -----------------------------------------------------------
localparam BALL_SIZE = 8;

// ball left, right boundary
wire [9:0] ball_x_l, ball_x_r;

// ball top, bottom boundary
wire [9:0] ball_y_t, ball_y_b;

// reg to track left, top position
reg [9:0] ball_x_reg, ball_y_reg;
wire [9:0] ball_x_next, ball_y_next;

// reg to track ball speed
reg [9:0] x_delta_reg, x_delta_next;
reg [9:0] y_delta_reg, y_delta_next;

// ball velocity can be pos or neg
parameter BALL_V_P = 1;
parameter BALL_V_N = -1;
localparam ssp2 = BALL_V_P*2;
localparam ssn2 = BALL_V_N*2;

// -----------------------------------------------------------
// round ball
// -----------------------------------------------------------
// include a pattern ROM to store the bit map and an address mapping 
// circuit to convert the scan coordinates to the ROM's row and column.
wire [2:0] rom_addr, rom_col;
reg [7:0] rom_data;
wire rom_bit;

// -----------------------------------------------------------
// object output signals
// -----------------------------------------------------------
wire wall_on, bar_on, sq_ball_on, rd_ball_on;
wire [2:0] wall_rgb, bar_rgb, ball_rgb;

// body
// -----------------------------------------------------------
// round ball image ROM
// -----------------------------------------------------------
always @*
case(rom_addr)
	3'h0: rom_data = 8'b00111100; //   ****
	3'h1: rom_data = 8'b01111110; //  ******
	3'h2: rom_data = 8'b11111111; // ********
	3'h3: rom_data = 8'b11111111; // ********
	3'h4: rom_data = 8'b11111111; // ********
	3'h5: rom_data = 8'b11111111; // ********
	3'h6: rom_data = 8'b01111110; //  ******
	3'h7: rom_data = 8'b00111100; //   ****
endcase

//registers
always @(posedge clk, posedge reset)
	if(reset)
		begin
			bar_y_reg <= 10'h0f0;
			ball_x_reg <= 10'h210;
			ball_y_reg <= 10'h0f0;
			x_delta_reg <= 10'h001;
			y_delta_reg <= 10'h001;
		end
	else if (resetcik)
		begin
			bar_y_reg <= 10'h0f0;
			ball_x_reg <= 10'h160;	//??
			ball_y_reg <= 10'h0f0;
			x_delta_reg <= x_delta_next;
			y_delta_reg <= y_delta_next;
		end
	else
		begin
			bar_y_reg   <= bar_y_next;
			ball_x_reg  <= ball_x_next;
			ball_y_reg  <= ball_y_next;
			x_delta_reg <= x_delta_next;
			y_delta_reg <= y_delta_next;
		end
// refr-tick: 1-clock tick asserted at start of v-sync
// i.e. when the screen is refreshed (60 Hz)
assign refr_tick = (pix_y == 481) && (pix_x==0);

// -----------------------------------------------------------
// (wall) left vertical strip
// -----------------------------------------------------------
// pixel within wall
// The wall-on signal, which indicates that the wall object should be turned on, is asserted when the current horizontal scan is within its region.
//Since the stripe covers the entire vertical column, there is no need for the y-axis boundaries.
assign wall_on = (WALL_X_L <= pix_x) && (pix_x <= WALL_X_R);

// wall rgb output
// The wall-rgb signal indicates that the color of the wall
assign wall_rgb = 3'b000; // blue

// -----------------------------------------------------------
// right vertical bar
// -----------------------------------------------------------
// The bottom boundary of the bar is the top boundary plus the bar length.
assign bar_y_t = bar_y_reg;
assign bar_y_b = bar_y_t + BAR_Y_SIZE - 1;	// burada sıkıntı var 

//pixel within bar
assign bar_on = (BAR_X_L <= pix_x) && (pix_x <= BAR_X_R) &&
				    (bar_y_t <= pix_y) && (pix_y <= bar_y_b);

// bar rgb output
assign bar_rgb = 3'b111; // green

// new bar y-position
// To accommodate the changing y-axis coordinates, 
// we replace the constants with two signals, bar-y-t and bar-y-b, 
// to represent the top and bottom boundaries and create a register, bar-yreg, 
// to store the current yaxis location of the top boundary.
always @*
begin
	bar_y_next = bar_y_reg; //no move
	
	// If one of the pushbuttons is pressed, bar-y-reg either increases or 
	// decreases a fixed amount when the r e f r-tick signal is asserted. 
	// The amount is defined by a constant, BAR-V, which stands for the 
	// bar velocity. We assume that assertion of the btn[1] and btn[0] signals 
	// causes the paddle to move up and down, respectively
	// the paddle stops moving when it reaches the top or the bottom of the screen.
	if(refr_tick)
		if(!btn[1] & (bar_y_b < (MAX_Y - 1 - BAR_V)))			// ???????? buraya hep düşüyor
			bar_y_next = bar_y_reg + BAR_V; // move down
		else if (!btn[0] & (bar_y_t > BAR_V))
			bar_y_next = bar_y_reg - BAR_V; // move up
			
end

// -----------------------------------------------------------
// square ball
// -----------------------------------------------------------
// boundary
// replace the four boundary constants with four signals, and create two 
// registers, ballxxeg and ball-y-reg, to store the current x- and y-axis 
// coordinates ofthe left and top boundaries.
assign ball_x_l = ball_x_reg;
assign ball_y_t = ball_y_reg;
assign ball_x_r = ball_x_l + BALL_SIZE - 1;
assign ball_y_b = ball_y_t + BALL_SIZE - 1;

assign sq_ball_on = (ball_x_l <= pix_x) && (pix_x <= ball_x_r) &&
					     (ball_y_t <= pix_y) && (pix_y <= ball_y_b);

// If the scan coordinates are within the square ball region,
// subtracting the three LSBs from the top boundary (i.e., ball-y-t) provides the corresponding ROM row (i.e., rom-addr),
assign rom_addr = pix_y[2:0] - ball_y_t[2:0];
// subtracting the three LSBs from the left boundary (i.e., ball-x-1) provides the corresponding ROM column (i.e., rom-col).
assign rom_col  = pix_x[2:0] - ball_x_l[2:0]; 
// The final bit is retrieved by an indexing operation.
assign rom_bit  = rom_data[rom_col];

// pixel within ball
assign rd_ball_on = sq_ball_on & rom_bit;

// ball rgb output
assign ball_rgb = 3'b111; // red

// new ball position 
assign ball_x_next = (refr_tick) ? ball_x_reg+x_delta_reg : ball_x_reg;
assign ball_y_next = (refr_tick) ? ball_y_reg+y_delta_reg : ball_y_reg;

// new ball velocity
// The ball usually moves at a constant velocity 
// (i.e., at a constant speed and in the same direction). 
// It may change direction when hitting the wall, the paddle, 
// or the bottom or top of the screen.
always @*
begin
	// decompose the velocity into an x-component and a y-component
	// whose values can be either a positive constant value, 
	// BALL-V-P, or a negative constant value, BALL-V-N.
	x_delta_next = x_delta_reg;
	resetcik = 1'b0;
	
	y_delta_next = y_delta_reg;

	if(ball_y_t < 1) //reach top
		y_delta_next = BALL_V_P;
	
	else if (ball_y_b > (MAX_Y - 1)) //reach bottom
		y_delta_next = BALL_V_N;
	
	
	else if ((BAR_X_L <= ball_x_r) && (ball_x_r <= BAR_X_R) &&
			   (bar_y_t <= ball_y_b) && (ball_y_t <= bar_y_b))
		x_delta_next = BALL_V_N;
	else if (ball_x_l<=1)
		x_delta_next = BALL_V_P;
	else if (ball_x_l >= MAX_X)
	begin
		resetcik = 1'b1;
		if (x_delta_reg >= 0)
		begin
			if (y_delta_reg >= 0)
				y_delta_next = BALL_V_N;
			else
				x_delta_next = BALL_V_N;
		end
		else
		begin
			if (y_delta_reg >= 0)
				x_delta_next = BALL_V_P;
			else
				y_delta_next = BALL_V_P;
		end	
	end
			// ????????? wtf
	if (counter == 5'b10111)
	begin
		if (x_delta_next > 0)
		begin
			if (y_delta_next > 0)
			begin
				x_delta_next = x_delta_next + 1; // + ssp2;balvp
				y_delta_next = y_delta_next + 1;
			end
			else
			begin
				x_delta_next = x_delta_next + 1;
				y_delta_next = y_delta_next - 1;//+ ssn2;balvn
			end
		end
		else
		begin
			if (y_delta_next > 0)
			begin
				x_delta_next = x_delta_next - 1;
				y_delta_next = y_delta_next + 1;
			end
			else
			begin
				x_delta_next = x_delta_next -1;
				y_delta_next = y_delta_next -1;
			end
		end
	end
end // if the paddle bar misses the ball, the ball continues moving to the right 
	 // and eventually wraps around.

// -----------------------------------------------------------
// rgb multiplexing circuit
// -----------------------------------------------------------
// examines the on signals of three objects and routes the corresponding rgb signal to output
always @*
	if(~video_on) // first checks whether the video-on is asserted
		graph_rgb = 3'b000; //blank
	// if this is the case, examines the three on signals in turn, 
	// an on signal is asserted, it indicates that the scan is within 
	// its region, and the corresponding rgb signal is passed to the output.
	else 
		if(wall_on)
			graph_rgb = wall_rgb;
		
		else if(bar_on)
			graph_rgb = bar_rgb;
		
		else if(rd_ball_on)
			graph_rgb = ball_rgb;
		
		// If no signal is asserted, the scan is in the "background" and
		//	the output is assigned to be " 1 10" (yellow).
		else
			graph_rgb = 3'b000; // yellow background

endmodule