package Parser.Nodes;
import Tokenizer.*;
import Parser.*;

import java.util.ArrayList;
import java.util.Hashtable;

public class ParametersTNode implements JottTree{

    private final String COMMA_CHAR = ",";
    private final String EMPTY_STRING = "";
    private ArrayList<JottTree> subnodes;

    private ExpressionNode expressionNode;
    private ParametersTNode parametersTNode;
    private ArrayList<Token> tokens;
    private int tabCount;
    private int cnt;
    private String function;
    private Token firstToken;
    Hashtable<String, SymbolData> symbolTable;

    public ParametersTNode(ArrayList<Token> tokens, int tc, int c, String func, Hashtable<String, SymbolData> symbolTable, Token firstToken) {
        try {
            function = func;
            cnt = c;
            tabCount = tc;
            this.tokens = tokens;
            if (this.tokens.size() == 0) {
                expressionNode = null;
                parametersTNode = null;
                this.firstToken = firstToken;
            }
            else {
                this.firstToken = this.tokens.get(0);
                if (this.tokens.get(0).getTokenType() != TokenType.COMMA)
                    CreateSyntaxError("Unexpected Token - Expected ','", this.tokens.get(0));
                this.tokens.remove(0);
                ArrayList<Token> expr = new ArrayList<>();
                int b_count = 0;
                while ((b_count != 0 || this.tokens.get(0).getTokenType() != TokenType.COMMA) && this.tokens.size() != 0) {
                    expr.add(this.tokens.remove(0));
                    if (this.tokens.size() == 0) break;
                    if (this.tokens.get(0).getTokenType() == TokenType.L_BRACKET) b_count++;
                    else if (this.tokens.get(0).getTokenType() == TokenType.R_BRACKET) b_count++;

                }
                expressionNode = new ExpressionNode(expr, tabCount, symbolTable);
                parametersTNode = new ParametersTNode(this.tokens, tabCount, cnt+1, func, symbolTable, firstToken);
            }
            this.symbolTable = symbolTable;
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
        if (expressionNode == null) return EMPTY_STRING;
        return ", "+expressionNode.convertToJott() + parametersTNode.convertToJott();
    }

    /**
     * Will output a string of this tree in Java
     * @return a string representing the Java code of this tree
     */
    public String convertToJava()
    {
        if (expressionNode == null) return EMPTY_STRING;
        return ", "+expressionNode.convertToJava() + parametersTNode.convertToJava();
    }

    /**
     * Will output a string of this tree in C
     * @return a string representing the C code of this tree
     */
    public String convertToC()
    {
        if (expressionNode == null) return EMPTY_STRING;
        return ", "+expressionNode.convertToC() + parametersTNode.convertToC();
    }

    /**
     * Will output a string of this tree in Python
     * @return a string representing the Python code of this tree
     */
    public String convertToPython()
    {
        if (expressionNode == null) return EMPTY_STRING;
        if (function.equals("concat")) {
            return expressionNode.convertToPython() + parametersTNode.convertToPython();
        }
        if (function.equals("input")) {
            return EMPTY_STRING;
        }
        return ", "+expressionNode.convertToPython() + parametersTNode.convertToPython();
    }

    /**
     * This will validate that the tree follows the semantic rules of Jott
     * Errors validating will be reported to System.err
     * @return true if valid Jott code; false otherwise
     */
    public boolean validateTree()
    {
        try {
            if (function.equals("print")) {
                // check for valid print call?
            } else {
                if (expressionNode == null)
                    if (symbolTable.get(function).Params.size() != cnt)
                        CreateSemanticError("Unexpected number of parameters for " + function, firstToken);
                    else return true;
                else if (symbolTable.get(function).ParamsTypes.size() > cnt && !symbolTable.get(function).ParamsTypes.get(cnt).equals(expressionNode.expr_type)) {
                    CreateSemanticError("Unexpected parameters for " + function, firstToken);
                }
                return expressionNode.validateTree() &&  parametersTNode.validateTree();
            }
            return expressionNode == null;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void CreateSyntaxError(String msg, Token token) throws Exception{
        System.err.println("Syntax Error:\n" + msg + "\n" + token.getFilename() + ":" + token.getLineNum());
        throw new Exception();
    }

    public void CreateSemanticError(String msg, Token token) throws Exception {
        System.err.println("Semantic Error:\n" + msg + "\n" + token.getFilename() + ":" + token.getLineNum());
        throw new Exception();
    }
}
