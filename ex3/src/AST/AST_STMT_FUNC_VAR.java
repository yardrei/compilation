package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;

public class AST_STMT_FUNC_VAR extends AST_STMT {
    public String id;
    public AST_VAR var;
    public AST_LIST<AST_EXP> list;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_STMT_FUNC_VAR(String id, AST_VAR var, AST_LIST<AST_EXP> list, int line) {
        super(line);

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id = id;
        this.var = var;
        this.list = list;
    }

    /******************************************************/
    /* The default message for an stmt func var AST node */
    /******************************************************/
    public void PrintMe() {
        /*************************************************/
        /* AST FUNC [var DOT] ID '('[exp [COMMA exp]*]')' */
        /*************************************************/
        System.out.print("AST NODE STMT FUNC VAR\n");

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
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("STMT\nFUNC VAR\nID(%s)", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (var != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        if (list != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

    }


    public TYPE SemantMe() {
        TYPE_FUNCTION func = null;
        if (var == null) {
            if (!(SYMBOL_TABLE.getInstance().find(id) instanceof TYPE_FUNCTION)) { // Note that null always returns
                                                                                   // false
                throw new SemanticException(line);
            }

            func = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(id);
        } else {
            if (!(var.SemantMe() instanceof TYPE_CLASS)) {
                throw new SemanticException(line);
            }

            TYPE_CLASS varType = (TYPE_CLASS) var.SemantMe();

            // Find function in varType
            TYPE param = varType.findType(id);

            // Throw an exception if a fitting function wasn't found
            if (param == null || !(param instanceof TYPE_FUNCTION)) {
                throw new SemanticException(line);
            }

            func = (TYPE_FUNCTION) param;
        }

        // TODO: Check that args match
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
}
