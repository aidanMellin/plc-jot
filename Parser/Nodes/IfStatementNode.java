package Parser.Nodes;
import Tokenizer.*;
import Parser.*;

import java.util.ArrayList;

public class IfStatementNode implements JottTree{

    private final String JOTT_IF = "if";
    private final String LBRACKET_CHAR = "[";
    private final String RBRACKET_CHAR = "]";
    private final String LBRACE_CHAR = "{";
    private final String RBRACE_CHAR = "}";
    private final String ELSE_STRING = "else";
    private final String ELSEIF_STRING = "elseif";

    private ArrayList<JottTree> subnodes;
    private ArrayList<Token> tokens;

    public IfStatementNode(ArrayList<Token> tokens) {
        this.tokens = tokens;
        if(this.tokens.size() == 0) subnodes = null;
        else {
            assert this.tokens.get(0).getTokenType() == TokenType.ID_KEYWORD;
            assert this.tokens.get(0).getToken() == JOTT_IF;
            this.tokens.remove(0);
            assert this.tokens.get(0).getTokenType() == TokenType.L_BRACKET;
            this.tokens.remove(0);
            // TODO: b_expr
            assert this.tokens.get(0).getTokenType() == TokenType.R_BRACKET;
            this.tokens.remove(0);
            assert this.tokens.get(0).getTokenType() == TokenType.L_BRACE;
            this.tokens.remove(0);
            // TODO: body
            assert this.tokens.get(0).getTokenType() == TokenType.R_BRACE;
            this.tokens.remove(0);
            if(this.tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && this.tokens.get(0).getToken() == ELSE_STRING) {
                subnodes.add(new ElseNode(this.tokens));
            }
            else if(this.tokens.get(0).getTokenType() == TokenType.ID_KEYWORD && this.tokens.get(1).getToken() == ELSEIF_STRING) {
                subnodes.add(new ElseIfListNode(this.tokens));
            }
        }
    }

    /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    public String convertToJott()
    {
        if(tokens.size() == 2) {
            return(JOTT_IF + LBRACKET_CHAR + "b_expr" + RBRACKET_CHAR + LBRACE_CHAR + "Body" + RBRACE_CHAR);
        }
        else {
            return(JOTT_IF + LBRACKET_CHAR + "b_expr" + RBRACKET_CHAR + LBRACE_CHAR + "Body" + RBRACE_CHAR + subnodes.get(2).convertToJott());
        }
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
}