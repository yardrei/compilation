package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_STMT_RET extends AST_STMT {

    private AST_EXP exp;

    //AST Annotations
    String function_name;

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
        TYPE function_type;

        function_type = SYMBOL_TABLE.getInstance().getLastFunctionType();
        function_name = SYMBOL_TABLE.getInstance().getLastFunctionName();

        if (exp == null) {
            if (function_type instanceof TYPE_VOID) {
                return null;
            }
            throw new SemanticException(line, "Function return type is not void");
        }

        t = exp.SemantMe();

        if (!t.isAssignableTo(function_type)) {
            throw new SemanticException(line, t.name + " is not assignable to function return type" + function_type.name);
        }

        return null;
    }

    @Override
    public TEMP IRme() {
        if (exp == null) {
            IR.
            getInstance().
            Add_IRcommand(new IRcommand_Function_Return(null, function_name));
        }
        else {
            TEMP t = exp.IRme();
            IR.
            getInstance().
            Add_IRcommand(new IRcommand_Function_Return(t, function_name));
        }
        return null;
    }
}
