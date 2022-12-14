package Parser.Nodes;
import Tokenizer.*;
import Parser.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class AssignmentNode implements JottTree {

    private final String JOTT_DOUBLE = "Double";
    private final String JOTT_INTEGER = "Integer";
    private final String JOTT_STRING = "String";
    private final String JOTT_BOOLEAN = "Boolean";

    private final String JAVA_DOUBLE = "double";
    private final String JAVA_INTEGER = "int";
    private final String JAVA_STRING = "String";
    private final String JAVA_BOOLEAN = "boolean";

    private final String C_DOUBLE = "double";
    private final String C_INTEGER = "int";
    private final String C_STRING = "char *";
    private final String C_BOOLEAN = "bool";

    private final String C_MALLOC = "= (char*) malloc(sizeof(char) * 1024);";

    private final String EQ_CHAR = "=";
    private ArrayList<JottTree> subnodes = new ArrayList<>();
    private ArrayList<Token> tokens;
    private int tabCount;
    private boolean isInit;
    private String initType;
    private String expr_type;
    private Token firstToken;
    Hashtable<String, SymbolData> symbolTable;
    private boolean notDeclared;

    public AssignmentNode(ArrayList<Token> tokens, int tc, Hashtable<String, SymbolData> symbolTable) {
        try {
            tabCount = tc;
            this.tokens = tokens;
            assert this.tokens != null;
            firstToken = this.tokens.get(0);
            Token last = this.tokens.get(this.tokens.size()-1);
            isInit = this.tokens.get(0).getToken().equals("Double") ||
                    this.tokens.get(0).getToken().equals("Integer") ||
                    this.tokens.get(0).getToken().equals("String") ||
                    this.tokens.get(0).getToken().equals("Boolean");
            if (isInit) initType = this.tokens.get(0).getToken();

                // Double <id> = <d_expr><end_statement>
            if (Objects.equals(this.tokens.get(0).getToken(), JOTT_DOUBLE)) {
                if (this.tokens.get(1).getTokenType().equals(TokenType.ID_KEYWORD)) {
                    subnodes.add(new IdNode(this.tokens.get(1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected <id>", this.tokens.get(1));
                }

                ArrayList<Token> d_expr = new ArrayList<>(this.tokens);
                d_expr.remove(0); // Double
                d_expr.remove(0); // <id>
                if (d_expr.get(0).getToken().equals(EQ_CHAR)) {
                    d_expr.remove(0); // =
                } else {
                    CreateSyntaxError("Unexpected Token - Expected =", this.tokens.get(0));
                }

                d_expr.remove(d_expr.size() - 1); // <d_expr>
                if (d_expr.size() == 0) CreateSyntaxError("Expected <exp> got <end_stmt>", this.tokens.get(this.tokens.size()-1));

                subnodes.add(new DoubleExprNode(d_expr, 0, symbolTable));
                expr_type = "Double";
                if (this.tokens.get(this.tokens.size() - 1).getTokenType().equals(TokenType.SEMICOLON)) {
                    subnodes.add(new EndStatementNode(this.tokens.get(this.tokens.size() - 1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected ;", this.tokens.get(this.tokens.size() - 1));
                }

            } // Integer <id> = <i_expr><end_statement>
            else if (Objects.equals(this.tokens.get(0).getToken(), JOTT_INTEGER)) {
                if (this.tokens.get(1).getTokenType().equals(TokenType.ID_KEYWORD)) {
                    subnodes.add(new IdNode(this.tokens.get(1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected <id>", this.tokens.get(1));
                }
                ArrayList<Token> i_expr = new ArrayList<>(this.tokens);
                i_expr.remove(0); // Integer
                i_expr.remove(0); // <id>
                if (i_expr.get(0).getToken().equals(EQ_CHAR)) {
                    i_expr.remove(0); // =
                } else {
                    CreateSyntaxError("Unexpected Token - Expected =", this.tokens.get(0));
                }
                i_expr.remove(i_expr.size() - 1);
                if (i_expr.size() == 0) CreateSyntaxError("Expected <exp> got <end_stmt>", this.tokens.get(this.tokens.size()-1));

                subnodes.add(new IntExprNode(i_expr, 0, symbolTable));
                expr_type = "Integer";
                if (this.tokens.get(this.tokens.size() - 1).getTokenType().equals(TokenType.SEMICOLON)) {
                    subnodes.add(new EndStatementNode(this.tokens.get(this.tokens.size() - 1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected ;", this.tokens.get(this.tokens.size() - 1));
                }
            } // Boolean <id> = <b_expr><end_statement>
            else if (Objects.equals(this.tokens.get(0).getToken(), JOTT_BOOLEAN)) {
                if (this.tokens.get(1).getTokenType().equals(TokenType.ID_KEYWORD)) {
                    subnodes.add(new IdNode(this.tokens.get(1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected <id>", this.tokens.get(1));
                }
                ArrayList<Token> b_expr = new ArrayList<>(this.tokens);
                b_expr.remove(0);
                b_expr.remove(0);
                if (b_expr.get(0).getToken().equals(EQ_CHAR)) {
                    b_expr.remove(0); // =
                } else {
                    CreateSyntaxError("Unexpected Token - Expected =", this.tokens.get(0));
                }
                b_expr.remove(b_expr.size() - 1);
                if (b_expr.size() == 0) CreateSyntaxError("Expected <exp> got <end_stmt>", this.tokens.get(this.tokens.size()-1));
                subnodes.add(new BoolExprNode(b_expr, tabCount, symbolTable));
                expr_type = "Boolean";
                if (this.tokens.get(this.tokens.size() - 1).getTokenType().equals(TokenType.SEMICOLON)) {
                    subnodes.add(new EndStatementNode(this.tokens.get(this.tokens.size() - 1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected ;", this.tokens.get(this.tokens.size() - 1));
                }
            } // string <id> = <s_expr><end_statement>
            else if (Objects.equals(this.tokens.get(0).getToken(), JOTT_STRING)) {
                if (this.tokens.get(1).getTokenType().equals(TokenType.ID_KEYWORD)) {
                    subnodes.add(new IdNode(this.tokens.get(1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected <id>", this.tokens.get(1));
                }
                ArrayList<Token> s_expr = new ArrayList<>(this.tokens);
                s_expr.remove(0);
                s_expr.remove(0);
                if (s_expr.get(0).getToken().equals(EQ_CHAR)) {
                    s_expr.remove(0); // =
                } else {
                    CreateSyntaxError("Unexpected Token - Expected =", this.tokens.get(0));
                }
                s_expr.remove(s_expr.size() - 1);
                if (s_expr.size() == 0) CreateSyntaxError("Expected <exp> got <end_stmt>", this.tokens.get(this.tokens.size()-1));

                subnodes.add(new StrExprNode(s_expr, 0, symbolTable));
                expr_type = "String";
                if (this.tokens.get(this.tokens.size() - 1).getTokenType().equals(TokenType.SEMICOLON)) {
                    subnodes.add(new EndStatementNode(this.tokens.get(this.tokens.size() - 1), 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected ;", this.tokens.get(this.tokens.size() - 1));
                }
            } else if (Objects.equals(this.tokens.get(0).getTokenType(), TokenType.ID_KEYWORD)) {
                subnodes.add(new IdNode(this.tokens.get(0), 0, symbolTable)); // <id>
                this.tokens.remove(0);
                if (this.tokens.get(0).getToken().equals(EQ_CHAR)) {
                    this.tokens.remove(0); // =
                } else {
                    CreateSyntaxError("Unexpected Token - Expected =", this.tokens.get(0));
                }


                // <b_expr>
                boolean bExprBool = false;
                for (int i = 0; i < this.tokens.size(); i++) {
                    if (this.tokens.get(i).getTokenType().equals(TokenType.REL_OP)) {
                        bExprBool = true;
                        ArrayList<Token> b_expr = this.tokens;
                        b_expr.remove(b_expr.size() - 1);
                        subnodes.add(new BoolExprNode(b_expr, 0, symbolTable));
                        expr_type = "Boolean";
                        break;
                    }
                }

                // <s_expr>
                boolean sExprBool = false;
                if (!bExprBool) {

                    if (this.tokens.get(0).getTokenType().equals(TokenType.STRING)) {
                        sExprBool = true;
                        ArrayList<Token> s_expr = this.tokens;
                        s_expr.remove(s_expr.size() - 1);
                        subnodes.add(new StrExprNode(s_expr, 0, symbolTable));
                        expr_type = "String";
                    }
                }

                boolean dExprBool = false;
                boolean iExprBool = false;
                if (!sExprBool && !bExprBool) {
                    for (int i = 0; i < this.tokens.size(); i++) {
                        if (this.tokens.get(i).getTokenType().equals(TokenType.NUMBER)) {
                            if (this.tokens.get(i).getToken().contains(".")) {
                                dExprBool = true;
                                ArrayList<Token> d_expr = this.tokens;
                                d_expr.remove(d_expr.size() - 1);
                                subnodes.add(new DoubleExprNode(d_expr, 0, symbolTable));
                                expr_type = "Double";
                            } else {
                                iExprBool = true;
                                ArrayList<Token> i_expr = this.tokens;
                                i_expr.remove(i_expr.size() - 1);
                                subnodes.add(new IntExprNode(i_expr, 0, symbolTable));
                                expr_type = "Integer";
                            }
                            break;
                        }
                    }
                }

                // All other <id> and <func_call> = <id> = [params]
                // s_expr, b_expr, d_expr, and i_expr all have <id> and <func_call>
                // , for this phase <id> doesn't have any type. So I believe it's safe to simply throw them
                // in s_expr for now, but in phase 3 this will have to change.
                if (!bExprBool && !sExprBool && !iExprBool && !dExprBool) {
                    if (this.tokens.get(0).getTokenType().equals(TokenType.ID_KEYWORD)) {
                        ArrayList<Token> s_expr = this.tokens;
                        s_expr.remove(s_expr.size() - 1);
                        subnodes.add(new BoolExprNode(s_expr, 0, symbolTable));
                        if (symbolTable.containsKey(s_expr.get(0).getToken()))
                            expr_type = symbolTable.get(s_expr.get(0).getToken()).ReturnType;
                    } else {
                        CreateSyntaxError("Unexpected Token - Expected <Expression Type First>", this.tokens.get(0));
                    }
                }

                if (last.getTokenType().equals(TokenType.SEMICOLON)) {
                    subnodes.add(new EndStatementNode(last, 0, symbolTable));
                } else {
                    CreateSyntaxError("Unexpected Token - Expected ;", this.tokens.get(this.tokens.size() - 1));
                }
            } else {
                CreateSyntaxError("Unexpected Token - Expected <assignment>", this.tokens.get(0));
            }
            if (isInit) {
                if (symbolTable.containsKey(subnodes.get(0).convertToJott()) && !symbolTable.get(subnodes.get(0).convertToJott()).IsFunction)
                    symbolTable.get(subnodes.get(0).convertToJott()).varCount++;
                else
                    symbolTable.put(subnodes.get(0).convertToJott(), new SymbolData(
                        subnodes.get(0).convertToJott(),
                        initType,
                        false,
                        true,
                        false,
                        null,
                        null,
                        1)
                );
            } else {
                if (symbolTable.containsKey(subnodes.get(0).convertToJott()) && !symbolTable.get(subnodes.get(0).convertToJott()).IsFunction)
                    symbolTable.get(subnodes.get(0).convertToJott()).IsInitialized = true;
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
        StringBuilder jott_asmt = new StringBuilder();
        switch (tokens.get(0).getToken()) {
            case JOTT_DOUBLE -> jott_asmt.append(JOTT_DOUBLE + " ");
            case JOTT_BOOLEAN -> jott_asmt.append(JOTT_BOOLEAN + " ");
            case JOTT_INTEGER -> jott_asmt.append(JOTT_INTEGER + " ");
            case JOTT_STRING -> jott_asmt.append(JOTT_STRING + " ");
        }
        jott_asmt.append(subnodes.get(0).convertToJott()).append(" ");
        jott_asmt.append(EQ_CHAR + " ");
        jott_asmt.append(subnodes.get(1).convertToJott());
        jott_asmt.append(subnodes.get(2).convertToJott());
        return jott_asmt.toString();
    }

    /**
     * Will output a string of this tree in Java
     * @return a string representing the Java code of this tree
     */
    public String convertToJava()
    {
        StringBuilder java_asmt = new StringBuilder();
        switch (tokens.get(0).getToken()) {
            case JOTT_DOUBLE -> java_asmt.append(JAVA_DOUBLE + " ");
            case JOTT_BOOLEAN -> java_asmt.append(JAVA_BOOLEAN + " ");
            case JOTT_INTEGER -> java_asmt.append(JAVA_INTEGER + " ");
            case JOTT_STRING -> java_asmt.append(JAVA_STRING + " ");
        }
        java_asmt.append(subnodes.get(0).convertToJava()).append(" ");
        java_asmt.append(EQ_CHAR + " ");
        java_asmt.append(subnodes.get(1).convertToJava());
        java_asmt.append(subnodes.get(2).convertToJava());
        return java_asmt.toString();
    }

    /**
     * Will output a string of this tree in C
     * @return a string representing the C code of this tree
     */
    public String convertToC()
    {

        StringBuilder java_asmt = new StringBuilder();
        switch (tokens.get(0).getToken()) {
            case JOTT_DOUBLE -> java_asmt.append(C_DOUBLE + " ");
            case JOTT_BOOLEAN -> java_asmt.append(C_BOOLEAN + " ");
            case JOTT_INTEGER -> java_asmt.append(C_INTEGER + " ");
            case JOTT_STRING -> {
                java_asmt.append(C_STRING + " ");
                java_asmt.append(subnodes.get(0).convertToC()).append(" ");
                java_asmt.append(C_MALLOC);
                //java_asmt.append(subnodes.get(1).convertToC().length()).append(");");

                java_asmt.append("\n").append("\t".repeat(tabCount));;
            }
        }

        java_asmt.append(subnodes.get(0).convertToC()).append(" ");
        //if(tokens.get(0).getToken() == JOTT_STRING)
            //java_asmt.append("[] ");
        java_asmt.append(EQ_CHAR + " ");
        java_asmt.append(subnodes.get(1).convertToC());
        java_asmt.append(subnodes.get(2).convertToC());
        return java_asmt.toString();
    }

    /**
     * Will output a string of this tree in Python
     * @return a string representing the Python code of this tree
     */
    public String convertToPython()
    {
        return subnodes.get(0).convertToPython() + " " +
                EQ_CHAR + " " +
                subnodes.get(1).convertToPython() +
                subnodes.get(2).convertToPython();
    }

    /**
     * This will validate that the tree follows the semantic rules of Jott
     * Errors validating will be reported to System.err
     * @return true if valid Jott code; false otherwise
     */
    public boolean validateTree()
    {
        try {
            if (isInit) {
                if (symbolTable.get(subnodes.get(0).convertToJott()).varCount != 1)
                    CreateSemanticError("Variable is already initialized or instantiated", firstToken);
                else if (symbolTable.get(subnodes.get(0).convertToJott()).IsFunction)
                    CreateSemanticError("Variable is already declared as a function", firstToken);
                else if (!symbolTable.get(subnodes.get(0).convertToJott()).ReturnType.equals(expr_type))
                    CreateSemanticError("Invalid assignment: types do not match", firstToken);
                else if (keywords.contains(subnodes.get(0).convertToJott()))
                    CreateSemanticError("Cannot use a keyword as a variable", firstToken);
            } else {
                if (!symbolTable.containsKey(subnodes.get(0).convertToJott()))
                    CreateSemanticError("Variable has not been initialized", firstToken);
                else if (symbolTable.get(subnodes.get(0).convertToJott()).IsFunction)
                    CreateSemanticError("Invalid assignment to a function:", firstToken);
                else if (!symbolTable.get(subnodes.get(0).convertToJott()).ReturnType.equals(expr_type))
                    CreateSemanticError("Invalid assignment: types do not match", firstToken);
            }
            return subnodes.get(0).validateTree() &&
                    subnodes.get(1).validateTree() &&
                    subnodes.get(2).validateTree();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void CreateSyntaxError(String msg, Token token) throws Exception {
        System.err.println("Syntax Error:\n" + msg + "\n" + token.getFilename() + ":" + token.getLineNum());
        throw new Exception();
    }

    public void CreateSemanticError(String msg, Token token) throws Exception {
        System.err.println("Semantic Error:\n" + msg + "\n" + token.getFilename() + ":" + token.getLineNum());
        throw new Exception();
    }
}