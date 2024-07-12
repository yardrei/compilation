package AST;

import TYPES.*;
import exceptions.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_VAR_DEC extends AST_DEC {
    public AST_TYPE type;
    public String id;
    public AST_EXP exp;

    //AST Annotations
    TYPE_VAR typeVar;
    scopeEnum scope; //For the IR command
    int location; //To know where in the stuck is the variable

    /*******************/
    /* CONSTRUCTOR(S) */

    /*******************/
    public AST_VAR_DEC(AST_TYPE type, String id, AST_EXP exp, int line) {
        super(line);
        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type = type;
        this.id = id;
        this.exp = exp;
    }

    /*********************************************************/
    /* The printing message for an var dec AST node */

    /*********************************************************/
    public void PrintMe() {
        /*********************/
        /* DEC VAR */
        /*********************/
        System.out.print("AST NODE VAR DEC\n");

        /****************************************/
        /* RECURSIVELY PRINT type + exp ... */
        /****************************************/
        if (type != null)
            type.PrintMe();
        if (exp != null)
            exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        if (id != null)
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("VAR\nDEC %s\n", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
        }
    }

    @Override
    public TYPE SemantMe() {
        TYPE t = type.SemantMe();
        TYPE expType = null;

        if (t == null || t instanceof TYPE_VOID) {
            throw new SemanticException(line, "t cannot be null or TYPE_VOID");
        }

        if (t.isVar()) {
            throw new SemanticException(line, "t cannot be TYPE_VAR");
        }

        if (SYMBOL_TABLE.getInstance().isInCurrentScope(id) &&
                (SYMBOL_TABLE.getInstance().isInCurrentClass(id)
                        || !(t.isAssignableTo(SYMBOL_TABLE.getInstance().find(id))))) {
            System.out.println(SYMBOL_TABLE.getInstance().isInCurrentClass(id));
            throw new SemanticException(line);
        }

        String scopeStr = SYMBOL_TABLE.getInstance().getScope();
        //Finding scope
        if (scopeStr.equals("global")) {
            scope = scopeEnum.Global;
            location = 0;
        }
        else if (scopeStr.equals("class")) {
            scope = scopeEnum.Class;
            location = 0;
        }
        else {
            scope = scopeEnum.Local;
            location = SYMBOL_TABLE.getInstance().getNewVariableIndex();
        }

        // System.out.println("scope: " + scopeStr + "\nlocation: " + location); ///////////////////

        if (exp == null) {
            typeVar = new TYPE_VAR(id, t, scope, location);
            SYMBOL_TABLE.getInstance().enter(id, typeVar);
            return typeVar;
        }

        expType = exp.SemantMe();

        if (expType == null || expType instanceof TYPE_VOID) {
            throw new SemanticException(line, "Expression cannot be of type void");
        }

        if ("class".equals(scopeStr) && !expType.constant) {
            throw new SemanticException(line, "Assignments must be constant in class declaration");
        } 

        if (!(expType.isAssignableTo(t))) {
            throw new SemanticException(line, "Expression must be assignable to the variable");
        }

        if (expType instanceof TYPE_ARRAY && t instanceof TYPE_ARRAY) {
            ((TYPE_ARRAY) t).size = ((TYPE_ARRAY) expType).size;
        }
        
        typeVar = new TYPE_VAR(id, t, scope, location);
        SYMBOL_TABLE.getInstance().enter(id, typeVar);
        return typeVar;
    }

    @Override
    public TEMP IRme() {
        TEMP temp;
        if (scope == scopeEnum.Global) { //Global variable
            if (typeVar.type.isString() && typeVar.constant) {
                String expString = "";
                if (exp != null) {
                    expString = ((AST_EXP_STRING) exp).value;
                }
                IR.getInstance().Add_IRcommand(new IRcommand_Const_String(id + "_const", expString));
                IR.getInstance().Add_IRcommand(new IRcommand_Global_Var(id, id + "_const"));
            }
            else {
                IR.getInstance().Add_IRcommand(new IRcommand_Global_Var(id));
                if (exp != null) {
                    IR.getInstance().Add_IRcommand(new IRcommand_Global_Store_Label(id));
                    temp = exp.IRme();
                    IR.getInstance().Add_IRcommand(new IRcommand_Global_Store_End(id, temp));
                }
            }
        }
        else if (scope == scopeEnum.Class) {
            if (exp != null) {
                temp = exp.IRme();
            }
            else {
                temp = TEMP_FACTORY.getInstance().getFreshTEMP();
                IR.getInstance().Add_IRcommand(new IRcommand_Exp_Nil(temp));
            }
            System.out.println("In vad dec of class var!\nThe location is: " + typeVar.location); ////////////
            IR.getInstance().Add_IRcommand(new IRcommand_Store_Class_Var(temp, typeVar.location));
        }
        else {//Local Variable
            if (exp != null) {
                temp = exp.IRme();
            }
            else {
                temp = TEMP_FACTORY.getInstance().getFreshTEMP();
                IR.getInstance().Add_IRcommand(new IRcommand_Exp_Nil(temp));
            }
            IR.getInstance().Add_IRcommand(new IRcommand_Store_Local(temp, true, typeVar.location));
        }
        return null;
    }
}