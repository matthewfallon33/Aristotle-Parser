/*
 * Aristotle Language Skeleton Java program
 * You should use these .java files as a start for your coursework (or use code that we covered in lab practicals)
 * All other coding must be your own (individual coursework)
 */

package aristotle;

import java.io.IOException;
import java.util.Hashtable;

public class Aristotle {
	public static final int MAX_NUM_TOKENS = 3000; // You may assume there will be no more than 3000 tokens

	public static Token[] tokenSequence = new Token[MAX_NUM_TOKENS];
	public static int currentToken = 0;
	public static Hashtable<String, IdentToken> idents; // All identifiers of the program, indexed by name
	public static boolean lexAnalysisSuccessful = true;
	public static boolean QMark = false;


	public static void main(String[] args) {
		LexAnalyser lex = new LexAnalyser();
		Token nextToken = new Token(TokenType.END_OF_EXPR);

		System.out.println("--- Beginning Lexical Analysis ---");

		do {
			try {
				nextToken = lex.scan();
				tokenSequence[currentToken] = nextToken;

				if (nextToken.returnType() == TokenType.END_OF_EXPR) {
					if (lexAnalysisSuccessful) {
						System.out.println("Lexical Analysis Successful! with " + (currentToken + 1) + " Tokens\n");
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
					return;
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
		Parser pars = new Parser(tokenSequence, idents);


		if (QMark) {
			
			while (pars.QCount < pars.QMax) {
				System.out.println("QCount: " + pars.QCount);
//				re-initializing variables, dunno if I should re-init vars??
				pars.position = 0;
				pars.innerTempVal = null;
				pars.prog();
				pars.QCount++;
			}
		} else {
//	no question marks
			pars.prog();
		}

		System.out.println("\n--- Ending Parsing ---");
		if(!QMark)		
		System.out.println("Expression evaluates to: " + pars.innerTempVal);
	}
}
