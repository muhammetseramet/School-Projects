module control(in,regdest,alusrc,memtoreg,regwrite,memread,memwrite,branch,aluop1,aluop2, brjmpcont,jump,bn);
input [5:0] in;
output regdest,alusrc,memtoreg,regwrite,memread,memwrite,branch,aluop1,aluop2,jump,bn; //we added jump and bn
output [2:0] brjmpcont; //we added this output to control if branch or jump
reg [2:0] brjmpcont;    //we added this register to control if branch or jump

wire rformat,lw,sw,beq,bmz,jrsal;
assign rformat=~|in;
assign lw=in[5]& (~in[4])&(~in[3])&(~in[2])&in[1]&in[0];
assign sw=in[5]& (~in[4])&in[3]&(~in[2])&in[1]&in[0];
assign beq=~in[5]& (~in[4])&(~in[3])&in[2]&(~in[1])&(~in[0]);

//instructions that we added
assign bmz=(~in[5])&in[4]&(~in[3])&in[2]&(~in[1])&(~in[0]);	//opcode = 010100 = 20
assign bn=(~in[5])&in[4]&in[3]&(~in[2])&(~in[1])&in[0];     //opcode = 011001 = 25
assign jrsal=(~in[5])&in[4]&(~in[3])&(~in[2])&in[1]&in[0];  //opcpde = 010011 = 19
assign jump=(~in[5])&(~in[4])&(~in[3])&(~in[2])&in[1]&(~in[0]);	//opcode = 000010=2 

assign regdest=rformat;
assign alusrc=lw|sw|bmz; //we added bmz
assign memtoreg=lw;
assign regwrite=rformat|lw;
assign memread=lw|jrsal;
assign memwrite=sw|jrsal;
assign branch=beq;
assign aluop1=rformat;
assign aluop2=beq;

always@(bmz or jrsal)
begin	
    if(bmz) brjmpcont = 3'b001;	 //if instruction is bmz
	if(jrsal) brjmpcont = 3'b100;	 //if instruction is jrsal
	if (~(bmz | jrsal))  brjmpcont=3'b000; // if instr is not i or j type
end

endmodule
