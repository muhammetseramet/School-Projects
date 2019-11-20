`timescale 1ns/1ns
module Datapath(
	input [7:0] address,
	input [3:0] read_reg1,
	input [3:0] read_reg2,
	input [3:0] write_reg,
	input [3:0] alu_imm_val,
	input we,
	input reg_clc,
	input reg_reset,
	input mem_store,
	input mem_load,
	input alu_imm,
	input alu_slc,
	output reg[15:0] src1,
	output reg[15:0] src2,
	output reg[15:0] dm_out,
	output reg[15:0] alu_out
);

pn alu(
	.src1 (src1),
	.src2 (src2),
	.imm (alu_imm_val),
	.alu_slc (alu_slc),
	.alu_imm (alu_imm),
	.out (alu_out)
);

pn register_file(
	.clk (reg_clc),
	.we (we),
	.clr (reg_reset),
	.reg_write_dest (write_reg),
	.reg_read_addr_1 (read_reg1),
	.reg_read_addr_2 (read_reg2),
	.reg_write_data (),
	.reg_read_data_1 (src1),
	.reg_read_data_2 (src2)
);

ram1#(.addr_width(12), .data_width(16)
) m1(.data_input(src1),.mem_store(mem_store),.mem_load(mem_load),
	.out(dm_out)
); 

endmodule