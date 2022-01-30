// 8945
// 72482
package objectcodetablegenerator;

import java.io.*;
import java.util.*;

public class Main {
  public Main() {
    // Empty.
  }

  private static Set<String> m_opNameSet = new HashSet<>();
  //private static PrintStream m_debugStream;
  private static final int ObjectCodeTableMapSize = 10;
  private static final byte NopOperator = -112;

  public static void load() {
    try {      
      //m_debugStream = new PrintStream("C:\\Users\\Stefan\\Documents\\A A C_Compiler_Assembler\\nasm\\Result.txt");
      String javaPath = "C:\\Users\\Stefan\\Documents\\A A C_Compiler_Assembler - A 16 bits\\src\\c_compiler\\";
//      m_debugStream = new PrintStream("C:\\Users\\stefanb\\DropBox\\c_compiler\\nasm\\Result.txt");
//      String javaPath = "C:\\Users\\stefanb\\DropBox\\c_compiler\\src\\c_compiler\\";
//      String javaPath = "C:\\Documents and Settings\\sbr02\\My Documents\\Dropbox\\c_compiler\\src\\c_compiler\\ObjectCodeTable.java";

      PrintStream[] printStreamArray = new PrintStream[ObjectCodeTableMapSize];

      for (int index = 0; index < printStreamArray.length; ++index) {
        printStreamArray[index] = new PrintStream(javaPath + "object_code_table\\ObjectCodeTable" + index + ".java");
        printStreamArray[index].println("package c_compiler.object_code_table;");
        printStreamArray[index].println();
        //printStreamArray[index].println("import java.util.*;");
        printStreamArray[index].println("import c_compiler.*;");
//        printStreamArray[index].println("import c_compiler.utility.*;");
        printStreamArray[index].println();
        printStreamArray[index].println("public class ObjectCodeTable" + index + " {");
/*        printStreamArray[index].println("  private static final Map<ObjectCodeInfo,byte[]> ByteArrayMap = new TreeMap<>();");
        printStreamArray[index].println();
        printStreamArray[index].println("  public ObjectCodeTable" + index + "() {");
        printStreamArray[index].println("    Main.MainArrayMap.putAll(ByteArrayMap);");
        printStreamArray[index].println("  }");
        printStreamArray[index].println();
        printStreamArray[index].println("  public static Map<ObjectCodeInfo,byte[]> getByteArrayMap() {");
        printStreamArray[index].println("    return ByteArrayMap;");
        printStreamArray[index].println("  }");
        printStreamArray[index].println();*/
        printStreamArray[index].println("  public static void init() {");
      }

      for (int index = 1; index < printStreamArray.length; ++index) {
        printStreamArray[0].println("    ObjectCodeTable" + index + ".init();");
      }

      String comPath = "C:\\Test\\x";
      read(printStreamArray, comPath);
      
      for (PrintStream printStream : printStreamArray) {
        //printStream.println("    Main.MainArrayMap.putAll(ByteArrayMap);");
        printStream.println("  }");
        printStream.print("}");
        printStream.close();
      }

      m_opNameSet.add("define_value");
      m_opNameSet.add("define_address");
      m_opNameSet.add("define_zero_sequence");
      m_opNameSet.add("call");
      m_opNameSet.add("empty");
      m_opNameSet.add("label");
      m_opNameSet.add("label2");
      m_opNameSet.add("comment");
      m_opNameSet.add("register_return");
      m_opNameSet.add("address_return");
      m_opNameSet.add("setTrackSize");
      m_opNameSet.add("short_jmp");
      m_opNameSet.add("long_jmp");
      
      PrintStream operatorStream = new PrintStream(javaPath + "ObjectOperator.java");
      operatorStream.println("package c_compiler;");
      operatorStream.println();
      operatorStream.print("public enum ObjectOperator {");
      
      boolean first = true;
      for (String opName : m_opNameSet) {
        operatorStream.println((first ? "" : ",") + opName);
        first = false;
      }
      
      operatorStream.println("};");
      operatorStream.close();

      //m_debugStream.close();
//      Main.DebugStream.println("Count: " + m_count);
//      Main.DebugStream.println("Index: " + m_index);

      System.out.println("Count: " + m_count);
      System.out.println("Index: " + m_index);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }        
  }

