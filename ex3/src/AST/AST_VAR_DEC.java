package AST;

import TYPES.*;
import exceptions.*;

import SYMBOL_TABLE.*;

public class AST_VAR_DEC extends AST_DEC {
    public AST_TYPE type;
    public String id;
    public AST_EXP exp;

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

        if (SYMBOL_TABLE.getInstance().isInCurrentScope(id) &&
                (SYMBOL_TABLE.getInstance().isInCurrentClass(id)
                        || !(t.isAssignableTo(SYMBOL_TABLE.getInstance().find(id))))) {
            throw new SemanticException(line);
        }

        String scope = SYMBOL_TABLE.getInstance().getScope();
        if (exp == null) {
            SYMBOL_TABLE.getInstance().enter(id, t);
            return t;
        }

        expType = exp.SemantMe();

        if (expType instanceof TYPE_VOID) {
            throw new SemanticException(line, "Expression cannot be of type void");
        }

        if ("class".equals(scope) && !expType.constant) {
            throw new SemanticException(line, "Assignments must be constant in class declaration");
        } 

        if (!expType.isAssignableTo(t)) {
            throw new SemanticException(line, "Expression must be assignable to the variable");
        }

        if ("class".equals(scope)) {
            
        }

        SYMBOL_TABLE.getInstance().enter(id, t);
        return t;
    }
}