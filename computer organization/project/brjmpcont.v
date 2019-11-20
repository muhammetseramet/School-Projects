//we created this
module brjmpcont(brjmpsignal, status, rtypein,addressout, addressin, select);
  input [2:0] brjmpsignal;
  input [1:0] status;
  input [31:0] addressin;
  input [2:0] rtypein;
  output[31:0] addressout;
  output select;
  reg select;
  reg   [31:0] addressout;

  initial
  begin
  select = 1'b0;
  end
  
  always @(addressin)
begin
	case(brjmpsignal)
		3'b000:
            begin
			    if(rtypein[0] && status[0]) //brz
				begin
				    addressout = addressin;
					select = 1'b1;
				end
				
				else if(rtypein[1]) //jmadd
				begin
				    addressout = addressin;
					select = 1'b1;
				end
				
				else
					select = 1'b0;
				
			end
		3'b001:		  //bmz					
			begin
				if(status[0])   
				begin
				    addressout =  addressin;
					select = 1'b1;
				end
				else
					select = 1'b0;
			end	
        3'b100:		  //jrsal			
			begin				
				    addressout =  addressin;
					select = 1'b1;
			end
		default: select = 1'b0;
	endcase
end
  
endmodule
