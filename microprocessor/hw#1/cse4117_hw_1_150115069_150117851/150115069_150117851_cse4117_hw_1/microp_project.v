module microp_project(clk, switch_1, switch_2, switch_3, switch_4, grounds, display);

output reg [3:0] grounds;
output reg [6:0] display;

input clk;
input wire switch_1, switch_2, switch_3, switch_4;
reg mode, blink;

reg [3:0] ids[0:17];
reg [3:0] data [0:3] ; //number to be printed on display
reg [1:0] count;       //which data byte to display
reg [25:0] clk1;

reg [5: 0] ind;

always @(posedge clk1[15]) begin

	
	grounds<={grounds[2:0],grounds[3]};
	count<=count+1;
end


	
always @(posedge clk1[22]) begin
	//making sure it blinks at one posedge and does not at next posedge
	if (blink==1) begin
		blink<=0;
	end else begin
		blink<=1;
	end
end
always @(posedge clk1[25]) begin
	// making sure switch_3 is not functional when switch_2 on
	if (switch_2==1) begin
		if (switch_3==1) begin
			ind<=ind+1;
			if (ind==18) begin
				ind<=0;
				mode<=0;
			end
		end else begin
			ind<=ind-1;
			if (ind==0) begin
				ind<=17;
				mode<=1;
			end 		
		end
	end else if (switch_2==0) begin
		if (ind==13) begin
			mode<=0;
		end else if (ind==0) begin
			mode<=1;
		end
		
		if (mode==0) begin
			ind<=ind-1;
		end else if (mode==1) begin
			ind<=ind+1;
		end
	end

end
always @(posedge clk) begin
	if (switch_1==0) begin
		clk1<=clk1+2;
	end else if (switch_1==1) begin
		clk1<=clk1+1;
	end
end
	
always @(*) begin
		 case(data[count])
			  0:display=7'b1111110; //starts with a, ends with g
			  1:display=7'b0110000;
			  2:display=7'b1101101;
			  3:display=7'b1111001;
			  4:display=7'b0110011;
			  5:display=7'b1011011;
			  6:display=7'b1011111;
			  7:display=7'b1110000;
			  8:display=7'b1111111;
			  9:display=7'b1111011;
			  'ha:display=7'b1110111;
			  'hb:display=7'b0011111;
			  'hc:display=7'b1001110;
			  'hd:display=7'b0111101;
			  'he:display=7'b1001111;
			  'hf:display=7'b1000111;
			  default display=7'b1111111;
		 endcase

		 if (switch_4==0) begin
			if (blink==1) begin
				display=7'b0000000;
			end
			
		 end
	 end


always @*
	begin
   data[0]=ids[ind%18];
   data[1]=ids[(ind+1)%18];
   data[2]=ids[(ind+2)%18];
   data[3]=ids[(ind+3)%18];
   end

initial begin
	ids[0] = 4'h1;
	ids[1] = 4'h5;
	ids[2] = 4'h0;
	ids[3] = 4'h1;
	ids[4] = 4'h1;
	ids[5] = 4'h7;
	ids[6] = 4'h8;
	ids[7] = 4'h5;
	ids[8] = 4'h1;
	ids[9] = 4'h1;
	ids[10] = 4'h5;
	ids[11] = 4'h0;
	ids[12] = 4'h1;
	ids[13] = 4'h1;
	ids[14] = 4'h5;
	ids[15] = 4'h0;
	ids[16] = 4'h6;
	ids[17] = 4'h9;
	count = 2'b0;
   grounds=4'b1110;
   clk1=0;
	ind=0;
	mode = 1; //left
	blink=0;
end
endmodule