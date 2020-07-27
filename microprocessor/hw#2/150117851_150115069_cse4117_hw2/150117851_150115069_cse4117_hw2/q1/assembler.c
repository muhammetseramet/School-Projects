#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>



int binary_to_hexa_converter(int binary_num)
{
    int i = 1,hexa= 0, rem;

    while (binary_num != 0)
    {
        rem = binary_num % 10;
        hexa = hexa + rem * i;
        i = i * 2;
        binary_num = binary_num / 10;
    }

    return hexa;
}


//Converts a hexadecimal string to integer.
int hex2int( char* hex)
{
    int result=0;

    while ((*hex)!='\0')
    {
        if (('0'<=(*hex))&&((*hex)<='9'))
            result = result*16 + (*hex) -'0';
        else if (('a'<=(*hex))&&((*hex)<='f'))
            result = result*16 + (*hex) -'a'+10;
        else if (('A'<=(*hex))&&((*hex)<='F'))
            result = result*16 + (*hex) -'A'+10;
        hex++;
    }
    return(result);
}

char *decimal_to_binary_converter(int decimal_num)
{
    int a, b, cnt;
    char *ptr;

    cnt = 0;
    ptr = (char*)malloc(2+1);



    for (a = 2 ; a >= 0 ; a--)
    {
        b = decimal_num >> a;

        if (b & 1)
            *(ptr+cnt) = 1 + '0';
        else
            *(ptr+cnt) = 0 + '0';

        cnt++;
    }
    *(ptr+cnt) = '\0';

    return  ptr;
}

