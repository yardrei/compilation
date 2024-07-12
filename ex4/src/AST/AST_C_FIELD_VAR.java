package AST;

import TYPES.*;
import TEMP.*;

public class AST_C_FIELD_VAR extends AST_C_FIELD {
    public AST_VAR_DEC var;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_C_FIELD_VAR(AST_VAR_DEC var, int line) {
        super(line);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
    }

    public String getId() {
        return var.id;
    }

    /***********************************************/
    /* The default message for an C FIELD VAR AST node */
    /***********************************************/
    public void PrintMe() {
        /************************************/
        /* AST NODE C FILED VAR */
        /************************************/
        System.out.print("AST NODE C Field VAR\n");

        /*****************************/
        /* RECURSIVELY PRINT var... */
        /*****************************/
        if (var != null)
            var.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "C FIELD\n VAR\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        if (var == null) {
            return null;
        }
        return var.SemantMe();
    }

    @Override
    public TEMP IRme() {
        var.IRme();
        return null;
    }
}
