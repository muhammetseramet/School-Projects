module keypad( 

output reg [3:0] rowwrite, 
input wire [3:0] colread,  
output reg [15:0] data_out,
input readyclr,
input a0,
input clk);

reg [3:0]keyread;

reg [3:0] rows;
reg [41:0] stable;
wire keypad_pressed;
reg rowpressed;

reg [25:0]clk1;
wire stablefor21scans;
reg [3:0]data;
reg readytemp;
reg [1:0]readytemp_acc;
reg ready;


always @(posedge clk1[14]) 
begin
	 rowwrite <= {rowwrite[2:0],rowwrite[3]};
end

always @(posedge clk)
    clk1<=clk1+1;


always @*

	 if ((rowwrite==4'b0111)&(colread == 4'b0111)) //1
    begin
        keyread=4'h1;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b0111)&(colread == 4'b1011)) //2 
    begin
        keyread=4'h2;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b0111)&(colread == 4'b1101)) //3 
    begin
        keyread=4'h3;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b0111)&(colread == 4'b1110)) //b
    begin
        keyread=4'ha;
        rowpressed=1'b1;
    end
   else if ((rowwrite==4'b1011)&&(colread == 4'b0111))//4
    begin
        keyread=16'h4;
        rowpressed=1'b1;
    end
	 else if ((rowwrite==4'b1011)&(colread == 4'b1011))//5
    begin
        keyread=4'h5;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1011)&(colread == 4'b1101))//6
    begin
        keyread=4'h6;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1011)&(colread == 4'b1110))//b
    begin
        keyread=4'hb;
        rowpressed=1'b1;
    end
    
    else if ((rowwrite==4'b1101)&(colread == 4'b0111)) //7
    begin
        keyread=4'h7;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1101)&(colread == 4'b1011)) //8
    begin
        keyread=4'h8;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1101)&(colread == 4'b1101)) //9
    begin
        keyread=4'h9;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1101)&(colread == 4'b1110)) //c
    begin
        keyread=4'hc;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1110)&(colread == 4'b0111)) //e
    begin
        keyread=4'he;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1110)&(colread == 4'b1011)) //0
    begin
        keyread=4'h0;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1110)&(colread == 4'b1101)) //f
    begin
        keyread=4'hf;
        rowpressed=1'b1;
    end
    else if ((rowwrite==4'b1110)&(colread == 4'b1110)) //d
    begin
        keyread=4'hd;
        rowpressed=1'b1;
    end
	 else 
    begin
        keyread=4'ha;
        rowpressed=1'b0;
    end

always @(posedge clk1[14])
begin
    rows <= {rows[2:0],rowpressed};
    if (rowpressed == 1'b1)
        data <= keyread;
end

assign keypad_pressed = |rows;

always @(posedge clk1[14])
    stable <= {stable[40:0],keypad_pressed} ;
	 
assign stablefor21scans = &stable;


 
 always @(posedge clk)
      readytemp_acc <= {readytemp_acc[0], stablefor21scans};


   always @(posedge clk)begin
        if(!readytemp_acc[1] && readytemp_acc[0])
            ready <= 1;
        else if(readyclr)  
            ready <= 0;
	end
	
	always @* 
	begin
      if (!a0)
         data_out = {12'b0,data}; 
      else
         data_out = {15'b0, ready};  

	end
 
 initial begin
	rowwrite = 4'b1110;
	rows = 4'h0;
	
	rowpressed = 1;
	clk1=0;
	end

 endmodule