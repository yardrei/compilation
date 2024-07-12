package AST;

import TYPES.*;
import exceptions.*;

import java.util.ArrayList;
import java.util.List;

import SYMBOL_TABLE.*;

public class AST_CLASS_DEC extends AST_DEC {
    public String id;
    public String parentId;
    public AST_LIST<AST_C_FIELD> cFieldList;

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

    public static <T> List<T> reverse(List<T> list) {
        List<T> reversed = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            reversed.add(list.get(i));
        }
        return reversed;
    }

    public TYPE SemantMe() {
        TYPE_CLASS parentType;
        TYPE_LIST parentParams = null;

        String scope = SYMBOL_TABLE.getInstance().getScope();
        if (!("global".equals(scope))) {
            throw new SemanticException(line, String.format("Class %s cannot be defined inside a scope", id));
        }

        if (SYMBOL_TABLE.getInstance().isInCurrentScope(id)) {
            throw new SemanticException(line, String.format("%s has already been defined", id));
        }

        SYMBOL_TABLE.getInstance().beginScope("class");

        if (parentId == null) {
            parentType = null;
        } else {
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
            parentParams = parentType.dataMembers;
        }
        SYMBOL_TABLE.getInstance().beginClass(id);

        // Enter the class without any data members to avoid issues with self-containing
        // classe
        TYPE_CLASS classType = new TYPE_CLASS(parentType, id, parentParams);
        SYMBOL_TABLE.getInstance().enter(id, classType);

        SYMBOL_TABLE.getInstance().addClassOrArrayName(id, line);

        // TYPE_LIST dataMembers = cFieldList.SemantMe();
        TYPE_LIST dataMembers = new TYPE_LIST();
        for(AST_LIST<AST_C_FIELD> curr = cFieldList; curr != null; curr = curr.next) {
            TYPE currType = curr.data.SemantMe();
            SYMBOL_TABLE.getInstance().enter(curr.data.getId(), currType);
            dataMembers.add(curr.data.getId(), currType);
            classType.dataMembers = dataMembers.concat(parentParams);
        }



        dataMembers = dataMembers.concat(parentParams);
            // TODO Check that all overriding functions have matching return types

        SYMBOL_TABLE.getInstance().endScope();

        classType = new TYPE_CLASS(parentType, id, dataMembers);
        SYMBOL_TABLE.getInstance().enter(id, classType);

        return classType;
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
}