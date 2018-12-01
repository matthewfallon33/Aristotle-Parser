/* A Token to represent an iodentifier (a variable) which has a name and a value
 * 
 */


package aristotle;

public class IdentToken extends Token {
    public String identifierName;
    public Boolean value; // either "T" or "F"
    
    public IdentToken(String identName) {
        super(TokenType.ID);
        identifierName = identName;
        value = false; // default behavior
    }

    public String getIdName() {
        return identifierName;
    }
    
    public void setValue(Boolean newValue) {
        value = newValue;
    }
    
    public Boolean getValue() {
        return value;
    }
    
    public void print() {
        System.out.println("Identifier Token: " + identifierName + " with value " + value);
    }
}