package AST;

import TYPES.*;

public class AST_TYPE_GEN extends AST_TYPE {

    int type;

    public AST_TYPE_GEN(int type, int line) {
        super(line);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        // System.out.print("====================== BINOP -> OP\n");

        /********************************/
        /* COPY INPUT DATA MEMBERS ...  */
        /* 0 == TYPE_INT                */
        /* 1 == TYPE_STRING             */
        /* 2 == TYPE_VOID               */
        /********************************/
        this.type = type;
    }

    /********************************************/
    /* The printing message for a TYPE AST node */
    /********************************************/
    public void PrintMe() {
        String typeString = "";

        /*********************************/
        /* CONVERT type to a printable typeString */
        /*********************************/
        if (type == 0) {
            typeString = "TYPE_INT";
        }
        if (type == 1) {
            typeString = "TYPE_STRING";
        }
        if (type == 2) {
            typeString = "TYPE_VOID";
        }

        /*************************************/
        /* AST NODE TYPE = AST TYPE */
        /*************************************/
        System.out.print("AST NODE TYPE\n");

        AST_GRAPHVIZ
                .getInstance()
                .logNode(SerialNumber, String.format("TYPE(%s)", typeString));
    }

    @Override
    public TYPE SemantMe() {
        switch(type){
            case 0:
                return new TYPE_INT(false);
            case 1:
                return new TYPE_STRING(false);
            case 2:
                return TYPE_VOID.getInstance();
        }
        return null;
    }
}
