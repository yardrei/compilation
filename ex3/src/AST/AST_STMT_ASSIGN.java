package AST;

import TYPES.*;
import exceptions.*;

public class AST_STMT_ASSIGN extends AST_STMT {
    /***************/
    /* var := exp */
    /***************/
    public AST_VAR var;
    public AST_EXP exp;

    /*******************/
    /* CONSTRUCTOR(S) */

    /*******************/
    public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp, int line) {
        super(line);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.exp = exp;
    }


    /*********************************************************/
    /* The printing message for an assign statement AST node */

    /*********************************************************/
    public void PrintMe() {
        /********************************************/
        /* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
        /********************************************/
        System.out.print("AST NODE ASSIGN STMT\n");

        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (var != null)
            var.PrintMe();
        if (exp != null)
            exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "ASSIGN\nleft := right\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        TYPE varType = var.SemantMe();
        TYPE expType = exp.SemantMe();
        
        if (expType.isAssignableTo(varType)) {
            return null;
        }
        throw new SemanticException(line);
    }
}
