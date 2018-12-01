/* The superclass Token, used to represent generic tokens (can be specialised). 
 * See subclasses BoolToken 
 */

package aristotle;

// Basic class for tokens, can be extended by subclasses for each particular token
public class Token {
	TokenType thisToken;

	public Token() {
		thisToken = TokenType.NULL_TOKEN;
	}

	public Token(TokenType inputToken) {
		thisToken = inputToken;
	}

	public TokenType returnType() {
		return thisToken;
	}
	
//	public boolean isNonTerminal(){}

	public void print() {
		switch (thisToken) {
		case OPERATOR:
			System.out.println("Operator Token");
			break;
		case END_OF_EXPR:
			System.out.println("End of Expression Token");
			break;
		case HYPHEN:
			System.out.println("'-' Token");
			break;
		case SEMICOLON:
			System.out.println("':' Token");
			break;
		case TRUE:
			System.out.println("False Token");
			break;
		case FALSE:
			System.out.println("False Token");
			break;
		case COLON:
			System.out.println("':' Token");
			break;
		case ID:
			System.out.println("Identifier Token");
			break;
		default:
			System.out.println("Unknown Token");
			break;
		}
	}
}