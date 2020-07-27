module test(grounds, display,display2, clk, rowwrite, colread);

    output wire [7:0] grounds;
    output wire [6:0] display;
	 output wire [6:0] display2;
    input   clk; 
	 
    output wire [3:0] rowwrite;
    input wire [3:0] colread;
	 reg readyclr;
	  reg     [15:0] memory [0:511]; 
    reg     [15:0]  data_in;
    wire     [11:0] address;
    wire     mem_wrt;
	 wire [15:0] data_out;
		wire [15:0] keypad_data;
    
    reg     [15:0] ss7;


    localparam     BEGINMEM=12'h000,
						 ENDMEM=12'h900,
                   ADDR_KEYPADDATA=12'ha00,
                   ADDR_KEYPADSTATUS=12'ha01,
                   SEVENSEG=12'hb00;

  




    bird cpu1(.data_out(data_out), .mem_wrt(mem_wrt), .address(address) ,.data_in(data_in) ,.clk(clk));
    seven_segment monitor1( .data_in(keypad_data),.data_in2(ss7),.grounds(grounds),.display2(display2), .display(display), .clk(clk));


   keypad keypad1(.rowwrite(rowwrite), .colread(colread),.data_out(keypad_data), .readyclr(readyclr), .a0(address[0]), .clk(clk));



   always @*  
		begin
        if ((BEGINMEM<=address) &&(address<=ENDMEM))
        begin
            data_in=memory[address];
            readyclr = 0;
        end
		  else if ( address==SEVENSEG )   
        begin
            data_in=16'hf400;
            readyclr=0;
        end
        else if (address==ADDR_KEYPADDATA) 
        begin
            data_in=keypad_data;    
            readyclr=1;  
        end
        else if ( address==ADDR_KEYPADSTATUS )   
        begin
            data_in=keypad_data;
            readyclr=0;
        end

        else
        begin
            data_in=16'hf345;  
            readyclr=0;
        end  
	end


    always @(posedge clk) 
		begin
	   if (mem_wrt)
           if ((BEGINMEM<=address) &&(address<=ENDMEM))
                memory[address]<=data_out;
           else if ( SEVENSEG==address) 
                ss7<=data_out;
	end

    initial begin
            $readmemh("calculator.dat", memory);
            end

endmodule