module processor;
reg [31:0] pc; //32-bit prograom counter
reg clk; //clock
reg [7:0] datmem[0:31],mem[0:31]; //32-size data and instruction memory (8 bit(1 byte) for each location)
wire [31:0] 
dataa,	//Read data 1 output of Register File
datab,	//Read data 2 output of Register File
out2,		//Output of mux with ALUSrc control-mult2
out3,		//Output of mux with MemToReg control-mult3
out4,		//Output of mux with (Branch&ALUZero) control-mult4
out5,       //we added Output of mux after Branch&ALUZero control-mult5
out6,       //we added output of branch and jump control mult6
out7,       //we added output before brjmpcont unit mult7
out8,       //we added output before register file(write data) mult8
out10,      //we added output before writedata in datamemory mult10
sum,		//ALU result
extad,	//Output of sign-extend unit
adder1out,	//Output of adder which adds PC and 4-add1
adder2out,	//Output of adder which adds PC+4 and 2 shifted sign-extend result-add2
sextad;	//Output of shift left 2 unit

reg [1:0] regstatus;
wire [1:0] status; //we added status output
wire [2:0] brjmpsignal; //we added brjmpsignal
wire [31:0] newaddress; //we added new address output
wire[31:0] fulljump;  
wire[27:0] shiftedjump;	
wire select;// we added select bit
wire jump; //we added jump signal
wire rsordatamem;
wire bnwire;
wire jmaddcont;
wire [1:0] jrsaljmaddcont; // mult4_to_1_5 input
wire [2:0] rtypeout; //alu cont output to brjmpcont 

wire [5:0] inst31_26;	//31-26 bits of instruction
wire [4:0] 
inst25_21,	//25-21 bits of instruction
inst20_16,	//20-16 bits of instruction
inst15_11,	//15-11 bits of instruction
out1,		//Write data input of Register File

out9; //we added output before writedata(jmadd and jrsal control)

wire [15:0] inst15_0;	//15-0 bits of instruction
wire [25:0] inst25_0;	// we added 25-0 bits of instruction for jump

wire [31:0] instruc,	//current instruction
dpack;	//Read data output of memory (data read from memory)

wire [2:0] gout;	//Output of ALU control unit

wire zout,	//Zero output of ALU
pcsrc,	//Output of AND gate with Branch and ZeroOut inputs
//Control signals
regdest,alusrc,memtoreg,regwrite,memread,memwrite,branch,aluop1,aluop0;

//32-size register file (32 bit(1 word) for each register)
reg [31:0] registerfile[0:31];

integer i;

// datamemory connections

always @(posedge clk)
//write data to memory
if (memwrite)
begin 
//sum stores address,datab stores the value to be written
datmem[sum[4:0]+3]=out10[7:0];
datmem[sum[4:0]+2]=out10[15:8];
datmem[sum[4:0]+1]=out10[23:16];
datmem[sum[4:0]]=out10[31:24];
end

//instruction memory
//4-byte instruction
 assign instruc={mem[pc[4:0]],mem[pc[4:0]+1],mem[pc[4:0]+2],mem[pc[4:0]+3]};
 assign inst31_26=instruc[31:26];
 assign inst25_21=instruc[25:21];
 assign inst20_16=instruc[20:16];
 assign inst15_11=instruc[15:11];
 assign inst15_0=instruc[15:0];
 assign inst25_0=instruc[25:0];


// registers

assign dataa=registerfile[inst25_21];//Read register 1
assign datab=registerfile[inst20_16];//Read register 2
always @(posedge clk)
 registerfile[out1]= regwrite ? out3:registerfile[out1];//Write data to register

//read data from memory, sum stores address
assign dpack={datmem[sum[5:0]],datmem[sum[5:0]+1],datmem[sum[5:0]+2],datmem[sum[5:0]+3]};

//multiplexers
//mux with RegDst control
mult2_to_1_5  mult1(out1, instruc[20:16],instruc[15:11],regdest);

assign jrsaljmaddcont[0] = (~(|brjmpsignal))&rtypeout[1];  //instr jmadd
assign jrsaljmaddcont[1] = brjmpsignal[2]; //instr jmadd
mult4_to_1_5  mult9(out9, out1, instruc[25:21], jrsaljmaddcont); //we added mux for control signals of old oldout,rs and $31

//mux with ALUSrc control
mult2_to_1_32 mult2(out2, datab,extad,alusrc);

//4
mult2_to_1_32 mult10(out10, datab,adder1out,brjmpsignal[2]); // we added mux for pc+4 or readdata2

//mux with MemToReg control
mult2_to_1_32 mult3(out3, sum,dpack,memtoreg);

//mux with (Branch&ALUZero) control
mult2_to_1_32 mult4(out4, adder1out,adder2out,pcsrc);

//we added jump instruction 56789-10-11-12
shift26_to_28 shift3(shiftedjump, instruc[25:0]);	
assign fulljump={adder1out[31:28], shiftedjump};
assign andout = regstatus[1] && bnwire; // take AND operation of bn signal from control and status n=1 from ALU
assign jumpmux = jump | andout; //select bit of jump operation at rightmost corner
mult2_to_1_32 mult5(out5,out4,fulljump,jumpmux);//
mult2_to_1_32 mult6(out6,out5,newaddress,select);//
mult2_to_1_32 mult8(out8,out3,adder1out,jmaddcont);// pc+4 or datamem outout

// load pc=out4
always @(negedge clk)
begin
pc=out6;
regstatus = status;
end

// alu, adder and control logic connections

//ALU unit
alu32 alu1(sum,dataa,out2,zout,gout, status);


//adder which adds PC and 4
adder add1(pc,32'h4,adder1out);

mult2_to_1_32 mult7(out7,dpack,dataa,rsordatamem);
brjmpcont bjc(brjmpsignal, regstatus, rtypeout,newaddress, out7, select);// we added new unit

//adder which adds PC+4 and 2 shifted sign-extend result
adder add2(adder1out,sextad,adder2out);

//Control unit
control cont(instruc[31:26],regdest,alusrc,memtoreg,regwrite,memread,memwrite,branch,
aluop0,aluop1,brjmpsignal, jump, bnwire);

//Sign extend unit
signext sext(instruc[15:0],extad);

//ALU control unit
alucont acont(aluop1,aluop0,instruc[5:0], gout, rtypeout,rsordatamem, jmaddcont);

//Shift-left 2 unit
shift shift2(sextad,extad);

//AND gate
assign pcsrc=branch && zout; 

//initialize datamemory,instruction memory and registers
//read initial data from files given in hex
initial
begin
$readmemh("test/bn_test_DM.dat",datmem); //read Data Memory
$readmemh("test/bn_test_IM.dat",mem);//read Instruction Memory
$readmemh("test/bn_test_Reg.dat",registerfile);//read Register File

	for(i=0; i<31; i=i+1)
	$display("Instruction Memory[%0d]= %h  ",i,mem[i],"Data Memory[%0d]= %h   ",i,datmem[i],
	"Register[%0d]= %h",i,registerfile[i]);

end

initial
begin
pc=0;
#400 $finish;
end

initial
begin
clk=0;
//40 time unit for each cycle
forever #20  clk=~clk;
end
initial 
begin
  $monitor($time,"PC %h",pc,"  SUM %h",sum,"   INST %h",instruc[31:0],
"   REGISTER %h %h %h %h ",registerfile[4],registerfile[5], registerfile[6],registerfile[1] );
end
endmodule

