`timescale 1ns/1ns
module ControlUnit(
	input [3:0] opcode,
	output reg aluSlc,
	output reg aluImm,
	output reg memLoad,
	output reg memSt,
	output reg compare,
	output reg regWe,
	output reg jump,
	output reg jmpRn,
	output reg jumpB,
	output reg jumpBE,
	output reg jumpA,
	output reg jumpAE,
	output reg jumpE
);

always @(opcode) begin
	aluSlc = ((~opcode[0]) & (~opcode[1]) & (opcode[2]) & (~opcode[3])) | ((~opcode[0]) & (~opcode[1]) & (opcode[2]) & (opcode[3]));
	aluImm = ((~opcode[0]) & (~opcode[1]) & (~opcode[2]) & (opcode[3]) | (~opcode[0]) & (~opcode[1]) & (opcode[2]) & (opcode[3]));
	memLoad = ((~opcode[0]) & (opcode[1]) & (~opcode[2]) & (~opcode[3]) & (~opcode[0]) & (opcode[1]) & (~opcode[2]) & (~opcode[3]));
	memSt = ((~opcode[0]) & (opcode[1]) & (~opcode[2]) & (opcode[3]) & (~opcode[0]) & (opcode[1]) & (~opcode[2]) & (opcode[3]));
	compare = ((~opcode[0]) & (opcode[1]) & (opcode[2]) & (~opcode[3]));
	regWe = ((~opcode[0]) & (~opcode[1]) & (~opcode[2]) & (~opcode[3]) | (~opcode[0]) & (~opcode[1]) & (~opcode[2]) & (opcode[3]) | (~opcode[0]) & (~opcode[1]) & (opcode[2]) & (~opcode[3]) | (~opcode[0]) & (~opcode[1]) & (opcode[2]) & (opcode[3]) | (~opcode[0]) & (opcode[1]) & (~opcode[2]) & (~opcode[3]));
	jump = ( (~opcode[0]) & (opcode[1]) & (opcode[2]) & (opcode[3]) | (opcode[0]) & (~opcode[1]) & (~opcode[2]) & (~opcode[3]) | (opcode[0]) & (~opcode[1]) & (~opcode[2]) & (opcode[3]) | (opcode[0]) & (~opcode[1]) & (opcode[2]) & (~opcode[3]) | (opcode[0]) & (~opcode[1]) & (opcode[2]) & (opcode[3]) | (opcode[0]) & (opcode[1]));
	jmpRn = (jump & (~opcode[0]) & (opcode[1]) & (opcode[2]) & (opcode[3]));
	jumpB = (jump & (opcode[0]) & (~opcode[1]) & (~opcode[2]) & (~opcode[3]));
	jumpBE = (jump & (opcode[0]) & (~opcode[1]) & (~opcode[2]) & (opcode[3]));
	jumpA = (jump & (opcode[0]) & (~opcode[1]) & (opcode[2]) & (~opcode[3]));
	jumpAE = (jump & (opcode[0]) & (~opcode[1]) & (opcode[2]) & (opcode[3]));
	jumpE = jump & opcode[0] & opcode[1];
end
endmodule