package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.TEMP;
import exceptions.*;

public class AST_FUNC_PARAM extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_FUNC_PARAM(AST_TYPE type, String id, int line) {
        super(line);

        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.type = type;
        this.id = id;
    }

    public void PrintMe() {
        if(type != null) {
            type.PrintMe();
        }

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC PARAM (%s)", id));
    }



    @Override
    public TYPE SemantMe() {
        TYPE t;
        // TYPE other;

        if(type == null) {
            throw new SemanticException(line);
        }

        t = type.SemantMe();
        if (t == null || t instanceof TYPE_VOID){
            throw new SemanticException(line);
        }

        // other = SYMBOL_TABLE.getInstance().find(id);
        // if (other != null && !(other.isPrimitive())) {
        //     throw new SemanticException(line);
        // }

        if (SYMBOL_TABLE.getInstance().isInCurrentScope(id)) {
            throw new SemanticException(line);
        }

        TYPE varType = new TYPE_VAR(id, t, scopeEnum.Local, 0);

        SYMBOL_TABLE.getInstance().enter(id, varType);
        return varType;
    }

    @Override
    public TEMP IRme() {
        return null;
    }
}