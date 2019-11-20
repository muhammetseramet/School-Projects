module ram#(
	parameter addr_width = 12,
	parameter data_width = 16
)(
	input [11:0] address,
	input [15:0] data_input,
	input mem_store,
	input mem_load,
	output reg[15:0] out
);
endmodule