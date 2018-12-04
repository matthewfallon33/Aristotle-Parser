/*
 * Aristotle Language Skeleton Java program
 * You should use these .java files as a start for your coursework (or use code that we covered in lab practicals)
 * All other coding must be your own (individual coursework)
 */

package aristotle;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

//Lexer/Parse error xxx:T; doesn't throw an error
//make sure that the program only accepts single letter identifiers

public class Aristotle {
	public static final int MAX_NUM_TOKENS = 3000; // You may assume there will be no more than 3000 tokens

	public static Token[] tokenSequence = new Token[MAX_NUM_TOKENS];
	public static int currentToken = 0;
	public static Hashtable<String, IdentToken> idents; // All identifiers of the program, indexed by name
	public static boolean lexAnalysisSuccessful = true;
	public static boolean QMark = false;

	/*
	 * need to assign values to variables need to make expr methods
	 */

	public static void main(String[] args) throws SyntaxException {
		LexAnalyser lex = new LexAnalyser();
		Token nextToken = new Token(TokenType.END_OF_EXPR);

		System.out.println("--- Beginning Lexical Analysis ---");

		do {
			try {
				nextToken = lex.scan();
				tokenSequence[currentToken] = nextToken;

				if (nextToken.returnType() == TokenType.END_OF_EXPR) {
					if (lexAnalysisSuccessful) {
						System.out.println("Lexical Analysis Successful! with " + currentToken + " Tokens\n");
						// System.out.println(idents.size() + " Idents created");

						for (int i = 0; i < tokenSequence.length; i++) {
							if (tokenSequence[i] == null) {
								break;
							}
							System.out.print(tokenSequence[i].returnType() + "\t");
						}
					}
				}
				if (nextToken.returnType() == TokenType.QMARK) {
					QMark = true;
				}
				if (nextToken.returnType() == TokenType.NULL_TOKEN) { // NULL TOKEN means syntax error
					lexAnalysisSuccessful = false;
					System.out.println("Lexical Analysis Unsuccessful NULL_TOKEN at token " + currentToken);
				}

				currentToken++;
			} catch (IOException ex) {
				// maybe read about IOException
				System.out.println("Syntax Error");
			}
		} while (nextToken.returnType() != TokenType.END_OF_EXPR);

		// Lexical analysis complete, now on to parsing..
		System.out.println("\n--- Beginning Parsing ---\n");
		idents = lex.getIdentifiers();
		// This next declaration passes the sequence of tokens and hashtable of
		// identifiers to the parser
		Parser pars = new Parser(tokenSequence, idents);
		// pars.printVars();
		// if(there is "?")

		if (QMark) {
			// this one breaks it x:?;-(x);
			// we need to actually set the values of the identTokens
			// so if x:?; set x to false once then next set it to true call prog twice and
			// see the different results
			System.out.println("There is a ?");
			while (pars.QCount < pars.QMax) {
				System.out.println("QCount: " + pars.QCount);

//				if we get a successful case for the '?' 
				
				pars.position = 0;
				// if( pars.innerTempVal != null || !pars.innerTempVal) {
				pars.innerTempVal = null;
//				a:?;b:T;- (a|b) ^ (!b)$ 
//				a:F;b:T;- (a|b) ^ (!b)$ 
//
				pars.prog();
				pars.QCount++;
			}
			// }
		} else {
			System.out.println("No question mark");
			pars.prog();
		}

		// pars.prog();

		System.out.println("\n--- Ending Parsing ---");
		System.out.println(idents.size());
	}
}
