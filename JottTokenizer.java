/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

// import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
  public static ArrayList<Token> tokenize(String filename){
      ArrayList<Token> tokens = new ArrayList<>();
      try {
          File file = new File(filename);
          Scanner scanner = new Scanner(file);
          int lineNumber = 1;
          while (scanner.hasNextLine()) {
              String line = scanner.nextLine();
              // System.out.println(line);
              for (int i=0; i<line.length(); i++) {
                
                switch (line.charAt(i)) {
                    case '#' -> {continue;}
                    case ',' -> tokens.add(makeNewToken(filename, lineNumber, ",", TokenType.COMMA));
                    case ']' -> tokens.add(makeNewToken(filename, lineNumber, "[", TokenType.R_BRACKET));
                    case '[' -> tokens.add(makeNewToken(filename, lineNumber, "[", TokenType.L_BRACKET));
                    case '}' -> tokens.add(makeNewToken(filename, lineNumber, "}", TokenType.R_BRACE));
                    case '{' -> tokens.add(makeNewToken(filename, lineNumber, "{", TokenType.L_BRACE));
                    case '=' -> {
                      //Conditional: if no other = -> ASSIGN, if == -> relOp. Else -> Error
                      if(i + 1 < line.length()){
                        char returned = checkEquals(line.charAt(i+1));
                        if (returned == 'a'){
                          tokens.add(makeNewToken(filename, lineNumber, "=", TokenType.ASSIGN));
                        } else {
                          tokens.add(makeNewToken(filename, lineNumber, "==", TokenType.REL_OP));
                          i+=1;
                        }
                      }else{
                        tokens.add(makeNewToken(filename, lineNumber, "=", TokenType.ASSIGN));

                      }
                    } 
                    case '<' -> {
                        if (i+1 < line.length()) {
                            if (checkEquals(line.charAt(i + 1)) == 'r') {
                                tokens.add(makeNewToken(filename, lineNumber, "<=", TokenType.REL_OP));
                                i++;
                            } else {
                                tokens.add(makeNewToken(filename, lineNumber, "<", TokenType.REL_OP));
                            }
                        } else {
                            tokens.add(makeNewToken(filename, lineNumber, "<", TokenType.REL_OP));
                        }
                    }
                    case '>' -> {
                        if (i+1 < line.length()) {
                            if (checkEquals(line.charAt(i + 1)) == 'r') {
                                tokens.add(makeNewToken(filename, lineNumber, ">=", TokenType.REL_OP));
                                i++;
                            } else {
                                tokens.add(makeNewToken(filename, lineNumber, ">", TokenType.REL_OP));
                            }
                        } else {
                            tokens.add(makeNewToken(filename, lineNumber, ">", TokenType.REL_OP));
                        }
                    }
                    case '+','-','*','/' -> tokens.add(makeNewToken(filename, lineNumber, String.valueOf(line.charAt(i)), TokenType.MATH_OP));
                    case ';' -> tokens.add(makeNewToken(filename, lineNumber, ";", TokenType.SEMICOLON));
                    case '.', '1','2','3','4','5','6','7','8','9','0' -> {
                      List<Object> cycled = cycleNumber(line, i);
                      String returned = (String) cycled.get(0);
                      i = (int) cycled.get(1);
                      tokens.add(makeNewToken(filename, lineNumber, returned, TokenType.NUMBER));
                    } // keep cycling for digit. if next token a ., cycle for more digits, then store as number. Else, store as number
                    case 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' -> {} //Keep searching for letter or digit. Store as id/keyword
                    case ':' -> tokens.add(makeNewToken(filename, lineNumber, ":", TokenType.COLON));
                    case '!' -> {
                        if (i+1 < line.length()) {
                            if (checkEquals(line.charAt(i + 1)) == 'r') {
                                tokens.add(makeNewToken(filename, lineNumber, "!=", TokenType.REL_OP));
                                i++;
                            } else {
                                tokens.add(makeNewToken(filename, lineNumber, "!", TokenType.ERROR));
                            }
                        } else {
                            tokens.add(makeNewToken(filename, lineNumber, "!", TokenType.ERROR));
                        }
                    }
                    case '\"' -> {} //Cycle until next " and assign string. if no closing before new line, error
                    default -> {tokens.add(makeError(filename, lineNumber, line, i));}
                }
              }
              lineNumber++;
          }
          System.out.println(tokens);
          scanner.close();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      /**
       * Will need line number from here
       */
    for(Token tok : tokens){
      if (tok.getTokenType() == TokenType.ERROR)
        return null;
    }
		return tokens;
	}

  private static Token makeError(String filename, int lineNumber, String line, Integer i){
    return makeNewToken(filename, lineNumber, String.valueOf(line.charAt(i)), TokenType.ERROR);
  }

  /**
   * 
   * @param c The char following an = in the tokenizer
   * @return char indicating if next token means that it will be accept, relOp, or an Error
   */
  private static char checkEquals(char c){
    if (c != '=')
      return 'a';
    else
      return 'r';
  }

  private static boolean isDigit(char c){
    int checkASCII = (int) c;
    boolean withinBounds = checkASCII <= 57 && checkASCII >= 48;
    return withinBounds;
  }

  /**
   * Basically we want to keep checking the next token to see if it is a valid digit.
   * In the event that it starts with a number, we want to accept a . as it will mean that it is a number with decimals, but not if the process started with a .
   * Will need to update the i value as line is cycled through
   * Lets just have it return the whole body to make it easier to add to tokens
   * @param line
   * @param i
   * @return body of token to be added, updated i value (in list form)
   */

  private static List<Object> cycleNumber(String line, int i){
    String returnedString = "";
    // returnedString += line.charAt(i);
    int count = 0;

    while (true){
      if (line.charAt(i+count) == '.'){
        returnedString += ".";
        count+=1;
      }
      if(count >= line.length()-i)
        break;
      if(isDigit(line.charAt(i+count))){
        while (isDigit(line.charAt(i+count))){
          returnedString += line.charAt(i+count);
          count += 1;
        }
      }else{
        break;
      }
    }

    return Arrays.asList(returnedString, i+count);
  }

  /**
   * 
   * @param filename
   * @param lineNumber
   * @param body
   * @return Token
   */
  public static Token makeNewToken(String filename, int lineNumber, String body, TokenType type){
    Token newToken = new Token(body, filename, lineNumber, type);
    return newToken;
  }


  public static void main(String[] args) {
    /**
     * Need to have the filename assigned
     */
    tokenize("phase1_tester/tokenizerTestCases/phase1ErrorExample.jott");
  }

}