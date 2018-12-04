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
		// we should make something in here
	}

	// This next method is for you to write, to scan the input and return the
	// correct Token for the next lexeme
	// The comments inside the scan() method may help you to structure your code
	public Token scan() throws IOException {
		// newId = null;.
		Token token = null;
		for (;; peek = (char) System.in.read()) {
			if (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r')
				continue;
			else
				break;
		}

		// I think this logic is steady enough now keep everything else if

		if (Character.isLowerCase(peek)) {
			// checking for an identifier
			newId = new IdentToken(Character.toString(peek));
			token = newId;
			// addIdentifier(newId);
			System.out.println(token.returnType());
			peek = (char) System.in.read();
			// return new Token(TokenType.ID);
		}

		else if (Character.isUpperCase(peek)) {
			if (peek == 'T') {
				if (newId != null) {
					newId.setValue(true);
					// if I enter T as the first character in the program ill get a nullpointer
					// this is because newId hasn't acctually been initialized
					System.out.println(newId.getIdName() + " = " + newId.getValue());
					addIdentifier(newId);
					token = new Token(TokenType.TRUE);
					System.out.println(token.returnType());
					peek = (char) System.in.read();
				} else {
					System.out.println("You need to assign a boolean to an identifier!");
				}
			} else if (peek == 'F') {
				if (newId != null) {
					newId.setValue(false);
					addIdentifier(newId);
					token = new Token(TokenType.FALSE);
					System.out.println(token.returnType());
					peek = (char) System.in.read();
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
			// just needa add bracket support below
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
					// if I enter T as the first character in the program ill get a nullpointer
					// this is because newId hasn't acctually been initialized
					System.out.println(newId.getIdName() + " initialized to " + newId.getValue());
					addIdentifier(newId);
					token = new Token(TokenType.QMARK);
//					DO WE ACC NEED A QMARK TOKEN?
					System.out.println(token.returnType());
					peek = (char) System.in.read();
				} else {
					System.out.println("You need to assign a boolean (T | F | ?) to an identifier!");
				}
//				peek = (char) System.in.read();
				break;
			default:
				System.out.println("Unknown: " + peek);
				token = new Token(TokenType.NULL_TOKEN);
				peek = (char) System.in.read();
				break;
			}
		}
		// Now determine what type of token we have...
		return token;
		// If we have gotten to here, we have not matched any token so print an error
		// message and return a NULL_TOKEN
	}

	public Hashtable<String, IdentToken> getIdentifiers() {
		return identifiers;
	}
}
