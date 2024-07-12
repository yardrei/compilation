package AST;

public abstract class AST_C_FIELD extends AST_Node {
    public AST_C_FIELD(int line) {
        super(line);
    }

    abstract public String getId();
}