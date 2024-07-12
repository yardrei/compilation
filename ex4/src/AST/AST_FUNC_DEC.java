package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_FUNC_DEC extends AST_Node {

    public AST_TYPE type;
    public String id;
    public AST_LIST<AST_FUNC_PARAM> funcParamList;
    public AST_LIST<AST_STMT> stmtList;

    //AST Annotation
    private boolean isInClass = false;
    private TYPE_FUNCTION funcType;

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
                    // TODO: Check in code if you have the same name in parent class for a variable
                    throw new SemanticException(line);
                }
            }

            isInClass = SYMBOL_TABLE.getInstance().getScope() == "class";



            if(isInClass) {
                SYMBOL_TABLE.getInstance().setLastFunctionName(
                    String.format("%s_%s", id, SYMBOL_TABLE.getInstance().getLastClassName())
                    );
            } else {
                SYMBOL_TABLE.getInstance().setLastFunctionName(id);
                if (id.equals("main")){
                    SYMBOL_TABLE.getInstance().setLastFunctionName("user_main");
                    id = "user_main";
                }
            }

            int numOfParams = 0;
            if (funcParamList == null) {
                type_list = null;
            } else {
                type_list = funcParamList.SemantMe();
                if (isInClass) {
                    numOfParams = type_list.numberList(-3, -1);
                }
                else {
                    numOfParams = type_list.numberList(-2, -1);
                }
            }
            
            SYMBOL_TABLE.getInstance().setLastFunctionType(returnType);
            SYMBOL_TABLE.getInstance().beginScope(id);
            
            funcType = new TYPE_FUNCTION(returnType, id, type_list, numOfParams);

            SYMBOL_TABLE.getInstance().enter(id, funcType);
            System.out.println("func id(func_dec): " + id);

            SYMBOL_TABLE.getInstance().resetVariableIndex();;
            
            stmtList.SemantMe();
            
            SYMBOL_TABLE.getInstance().endScope();
            if (SYMBOL_TABLE.getInstance().isInCurrentScope(id) &&
                    !(SYMBOL_TABLE.getInstance().isInCurrentClass(id)) &&
                    !(funcType.canOverwriteParent(SYMBOL_TABLE.getInstance().find(id)))) {
                throw new SemanticException(line);
            }
            SYMBOL_TABLE.getInstance().enter(id, funcType);

            SYMBOL_TABLE.getInstance().setLastFunctionName("");

            System.out.println("func id(func_dec): " + id);
            return funcType;
        }
    }

    @Override
    public TEMP IRme() {
        String label;

        if (isInClass) {
            label = id + "_" + funcType.className;
        }
        else {
            label = id;
        }

        // Functions
        IR.getInstance().Add_IRcommand(new IRcommand_Label(label));
        
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Prologue());

        // if (funcParamList != null) {
        //     funcParamList.IRme();
        // }

        if (stmtList != null) {
            stmtList.IRme();
        }

        IR.getInstance().Add_IRcommand(new IRcommand_Function_Epilogue(label));

        return null;
    }
}