  private enum Status {None, Load, Store};

  private static boolean isDigits(String text) {
    for (char c : text.substring(0, text.length() - 1).toCharArray()) {
      if (!Character.isDigit(c)) {
        return false;
      }
    }      

    return true;
  }

  /*private static boolean isDigits(String text) {
    if (text.endsWith("h")) {
      for (char c : text.substring(0, text.length() - 1).toCharArray()) {
        if (!Character.isDigit(c)) {
          return false;
        }
      }      
    
      return true;
    }
    
    return false;
  }*/
  
  private static void read(PrintStream[] printStreamArray, String pathName) {
    try {
      File file = new File(pathName);
      if (file.isDirectory()) {
        for (String fileName : file.list()) {
          if (fileName.endsWith(".com")) {
            int comIndex = fileName.lastIndexOf(".com");
            int index3 = fileName.lastIndexOf("_", comIndex - 1);
            int index2 = fileName.lastIndexOf("_", index3 - 1);
            int index1 = fileName.lastIndexOf("_", index2 - 1);

            String operator = fileName.substring(0, index1);
            operator = operator.replace("int", "interrupt");
            m_opNameSet.add(operator);
//            addToNameSet(operator);
            
            String operand1 = fileName.substring(index1 + 1, index2);
            String operand2 = fileName.substring(index2 + 1, index3);
            String operand3 = fileName.substring(index3 + 1, comIndex);

            operand1 = ((isDigits(operand1) || operand1.equals("null")) ? operand1 : ("Register." + operand1));
            operand2 = ((isDigits(operand2) || operand2.equals("null")) ? operand2 : ("Register." + operand2));
            operand3 = ((isDigits(operand3) || operand3.equals("null")) ? operand3 : ("Register." + operand3));
            
            operand1 = operand1.equals("0") ? "0" : operand1;
            operand2 = operand2.equals("0") ? "0" : operand2;
            operand3 = operand3.equals("0") ? "0" : operand3;
            
            operand1 = operand1.equals("1") ? "1" : operand1;
            operand2 = operand2.equals("1") ? "1" : operand2;
            operand3 = operand3.equals("1") ? "1" : operand3;
            
            operand1 = operand1.equals("256") ? "2" : operand1;
            operand2 = operand2.equals("256") ? "2" : operand2;
            operand3 = operand3.equals("256") ? "2" : operand3;
            
            operand1 = operand1.equals("65536") ? "4" : operand1;
            operand2 = operand2.equals("65536") ? "4" : operand2;
            operand3 = operand3.equals("65536") ? "4" : operand3;
            
            operand1 = operand1.equals("4294967296") ? "8" : operand1;
            operand2 = operand2.equals("4294967296") ? "8" : operand2;
            operand3 = operand3.equals("4294967296") ? "8" : operand3;

            operand1 = operand1.equals("256") ? "2" : operand1;
            operand2 = operand2.equals("256") ? "2" : operand2;
            operand3 = operand3.equals("256") ? "2" : operand3;
            
            operand1 = operand1.equals("65536") ? "4" : operand1;
            operand2 = operand2.equals("65536") ? "4" : operand2;
            operand3 = operand3.equals("65536") ? "4" : operand3;
            
            operand1 = operand1.equals("4294967296") ? "8" : operand1;
            operand2 = operand2.equals("4294967296") ? "8" : operand2;
            operand3 = operand3.equals("4294967296") ? "8" : operand3;

            
            if (((m_count++) % FILE_SIZE) == 0) {
              ++m_index;
              System.out.println("index: " + m_index);
            }
/*            else {
              printStreamArray[m_index].print(",\n     ");
            }*/

            printStreamArray[m_index].print("    Main.MainArrayMap.put(");
            printStreamArray[m_index].print("new ObjectCodeInfo(ObjectOperator." +
                                              operator + ", " + operand1 + ", " + operand2 + ", " + operand3 + "), ");
            printStreamArray[m_index].print("new byte[]{");

            boolean firstByte = true;
            File filePath = new File(file, fileName);
            FileInputStream comStream = new FileInputStream(filePath);

            while (comStream.available() != 0) {
              byte b = (byte) comStream.read();
              
              if (b != NopOperator) {
/*                int i  = (int) b;
                
                if (i < 0) {
                  i += 256;
                }
              
                printStreamArray[m_index].print((firstByte ? "" : ", ") + Integer.toString(i));*/
                printStreamArray[m_index].print((firstByte ? "" : ", ") + Byte.toString(b));
                firstByte = false;
              }
            }

            comStream.close();
            printStreamArray[m_index].println("});");

/*            printStreamArray[m_index].println("    { ObjectCodeInfo objectCodeInfo =");
            printStreamArray[m_index].println("        new ObjectCodeInfo(ObjectOperator." +
                                              operator + ", " + operand1 + ", " + operand2 + ", " + operand3 + ");");
            printStreamArray[m_index].print("      byte[] byteArray = new byte[]{");

            boolean firstByte = true;
            File filePath = new File(file, fileName);
            FileInputStream comStream = new FileInputStream(filePath);

            while (comStream.available() != 0) {
              byte b = (byte) comStream.read();
              
              if (b != NopOperator) {
                printStreamArray[m_index].print((firstByte ? "" : ", ") + Byte.toString(b));
                firstByte = false;
              }
            }

            comStream.close();
            printStreamArray[m_index].println("};");
            printStreamArray[m_index].println("      ByteArrayMap.put(objectCodeInfo, byteArray);");
            printStreamArray[m_index].println("    }");
            printStreamArray[m_index].println();*/
            
            /*if ((((m_count++) % FILE_SIZE) == 0) && (m_count > 1)) {
              printStreamArray[m_index].println("  }");
              printStreamArray[m_index].println();
              printStreamArray[m_index].println("  static {");
            }*/            
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

/*  
  private static void read(PrintStream[] printStreamArray, String pathName) {
    try {
      File file = new File(pathName);
      if (file.isDirectory()) {
        for (String fileName : file.list()) {
          if (fileName.endsWith(".com")) {
            int comIndex = fileName.lastIndexOf(".com");
            int index3 = fileName.lastIndexOf("_", comIndex - 1);
            int index2 = fileName.lastIndexOf("_", index3 - 1);
            int index1 = fileName.lastIndexOf("_", index2 - 1);

            String operator = fileName.substring(0, index1);
            operator = operator.replace("int", "interrupt");
            m_opNameSet.add(operator);
//            addToNameSet(operator);
            
            String operand1 = fileName.substring(index1 + 1 , index2);
            String operand2 = fileName.substring(index2 + 1, index3);
            String operand3 = fileName.substring(index3 + 1, comIndex);

            operand1 = ((operand1.equals("1") || operand1.equals("null")) ? operand1 : ("Register." + operand1));
            operand2 = ((operand2.equals("1") || operand2.equals("null")) ? operand2 : ("Register." + operand2));
            operand3 = ((operand3.equals("1") || operand3.equals("null")) ? operand3 : ("Register." + operand3));
            
            if (((m_count++) % FILE_SIZE) == 0) {
              ++m_index;
            }
            else {
              printStreamArray[m_index].print(",\n     ");
            }

            printStreamArray[m_index].print("new MyEntry(new Tuple<>(ObjectOperator." +
                                            operator + "," + operand1 + "," + operand2 + "," + operand3 + "),new byte[]{");

            boolean firstByte = true;
            File filePath = new File(file, fileName);
            FileInputStream comStream = new FileInputStream(filePath);

            while (comStream.available() != 0) {
              printStreamArray[m_index].print((firstByte ? "" : ",") + ((byte) comStream.read()));
              firstByte = false;
            }

            comStream.close();
            printStreamArray[m_index].print("})");
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
*/

  private static void read2(PrintStream[] printStreamArray, String pathName) {
    try {
      File file = new File(pathName);
      if (file.isDirectory()) {
        for (String fileName : file.list()) {
          if (fileName.endsWith(".com")) {
            fileName = fileName.replace(".com", "");

            int index3 = fileName.lastIndexOf("_");            
            int index2 = fileName.lastIndexOf("_", index3 - 1);
            int index1 = fileName.lastIndexOf("_", index2 - 1);

            String operator = fileName.substring(0, index1);
            String operand1 = fileName.substring(index1 + 1 , index2);
            String operand2 = fileName.substring(index2 + 1, index3);
            String operand3 = fileName.substring(index3 + 1);
            
            String shortPrefix = "";

            if (fileName.startsWith("zero_")) {
              shortPrefix = "zero_";
              fileName = fileName.substring(5);
            }
            
            else if (fileName.startsWith("short_")) {
              shortPrefix = "short_";
              fileName = fileName.substring(6);
            }
            
            else if (fileName.startsWith("normal_")) {
              shortPrefix = "normal_";
              fileName = fileName.substring(7);
            }
            
            else if (fileName.startsWith("long_")) {
              shortPrefix = "long_";
              fileName = fileName.substring(5);
            }
            
            String prefix = "";

/*            
            if (fileName.contains("st0") || fileName.contains("st1") ||
                fileName.contains("st2") || fileName.contains("st3") ||
                fileName.contains("st4") || fileName.contains("st5") ||
                fileName.contains("st6") || fileName.contains("st7")) {
              int index  = fileName.lastIndexOf("st");
              Register register = Register.valueOf(fileName.substring(index));
            }
*/

            if (fileName.startsWith("or_word_")  || fileName.startsWith("or_byte_")) {
              prefix = fileName.substring(0, 7);
              fileName = fileName.substring(7);
            }

            if (fileName.startsWith("cmp_word_") || fileName.startsWith("cmp_byte_") ||
                fileName.startsWith("neg_word_") || fileName.startsWith("neg_byte_") ||
                fileName.startsWith("not_word_") || fileName.startsWith("not_byte_") ||
                fileName.startsWith("inc_word_") || fileName.startsWith("inc_byte_") ||
                fileName.startsWith("dec_word_") || fileName.startsWith("dec_byte_") ||
                fileName.startsWith("mul_word_") || fileName.startsWith("mul_byte_") ||
                fileName.startsWith("div_word_") || fileName.startsWith("div_byte_") ||
                fileName.startsWith("add_word_") || fileName.startsWith("add_byte_") ||
                fileName.startsWith("sub_word_") || fileName.startsWith("sub_byte_") ||
                fileName.startsWith("shl_word_") || fileName.startsWith("shl_byte_") ||
                fileName.startsWith("shr_word_") || fileName.startsWith("shr_byte_") ||
                fileName.startsWith("xor_word_") || fileName.startsWith("xor_byte_") ||
                fileName.startsWith("and_word_") || fileName.startsWith("and_byte_")) {
              prefix = fileName.substring(0, 8);
              fileName = fileName.substring(8);
            }

            if (fileName.startsWith("cmp_dword_") || fileName.startsWith("neg_dword_") ||
                fileName.startsWith("not_dword_") || fileName.startsWith("and_dword_") ||
                fileName.startsWith("inc_dword_") || fileName.startsWith("dec_dword_") || 
                fileName.startsWith("shl_dword_") || fileName.startsWith("shr_dword_") ||
                fileName.startsWith("or_dword_")  || fileName.startsWith("xor_dword_") ||
                fileName.startsWith("mul_dword_") || fileName.startsWith("div_dword_") ||
                fileName.startsWith("add_dword_") || fileName.startsWith("sub_dword_")) {
              prefix = fileName.substring(0, 9);
              fileName = fileName.substring(9);
            }

            if (fileName.startsWith("loadi_word_") || fileName.startsWith("loadi_byte_")) {
              prefix = fileName.substring(0, 10);
              fileName = fileName.substring(10);
            }

            if (fileName.startsWith("loadi_dword_")) {
              prefix = fileName.substring(0, 11);
              fileName = fileName.substring(11);
            }

            if (fileName.startsWith("orloadi_word_") || fileName.startsWith("orloadi_byte_")) {
              prefix = fileName.substring(0, 12);
              fileName = fileName.substring(12);
            }

            if (fileName.startsWith("cmp_word_") || fileName.startsWith("cmp_byte_") ||
                fileName.startsWith("neg_word_") || fileName.startsWith("neg_byte_") ||
                fileName.startsWith("not_word_") || fileName.startsWith("not_byte_") ||
                fileName.startsWith("inc_word_") || fileName.startsWith("inc_byte_") ||
                fileName.startsWith("dec_word_") || fileName.startsWith("dec_byte_") ||
                fileName.startsWith("mul_word_") || fileName.startsWith("mul_byte_") ||
                fileName.startsWith("div_word_") || fileName.startsWith("div_byte_") ||
                fileName.startsWith("add_word_") || fileName.startsWith("add_byte_") ||
                fileName.startsWith("sub_word_") || fileName.startsWith("sub_byte_") ||
                fileName.startsWith("shl_word_") || fileName.startsWith("shl_byte_") ||
                fileName.startsWith("shr_word_") || fileName.startsWith("shr_byte_") ||
                fileName.startsWith("xor_word_") || fileName.startsWith("xor_byte_") ||
                fileName.startsWith("and_word_") || fileName.startsWith("and_byte_") ||
                fileName.startsWith("or_dword_") || fileName.startsWith("xor_dword_")) {
              prefix = fileName.substring(0, 8);
              fileName = fileName.substring(8);
            }

            if (fileName.startsWith("cmp_dword_") || fileName.startsWith("neg_dword_") ||
                fileName.startsWith("not_dword_") || fileName.startsWith("and_dword_") ||
                fileName.startsWith("inc_dword_") || fileName.startsWith("dec_dword_") || 
                fileName.startsWith("shl_dword_") || fileName.startsWith("shr_dword_") ||
                fileName.startsWith("mul_dword_") || fileName.startsWith("div_dword_") ||
                fileName.startsWith("add_dword_") || fileName.startsWith("sub_dword_")) {
              prefix = fileName.substring(0, 9);
              fileName = fileName.substring(9);
            }

            if (fileName.startsWith("imul_byte_") || fileName.startsWith("idiv_byte_") ||
                fileName.startsWith("imul_word_") || fileName.startsWith("idiv_word_")) {
              prefix = fileName.substring(0, 9);
              fileName = fileName.substring(9);
            }

            if (fileName.startsWith("imuli_byte_") || fileName.startsWith("idivi_byte_") ||
                fileName.startsWith("imuli_word_") || fileName.startsWith("idivi_word_")) {
              prefix = fileName.substring(0, 10);
              fileName = fileName.substring(10);
            }

            if (fileName.startsWith("imul_dword_") || fileName.startsWith("idiv_dword_") ||
                fileName.startsWith("store_byte_") || fileName.startsWith("store_word_")) {
              prefix = fileName.substring(0, 10);
              fileName = fileName.substring(10);
            }

            if (fileName.startsWith("imuli_dword_") || fileName.startsWith("idivi_dword_")) {
              prefix = fileName.substring(0, 11);
              fileName = fileName.substring(11);
            }

            if (fileName.startsWith("store_dword_")) {
              prefix = fileName.substring(0, 11);
              fileName = fileName.substring(11);
            }

            if (fileName.startsWith("fld_") || fileName.startsWith("fst_")) {
              prefix = fileName.substring(0, 4);
              fileName = fileName.substring(4);
            }

            if (fileName.startsWith("fild_") || fileName.startsWith("fist_") || fileName.startsWith("fstp_")) {
              prefix = fileName.substring(0, 5);
              fileName = fileName.substring(5);
            }

            if (fileName.startsWith("fistp_")) {
              prefix = fileName.substring(0, 6);
              fileName = fileName.substring(6);
            }

            String op = prefix + fileName.substring(0, index1), shortSuffix = "";

            if (index3 != -1) {
              shortSuffix = fileName.substring(index3);
              fileName = fileName.substring(0, index3);
            }
            
//            Status status = op.equals("loadr") ? Status.Store : (op.equals("load") ? Status.Load : Status.None);
            String left = null, right = null;
              
            try {
              left = fileName.substring(index1 + 1, index2);
              right = fileName.substring(index2 + 1);
            }
            catch (Exception e) {
              // Empty.
            }

            left = left.contains("null") ? null : left;
            right = right.contains("null") ? null : right;

            get(printStreamArray, pathName + shortPrefix + prefix + fileName + shortSuffix + ".com", shortPrefix, op, left, right, shortSuffix/*, status*/);
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  //private static final int FILE_SIZE = 1257;
  private static final int FILE_SIZE = 1007;
  //private static final int FILE_SIZE = 499;
  //private static final int FILE_SIZE = 1368;
  private static int m_count = 0;
  private static int m_index = 0;
  
  private static void get(PrintStream[] printStreamArray, String pathName, String shortPrefix, String opName, String left, String right, String shortSuffix/*, Status status*/) throws Exception { 
    if (((m_count++) % FILE_SIZE) == 0) {
      ++m_index;
    }
    else {
      printStreamArray[m_index].print(",\n     ");
    }
    
    opName = (opName.equals("add") && (right == null)) ? "addi" : opName;
    opName = (opName.equals("sub") && (right == null)) ? "subi" : opName;
    opName = (opName.equals("or") && (right == null)) ? "ori" : opName;
    opName = (opName.equals("xor") && (right == null)) ? "xori" : opName;
    opName = (opName.equals("and") && (right == null)) ? "andi" : opName;
    opName = (opName.equals("shl") && (right == null)) ? "shli" : opName;
    opName = (opName.equals("shr") && (right == null)) ? "shri" : opName;
    opName = (opName.equals("cmp") && (right == null)) ? "cmpi" : opName;
    opName = (opName.equals("mov") && (right == null)) ? "movi" : opName;

//    opName = (status == Status.Load) ? "load" : ((status == Status.Store) ? "loadr" : opName);
    opName = opName.equals("int") ? "interrupt" : opName;

    String leftText = (left != null) ? ("Register." + left) : "null";
    String rightText = (right != null) ? ("Register." + right) : "null";

    m_opNameSet.add(opName);
    m_opNameSet.add(shortPrefix + opName + shortSuffix);
    printStreamArray[m_index].print("new MyEntry(new Tuple<>(ObjectOperator." +
                      shortPrefix + opName + shortSuffix + "," + leftText + "," + rightText + "),new byte[]{");

    boolean firstByte = true;
    FileInputStream comStream = new FileInputStream(pathName);
  
/*    ObjectOperator op = ObjectOperator.valueOf(opName);
    
    while (comStream.available() != 0) {
      if ((left == null) && ObjectCode.EQJumpSet.contains(op) && (count > 1)) {
        printStreamArray[m_index].print(",0");
        comStream.read();
      }
      else if ((left == null) && !ObjectCode.EQJumpSet.contains(op) &&
               ObjectCode.CallSet.contains(op) && (count > 0)) {
        printStreamArray[m_index].print(",0");
        comStream.read();
      }
      else {
        printStreamArray[m_index].print((firstByte ? "" : ",") + ((byte) comStream.read()));
      }

      firstByte = false;
      ++count;
    }
 */   
    comStream.close();
    printStreamArray[m_index].print("})");
  }
  
/*  
  private static void addToNameSet(String name) {
    m_opNameSet.add(name);
    
    name = name.replace("zero_", "");
    name = name.replace("short_", "");
    name = name.replace("normal_", "");

    name = name.replace("_zero", "");
    name = name.replace("_short", "");
    name = name.replace("_normal", "");
    name = name.replace("_long", "");

    m_opNameSet.add(name);
  }
*/
  
  public static void main(String args[]) {
    Main.load();
  }
}