module cpu(clk, data_in, data_out, address, mem_wrt, reg5, reg4);
	
input clk;
input [15:0] data_in;
output reg [15:0] data_out;
output reg [11:0] address;
output wire mem_wrt;
output reg [15:0] reg5, reg4;	
reg zero;
reg [11:0] PC, IR;
	
reg [4:0] STATE;
reg [15:0] reg_bank[7:0];
reg [15:0] result;
	
wire zero_result;
	
localparam FETCH = 4'b0000;
localparam LDI   = 4'b0001;
localparam LD	 = 4'b0010;
localparam ST	 = 4'b0011;
localparam JZ 	 = 4'b0100;
localparam JMP   = 4'b0101;
localparam ALU   = 4'b0111;
localparam PSH	 = 4'b1000;
localparam POP   = 4'b1001;
localparam CALL  = 4'b1010;
localparam RET	 = 4'b1011;
localparam POP2  = 4'b1100;
localparam RET2  = 4'b1101;

always @*
begin
	reg5 = reg_bank[5];
	reg4 = reg_bank[3];
end

always @(posedge clk)
begin
	case(STATE)
	
		FETCH: 
		begin
			if ( data_in[15:12]==JZ) 
				if (zero)
					STATE <= JMP;
				else
					STATE <= FETCH;
			else
				STATE <= data_in[15:12];
			IR<=data_in[11:0];
			PC<=PC+1;
		end

		LDI:
		begin
			reg_bank[ IR[2:0] ] <= data_in;
			PC<=PC+1;
			STATE <= FETCH;
		end

		LD:
		begin
			reg_bank[IR[2:0]] <= data_in;
			STATE <= FETCH;  
		end 

		ST:
		begin
			
			STATE <= FETCH;  
		end    

		JMP:
		begin
			PC <= PC+IR;
			STATE <= FETCH;  
		end          

		ALU:
		begin
			reg_bank[IR[2:0]]<=result;
			zero <= zeroresult;
			STATE <= FETCH;
		end
		
		PSH:
		begin
			reg_bank[7] <= reg_bank[7]-1;
			STATE<=FETCH;
		end
		
		POP:
		begin
			reg_bank[7] <= reg_bank[7]+1;
			STATE <= POP2;
		end
		
		POP2:
		begin
			reg_bank[IR[2:0]] <= data_in;
			STATE <= FETCH;		
		end
		
		CALL:
		begin

			PC <= PC+IR[11:0];
			reg_bank[7] <= reg_bank[7]-1;
			STATE <= FETCH;		
		end
		
		RET:
		begin
			reg_bank[7] <= reg_bank[7]+1;
			STATE <= RET2;		
		end
		
		RET2:
		begin
			PC <= data_in[11:0];
			STATE <= FETCH;		
		end

	endcase
end

always @*
begin
	if((STATE==LD) || (STATE==ST))
		address = reg_bank[IR[5:3]][11:0];
	else if ((STATE==PSH) || (STATE==POP2) || (STATE==CALL) || (STATE==RET2))
		address = reg_bank[7][11:0];	//addr=SP
	else
		address = PC;
		
end

always @*
begin
	if (STATE == CALL)
		data_out = PC;
		
	else if (STATE==PSH)
		data_out = reg_bank[IR[8:6]];
		
	else if (STATE==ST)
		data_out = reg_bank[IR[8:6]];
		
	else
		data_out = PC;
end

assign mem_wrt = (STATE==ST) || (STATE==PSH) || (STATE==CALL);

always @*
begin
	case (IR[11:9])
		3'h0: result = reg_bank[IR[8:6]]+reg_bank[IR[5:3]];
		3'h1: result = reg_bank[IR[8:6]]-reg_bank[IR[5:3]];
		3'h2: result = reg_bank[IR[8:6]]&reg_bank[IR[5:3]];
		3'h3: result = reg_bank[IR[8:6]]|reg_bank[IR[5:3]];
		3'h4: result = reg_bank[IR[8:6]]^reg_bank[IR[5:3]];
		3'h7: case (IR[8:6])
			3'h0: result = !reg_bank[IR[5:3]];
			3'h1: result = reg_bank[IR[5:3]];
			3'h2: result = reg_bank[IR[5:3]]+1;
			3'h3: result = reg_bank[IR[5:3]]-1;
			default: result=16'h0000;
		endcase
		default: result=16'h0000;
	endcase
end
assign zeroresult = ~|result;


initial begin;
		
	STATE=FETCH;
	
end   


endmodule