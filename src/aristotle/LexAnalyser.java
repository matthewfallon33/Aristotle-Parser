/* Lexical Analysis class for Aristotle language
 * 
 */

package aristotle;

import java.io.IOException;
import java.util.Hashtable;

public class LexAnalyser {
	public int lineNumber = 1;
	private char peek = ' '; // Next character to be read in from the input
	private Hashtable<String, IdentToken> identifiers = new Hashtable<String, IdentToken>();
	private IdentToken newId;

	void addIdentifier(IdentToken t) {
		identifiers.put(t.identifierName, t);
	}

	public LexAnalyser() {

	}

	public Token scan() throws IOException {
		// newId = null;.
		Token token = null;
		for (;; peek = (char) System.in.read()) {
//			ignoring whitespace characters and line breaks etc
			if (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r')
				continue;
			else
				break;
		}

		if (Character.isLowerCase(peek)) {
			// checking for an identifier
			newId = new IdentToken(Character.toString(peek));
			token = newId;
			System.out.println(token.returnType());
			peek = (char) System.in.read();

		}

		else if (Character.isUpperCase(peek)) {
//			checks boolean value then applys the boolean value to the identToken
			if (peek == 'T') {
				if (newId != null) {
					newId.setValue(true);
					if (identifiers.get(newId.getIdName()) != null) {
						System.out.println("Duplicate Variable: Already an identifier with this name " + newId.getIdName());
						return token = new Token(TokenType.NULL_TOKEN);
					} else {
						// System.out.println("Not a duplicate");
						addIdentifier(newId);
						token = new Token(TokenType.TRUE);
						System.out.println(token.returnType());
						peek = (char) System.in.read();
					}

				} else {
//					if no identifier set for a boolean value
					System.out.println("You need to assign a boolean to an identifier!");
				}
			} else if (peek == 'F') {
				if (newId != null) {
					newId.setValue(false);
					if (identifiers.get(newId.getIdName()) != null) {
						System.out.println("already an identifier with this name " + newId.getIdName());
						return token = new Token(TokenType.NULL_TOKEN);
					} else {
						addIdentifier(newId);
						token = new Token(TokenType.FALSE);
						System.out.println(token.returnType());

						peek = (char) System.in.read();
					}
				} else {
					System.out.println("You need to assign a boolean to an identifier!");
				}

			} else {
				token = new Token(TokenType.NULL_TOKEN);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
			}

		}

		else if (!Character.isAlphabetic(peek)) {
//			checking for non alphabetic characters
			switch (peek) {
			case '$':
				token = new Token(TokenType.END_OF_EXPR);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case ';':
				token = new Token(TokenType.SEMICOLON);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case '|':
				token = new OperatorToken(OperatorType.OR);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case '!':
				token = new OperatorToken(OperatorType.NOT);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case ':':
				token = new Token(TokenType.COLON);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case '-':
				token = new Token(TokenType.HYPHEN);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case '^':
				token = new OperatorToken(OperatorType.AND);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case '(':
				token = new Token(TokenType.LBRACKET);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case ')':
				token = new Token(TokenType.RBRACKET);
				System.out.println(token.returnType());
				peek = (char) System.in.read();
				break;
			case '?':
				if (newId != null) {
					newId.setValue(false);
//					init the '?' var to false
					addIdentifier(newId);
					token = new Token(TokenType.QMARK);
					System.out.println(token.returnType());
					peek = (char) System.in.read();
				} else {
					System.out.println("You need to assign a boolean (T | F | ?) to an identifier!");
				}
				break;
			default:
				System.out.println("Unknown: " + peek);
				token = new Token(TokenType.NULL_TOKEN);
//				don't wanna read in anymore after getting a null token
//				peek = (char) System.in.read();
				break;
			}
		}

		return token;
	}

	public Hashtable<String, IdentToken> getIdentifiers() {
		return identifiers;
	}
}
