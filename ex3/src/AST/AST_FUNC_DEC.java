package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;

public class AST_FUNC_DEC extends AST_Node {

    public AST_TYPE type;
    public String id;
    public AST_LIST<AST_FUNC_PARAM> funcParamList;
    public AST_LIST<AST_STMT> stmtList;

    public AST_FUNC_DEC(AST_TYPE type, String id, AST_LIST<AST_FUNC_PARAM> funcParamList, AST_LIST<AST_STMT> stmtList,
            int line) {
        super(line);
        this.type = type;
        this.id = id;
        this.funcParamList = funcParamList;
        this.stmtList = stmtList;
    }

    public void PrintMe() {
        if (type != null) {
            type.PrintMe();
        }
        if (funcParamList != null) {
            funcParamList.PrintMe();
        }

        if (stmtList != null) {
            stmtList.PrintMe();
        }

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC_DEC(%s)", id));

        if (type != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        }

        if (funcParamList != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, funcParamList.SerialNumber);
        }

        if (stmtList != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, stmtList.SerialNumber);
        }
    }

    @Override
    public TYPE SemantMe() {
        {
            TYPE returnType = null;
            TYPE_LIST type_list = null;

            if (type == null) {
                throw new SemanticException(line);
            }
            returnType = type.SemantMe();
            if (returnType == null) {
                throw new SemanticException(line);
            }
            TYPE other = SYMBOL_TABLE.getInstance().find(id);
            if (other != null) {
                if (SYMBOL_TABLE.getInstance().isInCurrentScope(id) &&
                        SYMBOL_TABLE.getInstance().isInCurrentClass(id)) {
                    throw new SemanticException(line);
                }
                if (SYMBOL_TABLE.getInstance().isClassOrArrayName(id)) {
                    throw new SemanticException(line);
                }
            }

            SYMBOL_TABLE.getInstance().setLastFunction(returnType);
            SYMBOL_TABLE.getInstance().beginScope(id);

            if (funcParamList == null) {
                type_list = null;
            } else {
                type_list = funcParamList.SemantMe();
            }

            TYPE_FUNCTION funcType = new TYPE_FUNCTION(returnType, id, type_list);

            SYMBOL_TABLE.getInstance().enter(id, funcType);
            
            stmtList.SemantMe();
            
            SYMBOL_TABLE.getInstance().endScope();
            if (SYMBOL_TABLE.getInstance().isInCurrentScope(id) &&
                    !(SYMBOL_TABLE.getInstance().isInCurrentClass(id)) &&
                    !(funcType.canOverwriteParent(SYMBOL_TABLE.getInstance().find(id)))) {
                throw new SemanticException(line);
            }
            SYMBOL_TABLE.getInstance().enter(id, funcType);
            return funcType;
        }
    }
}