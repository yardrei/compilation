package AST;

import TYPES.*;
import TEMP.*;

public class AST_EXP_EXP extends AST_EXP
{
    public AST_EXP exp;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_EXP(AST_EXP exp, int line)
    {
        super(line);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.exp = exp;
    }

    /***********************************************/
    /* The default message for an exp exp AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE EXP EXP\n");

        /*****************************/
        /* RECURSIVELY PRINT exp ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "EXP\nEXP");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

    }
    public TYPE SemantMe() throws RuntimeException{
        return exp.SemantMe();
    }

    @Override
    public TEMP IRme() {
        return exp.IRme();
    }
}
