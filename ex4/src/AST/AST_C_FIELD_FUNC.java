package AST;

import TYPES.*;
import TEMP.*;

public class AST_C_FIELD_FUNC extends AST_C_FIELD {
    public AST_FUNC_DEC func;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_C_FIELD_FUNC(AST_FUNC_DEC func, int line) {
        super(line);
        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.func = func;
    }

    public String getId() {
        return func.id;
    }

    /***********************************************/
    /* The default message for an C FIELD FUNC AST node */
    /***********************************************/
    public void PrintMe() {
        /************************************/
        /* AST NODE C FILED FUNC */
        /************************************/
        System.out.print("AST NODE C Field FUNC\n");

        /*****************************/
        /* RECURSIVELY PRINT func... */
        /*****************************/
        if (func != null)
            func.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "C FIELD\n FUNC\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        if (func == null) {
            return null;
        }
        return func.SemantMe();
    }

    @Override
    public TEMP IRme() {
        func.IRme();
        return null;
    }
}