main()
{


    FILE *fp;
    char line[100];
    char *token = NULL;
    char *op1, *op2, *op3, *label;
    char ch;
    int  chch;
    char sp = '9';
    int jumpdistance;

    int program[1000];
    int instructions[1000];
    int counter=0;
    char* REGISTER0;
	char* REGISTER1;
	char* REGISTER2;
	char* REGISTER3;
	char* REGISTER4;
	char* REGISTER5;
	char* REGISTER6;
	char* REGISTER7;
    REGISTER0 = decimal_to_binary_converter(0);
    REGISTER1 = decimal_to_binary_converter(1);
    REGISTER2 = decimal_to_binary_converter(2);
    REGISTER3 = decimal_to_binary_converter(3);
    REGISTER4 = decimal_to_binary_converter(4);
    REGISTER5 = decimal_to_binary_converter(5);
    REGISTER6 = decimal_to_binary_converter(6);
    REGISTER7 = decimal_to_binary_converter(7);



    
    struct label
    {
        int location;
        char *label;
    };
    struct label labeltable[50];
    int nooflabels = 0;


    struct jumpinstruction
    {
        int location;
        char *label;
    };
    struct jumpinstruction jumptable[100];
    int noofjumps=0;


    struct variable
    {
        int location;
        char *name;
    };
    struct variable variabletable[50];
    int noofvariables = 0;


    struct ldiinstruction
    {
        int location;
        char *name;
    };
    struct ldiinstruction lditable[100];
    int noofldis=0;


    char *input_file_txt = "input_code.txt";

    
    fp = fopen(input_file_txt,"r");

    if (fp != NULL)
    {
        counter = 0;
        while(fgets(line,sizeof line,fp)!= NULL)
        {
            token=strtok(line,"\n\t\r ");
            if (strcmp(token,".code")==0)
            {
                printf("\nsegment .code\n");
                break;
            }
        }
        while(fgets(line,sizeof line,fp)!= NULL)
        {
            token=strtok(line,"\n\t\r ");
            if (token[strlen(token) - 1] == ':')
            {
                char *p = token;
                p[strlen(token)-1] = 0;
                printf("label\t%s\t%d\n", token, counter);
                labeltable[nooflabels].location = counter;
                op1=(char*)malloc(sizeof(token));
                strcpy(op1,token);
                labeltable[nooflabels].label=op1;
                nooflabels++;

                counter--;
            }

            if (strcmp(token,"ldi")==0)
            {
                counter++;
                counter++;

            }
            else
                counter++;


        }

    }


    fp = fopen(input_file_txt,"r");

    counter = 0;

    while(fgets(line,sizeof line,fp)!= NULL)
    {
        token=strtok(line,"\n\t\r ");
        if (strcmp(token,".code")==0)
        {
            printf("\nsegment .code\n");
            break;
        }
    }
    while(fgets(line,sizeof line,fp)!= NULL)
    {
        token=strtok(line,"\n\t\r ");

//  FIRST PASS
        while (token)
        {
            if (strcmp(token,"ldi")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");                               
                op2 = strtok(NULL,"\n\t\r ");                                
                printf("\n\t%s\t%s   %s\n",strupr(token),op1,op2);			
                program[counter]=0x1000+(int)strtol(op1, NULL, 0);                        
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;                                                   
                if ((op2[0]=='0')&&(op2[1]=='x'))
                {

                    program[counter]=hex2int(op2+2)&0xffff;
                }
                else if ((  (op2[0])=='-') || ((op2[0]>='0')&&(op2[0]<='9')))
                {

                    program[counter]=atoi(op2)&0xffff;
                }
                else                                                           
                {
                    
                    lditable[noofldis].location = counter;                 
                    op1=(char*)malloc(sizeof(op2));                        
                    strcpy(op1,op2);                                       
                    lditable[noofldis].name = op1;
                    noofldis++;
                }
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
                printf("%d",counter);
            }
            else if (strcmp(token,"ld")==0)
            {

                op1 = strtok(NULL,"\n\t\r ");                //get the 1st operand of ld, which is the destination register
                op2 = strtok(NULL,"\n\t\r ");                //get the 2nd operand of ld, which is the source register
                printf("\n\t%s\t%s   %s\n",strupr(token),op1,op2);
                int dest = op1[0]-48 ; // dest
                int src1 = op2[0]-48 ; // src2
                char str_st[9];
                unsigned int ch2;
                char* dest_1, *src1_2;
                dest_1 = decimal_to_binary_converter(dest);
                src1_2 = decimal_to_binary_converter(src1);
                strcpy(str_st,"");
                strcat(str_st, REGISTER0);
                strcat(str_st, REGISTER0);
                strcat(str_st,src1_2);
                strcat(str_st,dest_1);
                int num = atoi(str_st);
                ch2 = binary_to_hexa_converter(num);
                program[counter]=0x2000+((ch2)&0xffff);
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
                free(src1_2);
                free(dest_1);
                //skip to the next empty location in memory
            }
            else if (strcmp(token,"st")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");                //get the 1st operand of ld, which is the destination register
                op2 = strtok(NULL,"\n\t\r ");                //get the 2nd operand of ld, which is the source register
                printf("\n\t%s\t%s   %s\n",strupr(token),op1,op2);
                int dest = op2[0]-48 ; // dest
                int src1 = op1[0]-48 ; // src1
                char str_st[20];
                char* dest_1, *src1_2;
                dest_1 = decimal_to_binary_converter(dest);
                src1_2 = decimal_to_binary_converter(src1);
                strcpy(str_st, REGISTER0);
                strcat(str_st, src1_2);
                strcat(str_st,dest_1);
                strcat(str_st,REGISTER0);
                unsigned int ch = strtol(str_st, NULL,2);
                free(dest_1);
                free(src1_2);
                program[counter]=0x3000+((ch)&0x01ff);       //form the instruction and write it to memory
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;                                   //skip to the next empty location in memory
            }

            else if (strcmp(token,"jmp")==0)  
            {
                op1 = strtok(NULL,"\n\t\r ");		
                printf("\n\t%s\t%s\n",strupr(token),op1);
                jumptable[noofjumps].location = counter;	
                op2=(char*)malloc(sizeof(op1)); 		
                strcpy(op2,op1);				
                jumptable[noofjumps].label=op2;			
                noofjumps++;					

                program[counter]=0x5000;
                int n;


                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;					
            }
            else if (strcmp(token,"jz")==0) 
            {
                op1 = strtok(NULL,"\n\t\r ");	
                printf("\n\t%s\t%s\n",strupr(token),op1);
                jumptable[noofjumps].location = counter;	
                op2=(char*)malloc(sizeof(op1)); 		
                strcpy(op2,op1);				
                jumptable[noofjumps].label=op2;			
                noofjumps++;					

                program[counter]=0x4000;
                int n;


                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;					
            }
            else if (strcmp(token,"add")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                op2 = strtok(NULL,"\n\t\r "); 
                op3 = strtok(NULL,"\n\t\r "); 
                printf("\n\t%s\t%s   %s   %s\n",strupr(token),op1,op2,op3);
                chch = (op1[0]-48) | ((op3[0]-48)<<3) | ((op2[0]-48)<<6) | (0<<9);
                program[counter]=0x7000+((chch)&0xffff); 
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"sub")==0)
            {

                op1 = strtok(NULL,"\n\t\r ");
                op2 = strtok(NULL,"\n\t\r "); 
                op3 = strtok(NULL,"\n\t\r "); 
                printf("\n\t%s\t%s   %s   %s\n",strupr(token),op1,op2,op3);
                chch = (op1[0]-48) | ((op3[0]-48)<<3) | ((op2[0]-48)<<6) | (1<<9) ;
                program[counter]=0x7000+((chch)&0xffff); 
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"and")==0)
            {
                op1 = strtok(NULL,"\n\t\r "); 
                op2 = strtok(NULL,"\n\t\r "); 
                op3 = strtok(NULL,"\n\t\r "); 
                printf("\n\t%s\t%s   %s   %s\n",strupr(token),op1,op2,op3);

                int dest = op1[0]-48 ;
                int src1 = op2[0]-48 ;
                int src2 = op3[0]-48 ;
                char str[20];
                char* dest_1, * src1_2, *src2_2;
                dest_1 = decimal_to_binary_converter(dest);
                src1_2 = decimal_to_binary_converter(src1);
                src2_2 = decimal_to_binary_converter(src2);
                strcpy(str, REGISTER2); 
                strcat(str, src1_2);
                strcat(str,src2_2);
                strcat(str,dest_1);
                unsigned int ch = strtol(str, NULL,2);
                free(dest_1);
                free(src1_2);
                free(src2_2);
                program[counter]=0x7000+((ch)&0xffff);
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"or")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                op2 = strtok(NULL,"\n\t\r ");
                op3 = strtok(NULL,"\n\t\r ");
                int dest = op1[0]-48 ;
                int src1 = op2[0]-48 ;
                int src2 = op3[0]-48 ;
                printf("\n\t%s\t%s   %s   %s\n",strupr(token),op1,op2,op3);
                char binary[20];
                char* dest_1, * src1_2, *src2_2;
                dest_1 = decimal_to_binary_converter(dest);
                src1_2 = decimal_to_binary_converter(src1);
                src2_2 = decimal_to_binary_converter(src2);
                strcpy(binary, REGISTER3); 
                strcat(binary, src1_2);
                strcat(binary,src2_2);
                strcat(binary,dest_1);
                unsigned int ch = strtol(binary, NULL,2);
                free(dest_1);
                free(src1_2);
                free(src2_2);

                program[counter]=0x7000+((ch)&0xffff);

                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"xor")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                op2 = strtok(NULL,"\n\t\r ");
                op3 = strtok(NULL,"\n\t\r ");
                int dest = op1[0]-48 ;
                int src1 = op2[0]-48 ;
                int src2 = op3[0]-48 ;
                printf("\n\t%s\t%s   %s   %s\n",strupr(token),op1,op2,op3);
                char binary_code[20];
                char* dest_1, * src1_2, *src2_2;
                dest_1 = decimal_to_binary_converter(dest);
                src1_2 = decimal_to_binary_converter(src1);
                src2_2 = decimal_to_binary_converter(src2);
                strcpy(binary_code, REGISTER4); 
                strcat(binary_code, src1_2);
                strcat(binary_code,src2_2);
                strcat(binary_code,dest_1);
                unsigned int ch = strtol(binary_code, NULL,2);
                free(dest_1);
                free(src1_2);
                free(src2_2);
                program[counter]=0x7000+((ch)&0xffff); 

                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"not")== 0)
            {
                op1 = strtok(NULL,"\n\t\r ");  
                op2 = strtok(NULL,"\n\t\r ");  
                printf("\n\t%s\t%s   %s\n",strupr(token),op1,op2);
                ch = (op1[0]-48) | ((op2[0]-48)<<3 | 0<<6 | 7 << 9);
                program[counter]=0x7e00+((ch)&0xffff); 
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"mov")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                op2 = strtok(NULL,"\n\t\r ");
                printf("\n\t%s\t%s   %s\n",strupr(token),op1,op2);
                ch = (op1[0]-48) | ((op2[0]-48)<<3) | 1<<6  ;
                program[counter]=0x7e00+((ch)&0xffff); // 7 opcode of ALU operations, 6 ALU code of MOV operation
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"inc")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                printf("\n\t%s\t%s\n",strupr(token),op1);
                int x =op1[0]-48;
                ch = (x | ((x)<<3)) ;
                program[counter]=0x7e80+((ch)&0xffff); // 7 opcode of ALU operations, 7 ALU code of INC operation
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"dec")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                printf("\n\t%s\t%s\n",strupr(token),op1);
                int x = op1[0]-48;
                ch = (x) | ((x)<<3);
                program[counter]=0x7ec0+((ch)&0xffff); // 0111 / 111 / 011 / op1 / op1
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"push")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                printf("\n\t%s\t%s\n",strupr(token),op1);
                int x = op1[0]-48 ;
                char *pointer,str[30];
                pointer = decimal_to_binary_converter(x);
                strcpy(str, REGISTER0);
                strcat(str, pointer);
                strcat(str, REGISTER7); // -< stack reg
                strcat(str, REGISTER0);
                free(pointer);
                unsigned int ch = strtol(str, NULL, 2);
                program[counter]=0x8000+((ch)&0x01ff);
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"pop")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");
                printf("\n\t%s\t%s\n",strupr(token),op1);
                int x = op1[0]-48 ;
                char *pointer,str[30];
                pointer = decimal_to_binary_converter(x);
                strcpy(str, REGISTER0);
                strcat(str, REGISTER0);
                strcat(str, REGISTER7);
                strcat(str, pointer);
                free(pointer);
                unsigned int ch = strtol(str, NULL, 2);
                program[counter]=0x9000+((ch)&0xffff); 

                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }
            else if (strcmp(token,"call")==0)
            {
                op1 = strtok(NULL,"\n\t\r ");			
                printf("\n\t%s\t%s\n",strupr(token),op1);
                jumptable[noofjumps].location = counter;	
                op2=(char*)malloc(sizeof(op1)); 	
                strcpy(op2,op1);				//copy the label into the allocated space
                jumptable[noofjumps].label=op2;			//point to the label from the jumptable
                noofjumps++;					//skip to the next empty location in jumptable

                int n;
                program[counter]=0xa000;

                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }


            else if (strcmp(token,"ret")==0)
            {
                printf("\n\t%s\n",strupr(token));
                program[counter]=0xb000; // 7 opcode of ALU operations, 8 ALU code of DEC operation
                printf("> %d\t%04x\n",counter,program[counter]);
                counter++;
            }

            token = strtok(NULL,",\n\t\r ");
        }
    }


