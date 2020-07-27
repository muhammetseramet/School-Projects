module seven_segment( data_in,data_in2,grounds, display, display2, clk);

input wire [15:0] data_in;
input wire [15:0] data_in2;
output reg [7:0] grounds;
output reg [6:0] display;
output reg [6:0] display2;
input clk; 

reg [3:0] data [7:0] ; 
reg [2:0] count;       
reg [25:0] clk1;

always @(posedge clk1[15]) //25 slow //19 wavy //15 perfect
begin
    grounds<={grounds[0],grounds[7:1]};
    count<=count+1;
end

always @(posedge clk)
    clk1<=clk1+1;

always @(*)
if (count>3)
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
else
	case(data[count])
        0:display2=7'b1111110;
        1:display2=7'b0110000;
        2:display2=7'b1101101;
        3:display2=7'b1111001;
        4:display2=7'b0110011;
        5:display2=7'b1011011;
        6:display2=7'b1011111;
        7:display2=7'b1110000;
        8:display2=7'b1111111;
        9:display2=7'b1111011;
        'ha:display2=7'b1110111;
        'hb:display2=7'b0011111;
        'hc:display2=7'b1001110;
        'hd:display2=7'b0111101;
        'he:display2=7'b1001111;
        'hf:display2=7'b1000111;
        default display2=7'b1111111;
    endcase

always @*
   begin
	 data[7]=data_in2[3:0];
    data[6]=data_in2[7:4];
    data[5]=data_in2[11:8];
    data[4]=data_in2[15:12];
    data[3]=data_in[3:0];
    data[2]=data_in[7:4];
    data[1]=data_in[11:8];
    data[0]=data_in[15:12];
	 

	 
	 
    end

initial begin

	 grounds=8'b01111111;
    clk1=0;
    count = 3'b0;

end

endmodule