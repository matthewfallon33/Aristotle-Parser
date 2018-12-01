/* This class will not compile until you have written the required parsing methods of prog()
 * 
 */

package aristotle;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

//NEED TO HANDLE E PRODS WITH DECLS AND EXPR
//make prettier decls

public class Parser {
	public Token[] tokens; // Array of tokens to be parsed
	public int position; // Current position in array
	ArrayList<Boolean> exprOutcomes = new ArrayList<Boolean>();
	// IdentToken curId;

	public Hashtable<String, IdentToken> vars; // Hashtable of all identifiers
	public boolean parseError = false;

	public Parser(Token[] tokenSeq, Hashtable<String, IdentToken> variables) {
		tokens = tokenSeq;
		position = 0; // Current position in sequence of tokens
		vars = variables;
	}

	// public void printVars() {
	// System.out.println(vars.keySet().toString().split(",")[0]);
	// }

	private void parseError() throws SyntaxException {
		System.out.println("Parse Error on token: " + (position + 1));
		tokens[position].print();
		// throw new SyntaxException("dodgy syntax!");

		// throw new SyntaxException("Syntax Error");

	}

	// The following helper method allows us to check if the current token
	// is of type tokType and if so increment the position counter
	private void match(TokenType tokType) throws SyntaxException {

		if (tokens[position].returnType() != tokType) {
			System.out.println(
					"before parseError " + tokens[position].returnType() + " doesn't match expected " + tokType);
			parseError();

		} else {
			System.out.println(tokens[position].returnType() + " Matches " + tokType);
			if (isIdent(tokens[position])) {
				// curId = (IdentToken) tokens[position];
				// vars.put(curId.getIdName(), curId);
				// System.out.println(vars.get(curId.getIdName()).getIdName());
			}
			position++;
			// System.out.println(position);
		}

	}

	// The following helper method allows us to check if the current token
	// is of type tokType without incrementing the position counter
	private boolean check(TokenType tokType) {

		if (tokens[position].returnType() != tokType) {
			// System.out.println(tokens[position].returnType() + " < actual : expected > "
			// + tokType);
			return false;
		}
		return true;
	}

	// Start to parse the program (you will need to write this method)
	public void prog() throws SyntaxException {
		// First parse declarations
		// decls();
		// hyphen();
		// System.out.println(vars.size());
		expr();
		// expression();
		end_of_file();
		// just getting the length of the entered tokens vs the position the parser has
		// gotten to without failing
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] == null) {
				System.out.println("Leng:" + (i - 1));
				break;
			}
		}
		System.out.println("POS " + position);

		// Next parse a hyphen
		// hyphen();
		// Next parse the expression
		// expr();
		// Finally parse the end of file character

	}

	private void hyphen() throws SyntaxException {
		// lookahead = '-'
		if (check(TokenType.HYPHEN)) {
			System.out.println("HYPHEN!");
			match(TokenType.HYPHEN);
		}
	}

	// maybe pass in identToken.getNameId() curIdent.getNameId()
	public boolean getBoolVal(String id) {
		return vars.get(id).getValue();
	}

	private void decls() throws SyntaxException {
		System.out.println("DECLS!!");

		boolean tru = false;

		if (check(TokenType.ID)) {
			match(TokenType.ID);
			if (check(TokenType.COLON)) {
				match(TokenType.COLON);
				if (true) {
					if (check(TokenType.TRUE)) {
						tru = true;
						match(TokenType.TRUE);
					}
					if (check(TokenType.FALSE)) {
						tru = true;
						match(TokenType.FALSE);
					}

					if (!tru) {
						System.out.println("Expected Boolean Value (T|F)");
						parseError = true;
						parseError();
						return;
					}
					if (check(TokenType.SEMICOLON)) {
						match(TokenType.SEMICOLON);
					} else {
						System.out.println("Syntax Error: Expected ';'");
						parseError = true;
						parseError();
					}

				}
			} else {
				System.out.println("Syntax Error: Expected ':'");
				parseError = true;
				parseError();
			}
		} else {
			System.out.println("Syntax Error: Expected Identifier");
			parseError = true;
			parseError();
		}

		// instead of me checking position +1 I should really just say position on its
		// own
		// because the position will have already been incremented by the previous
		// successful match() call

		if (tokens[position] != null && tokens[position].returnType() != TokenType.HYPHEN && !parseError) {
			System.out.println("this is nextTOken in if decls " + tokens[position].returnType());
			decls();
		} else {
			return;
		}
		// when we get an error I no longer want to increment the counter, just kill the
		// program

	}

	// trying to write comparison methods

	private boolean convertToBool(TokenType bool) {
		Boolean val = null;
		if (bool == TokenType.FALSE) {
			val = false;
		}
		if (bool == TokenType.TRUE) {
			val = true;
		}
		return val;
	}

	private void end_of_file() throws SyntaxException {
		// try bring this into the program

		if (tokens[position].returnType() == TokenType.END_OF_EXPR) {
			match(tokens[position].thisToken);
			position++;
		}
	}

	private void expr() throws SyntaxException {
		System.out.println("EXPR");
		factor();

	}

	// expr: lbrack, id operator id, rbrack

	void term() throws SyntaxException {
		if (tokens[position - 1].returnType() == TokenType.LBRACKET) {
			match(TokenType.ID);
			if (check(TokenType.OPERATOR)) {
				if (isOuterOp(tokens[position])) {
					System.out.println("IsOuterOp " + tokens[position].returnType());
					factor();
				} else {
					
					match(TokenType.ID);
				}
			}

			// match(TokenType.RBRACKET);
		} else {
			System.out.println("else in term " + tokens[position].returnType());
		}
	}

	// (t | d)$
	// (t | d) | (c | d)

	void factor() throws SyntaxException {

		if (check(TokenType.LBRACKET)) {
			System.out.println("lbracket");
			match(TokenType.LBRACKET);
			term();
			match(TokenType.RBRACKET);
		} else {
			System.out.println("Else in factor " + tokens[position].returnType());
			// match(TokenType.RBRACKET);
		}
	}

	boolean isOuterOp(Token t) {
		if (tokens[position - 1].returnType() == TokenType.LBRACKET
				&& tokens[position + 1].returnType() == TokenType.RBRACKET) {
			return true;
		}
		return false;
	}

	// private void lbracket() throws SyntaxException {
	// System.out.println("LBRACKET");
	// if (check(TokenType.LBRACKET)) {
	// match(TokenType.LBRACKET);
	//
	// } else {
	//// e | error
	// }
	// };
	//
	// private void rbracket() throws SyntaxException {
	// System.out.println("RBRACKET");
	// if (check(TokenType.RBRACKET)) {
	// match(TokenType.RBRACKET);
	// }
	// }
	//
	//
	// private void operator() throws SyntaxException {
	// System.out.println("operator check");
	// }
	//
	// private void id() throws SyntaxException {
	// System.out.println("id");
	// if (check(TokenType.ID)) {
	// match(TokenType.ID);
	// } else {
	//
	// }
	// };
	//
	// private void colon() throws SyntaxException {
	// System.out.println("colon");
	// if (check(TokenType.COLON)) {
	// match(TokenType.COLON);
	// } else {
	//
	// }
	// };
	//
	// private void bool() throws SyntaxException {
	// };
	//
	// private void semicolon() throws SyntaxException {
	// System.out.println("semi colon");
	// if (check(TokenType.SEMICOLON)) {
	// match(TokenType.SEMICOLON);
	// }
	// };

	private boolean isIdent(Token t) {
		if (t instanceof IdentToken) {
			return true;
		}
		return false;
	}

	// The rest of the methods are up to you to write

}
