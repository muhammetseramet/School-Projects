module bird(  data_out, mem_wrt, address, data_in,clk);
input clk;
input [15:0] data_in;
output reg [11:0] address;
output reg mem_wrt;
output reg [15:0] data_out;
reg [15:0] regbank [7:0];
reg [11:0] ir;
reg [11:0] pc;
reg [3:0] state,next_state;
reg [15:0] aluout;
wire zero_result;

localparam  FETCH   = 4'b0000; 
localparam  LDI     = 4'b0001; 
localparam  LD      = 4'b0010; 
localparam  ST      = 4'b0011; 
localparam  JMP     = 4'b0100; 
localparam  JZ      = 4'b0101; 
localparam  ALU     = 4'b0111; 
localparam  POP1    = 4'b1001; 
localparam  POP2    = 4'b1100;
localparam  PUSH    = 4'b1000; 
localparam  CALL    = 4'b1010; 
localparam  RET1    = 4'b1011; 
localparam  RET2    = 4'b1101; 

always @(posedge clk)
	case(state)
		FETCH: begin
		ir<=data_in[11:0];
		pc<=pc+1;
		end

		LDI: begin
		regbank[ir[2:0]] <=data_in;
		pc<=pc+1;
		end

		LD:begin
		regbank[ir[2:0]] <=data_in;
		end

		ST: begin
		data_out<=regbank[ir[8:6]];
		end
		
		JMP: begin
		pc<= pc+ir;
		end

		ALU: begin
		regbank[ir[2:0]] <=aluout;
		regbank[6][0]<=zero_result;			
		end

		PUSH: begin
      data_out <= regbank[ir[8:6]];
      regbank[7] <= regbank[7] - 1;
      end

      POP1: begin
      regbank[7] <= regbank[7] + 1;
      end

      POP2: begin
      regbank[ir[2:0]] <= data_in;
      end

      CALL: begin
      pc <= pc + ir;
      regbank[7] <= regbank[7] - 1;
      end

      RET1: begin
      regbank[7] <= regbank[7] + 1;
      end
		
		RET2: begin
      pc <= data_in;
      end

	endcase

always @*
	case (state)
		FETCH: next_state = data_in[15:12]; 
		JZ:    if (regbank[6][0])
					next_state = JMP;
				 else
					next_state = FETCH;
		POP1: next_state = POP2;
		RET1: next_state = RET2;
		default: next_state = FETCH;
	endcase

always @(posedge clk)
	state <= next_state;


always @*   
	case (state)
		LD: begin
			address = regbank[ ir[5:3] ][11:0];
      end

      ST: begin
			address = regbank[ ir[5:3] ][11:0];
			mem_wrt=1;
      end

      PUSH: begin
			address = regbank[7];
			mem_wrt=1;
      end

      POP2: begin
			address = regbank[7];
      end

      CALL: begin
			address = regbank[7];
			mem_wrt=1;
      end

		RET2: begin
			address = regbank[7];
		end

      default: begin
			address = pc;
			mem_wrt=0;
      end

	endcase





always @*
	case(ir[11:9])
		3'h0: aluout = aluin1 + aluin2;
		3'h1: aluout = aluin1 - aluin2;
		3'h2: aluout = aluin1 & aluin2;
		3'h3: aluout = aluin1 | aluin2;
		3'h4: aluout = aluin1 ^ aluin2;
		3'h7: case(ir[8:6])
			3'h0: aluout = aluin1;
			3'h1: aluout = ~aluin1;
			3'h2: aluout = aluin1 + 16'b1;
			3'h3: aluout = aluin1 - 16'b1;
			default: aluout =0;
			endcase
		default: aluout =0;
		endcase

assign zero_result= ~|aluout;
assign aluin1 = regbank[ir[5:3]];
assign aluin2 = regbank[ir[8:6]];

initial begin
	$readmemh("register_data.dat", regbank);
	state = FETCH;
	pc = 0;
	address = 0;
	data_out = 0;
	mem_wrt=0;
 end

endmodule