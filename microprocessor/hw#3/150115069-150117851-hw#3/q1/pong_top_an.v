module pong_top_an
(
input wire clk,reset,
input wire [1:0] btn,
output wire hsync, vsync,
output wire [2:0] rgb
);

// signal decleration
wire [9:0] pixel_x, pixel_y;
wire video_on, pixel_trick;
reg [2:0] rgb_reg;
wire [2:0] rgb_next;

// body
// instantiate vga sync circuit
vga_sync vsync_unit(.clk(clk), .reset(reset), .hsync(hsync), .vsync(vsync), 
						  .video_on(video_on), .p_tick(pixel_trick),
						  .pixel_x(pixel_x), .pixel_y(pixel_y));

// instantiate graphic generator
pong_graph_animate pong_graph_an_unit(.clk(clk), .reset(reset), .btn(btn),
												  .video_on(video_on),.pix_x(pixel_x),
												  .pix_y(pixel_y), .graph_rgb(rgb_next));
// hsync and vsnyc have one-clock delay.
// add a buffer for the rgb signal in the pixel generation circuit to compensate for the delay.

//rgb buffer
always @(posedge clk)
	if(pixel_trick)
		rgb_reg <= rgb_next;

//output
assign rgb = rgb_reg;

endmodule
