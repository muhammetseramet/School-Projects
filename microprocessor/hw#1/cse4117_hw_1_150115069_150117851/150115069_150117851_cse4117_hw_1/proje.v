module proje( button, clk, grounds, display);

input wire clk;
input wire button;
output wire [3:0] grounds;
output wire [6:0] display;
wire [15:0] dout;

cpu cpu1(
	.clk(button),
	.dout(dout)
	);

seven_seg seven(
	.din(dout),
	.grounds(grounds),
	.display(display),
	.clk(clk)
	);

endmodule 
