module seven_segment_red(clk, data_in,grounds, display);

input wire [15:0] data_in;
output reg [3:0] grounds;
output reg [6:0] display;
input clk; 

reg [3:0] data [3:0] ; //number to be printed on display
reg [1:0] count;       //which data byte to display.
reg [25:0] clk1;

always @(posedge clk1[15]) //25 slow //19 wavy //15 perfect
begin
    grounds<={grounds[2:0],grounds[3]};
    count<=count+1;
end

always @(posedge clk)
    clk1<=clk1+1;

always @(*)
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
always @*
   begin
    data[3]=data_in[3:0];
    data[2]=data_in[7:4];
    data[1]=data_in[11:8];
    data[0]=data_in[15:12];
    end


initial begin
    
    count = 2'b0;
    grounds=4'b1110;
    clk1=0;
end

endmodule