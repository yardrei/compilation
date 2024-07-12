package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;

public class AST_STMT_RET extends AST_STMT {

    private AST_EXP exp;

    public AST_STMT_RET(AST_EXP exp, int line) {
        super(line);

        this.exp = exp;
    }

    public AST_STMT_RET(int line) {
        this(null, line);
    }

    protected String name() {
        return "RETURN";
    }

    public void PrintMe() {

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT\nRET\n");

        if (exp != null) {
            exp.PrintMe();
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
        }
    }

    @Override
    public TYPE SemantMe() throws SemanticException {
        TYPE t = null;

        if (exp == null) {
            if (SYMBOL_TABLE.getInstance().getLastFunctionType() instanceof TYPE_VOID) {
                return null;
            }
            throw new SemanticException(line, "Function return type is not void");
        }

        t = exp.SemantMe();

        if (!t.isAssignableTo(SYMBOL_TABLE.getInstance().getLastFunctionType())) {
            throw new SemanticException(line, t.name + " is not assignable to function return type" + SYMBOL_TABLE.getInstance().getLastFunctionType().name);
        }

        return null;
    }
}
