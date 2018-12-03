/* A Token to represent an iodentifier (a variable) which has a name and a value
 * 
 */


package aristotle;

public class IdentToken extends Token {
    public String identifierName;
    public Boolean value; // either "T" or "F"
    public boolean negated = false;
    
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
    
    public void setNegated(boolean b) {
    	negated = b;
    }
    
    
    public Boolean getValue() {
        return value;
    }
    
    public boolean isNegated() {
    	return negated;
    }
    
    public void print() {
        System.out.println("Identifier Token: " + identifierName + " with value " + value);
    }
}