//================================= SECOND PASS ==============================

    //supply the address fields of the jump and jz instructions from the

    int i,j;
    for (i=0; i<noofjumps; i++)                                                                  
    {
        j=0;
        while ( strcmp(jumptable[i].label, labeltable[j].label) != 0 )              
            j++;                                                                
        program[jumptable[i].location] +=(labeltable[j].location-jumptable[i].location-1)&0x0fff;       
    }






    rewind(fp);
    while(fgets(line,sizeof line,fp)!= NULL)
    {
        token=strtok(line,"\n\t\r ");
        if (strcmp(token,".data")==0)
        {
            printf("\nsegment .data\n");
            break;
        }

    }

    int dataarea=0;
    while(fgets(line,sizeof line,fp)!= NULL)
    {
        token=strtok(line,"\n\t\r ");
        if (strcmp(token,".code")==0)  //go till the .code segment
        {
            printf("\nsegment .code\n");
            break;
        }
        else if (token[strlen(token)-1]==':')
        {
            token[strlen(token)-1]='\0';
            variabletable[noofvariables].location=counter+dataarea;
            op1=(char*)malloc(sizeof(token));
            strcpy(op1,token);
            variabletable[noofvariables].name=op1;
            token = strtok(NULL,",\n\t\r ");
            if (token==NULL)
                program[counter+dataarea]=0;
            else if (strcmp(token, ".space")==0)
            {
                token=strtok(NULL,"\n\t\r ");
                dataarea+=atoi(token);
            }
            else if((token[0]=='0')&&(token[1]=='x'))
                program[counter+dataarea]=(int)strtol(token, NULL, 0)&0xffff;
            else if ((  (token[0])=='-') || ('0'<=(token[0])&&(token[0]<='9'))  )
                program[counter+dataarea]=atoi(token)&0xffff;
            noofvariables++;
            dataarea++;
        }
    }

