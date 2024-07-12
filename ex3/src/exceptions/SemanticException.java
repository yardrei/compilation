package exceptions;

public class SemanticException extends CompilerException {

    public SemanticException(int line) {
        super(line);
    }

    public SemanticException(int line, String message) {
        super(line, message);
    }

}