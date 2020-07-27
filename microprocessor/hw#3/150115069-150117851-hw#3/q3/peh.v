module peh(clk, row_write, col_read, grounds, grounds2, display, display2, push_button);

input wire clk, push_button;
input wire [3:0] col_read;
output wire [3:0] grounds;
output wire [3:0] grounds2;
output wire [6:0] display;
output wire [6:0] display2;
output wire [3:0] row_write;


reg [15:0] memory [511:0];
reg [15:0] data_in, ss7;

wire [11:0] address;
wire mem_wrt;
wire [15:0] data_out,keypadv;
wire [15:0] reg5, reg4;
localparam 	BEGINMEM=12'h000;
localparam	ENDMEM=12'h3ff;
localparam	KEYPAD=12'h500;
localparam	ADDR_SEVENSEG=12'h700; 

				
cpu cpu1(.clk(clk), .data_in(data_in), .data_out(data_out), .address(address), .mem_wrt(mem_wrt), .reg5(reg5), .reg4(reg4));

seven_segment_red sevensegment1(.clk(clk), .data_in(reg5), .grounds(grounds), .display(display));	 
seven_segment_green sevensegment2(.clk(clk), .data_in(ss7), .grounds(grounds2), .display(display2));
keypad keypad1(.dataout(keypadv), .rowwrite(row_write), .colread(col_read), .readyclr(address==KEYPAD), .a0(address==KEYPAD+1), .clk(clk));

always @*
begin
	if ((BEGINMEM<=address) &&(address<=ENDMEM))
			data_in=memory[address];
	else if((KEYPAD == address) || (address == KEYPAD+1))
		data_in = keypadv;
	else
		data_in = 16'hf345;
end

always @(posedge clk) 
begin
		if (mem_wrt)
			if ((BEGINMEM<=address) &&(address<=ENDMEM))
				memory[address]<=data_out;
			else if ( address==ADDR_SEVENSEG) 
				ss7<=data_out;
end
initial begin
    $readmemh("son.dat", memory);  //must be exactly 512 locations
end
				
endmodule