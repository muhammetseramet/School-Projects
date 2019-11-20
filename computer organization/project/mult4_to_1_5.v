module mult4_to_1_5(out, i0,i1,select_two);
output [4:0] out;
reg[4:0] out;
input [4:0]i0,i1;
input [1:0] select_two;

always @(*)
begin
	case(select_two)
		2'b00: out=i0;
		2'b01: out=5'b11111;
		2'b10: out=i1;
	default: out=i0;
	endcase
end
endmodule