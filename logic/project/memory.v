module memory();

reg [15:0] InstructionMemory [15:0];

initial begin
	$readmemh("output.hex",InstructionMemory);
end
endmodule