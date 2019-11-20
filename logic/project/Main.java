import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static final String FILENAME = "C:\\Users\\Mahmut\\Desktop\\output.txt";
    public static void main(String args[]) throws IOException {


        BufferedWriter out = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(FILENAME, true);
            out = new BufferedWriter(fw);
        }catch (IOException e){
            e.printStackTrace();
        }


        HashMap<String, Integer> opcodes = new HashMap<>();
        opcodes.put("ADD", 0b0000);opcodes.put("ADDI", 0b0001);opcodes.put("AND", 0b0010);opcodes.put("ANDI", 0b0011);
        opcodes.put("LD", 0b0100);opcodes.put("ST", 0b0101);opcodes.put("CMP", 0b0110);opcodes.put("JMP", 0b0111);
        opcodes.put("JB", 0b1000);opcodes.put("JBE", 0b1001);opcodes.put("JA", 0b1010);opcodes.put("JAE", 0b1011);
        opcodes.put("JE", 0b1100); opcodes.put("JE", 0b1101); opcodes.put("JE", 0b1110); opcodes.put("JE", 0b1111);

        HashMap<String, Integer> registers = new HashMap<>();
        registers.put("R0", 0b0000); registers.put("R1", 0b0001); registers.put("R2", 0b0010); registers.put("R3", 0b0011);
        registers.put("R4", 0b0100);registers.put("R5", 0b0101);registers.put("R6", 0b0110);registers.put("R7", 0b0111);
        registers.put("R8", 0b1000);registers.put("R9", 0b1001);registers.put("R10", 0b1010);registers.put("R11", 0b1011);
        registers.put("R12", 0b1100);registers.put("R13", 0b1101);registers.put("R14", 0b1110);registers.put("R15", 0b1111);

        try {
            FileReader reader = new FileReader("C:\\Users\\Mahmut\\Desktop\\input.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            try {
                out.write("v2.0 raw" + "\n");


            } catch (IOException e) {

                e.printStackTrace();
            }

            while ((line = bufferedReader.readLine()) != null) {

                System.out.println("Your code is " + line);

                String[] word = line.split(" ", 8);

                int opcode = opcodes.get(word[0]);
                String hexOp = Integer.toHexString(opcode);
                switch (opcode) {


                    case 0://add
                    case 2://and

                        int dest = registers.get(word[1]);
                        String hexDest = Integer.toHexString(dest);
                        int src1 = registers.get(word[2]);
                        String hexSrc1 = Integer.toHexString(src1);
                        int src2 = registers.get(word[3]);
                        String hexSrc2 = Integer.toHexString(src2);

                        try {
                            out.write(hexOp + hexDest + hexSrc1 + hexSrc2 + " ");


                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                        break;

                    case 1://addi
                    case 3://andi
                        int dest1 = registers.get(word[1]);
                        String hexDest1 = Integer.toHexString(dest1);
                        int src11 = registers.get(word[2]);
                        String hexSrc11 = Integer.toHexString(src11);
                        int immediate = Integer.parseInt(word[3]);
                        String hexImm = Integer.toHexString(immediate);


                        try {
                            out.write(hexOp + hexDest1 + hexSrc11 + hexImm + " " + "");


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        break;

                    case 4: //ld
                    case 5: //st

                        int dest2 = registers.get(word[1]);
                        String hexDest2 = Integer.toHexString(dest2);
                        int offset = Integer.parseInt(word[2]);
                        String hexOffset = Integer.toHexString(offset);
                        try {
                            out.write(hexOp + hexDest2 + hexOffset + " " + "" + "");

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        break;

                    case 6: //cmp
                        int src12 = registers.get(word[2]);
                        String hexSrc12 = Integer.toHexString(src12);
                        int src22 = registers.get(word[3]);
                        String hexSrc22 = Integer.toHexString(src22);
                        try{
                            out.write("" + hexOp + hexSrc12 + hexSrc22 + " ");

                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        break;

                    case 7: //jmp
                    case 8: //jb
                    case 9: //jbe
                    case 10: //ja
                    case 11: //jae
                    case 12: //je
                    case 13: //je
                    case 14: //je
                    case 15: //je
                        int location = Integer.parseInt(word[1]);
                        String hexLocation = Integer.toHexString(location);
                        try{
                            out.write( hexOp + hexLocation + " " + "" + "" + "");

                        }catch (IOException e){
                            e.printStackTrace();
                        }

                }

            }
            reader.close();
            out.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
