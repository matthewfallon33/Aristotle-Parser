package aristotle;

import java.util.Hashtable;

public class Parser {
	public Token[] tokens; // Array of tokens to be parsed
	public int position; // Current position in array


	Boolean innerTempVal = null;
	Boolean outerTempVal = null;
	int QMax = 2;
	int QCount = 0;

	public Hashtable<String, IdentToken> vars; // Hashtable of all identifiers
	public boolean parseError = false;

	public Parser(Token[] tokenSeq, Hashtable<String, IdentToken> variables) {
		tokens = tokenSeq;
		position = 0; // Current position in sequence of tokens
		vars = variables;
	}

	private void parseError() {
		System.out.println("Parse Error: on token: " + (position + 1));
		tokens[position].print();
	}

	// The following helper method allows us to check if the current token
	// is of type tokType and if so increment the position counter
	private void match(TokenType tokType) {

		if (tokens[position].returnType() != tokType) {
			System.out.println("MATCH: " + tokens[position].returnType() + " doesn't match expected " + tokType + " at position "  + (position + 1));
			parseError();
		} else {
			position++;
		}

	}

	// The following helper method allows us to check if the current token
	// is of type tokType without incrementing the position counter
	private boolean check(TokenType tokType) {

		if (tokens[position].returnType() != tokType) {
			return false;
		}
		return true;
	}

	public void prog() {
		inits();
		hyphen();
		expression();
		end_of_file();
		if(Aristotle.QMark)
			System.out.println("Expression is: " + innerTempVal);
//		System.out.println("Expression evaluates to: " + innerTempVal);

	}

	private void hyphen() {
		if (check(TokenType.HYPHEN)) {
			match(TokenType.HYPHEN);
		} else {
			if (!parseError) {
				parseError = true;
				System.out.println("HYPHEN: Expected HYPHEN at " + (position + 1));
				}
		}
	}

	public void inits() {
//		below is the format of an init
		id();
		colon();
		bool();
		semicolon();
	}

	private void end_of_file() {
		if (tokens[position].returnType() == TokenType.END_OF_EXPR && !parseError) {
			match(tokens[position].thisToken);
		}
	}

	private void expression() {
//		match lbracket call innerExpression to get the value of the expression between the brackets, then call rbracket
		lbracket();
		innerExpr();
		rbracket();
//this is where we cross from one clause to another comparing with and
//		checks if next operator is and if the left clause is true it will advance if not it'll stop and the expression will evaluate to false
		if (check(TokenType.OPERATOR)) {
			if (((OperatorToken) tokens[position]).getType() == (OperatorType.AND)) {
				if (innerTempVal) {
					match(TokenType.OPERATOR);
					expression();
				}
			} else {
				if (check(TokenType.END_OF_EXPR)) {
					match(TokenType.END_OF_EXPR);
				} else {
					System.out.println("I dunno what's happening + " + ((OperatorToken) tokens[position]).getType());
					return;
				}
			}
		}
	}

	private void innerExpr() {

		if (check(TokenType.ID)) {
//			check for normal non-negated id
			String identName = ((IdentToken) tokens[position]).getIdName();
			if (vars.get(identName) == null) {
//				checking for undeclared variable in expression
				System.out.println("Undeclared Variable " + identName);
				parseError();
				parseError = true;
				return;
			}
			if (innerTempVal == null) {
				innerTempVal = vars.get(((IdentToken) tokens[position]).getIdName()).getValue();
				match(TokenType.ID);
				innerExpr();
			}
		}

		if (check(TokenType.OPERATOR)) {
			if (isOp(tokens[position])) {
				if (((OperatorToken) tokens[position]).getType() == (OperatorType.NOT)) {
//					check for not operator and if so negate the following ident tokens value
					match(TokenType.OPERATOR);
					if (innerTempVal == null) {
						innerTempVal = !vars.get(((IdentToken) tokens[position]).getIdName()).getValue();
					} else {
						innerTempVal = !vars.get(((IdentToken) tokens[position]).getIdName()).getValue();
					}
					match(TokenType.ID);
					innerExpr();
				}
			}
		}

		if (check(TokenType.OPERATOR)) {
			if (isOp(tokens[position])) {
				if (((OperatorToken) tokens[position]).getType() == (OperatorType.OR)) {
//					check for or operator, then check if the next literal is negated(NOT) or just normal non negated
					match(TokenType.OPERATOR);
					if (isOp(tokens[position])) {
						if (((OperatorToken) tokens[position]).getType() == (OperatorType.NOT)) {
							match(TokenType.OPERATOR);
							String idName = ((IdentToken) tokens[position]).getIdName();
							IdentToken newToken = vars.get(idName);
							newToken.setValue(!vars.get(idName).value);
							if (innerTempVal || newToken.getValue()) {
								innerTempVal = true;
							} else {
								innerTempVal = false;
							}
							match(TokenType.ID);
						}
					}
					// x:?-(x)$

					if (check(TokenType.ID)) {
						String idName = ((IdentToken) tokens[position]).getIdName();
						if (innerTempVal || vars.get(idName).getValue()) {
							innerTempVal = true;
						} else {
							innerTempVal = false;
						}
						match(TokenType.ID);
					}
				}
			}
		}
	}

