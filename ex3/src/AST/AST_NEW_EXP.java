package AST;

import TYPES.*;
import exceptions.*;

public class AST_NEW_EXP extends AST_EXP {
    public AST_TYPE type;
    public AST_EXP exp;

    /******************/
    /* CONSTRUCTOR(S) */

    /******************/
    public AST_NEW_EXP(AST_TYPE type, AST_EXP exp, int line) {
        super(line);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type = type;
        this.exp = exp;
    }

    /***********************************************/
    /* The default message for an newexp AST node */

    /***********************************************/
    public void PrintMe() {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE newEXP\n");

        /************************************/
        /* RECURSIVELY PRINT type + exp ... */
        /************************************/
        if (type != null)
            type.PrintMe();
        if (exp != null)
            exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "newEXP\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);

    }

    // public TYPE SemantMe() {
    // if(exp != null && !(exp.SemantMe() instanceof TYPE_INT)) {
    // throw new SemanticException(line);
    // }

    // return new TYPE_ARRAY()
    // }

    public TYPE SemantMe() {
        TYPE semantedType = type.SemantMe();
        if (exp == null) {
            if (semantedType instanceof TYPE_VOID) {
                throw new SemanticException(line);
            }
            return semantedType;
        }

        TYPE semntexp = exp.SemantMe();

        if (!(semntexp instanceof TYPE_INT)) {
            throw new SemanticException(line);
        }
        if ((exp instanceof AST_EXP_INT) && (((AST_EXP_INT) exp).value <= 0)) {
            throw new SemanticException(line);
        }

        return new TYPE_ARRAY("NEW", semantedType);

        // TYPE cl = SYMBOL_TABLE.getInstance().findClass(semantedType.typeName);
        // if (cl == null) {
        //     throw new SemanticException(line);
        // }

        // return cl;
    }

}
