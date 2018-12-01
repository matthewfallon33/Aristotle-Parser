package aristotle;

public class SyntaxException extends Exception {
    public SyntaxException () {

    }

    public SyntaxException (String message) {
        super (message);
    }

    public SyntaxException (Throwable cause) {
        super (cause);
    }

    public SyntaxException (String message, Throwable cause) {
        super (message, cause);
    }
}