	private void lbracket() {
		if (parseError)
			return;
		if (check(TokenType.LBRACKET)) {
			match(TokenType.LBRACKET);
		} else {
			parseError = true;
			System.out.println("LBRACKET: Expecting LBRACKET at position " + (position + 1));
		}
	};

	private void rbracket() {
		if (parseError)
			return;
		if (check(TokenType.RBRACKET)) {
			match(TokenType.RBRACKET);
		} else {
//			System.out.println("RBRACKET: Expecting RBRACKET instead of" + tokens[position].returnType() + " at "
//					+ (position + 1));
		}
	}

	private void id() {
		if (parseError)
			return;
		if (check(TokenType.ID)) {
			match(TokenType.ID);
			if (check(TokenType.ID)) {
				id();
			}
		} else {
			parseError = true;
			System.out.println(
					"ID: Expected ID instead of " + tokens[position].returnType() + " at token " + (position + 1));
		}
	};

	private void colon() {
		if (parseError)
			return;
		if (check(TokenType.COLON)) {
			match(TokenType.COLON);
		} else {
			parseError = true;
			System.out.println("COLON: Expected COLON instead of " + tokens[position].returnType() + " at token "
					+ (position + 1));
		}
	};

	private void bool() {
		if (parseError)
			return;
		if (check(TokenType.TRUE)) {
			match(TokenType.TRUE);
		} else if (check(TokenType.FALSE)) {
			match(TokenType.FALSE);
		} else if (check(TokenType.QMARK)) {
			if (QCount == 0) {
				System.out.println(((IdentToken) tokens[position - 2]).getIdName() + " val is "
						+ ((IdentToken) tokens[position - 2]).getValue());
				match(TokenType.QMARK);
			}
			if (QCount == 1) {
				String id = ((IdentToken) tokens[position - 2]).getIdName();
				IdentToken idToken = vars.get(id);
				idToken.setValue(true);
				vars.put(id, idToken);
				System.out.println(((IdentToken) tokens[position - 2]).getIdName() + " val is "
						+ ((IdentToken) tokens[position - 2]).getValue());
				match(TokenType.QMARK);
			}
		} else {
			parseError = true;
			System.out.println("Bool: Expected Boolean Value(T|F) instead of " + tokens[position].returnType()
					+ " at token " + (position + 1));
		}
	}


	private void semicolon() {
		if (parseError)
			return;
		if (check(TokenType.SEMICOLON)) {
			match(TokenType.SEMICOLON);
			if (tokens[position].returnType() == TokenType.ID)
				inits();
			else {
				}
		} else {
			parseError = true;
			System.out.println("SEMICOLON: Expected SEMICOLON instead of " + tokens[position].returnType()
					+ " at token " + (position + 1));
		}
	};

	private boolean isIdent(Token t) {
//		allows for safe checking on ident tokens
		if (t instanceof IdentToken) {
			return true;
		}
		return false;
	}

	private boolean isOp(Token t) {
//		allows for safe checking on operator tokens
		if (t instanceof OperatorToken)
			return true;
		return false;
	}
}