
package aristotle;

public class OperatorToken  extends Token {
    public OperatorType opType;
    
    public OperatorToken(OperatorType thisOp) {
        super(TokenType.OPERATOR);
        opType = thisOp;
    }
    
    public OperatorType getType() {
        return opType;
    }
    
    public void print() {
        switch(opType) {
            case AND: System.out.println("Operator Token: AND"); break;
            case OR: System.out.println("Operator Token: OR"); break;
            case NOT: System.out.println("Operator Token: NOT"); break;
        }
    }
}