//display the resulting tables

    printf("LABEL TABLE\n");
    for (i=0; i<nooflabels; i++)
        printf("%d %s\n", labeltable[i].location, labeltable[i].label);
    printf("\n");
    printf("JUMP TABLE\n");
    for (i=0; i<noofjumps; i++)
        printf("%d %s\n", jumptable[i].location, jumptable[i].label);
    printf("\n");
    printf("VARIABLE TABLE\n");
    for (i=0; i<noofvariables; i++)
        printf("%d %s\n", variabletable[i].location, variabletable[i].name);
    printf("\n");
    printf("LDI INSTRUCTIONS\n");
    for (i=0; i<noofldis; i++)
        printf("%d %s\n", lditable[i].location, lditable[i].name);
    printf("\n");
    fclose(fp);

    for (i=0; i<counter; i++)
    {
        printf("%04x\n",program[i]);
    }
    char *output_hex = "output.hex";

    fp = fopen(output_hex,"w");
    fprintf(fp,"v2.0 raw\n");  //needed for logisim, remove this line for verilog..
    for (i=0; i<counter+dataarea; i++) //complete this for memory size in verilog
        fprintf(fp,"%04x\n",program[i]);
    fclose(fp);
    char *output_txt = "output.txt";
    fp = fopen(output_txt,"w");
    for (i=0; i<counter; i++) //complete this for memory size in verilog
        fprintf(fp,"%04x\n",program[i]);

    for (i=0; i<512-counter; i++) //complete this for memory size in verilog -512 fpga memory size
        fprintf(fp,"%04x\n",0000);
    fclose(fp);

}






