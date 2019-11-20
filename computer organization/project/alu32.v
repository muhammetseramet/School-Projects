module alu32(sum,a,b,zout,gin,statusoutput);//ALU operation according to the ALU control line values
output [31:0] sum;
output [1:0] statusoutput;
input [31:0] a,b; 
input [2:0] gin;//ALU control line
reg [31:0] sum;
reg [31:0] less;
reg [1:0] statusoutput;
output zout;
reg zout;
always @(a or b or gin)
begin
	case(gin)
	3'b010: sum=a+b; 		//ALU control line=010, ADD
	3'b011: begin
	        sum=b<<a;
	        end
	3'b110: sum=a+1+(~b);	//ALU control line=110, SUB
	3'b111: begin less=a+1+(~b);	//ALU control line=111, set on less than
			if (less[31]) sum=1;	
			else sum=0;
		  end
	3'b000: sum=a & b;	//ALU control line=000, AND
	3'b001: sum=a|b;		//ALU control line=001, OR
	default: sum=31'bx;	
	endcase
zout=~(|sum);
statusoutput[0] = zout;    // we added z signal of status
statusoutput[1] = sum[31]; //we added n signal of status

//overflow control yapılcak.
end
endmodule
