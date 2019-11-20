`timescale 1ns/1ns
module IrLogic(
	input irload,
	input clk,
	input [15:0]instruction,
	output reg [7:0]address,
	output reg [3:0]treg,
	output reg [3:0]sreg,
	output reg [3:0]dreg,
	output reg [3:0]opcode
);

reg [15:0]Ir;

initial begin
	Ir <= instruction;
end

always @(posedge clk, irload) begin
	if(irload) begin
		opcode = Ir[15:12];
		address = Ir[7:0];
		dreg = Ir[11:8];
		sreg = Ir[7:4];
		treg = Ir[3:0];
	end
end
endmodule