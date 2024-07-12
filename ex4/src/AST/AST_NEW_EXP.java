package AST;

import TYPES.*;
import exceptions.*;
import IR.*;
import TEMP.*;

public class AST_NEW_EXP extends AST_EXP {
    public AST_TYPE type;
    public AST_EXP exp;

    //AST Annotation
    TYPE semantedType;

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

    public TYPE SemantMe() {
        semantedType = type.SemantMe();
        if (semantedType == null || semantedType.isVar() || semantedType instanceof TYPE_VOID) {
            //TODO: test this with var
            throw new SemanticException(line);
        }
        if (exp == null) {
            if (semantedType instanceof TYPE_VOID) {
                throw new SemanticException(line);
            }
            if (type instanceof AST_TYPE_ID) {
                System.out.println("type ID: " + ((AST_TYPE_ID)type).id);////////////////////
            }
            System.out.println("semented type: " + semantedType.name);////////////////////
            return semantedType;
        }

        TYPE semntexp = exp.SemantMe();

        if (semntexp == null || !(semntexp.isInt())) {
            throw new SemanticException(line);
        }
        if ((exp instanceof AST_EXP_INT) && (((AST_EXP_INT) exp).value <= 0)) {
            throw new SemanticException(line);
        }

        System.out.println("size: " + ((AST_EXP_INT) exp).value);/////////////////////

        semantedType = new TYPE_ARRAY("NEW", semantedType, ((AST_EXP_INT) exp).value);
        return semantedType;
    }

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Allocate_New(dst, semantedType));
        return dst;
    }
}
