package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;

public class AST_ARRAY_TYPE_DEF extends AST_Node {
    public String id;
    public AST_TYPE type;

    /*******************/
    /*  CONSTRUCTOR(S) */

    /*******************/
    public AST_ARRAY_TYPE_DEF(String id, AST_TYPE type, int line) {
        super(line);
        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id = id;
        this.type = type;
    }

    /*********************************************************/
    /* The printing message for an array type def AST node */

    /*********************************************************/
    public void PrintMe() {
        /*********************************************/
        /* ARRAY TYPE DEF                            */
        /*********************************************/
        System.out.print("AST NODE ARRAY DEF\n");

        /***********************************/
        /* RECURSIVELY PRINT TYPE ... */
        /***********************************/
        if (type != null) type.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "ARRAY\nDEF\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        String scope = SYMBOL_TABLE.getInstance().getScope();

        TYPE t = type.SemantMe();
        if(t instanceof TYPE_VOID || t instanceof TYPE_NIL) {
            throw new SemanticException(line, "type cannot be void or nil");
        }

        if (!scope.equals("global")
                || SYMBOL_TABLE.getInstance().isInCurrentScope(id)) { // Duplicate
            throw new SemanticException(line);
        }

        TYPE_ARRAY typeArray = new TYPE_ARRAY(id, type.SemantMe());
        SYMBOL_TABLE.getInstance().enter(id, typeArray);
        SYMBOL_TABLE.getInstance().addClassOrArrayName(id, line);

        return typeArray;
    }
}
