package exceptions;

public class LexicalException extends CompilerException {
    public LexicalException(int line) {
        super(line);
    }

    public LexicalException(int line, String message) {
        super(line, message);
    }

}