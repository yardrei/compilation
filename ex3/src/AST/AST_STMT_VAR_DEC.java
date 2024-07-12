package AST;

import TYPES.*;

public class AST_STMT_VAR_DEC extends AST_STMT {
    public AST_VAR_DEC varDec;

    public AST_STMT_VAR_DEC(AST_VAR_DEC varDec, int line) {
        super(line);

        this.varDec = varDec;
    }

    protected String name() {
        return "Stmt var dec";
    }

    public void PrintMe() {
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT\nVAR\nDEC\n");

        if (varDec != null) {
            varDec.PrintMe();

            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, varDec.SerialNumber);
        }
    }

    @Override
    public TYPE SemantMe() {
        varDec.SemantMe();
        return null;
    }
}