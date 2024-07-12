package exceptions;

public class SyntacticException extends CompilerException {
    public int line;
    public String message;

    public SyntacticException(int line) {
        super(line);
    }

    public SyntacticException(int line, String message) {
        super(line, message);
    }

}