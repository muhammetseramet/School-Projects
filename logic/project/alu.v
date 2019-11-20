`timescale 1ns/1ns
module Alu(
	input [15:0]src1,
	input [15:0]src2,
	input [3:0]imm,
	input alu_slc,alu_imm,
	output reg [15:0]out
);

reg [15:0] extended;	
always @(src1,src2) begin		
	if(alu_imm)
		assign extended = { {12{imm[3]}}, imm[3:0] };
	else
		assign extended = src2;
			
	if (alu_slc)
		out = src1 & extended;
	else
		out = src1 + extended;
end
endmodule	