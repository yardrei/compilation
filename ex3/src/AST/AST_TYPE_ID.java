package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import exceptions.SemanticException;

public class AST_TYPE_ID extends AST_TYPE {

    String id;

    public AST_TYPE_ID(String id, int line) {
        super(line);

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        // System.out.print("====================== type -> ID\n");

        /********************************/
        /* COPY INPUT DATA MEMBERS ... */
        /********************************/
        this.id = id;
    }

    /********************************************/
    /* The printing message for a TYPE AST node */
    /********************************************/
    public void PrintMe() {
        /*********************/
        /* DEC VAR */
        /*********************/
        System.out.print("AST NODE TYPE ID\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        if (id != null)
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("TYPE\nID(%s)\n", id));
    }

    @Override
    public TYPE SemantMe() {
        TYPE t = SYMBOL_TABLE.getInstance().find(id);
        if (t != null && (t.isArray() || t.isClass())) {
            return t;
        }
        throw new SemanticException(line);
    }
}