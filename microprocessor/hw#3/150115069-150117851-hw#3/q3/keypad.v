module keypad(dataout, rowwrite, colread,readyclr, a0, clk);

output reg [3:0] rowwrite; 
output reg [15:0] dataout;
input wire [3:0] colread;
input readyclr;
input a0;
input clk;

reg [3:0] keyread;
reg rowpressed;
reg [3:0] rows;
wire keypressed;
reg [41:0] stable;
wire stablefor21scans;
reg [25:0] clk1;
reg [3:0] data;
reg [1:0] readytemp_acc;
reg ready;

always @(posedge clk)
	clk1 <= clk1+1;		//14th bit

always @(posedge clk1[16])		//1KHz
	rowwrite <= {rowwrite[2:0],rowwrite[3]};
	
always @*
	if(rowwrite==4'b0111 && colread==4'b0111)
		begin
		keyread = 4'h1;
		rowpressed = 1'b1;
		end
		
	else if(rowwrite==4'b0111 && colread==4'b1011)
		begin
		keyread = 4'h2;
		rowpressed = 1'b1;
		end
		
	else if(rowwrite==4'b0111 && colread==4'b1101)
		begin
		keyread = 4'h3;
		rowpressed = 1'b1;
		end	
		
	else if(rowwrite==4'b0111 && colread==4'b1110)
		begin
		keyread = 4'ha;
		rowpressed = 1'b1;
		end	
		
	else if(rowwrite==4'b1011 && colread==4'b0111)
		begin
		keyread = 4'h4;
		rowpressed = 1'b1;
		end	
		
	else if(rowwrite==4'b1011 && colread==4'b1011)
		begin
		keyread = 4'h5;
		rowpressed = 1'b1;
		end	

	else if(rowwrite==4'b1011 && colread==4'b1101)
		begin
		keyread = 4'h6;
		rowpressed = 1'b1;
		end			


	else if(rowwrite==4'b1011 && colread==4'b1110)
		begin
		keyread = 4'hb;
		rowpressed = 1'b1;
		end
		
	else if(rowwrite==4'b1101 && colread==4'b0111)
		begin
		keyread = 4'h7;
		rowpressed = 1'b1;
		end
		
	else if(rowwrite==4'b1101 && colread==4'b1011)
		begin
		keyread = 4'h8;
		rowpressed = 1'b1;
		end	
		
	else if(rowwrite==4'b1101 && colread==4'b1101)
		begin
		keyread = 4'h9;
		rowpressed = 1'b1;
		end	
		
	else if(rowwrite==4'b1101 && colread==4'b1110)
		begin
		keyread = 4'hc;
		rowpressed = 1'b1;
		end
	
	else if(rowwrite==4'b1110 && colread==4'b0111)
		begin
		keyread = 4'he; //*****************
		rowpressed = 1'b1;
		end	
	
	else if(rowwrite==4'b1110 && colread==4'b1011)
		begin
		keyread = 4'h0;
		rowpressed = 1'b1;
		end
	
	else if(rowwrite==4'b1110 && colread==4'b1101)
		begin
		keyread = 4'hf; //##############
		rowpressed = 1'b1;
		end	
		
	else if(rowwrite==4'b1110 && colread==4'b1110)
		begin
		keyread = 4'hd;
		rowpressed = 1'b1;
		end
		
	else
		begin
		keyread = 4'ha;
		rowpressed = 1'b0;
		end
		
		
always @(posedge clk1[16])	//for debouncing
	begin
	rows <= {rows[2:0], rowpressed};
	if(rowpressed==1'b1)
		data <= keyread;
	end
	
assign keypressed = |rows;

always @(posedge clk1[16])
	stable <= {stable[40:0], keypressed};
	
assign stablefor21scans = &stable;

always @(posedge clk)
	readytemp_acc <= {readytemp_acc[0], stablefor21scans};
	
always @(posedge clk)
	if(!readytemp_acc[1] && readytemp_acc[0])
		ready <= 1;
	else if(readyclr)
		ready <= 0;
		
always @*
	if(!a0) 
		dataout = {12'b0, data};
	else
		dataout = {15'b0, ready};
		
initial begin
	rowwrite = 4'b1110;
	ready = 1'b0;
	rows = 4'h0;
	data = 4'h0;
	rowpressed = 1;
	clk1=0;
end

endmodule