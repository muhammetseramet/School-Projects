module vga_sync
(
input wire  clk,reset, 
output wire hsync, vsync, video_on, p_tick,
output wire [9:0] pixel_x,pixel_y
);

// constant declaration, The values of various regions of the horizontal 
// and vertical scans. They can easily be modified if a different
// resolution or refresh rate is used.
// VGA 640-by-480 sync parameters

// => A period of the hsync signal contains 800 pixels
localparam HD = 640; // horizontal display area, The length of this region is 640 pixels
localparam HF = 48 ; // horizontal front(left) border, region that forms the left border of the display region. 
							//It is also know as the backporch (i.e., porch after retrace).
localparam HB = 16 ; // horizontal back (right) border, region that forms the right border of the display region.
							//It is also know as the front porch (i.e., porch before retrace)
localparam HR = 96 ; // horizontal retrace, region in which the electron beams return to the left edge

//A period of the vsync signal is 525 lines
localparam VD = 480; // vertical display area, The length of this region is 480 lines
localparam VF = 10;  // vertical front (top) border, region that forms the bottom border of the display region. 
							// It is also know as the frontporch (i.e., porch before retrace)
localparam VB = 33;  // vertical back (bottom) border,region that forms the top border of the display region. 
							//It is also know as the backporch (i.e., porch afier retrace).
localparam VR = 2;   // vertical retrace, region that the electron beams return to the top of the screen

// mod-2 counter, to generate the 25 MHz enable tick and two counters for 
//the horizontal and vertical scans
reg mod2_reg;
wire mod2_next;

// sync counters, To remove potential glitches, output buffers are 
// inserted for the hsync and vsync signals
// This leads to a one-clock-cycle delay. We should add a similar buffer 
// for the rgb signal in the pixel generation circuit to compensate for the delay.
reg [9:0] h_count_reg, h_count_next;
reg [9:0] v_count_reg, v_count_next;

//output buffer
reg v_sync_reg, h_sync_reg;
wire v_sync_next, h_sync_next;

// status signal, to indicate completion of the horizontal and vertical scans
wire h_end, v_end, pixel_trick;

// body
// registers
always @(posedge clk, posedge reset)
	if(reset) 
		begin
			mod2_reg    <= 1'b0;
			v_count_reg <= 0;
			h_count_reg <= 0;
			v_sync_reg  <= 1'b0;
			h_sync_reg  <= 1'b0;
		end
	
	else 
		begin
			mod2_reg    <= mod2_next;
			v_count_reg <= v_count_next;
			h_count_reg <= h_count_next;
			v_sync_reg  <= v_sync_next;
			h_sync_reg  <= h_sync_next;
		end

// mod-2 circuit to generate 25 MHz enable tick
// => if the system clock is 25 MHz, the circuit can be implemented by 
// => two special counters:
assign mod2_next   = ~mod2_reg;
assign pixel_trick =  mod2_reg;

// end of horizontal counter (799)
assign h_end = (h_count_reg == (HD + HF + HB + HR - 1));
// end of vertical counter (524)
assign v_end = (v_count_reg == (VD + VF + VB + VR - 1));

// next-state logic of mod-800 horizontal sync counter
// => a mod-800 counter to keep track of the horizontal scan and
always @* 
	if(pixel_trick)
		if(h_end)
			h_count_next = 0;
		else
			h_count_next = h_count_reg + 1;
	else
		h_count_next = h_count_reg;

// next-state logic of mod-525 vertical sync counter
// => a mod-525 counter to keep track of the vertical scan 
always @* 
	if(pixel_trick & h_end)
		if(v_end) 
			v_count_next = 0;
		else
			v_count_next = v_count_reg + 1;
	else 
		v_count_next = v_count_reg;

// h_sync_next asserted between 656 and 751 
assign h_sync_next = (h_count_reg >= (HD + HB) && h_count_reg <= (HD + HB + HR -1));
// v_sync_next asserted between 490 and 491
assign v_sync_next = (v_count_reg >= (VD + VB) && v_count_reg <= ( VD + VB + VR -1));

assign video_on = (h_count_reg < HD) && ( v_count_reg < VD);

assign hsync   = h_sync_reg;
assign vsync   = v_sync_reg;
assign pixel_x = h_count_reg;
assign pixel_y = v_count_reg;
assign p_tick  = pixel_trick;

endmodule