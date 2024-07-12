package exceptions;

public abstract class CompilerException extends RuntimeException {
    public int line;
    public String message;

    public CompilerException(int line) {
        this.line = line;
    }

    public CompilerException(int line, String message) {
        this.line = line;
        this.message = message;
    }

}