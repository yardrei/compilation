package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_EXP_CLASS_METHOD extends AST_EXP {

    public AST_VAR var;
    public String id;
    public AST_LIST<AST_EXP> list;

    //AST Annotation
    public TYPE_FUNCTION func;

    /******************/
    /* CONSTRUCTOR(S) */

    /******************/
    public AST_EXP_CLASS_METHOD(AST_VAR var, String id, AST_LIST<AST_EXP> list, int line) {
        super(line);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.var = var;
        this.id = id;
        this.list = list;
    }

    /*****************************************************/
    /* The default message for an CLASS METHOD AST node */

    /*****************************************************/
    public void PrintMe() {
        /*************************************************/
        /* AST EXP [var DOT] ID '('[exp [COMMA exp]*]')' */
        /*************************************************/
        System.out.print("AST NODE EXP CLASS MEOTHD\n");

        /************************************/
        /* RECURSIVELY PRINT var + list ... */
        /************************************/
        if (var != null)
            var.PrintMe();
        if (list != null)
            list.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        if (id != null)
            AST_GRAPHVIZ
                    .getInstance()
                    .logNode(SerialNumber, String.format("EXP\nCLASS METHOD\nID(%s)", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (var != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        }

        if (list != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
        }
    }

    public TYPE SemantMe() {
        TYPE varType;
        TYPE_CLASS classType;
        if (var == null) {
            if (!(SYMBOL_TABLE.getInstance().find(id) instanceof TYPE_FUNCTION)) { // Note that null always returns
                // false
                throw new SemanticException(line);
            }

            func = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(id);
        } else {
            varType = var.SemantMe();
            if (varType == null || !(varType.isClass())) {
                throw new SemanticException(line);
            } 
            else if(varType.isVar()) {
                classType = (TYPE_CLASS) ((TYPE_VAR) varType).type;
            }
            else {
                throw new SemanticException(line);
            }            

            // Find function in the class(classType)
            TYPE param = classType.findFunc(id);

            // Throw an exception if a fitting function wasn't found
            if (param == null || !(param instanceof TYPE_FUNCTION)) {
                throw new SemanticException(line);
            }

            func = (TYPE_FUNCTION) param;
        }

        // Check that args match
        if (list == null) {
            if (func.params != null){
                throw new SemanticException(line);
            }
        }
        else {
            if (!(list.SemantMe().isAssignableTo(func.params))) {
                throw new SemanticException(line);
            }
        }

        return func.returnType;
    }

    @Override
    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        boolean isVoid = func.returnType instanceof TYPE_VOID;
        TEMP tmp;
        if (func.name == "PrintInt") {
            tmp = list.data.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(tmp));
            return dst;
        }
        else if (func.name == "PrintString") {
            tmp = list.data.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_PrintString(tmp));
            return dst;
        }
        
        //Adding the function parameters to the stack
        if (list != null) {
            int i = 0;
            for (AST_LIST<AST_EXP> curr = list; curr != null; curr = curr.next, i++) {
                tmp = curr.data.IRme();
                IR.getInstance().Add_IRcommand(new IRcommand_Store_Param(tmp, i, list.size(), curr == list));  
            }
        }

        //Function
        if (var == null) {
            IR.getInstance().Add_IRcommand(new IRcommand_Function_Call(dst, id, func.numOfParams, isVoid));
        }
        //Method
        else {
            TEMP locTemp = var.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Method_Call(dst, locTemp, func.location, func.numOfParams, isVoid));
        }
        return dst;
    }
}
