package aristotle;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

//NEED TO HANDLE E PRODS WITH DECLS AND EXPR
//make prettier decls

public class Parser {
	public Token[] tokens; // Array of tokens to be parsed
	public int position; // Current position in array
	Boolean innerTempVal = null;
	Boolean outerTempVal = null;
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
		System.out.println("Parse Error: on token: " + (position + 1));
		tokens[position].print();
		// throw new SyntaxException("dodgy syntax!");

		// throw new SyntaxException("Syntax Error");

	}

	// The following helper method allows us to check if the current token
	// is of type tokType and if so increment the position counter
	private void match(TokenType tokType) throws SyntaxException {

		if (tokens[position].returnType() != tokType) {
			// shouldn't really get this msg because we shouldn't match without
			// checking
			System.out.println("MATCH: " + tokens[position].returnType() + " doesn't match expected " + tokType);
			parseError();

		} else {
			// System.out.println(tokens[position].returnType() + " Matches " +
			// tokType);
			if (isIdent(tokens[position])) {
				System.out.println(tokens[position].returnType() + " is an ident");
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
			// System.out.println(tokens[position].returnType() + " < actual :
			// expected > "
			// + tokType);
			return false;
		}
		return true;
	}

	// Start to parse the program (you will need to write this method)
	public void prog() throws SyntaxException {

		System.out.println("isop " + isOp(new OperatorToken(OperatorType.AND)));
		// First parse declarations
		decls2();
		hyphen();
		expression();
		end_of_file();
		// just getting the length of the entered tokens vs the position the
		// parser has
		// gotten to without failing
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] == null) {
				System.out.println("Leng:" + (i - 1));
				break;
			}
		}
		System.out.println("POS " + position);

	}

	private void hyphen() throws SyntaxException {
		// lookahead = '-'
		if (check(TokenType.HYPHEN)) {
			System.out.println("HYPHEN!");
			match(TokenType.HYPHEN);
		} else {
			if (!parseError)
				System.out.println("HYPHEN: Expected HYPHEN");
		}
	}

	public void decls2() throws SyntaxException {

		// now just to work on the final result and then cleaning the code up
		// and honing in on the spec

		// x:T;
		id();
		colon();
		bool();
		semicolon();

		// yeah so even if the program comes accross an error its still going to
		// call

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
		if (tokens[position].returnType() == TokenType.END_OF_EXPR && !parseError) {
			match(tokens[position].thisToken);
		}
	}

	// expr: lbrack, id operator id, rbrack
	// (d | t)

	private void expression() throws SyntaxException {
		lbracket();
		innerExpr();
		rbracket();
		System.out.println(vars);
		vars.get("a").print();
		vars.get("x").print();
		System.out.println(innerTempVal);
		if (check(TokenType.OPERATOR)) {
			// do you need outerval?
			if (((OperatorToken) tokens[position]).getType() == (OperatorType.AND)) {
				if (innerTempVal) {
					System.out.println("inner temp val true");
					match(TokenType.OPERATOR);
					expression();
				}
			} else {
				System.out.println("inner temp val not true " +  innerTempVal);
				if (check(TokenType.END_OF_EXPR)) {
					match(TokenType.END_OF_EXPR);
				} else {
					System.out.println("I dunno what's happening + " + ((OperatorToken) tokens[position]).getType());
					return;
				}
			}
		}
	} 

	private void innerExpr() throws SyntaxException {
		System.out.println("innerExpr");
		// x:T;a:T;-(a) ^ (!x)$ this one not evaluating properly need to set
		// outerTempVal properly
		// so it's gonna keep matching in here until it finds a right bracket )
		// NOT
		if (check(TokenType.ID)) {
			System.out.println(((IdentToken) tokens[position]).identifierName + " is an ID");
			if (innerTempVal == null) {
				innerTempVal = vars.get(((IdentToken) tokens[position]).getIdName()).getValue();
				System.out.println("InnerT " + innerTempVal);
				match(TokenType.ID);
				innerExpr();
				// should we recurse here to go to OR?
			}}
			// try this without check use isOp it looks silly using both
			if (check(TokenType.OPERATOR)) {
				if (isOp(tokens[position])) {
					System.out.println("this is where it's coming through keeee");
					System.out.println(((OperatorToken) tokens[position]).getType() + " is an OPERATOR");
					System.out.println("After coming through isOp " + ((OperatorToken) tokens[position]).getType());

					if (((OperatorToken) tokens[position]).getType() == (OperatorType.NOT)) {
						System.out.println("come through not route");
						match(TokenType.OPERATOR);
						System.out.println();
						// so it'll go to next id
						String idName = ((IdentToken) tokens[position]).getIdName();
						IdentToken newToken = vars.get(idName);
						newToken.setValue(!vars.get(idName).value);
						newToken.setNegated(true);
//						to say that the tokens value is the opposite of it's initial value
						if (innerTempVal == null) {
							System.out.println("innerTempVal was null but now its the value of " + newToken.getIdName()
									+ " " + newToken.getValue());
							innerTempVal = newToken.getValue();
						} else {
							System.out.println("else " + newToken.getIdName() + " is " + newToken.getValue());
							innerTempVal = newToken.getValue();
						}
						// if (innerTempVal) {
						// // innerTempVal = ;
						// // is this an inner problem or an outer?
						// }
						// LOOK AT THIS AGAIN
						// something needs to happen here
						match(TokenType.ID);
						innerExpr();
					}
				}
			}
			// maybe add a check here?
			if (check(TokenType.OPERATOR)) {
				if (isOp(tokens[position])) {
					System.out.println("THE ISOP I WANT IT TO COME THROUGH");
					if (((OperatorToken) tokens[position]).getType() == (OperatorType.OR)) {

						System.out.println("come through or");
						match(TokenType.OPERATOR);
						// at this point we've matched the or
						// but the next token can either be an ID or a NOT so we can
						// handle it with two ifs instead of if else: dont ask me
//						x:T;a:T;-(a) ^ (x)$


						if (isOp(tokens[position])) {
							if (((OperatorToken) tokens[position]).getType() == (OperatorType.NOT)) {
								System.out.println(
										((OperatorToken) tokens[position]).getType() + " coming through not routeeeee");
								// increment the position to next token
								match(TokenType.OPERATOR);
								// so here it should match the not but it doesn't ????
								String idName = ((IdentToken) tokens[position]).getIdName();
								IdentToken newToken = vars.get(idName);
								newToken.setValue(!vars.get(idName).value);
								newToken.setNegated(true);
								System.out.println(innerTempVal + " || " + newToken.getValue());
								if (innerTempVal || newToken.getValue()) {
									innerTempVal = true;
								} else {
									innerTempVal = false;
								}
								match(TokenType.ID);
							}
						}
						if (check(TokenType.ID)) {
							System.out.println("checked a non negated id after the or" + tokens[position].returnType());
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
		// ID for non not values

//it all works just need to start working with the negations (!a | !t) ^ (a) = false because a has been negated
//	although the program shouldn't work like this it should result to true
//	so we should find a way of ensuring we are checking the initial values rather than the ones the tokens are being set too
//	i have made a negated boolean so start working on that

	private void lbracket() throws SyntaxException {
		System.out.println("LBRACKET");
		if (parseError)
			return;
		if (check(TokenType.LBRACKET)) {
			match(TokenType.LBRACKET);
		} else {
			System.out.println("LBRACKET: Expecting LBRACKET");
		}
	};

	private void rbracket() throws SyntaxException {
		System.out.println("RBRACKET");
		if (parseError)
			return;
		if (check(TokenType.RBRACKET)) {
			match(TokenType.RBRACKET);
		} else {
			System.out
					.println("RBRACKET: Expecting RBRACKET instead of" + tokens[position].returnType());
		}
	}

	private void operator() throws SyntaxException {
		System.out.println("operator check");
		// this is where fancy stuff should happen
		// we should use is inner op to compare to values OR two expressions

	}

	boolean isOuterOp(Token t) {
		if (tokens[position - 1].returnType() == TokenType.LBRACKET
				&& tokens[position + 1].returnType() == TokenType.RBRACKET) {
			return true;
		}
		return false;
	}

	private void id() throws SyntaxException {
		System.out.println("id");
		if (parseError)
			return;
		if (check(TokenType.ID)) {
			match(TokenType.ID);
		} else {
			parseError = true;
			System.out.println(
					"ID: Expected ID instead of " + tokens[position].returnType() + " at token " + (position + 1));
		}
	};

	private void colon() throws SyntaxException {
		System.out.println("colon");
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

	private void bool() throws SyntaxException {
		System.out.println("bool");
		if (parseError)
			return;
		if (check(TokenType.TRUE)) {
			match(TokenType.TRUE);
		} else if (check(TokenType.FALSE)) {
			match(TokenType.FALSE);
		} else {
			parseError = true;
			System.out.println("Bool: Expected Boolean Value(T|F) instead of " + tokens[position].returnType()
					+ " at token " + (position + 1));
		}
	};

	private void semicolon() throws SyntaxException {
		System.out.println("semi colon");
		if (parseError)
			return;
		if (check(TokenType.SEMICOLON)) {
			match(TokenType.SEMICOLON);
			if (tokens[position].returnType() == TokenType.ID)
				decls2();
			else
				System.out.println("End of decls");
		} else {
			parseError = true;
			System.out.println("SEMICOLON: Expected SEMICOLON instead of " + tokens[position].returnType()
					+ " at token " + (position + 1));
		}
	};

	private boolean isIdent(Token t) {
		if (t instanceof IdentToken) {
			return true;
		}
		return false;
	}

	private boolean isOp(Token t) {
		if (t instanceof OperatorToken)
			return true;
		return false;
	}
}
