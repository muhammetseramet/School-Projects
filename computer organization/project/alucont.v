module alucont(aluop1,aluop0,instructions,gout,rtypeout, rsordatamem, jmaddcont);//Figure 4.12 
input aluop1,aluop0;
input [5:0] instructions;
output [2:0] gout;
reg [2:0] gout;
output [2:0] rtypeout; // brz mi başka r type mı
reg [2:0] rtypeout;
output rsordatamem;//data mı rgister mı branch edilcek onu belirliyor
reg rsordatamem;
output jmaddcont;
reg jmaddcont;
always @(instructions or aluop0 or aluop1)
begin
if(~(aluop1|aluop0)) gout=3'b010;
if(aluop1) gout=3'b110;
if(aluop0)//R-type
begin
	case(instructions)
		6'b000000:
		begin 
			rsordatamem = 1'b1;
			rtypeout = 3'b100;
			jmaddcont = 1'b0; 
			gout=3'b010; 
		end //function code=000000,ALU control=010 (add)
		6'b001010:
			begin 
			rsordatamem = 1'b0;
			rtypeout = 3'b100;
			jmaddcont = 1'b0; 
			gout=3'b111; 
		end //function code=001010,ALU control=111 (set on less than)
		6'b100010:
		begin		
			rsordatamem = 1'b0; 
			rtypeout = 3'b100;
			jmaddcont = 1'b0; 
			gout=3'b110; 
		end //function code=100010,ALU control=110 (sub)
		6'b011001:
		begin 
			rsordatamem = 1'b0; 
			rtypeout = 3'b100;
			jmaddcont = 1'b0; 
			gout=3'b001; 
		end //function code=xxx1x1,ALU control=001 (or)
		6'b101000:
		begin 
			rsordatamem = 1'b0; 
			rtypeout = 3'b100;
			jmaddcont = 1'b0; 
			gout=3'b000; 
		end //function code=101000,ALU control=000 (and)
		//instructions that we added
		6'b010100:
		begin 
			gout=3'b000; 
			rtypeout = 3'b001;  
			rsordatamem = 1'b1; 
			jmaddcont = 1'b0; 
		end //brz
		6'b000100:
		begin 
			rsordatamem = 1'b0; 
			rtypeout = 3'b100;
			jmaddcont = 1'b0; 
			gout=3'b011; 
		end // shift yap emri aluya gönderildi.  //sllv
		6'b100000:
		begin 
			gout=3'b010; 
			rtypeout=3'b010; 
			jmaddcont = 1'b1; 
			rsordatamem = 1'b0; 
		end //jmadd
	endcase
end
end
endmodule
