package Parser.Nodes;
import Tokenizer.*;
import Parser.*;

import java.util.ArrayList;

public class StatementNode implements JottTree{

    private ArrayList<JottTree> subnodes = new ArrayList<>();
    private ArrayList<Token> tokens;
    private int tabCount;

    public StatementNode(ArrayList<Token> tokens, int tc) {
        try {
            tabCount = tc;
            this.tokens = tokens;
            assert this.tokens != null;
            if (this.tokens.get(1).getTokenType() == TokenType.L_BRACKET) {
                Token end_stmt = this.tokens.remove(this.tokens.size() - 1);
                subnodes.add(new FunctionCallNode(this.tokens, tabCount));
                subnodes.add(new EndStatementNode(end_stmt, tabCount));
            } else if (this.tokens.size() == 3) {
                subnodes.add(new VariableDeclarationNode(this.tokens, tabCount));
            } else {
                subnodes.add(new AssignmentNode(this.tokens, tabCount));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    public String convertToJott()
    {
        StringBuilder jott_stmt = new StringBuilder();
        for (JottTree node : subnodes) jott_stmt.append(node.convertToJott());
        return "\t".repeat(tabCount) + jott_stmt;
    }

    /**
     * Will output a string of this tree in Java
     * @return a string representing the Java code of this tree
     */
    public String convertToJava()
    {
        return("");
    }

    /**
     * Will output a string of this tree in C
     * @return a string representing the C code of this tree
     */
    public String convertToC()
    {
        return("");
    }

    /**
     * Will output a string of this tree in Python
     * @return a string representing the Python code of this tree
     */
    public String convertToPython()
    {
        return("");
    }

    /**
     * This will validate that the tree follows the semantic rules of Jott
     * Errors validating will be reported to System.err
     * @return true if valid Jott code; false otherwise
     */
    public boolean validateTree()
    {
        return(false);
    }

    public void CreateSyntaxError(String msg, Token token) throws Exception{
        System.err.println("Syntax Error:\n" + msg + "\n" + token.getFilename() + ":" + token.getLineNum());
        throw new Exception();
    }
}
