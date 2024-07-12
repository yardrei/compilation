package AST;

import TYPES.*;

public abstract class AST_TYPE extends AST_Node
{



    public AST_TYPE(int line) {
        super(line);
    }

    abstract public TYPE SemantMe();
}