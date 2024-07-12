package AST;

import TYPES.*;
import exceptions.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;

public class AST_CLASS_DEC extends AST_DEC {
    public String id;
    public String parentId;
    public AST_LIST<AST_C_FIELD> cFieldList;

    TYPE_LIST funcList = new TYPE_LIST(); // ex4

    /*******************/
    /* CONSTRUCTOR(S) */

    /*******************/
    public AST_CLASS_DEC(String id, String parentId, AST_LIST<AST_C_FIELD> cFieldList, int line) {
        super(line);
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.id = id;
        this.parentId = parentId;
        this.cFieldList = cFieldList;
    }

    /*********************************************************/
    /* The printing message for an class dec AST node */

    /*********************************************************/
    public void PrintMe() {
        /*********************/
        /* DEC CLASS */
        /*********************/
        System.out.print("AST NODE CLASS DEC\n");

        /****************************************/
        /* RECURSIVELY PRINT cField ... */
        /****************************************/
        if (cFieldList != null)
            cFieldList.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        if (parentId != null && id != null)
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("CLASS\nDEC (%s extends %s)\n", id, parentId));
        if (id != null)
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("CLASS\nDEC (%s)\n", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cFieldList.SerialNumber);
    }

    public TYPE SemantMe() {
        TYPE_CLASS parentType;
        // TYPE_LIST parentParams = null; // ex3
        TYPE_LIST varList = new TYPE_LIST(); // ex4
        int numberOfVars = 0; //ex4

        String scope = SYMBOL_TABLE.getInstance().getScope();
        if (!("global".equals(scope))) {
            throw new SemanticException(line, String.format("Class %s cannot be defined inside a scope", id));
        }

        if (SYMBOL_TABLE.getInstance().isInCurrentScope(id)) {
            throw new SemanticException(line, String.format("%s has already been defined", id));
        }

        SYMBOL_TABLE.getInstance().beginScope("class");

        
        if (parentId == null) {
            parentType = null; // ex3
        } 
        else {
            TYPE potentialParentType = SYMBOL_TABLE.getInstance().find(parentId);
            if (!(potentialParentType instanceof TYPE_CLASS)) {
                throw new SemanticException(line,
                String.format("%s is not a class. Received type: %s",
                parentId,
                potentialParentType == null ? "null" : potentialParentType.getClass().getSimpleName()));
            }
            
            parentType = (TYPE_CLASS) potentialParentType;
            for (TYPE_CLASS anc = parentType; anc != null; anc = anc.father) {
                for (TYPE_LIST data = anc.dataMembers; data != null; data = data.next) {
                    if (!SYMBOL_TABLE.getInstance().isInCurrentScope(data.name)) {
                        SYMBOL_TABLE.getInstance().enter(data.name, data.head);
                    }
                }
            }
            // parentParams = parentType.dataMembers; // ex3
            varList = new TYPE_LIST(parentType.varList); // ex4
            funcList = new TYPE_LIST(parentType.funcList); // ex4
            numberOfVars = parentType.size;
        }
        SYMBOL_TABLE.getInstance().beginClass(id);

        // Enter the class without any data members to avoid issues with self-containing
        // classes
        TYPE_CLASS classType = new TYPE_CLASS(parentType, id, varList, funcList, numberOfVars);
        SYMBOL_TABLE.getInstance().enter(id, classType);

        SYMBOL_TABLE.getInstance().addClassOrArrayName(id, line);

        // old
        // TYPE_LIST dataMembers = cFieldList.SemantMe();
        // TYPE_LIST dataMembers = new TYPE_LIST();
        // for(AST_LIST<AST_C_FIELD> curr = cFieldList; curr != null; curr = curr.next) {
            //     TYPE currType = curr.data.SemantMe();
            //     SYMBOL_TABLE.getInstance().enter(curr.data.getId(), currType);
            //     dataMembers.add(curr.data.getId(), currType);
        //     classType.dataMembers = dataMembers.concat(parentParams);
        // }
        
        
        // dataMembers = dataMembers.concat(parentParams);
        //End of old
        //New - two lists: one for variables and one for methods
        
        SYMBOL_TABLE.getInstance().setLastClassName(id);
        
        System.out.println("class name: " + id); ///////////////
        for(AST_LIST<AST_C_FIELD> curr = cFieldList; curr != null; curr = curr.next) {
            TYPE currType = curr.data.SemantMe();
            if (currType.isFunction()) {
                TYPE_FUNCTION funcType = (TYPE_FUNCTION) currType;
                System.out.println("Added function name: " + curr.data.getId());
                funcType.setClassName(id);
                funcList.selectiveAdd(curr.data.getId(), currType);
            }
            else {
                varList.selectiveAdd(curr.data.getId(), currType);
                System.out.println("var name: " + curr.data.getId()); ///////////////
            }
            SYMBOL_TABLE.getInstance().enter(curr.data.getId(), currType);
        }

        numberOfVars = varList.numberList(1, 1);
        funcList.numberList(0, 1);

        //End of new
        
        SYMBOL_TABLE.getInstance().endScope();
        
        //classType = new TYPE_CLASS(parentType, id, dataMembers); // ex3
        classType.funcList = funcList;
        classType.varList = varList;
        classType.size = numberOfVars + 1;
        SYMBOL_TABLE.getInstance().enter(id, classType);

        SYMBOL_TABLE.getInstance().setLastClassName("");

        return classType;
    }

    @Override
    public TEMP IRme() {
        if (funcList.head != null) {
            IR.getInstance().Add_IRcommand(new IRcommand_Class_Vtable(id, funcList));
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Class_Init(id, parentId, funcList.head != null));
        for(AST_LIST<AST_C_FIELD> curr = cFieldList; curr != null; curr = curr.next) {
            if(curr.data instanceof AST_C_FIELD_VAR) {
                curr.data.IRme();
            }
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Function_Epilogue("init_" + id));
        for(AST_LIST<AST_C_FIELD> curr = cFieldList; curr != null; curr = curr.next) {
            if(curr.data instanceof AST_C_FIELD_FUNC) {
                curr.data.IRme();
            }
        }
        return null;
    }
}