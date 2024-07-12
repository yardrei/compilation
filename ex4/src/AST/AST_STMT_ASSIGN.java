package AST;

import TYPES.*;
import exceptions.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_ASSIGN extends AST_STMT {
    /***************/
    /* var := exp */
    /***************/
    public AST_VAR var;
    public AST_EXP exp;

    //AST Annotations
    int location;
    scopeEnum scope;

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

        if (varType == null || !varType.isVar()) {
            throw new SemanticException(line, "varType needs to be TYPE_VAR");
        }

        if (expType == null) {
            throw new SemanticException(line, "expType equals to null");
        }

        if (expType.isAssignableTo(varType)) {
            TYPE_VAR type = (TYPE_VAR) varType;
            this.location = type.location;
            this.scope = type.scope;
            chackIfNeedToPassSizeArray(varType, expType);
            return null;
        }
        throw new SemanticException(line);
    }

    private void chackIfNeedToPassSizeArray(TYPE varType, TYPE expType) {
        if (varType.isArray() && expType.isArray()) {
            System.out.println("both Arrays"); ////////////////
            if(expType.isVar()) {
                ((TYPE_ARRAY) ((TYPE_VAR) varType).type).size = ((TYPE_ARRAY) ((TYPE_VAR) expType).type).size;
            }
            else {
                ((TYPE_ARRAY) ((TYPE_VAR) varType).type).size = ((TYPE_ARRAY) expType).size;
            }
        }
    }

    @Override
    public TEMP IRme() {
        TEMP locTemp = var.IRme();
        TEMP t = exp.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Store_Var(locTemp, t));
        return null;
    }
}
