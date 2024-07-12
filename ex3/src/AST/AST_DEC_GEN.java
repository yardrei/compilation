package AST;

import TYPES.*;

public class AST_DEC_GEN<T extends AST_Node> extends AST_DEC
{
    public T dec;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_DEC_GEN(T dec, int line)
    {
        super(line);
        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.dec = dec;
    }

    /*********************************************************/
    /* The printing message for an array type def AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /********************/
        /* DEC ARRAY        */
        /********************/
        System.out.print("AST NODE DEC GEN\n");

        /************************************/
        /* RECURSIVELY PRINT DECLARATION... */
        /************************************/
        if (dec != null) dec.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("DEC\n%s\n",dec.getClass()));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,dec.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        dec.SemantMe();
        return null;
    }
}
