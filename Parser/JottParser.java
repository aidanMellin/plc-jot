/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

package Parser;
import Tokenizer.*;
import Parser.Nodes.*;

import java.util.ArrayList;

public class JottParser {

    /**
     * Parses an ArrayList of Jott tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){
        try {
            return new ProgramNode(tokens, 0);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        ArrayList<Token> tokens = JottTokenizer.tokenize("Parser/phase3TestCases/largerValid.jott");
        //ArrayList<Token> tokens = JottTokenizer.tokenize("Parser/phase2Tester/parserTestCases/validLoop.jott");
        JottTree jottTree = parse(tokens);
        String output = (jottTree != null) ? jottTree.convertToPython() : "error resulting in null";
        System.out.println(output);
    }
}
