`timescale 1ns/1ns
module Compare(
	input [15:0]src1,
	input [15:0]src2,
	output reg cf,
	output reg zf
);

always @(src1, src2) begin

	if(src1 == src2) begin
		zf = 1'b1;
		cf = 1'b0;
		end
	else if(src1 > src2) begin
		zf = 1'b0;
		cf = 1'b0;
		end
	else begin
		zf = 1'b0;
		cf = 1'b1;
		end
end
endmodule