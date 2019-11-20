`timescale 1ns/1ns
module Register_File(
	input clk,
	input we,
	input clr,
	input [3:0]reg_write_dest,
	input [3:0]reg_read_addr_1,
	input [3:0]reg_read_addr_2,
	input [15:0]reg_write_data,
	output [15:0]reg_read_data_1,
	output [15:0]reg_read_data_2
);

reg [15:0] reg_array [15:0];
integer i;
initial begin
	for(i=0;i<16;i=i+1)
		reg_array[i] <= 16'd0;
end
always @(posedge clk ) begin
	if(we) begin
		reg_array[reg_write_dest] <= reg_write_data;
		end
end
integer j;
always @(clr) begin
	for(j=0; i<16;j=j+1)
		reg_array[i] <= 16'd0;
end

assign reg_read_data_1 = reg_array[reg_read_addr_1];
assign reg_read_data_2 = reg_array[reg_read_addr_2];

endmodule