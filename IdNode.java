import java.util.ArrayList;

public class IdNode implements JottTree {

    private Token idToken;
    private ArrayList<JottTree> subnodes;
    private String idStored;

    public IdNode(Token token){
        this.idToken = token;
        this.idStored = idToken.getToken();
        assert idStored != null;
        assert idStored.matches("[a-z][a-zA-z0-9]*");
        for (int i=0; i<idStored.length(); i++) subnodes.add(new CharNode(idStored.charAt(i)));
    }
     /**
     * Will output a string of this tree in Jott
     * @return a string representing the Jott code of this tree
     */
    public String convertToJott()
    {
        StringBuilder jott_id = new StringBuilder();
        for (JottTree node : subnodes) jott_id.append(node.convertToJott());
        return jott_id.toString();
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