#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#pragma warning(disable:4996)

//Converts a hexadecimal string to integer.
int hex2int(char* hex)
{
	int result = 0;

	while ((*hex) != '\0')
	{
		if (('0' <= (*hex)) && ((*hex) <= '9'))
			result = result * 16 + (*hex) - '0';
		else if (('a' <= (*hex)) && ((*hex) <= 'f'))
			result = result * 16 + (*hex) - 'a' + 10;
		else if (('A' <= (*hex)) && ((*hex) <= 'F'))
			result = result * 16 + (*hex) - 'A' + 10;
		hex++;
	}
	return(result);
}

main()
{
	FILE* fp;
	char line[100];
	char* token = NULL;
	char* op1, * op2, * op3;
	char ch;
	int  chch;

	int program[1000];
	int counter = 0;  //holds the address of the machine code instruction = program counter(pc)
	int dataarea = 512; 

	fp = fopen("input.txt", "r");

	while (fgets(line, sizeof line, fp) != NULL)
	{
		token = strtok(line, "\n\t\r ");  //get the instruction mnemonic or label
		
		// Instruction set architecture of LDI
		// LDI 0 192 ==> REG0=192
		// 0100 0000 0000 00DESTREG(2bit)
		if (strcmp(token, "ldi") == 0)        //---------------LDI INSTRUCTION--------------------
		{
			op1 = strtok(NULL, "\n\t\r ");                                //get the 1st operand of ldi, which is the register that ldi loads
			op2 = strtok(NULL, "\n\t\r ");                                //get the 2nd operand of ldi, which is the data that is to be loaded
			program[counter] = 0x4000 + hex2int(op1);                     //generate the first 16-bit of the ldi instruction
			// yukarýdaki satýrda opcode(01 ama hex þeklinde 0100 = 0x4)
			// sonra destination registerý ekliyor
			counter++;                                                   //move to the second 16-bit of the ldi instruction
			// geri kalan kýsýmda pc++ yaptýktan sonra altta okunacak deðerin 
			// hex veya decimal verilip verilmediðine bakýyor ve okuyor
			// eðer ikiside deðilse hata veriyor...
			if ((op2[0] == '0') && (op2[1] == 'x'))                            		//if the 2nd operand is twos complement hexadecimal
				program[counter] = hex2int(op2 + 2) & 0xffff;              			//convert it to integer and form the second 16-bit 
			else if (((op2[0]) == '-') || ((op2[0] >= '0') && (op2[0] <= '9')))     //if the 2nd operand is decimal 
				program[counter] = atoi(op2) & 0xffff;                         		//convert it to integer and form the second 16-bit 
			else                                                           			//if the second operand is not decimal or hexadecimal, it is a laber or a variable.
			{                                                               		//in this case, the 2nd 16-bits of the ldi instruction cannot be generated.
				printf("unrecognizable LDI offset\n");
			}
			counter++;                                                     			//skip to the next memory location 
		}
		// Instruction set architecture of ADD
		// ADD 0 1 2 ==> REG0 = REG1 + REG2
		// 1000 0000 SRCL(2bit)0 SRCR(2bit)0 DESTREG(2bit)
		else if (strcmp(token, "add") == 0) //----------------- ADD -------------------------------
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCR ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			op3 = strtok(NULL, "\n\t\r ");	// SRCL ye gelecek, 6bit shift edilmeli ki Instruction set architecture'a uysun
			chch = (op1[0] - 48) | ((op2[0] - 48) << 3) | ((op3[0] - 48) << 6); // Instruction set architecture'daki son 8 biti oluþturdu ama
																				// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
																				// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8000 + ((chch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0000 = 0x80 birleþtircek
			counter++;										
		}
		// Instruction set architecture of SUB
		// 1000 0001 SRCL(2bit)0 SRCR(2bit)0 DESTREG(2bit)
		else if (strcmp(token, "sub") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCR ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			op3 = strtok(NULL, "\n\t\r ");	// SRCL ye gelecek, 6bit shift edilmeli ki Instruction set architecture'a uysun
			chch = (op1[0] - 48) | ((op2[0] - 48) << 3) | ((op3[0] - 48) << 6);// Instruction set architecture'daki son 8 biti oluþturdu ama
																				// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
																				// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8100 + ((chch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0001 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of AND
		// AND 0 1 2 ==> REG0 = REG1 & REG2
		// 1000 0010 SRCL(2bit)0 SRCR(2bit)0 DESTREG(2bit)
		else if (strcmp(token, "and") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCR ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			op3 = strtok(NULL, "\n\t\r ");	// SRCL ye gelecek, 6bit shift edilmeli ki Instruction set architecture'a uysun
			chch = (op1[0] - 48) | ((op2[0] - 48) << 3) | ((op3[0] - 48) << 6);// Instruction set architecture'daki son 8 biti oluþturdu ama
																				// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
																				// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8200 + ((chch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0010 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of OR
		// OR 0 1 2 ==> REG0 = REG1 | REG2
		// 1000 0011 SRCL(2bit)0 SRCR(2bit)0 DESTREG(2bit)
		else if (strcmp(token, "or") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCR ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			op3 = strtok(NULL, "\n\t\r ");	// SRCL ye gelecek, 6bit shift edilmeli ki Instruction set architecture'a uysun
			chch = (op1[0] - 48) | ((op2[0] - 48) << 3) | ((op3[0] - 48) << 6);// Instruction set architecture'daki son 8 biti oluþturdu ama
																				// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
																				// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8300 + ((chch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0011 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of XOR
		// XOR 0 1 2 ==> REG0 = REG1 ^ REG2
		// 1000 0100 SRCL(2bit)0 SRCR(2bit)0 DESTREG(2bit)
		else if (strcmp(token, "xor") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCR ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			op3 = strtok(NULL, "\n\t\r ");	// SRCL ye gelecek, 6bit shift edilmeli ki Instruction set architecture'a uysun
			chch = (op1[0] - 48) | ((op2[0] - 48) << 3) | ((op3[0] - 48) << 6);// Instruction set architecture'daki son 8 biti oluþturdu ama
																				// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
																				// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8400 + ((chch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0100 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of NOT
		// NOT 0 1 ==> REG0 = !REG1
		// 1000 0101 000 SRCR(2bit)0 DESTREG(2bit)
		else if (strcmp(token, "not") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCR ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			ch = (op1[0] - 48) | ((op2[0] - 48) << 3);// Instruction set architecture'daki son 8 biti oluþturdu ama
													// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
													// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8500 + ((ch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0101 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of MOV
		// MOV 0 1 ==> REG0 = REG1
		// 1000 0110 SRCL(2bit)00 00DESTREG(2bit)
		else if (strcmp(token, "mov") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			op2 = strtok(NULL, "\n\t\r ");	// SRCL ye gelecek, 3bit shift edilmeli ki Instruction set architecture'a uysun
			// op2 yi muxl ye yolla, op1 destinationda kalsýn
			// muxr içinse << 3 yap
			ch = (op1[0] - 48) | ((op2[0] - 48) << 6);// Instruction set architecture'daki son 8 biti oluþturdu ama
													// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
													// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8600 + ((ch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0110 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of INC
		// INC 0 ==> REG0 = REG0+1
		// 1000 0111 0000 00DESTREG(2bit)
		else if (strcmp(token, "inc") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			ch = (op1[0] - 48) | ((op1[0] - 48) << 3);// Instruction set architecture'daki son 8 biti oluþturdu ama
													// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
													// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8700 + ((ch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 0111 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of DEC
		// DEC 0 ==> REG0 = REG0-1
		// 1000 1000 0000 00DESTREG(2bit)
		else if (strcmp(token, "dec") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");	// DESTREG e gelecek, hiç shift edilmemeli ki Instruction set architecture'a uysun
			ch = (op1[0] - 48) | ((op1[0] - 48) << 3);// Instruction set architecture'daki son 8 biti oluþturdu ama
													// bunu 16 bitlik integerda tutuyor onun ilk 8 bitini sýfýrlayýp
													// devamýný tutabilmek için 0000 0000 1111 1111 ile and gateliyo
			program[counter] = 0x8800 + ((ch) & 0x00ff);	// oluþturduðu son 8 bitle beraber ilk 8 biti yani 1000 1000 = 0x80 birleþtircek
			counter++;
		}
		// Instruction set architecture of ADDI
		// ADDI 0 1 19F ==> REG0 = REG1 + 19F
		// 1100 0000 SRCL(2bit)00 00DESTREG(2bit)
		// Bu immadiate kýsmýndaki kodlarýn hepsi ldi kýsmýndan geliyo
		else if (strcmp(token, "addi") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");                              //get the 1st operand of addi, which is the destination register
			op2 = strtok(NULL, "\n\t\r ");                              //get the 2nd operand of addi, which is the register that used in operation
			op3 = strtok(NULL, "\n\t\r ");								//get the 3rd operand of addi, which is the immediate value that is to be loaded
			ch = (op1[0] - 48) | ((op2[0] - 48) << 6);	// SRCL ye gelecek þekilde shift et, destreg'e karýþma
			program[counter] = 0xc000 + ((ch) & 0x00ff);                        
			counter++;                                                   //move to the second 16-bit of the ldi instruction
			if ((op3[0] == '0') && (op3[1] == 'x'))                            //if the 2nd operand is twos complement hexadecimal
				program[counter] = hex2int(op3 + 2) & 0xffff;              //convert it to integer and form the second 16-bit 
			else if (((op3[0]) == '-') || ((op3[0] >= '0') && (op3[0] <= '9')))       //if the 2nd operand is decimal 
				program[counter] = atoi(op3) & 0xffff;                         //convert it to integer and form the second 16-bit 
			else                                                           //if the second operand is not decimal or hexadecimal, it is a laber or a variable.
			{                                                               //in this case, the 2nd 16-bits of the ldi instruction cannot be generated.
				printf("unrecognizable ADDI offset\n");
			}
			counter++;
		}
		// Instruction set architecture of SUBI
		// SUBI 0 1 19F ==> REG0 = REG1 - 19F
		// 1100 0001 SRCL(2bit)00 00DESTREG(2bit)
		else if (strcmp(token, "subi") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");                              //get the 1st operand of addi, which is the destination register
			op2 = strtok(NULL, "\n\t\r ");                              //get the 2nd operand of addi, which is the register that used in operation
			op3 = strtok(NULL, "\n\t\r ");								//get the 3rd operand of addi, which is the immediate value that is to be loaded
			ch = (op1[0] - 48) | ((op2[0] - 48) << 6);// SRCL ye gelecek þekilde shift et, destreg'e karýþma
			program[counter] = 0xc100 + ((ch) & 0x00ff);
			counter++;                                                   //move to the second 16-bit of the ldi instruction
			if ((op3[0] == '0') && (op3[1] == 'x'))                            //if the 2nd operand is twos complement hexadecimal
				program[counter] = hex2int(op3 + 2) & 0xffff;              //convert it to integer and form the second 16-bit 
			else if (((op3[0]) == '-') || ((op3[0] >= '0') && (op3[0] <= '9')))       //if the 2nd operand is decimal 
				program[counter] = atoi(op3) & 0xffff;                         //convert it to integer and form the second 16-bit 
			else                                                           //if the second operand is not decimal or hexadecimal, it is a laber or a variable.
			{                                                               //in this case, the 2nd 16-bits of the ldi instruction cannot be generated.
				printf("unrecognizable SUBI offset\n");
			}
			counter++;

		}
		// Instruction set architecture of SUBI
		// SUBI 0 1 19F ==> REG0 = REG1 - 19F
		// 1100 0010 SRCL(2bit)00 00DESTREG(2bit)
		else if (strcmp(token, "andi") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");                              //get the 1st operand of addi, which is the destination register
			op2 = strtok(NULL, "\n\t\r ");                              //get the 2nd operand of addi, which is the register that used in operation
			op3 = strtok(NULL, "\n\t\r ");								//get the 3rd operand of addi, which is the immediate value that is to be loaded
			ch = (op1[0] - 48) | ((op2[0] - 48) << 6);
			program[counter] = 0xc200 + ((ch) & 0x00ff);
			counter++;                                                   //move to the second 16-bit of the ldi instruction
			if ((op3[0] == '0') && (op3[1] == 'x'))                            //if the 2nd operand is twos complement hexadecimal
				program[counter] = hex2int(op3 + 2) & 0xffff;              //convert it to integer and form the second 16-bit 
			else if (((op3[0]) == '-') || ((op3[0] >= '0') && (op3[0] <= '9')))       //if the 2nd operand is decimal 
				program[counter] = atoi(op3) & 0xffff;                         //convert it to integer and form the second 16-bit 
			else                                                           //if the second operand is not decimal or hexadecimal, it is a laber or a variable.
			{                                                               //in this case, the 2nd 16-bits of the ldi instruction cannot be generated.
				printf("unrecognizable ANDI offset\n");
			}
			counter++;
		}
		// Instruction set architecture of ORI
		// ORI 0 1 19F ==> REG0 = REG1 | 19F
		// 1100 0011 SRCL(2bit)00 00DESTREG(2bit)
		else if (strcmp(token, "ori") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");                              //get the 1st operand of addi, which is the destination register
			op2 = strtok(NULL, "\n\t\r ");                              //get the 2nd operand of addi, which is the register that used in operation
			op3 = strtok(NULL, "\n\t\r ");								//get the 3rd operand of addi, which is the immediate value that is to be loaded
			ch = (op1[0] - 48) | ((op2[0] - 48) << 6);
			program[counter] = 0xc300 + ((ch) & 0x00ff);
			counter++;                                                   //move to the second 16-bit of the ldi instruction
			if ((op3[0] == '0') && (op3[1] == 'x'))                            //if the 2nd operand is twos complement hexadecimal
				program[counter] = hex2int(op3 + 2) & 0xffff;              //convert it to integer and form the second 16-bit 
			else if (((op3[0]) == '-') || ((op3[0] >= '0') && (op3[0] <= '9')))       //if the 2nd operand is decimal 
				program[counter] = atoi(op3) & 0xffff;                         //convert it to integer and form the second 16-bit 
			else                                                           //if the second operand is not decimal or hexadecimal, it is a laber or a variable.
			{                                                               //in this case, the 2nd 16-bits of the ldi instruction cannot be generated.
				printf("unrecognizable ORI offset\n");
			}
			counter++;
		}
		// Instruction set architecture of XORI
		// XORI 0 1 19F ==> REG0 = REG1 ^ 19F
		// 1100 0100 SRCL(2bit)00 00DESTREG(2bit)
		else if (strcmp(token, "xori") == 0)
		{
			op1 = strtok(NULL, "\n\t\r ");                              //get the 1st operand of addi, which is the destination register
			op2 = strtok(NULL, "\n\t\r ");                              //get the 2nd operand of addi, which is the register that used in operation
			op3 = strtok(NULL, "\n\t\r ");								//get the 3rd operand of addi, which is the immediate value that is to be loaded
			ch = (op1[0] - 48) | ((op2[0] - 48) << 6);
			program[counter] = 0xc400 + ((ch) & 0x00ff);
			counter++;                                                   //move to the second 16-bit of the ldi instruction
			if ((op3[0] == '0') && (op3[1] == 'x'))                            //if the 2nd operand is twos complement hexadecimal
				program[counter] = hex2int(op3 + 2) & 0xffff;              //convert it to integer and form the second 16-bit 
			else if (((op3[0]) == '-') || ((op3[0] >= '0') && (op3[0] <= '9')))       //if the 2nd operand is decimal 
				program[counter] = atoi(op3) & 0xffff;                         //convert it to integer and form the second 16-bit 
			else                                                           //if the second operand is not decimal or hexadecimal, it is a laber or a variable.
			{                                                               //in this case, the 2nd 16-bits of the ldi instruction cannot be generated.
				printf("unrecognizable XORI offset\n");
			}
			counter++;
		}
		else //------WHAT IS ENCOUNTERED IS NOT A VALID INSTRUCTION OPCODE
		{
			printf("no valid opcode\n");
		}
	} //while

	fclose(fp);
	FILE* fpv = fopen("output_v.txt", "w");
	fp = fopen("output_l.txt", "w");
	fprintf(fp, "v2.0 raw\n");  //needed for logisim, remove this line for verilog..
	for (int i = 0; i < counter; i++){  //complete this for memory size in verilog
		fprintf(fp, "%04x\n", program[i]);
		fprintf(fpv, "%04x\n", program[i]);
	}
	for (int i = 0; i < dataarea - counter; i++)
		fprintf(fpv, "%04x\n", 0000);
	
	fclose(fp);
	fclose(fpv);
} //main