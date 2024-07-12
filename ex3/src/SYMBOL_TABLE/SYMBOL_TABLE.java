/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/

import java.io.PrintWriter;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import exceptions.*;

/****************/
/* SYMBOL TABLE */

/****************/
public class SYMBOL_TABLE {
    private int hashArraySize = 13;

    /**********************************************/
    /* The actual symbol table data structure ... */
    /**********************************************/
    private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
    private SYMBOL_TABLE_ENTRY top;
    private int top_index = 0;
    private TYPE lastFunctionType = null;
    private TYPE_CLASS lastClassType = null;
    public TYPE_CLASS parent = null;
    public String scope = null;
    public List<String> classAndArrayList = new ArrayList<>();

    /**************************************************************/
    /* A very primitive hash function for exposition purposes ... */

    /**************************************************************/
    private int hash(String s) {
        return Math.floorMod(s.hashCode(), hashArraySize);
    }

    public void addClassOrArrayName(String name, int line) {
        if (isClassOrArrayName(name)) {
            throw new SemanticException(line);
        }
        classAndArrayList.add(name);
    }

    public boolean isClassOrArrayName(String name) {
        return classAndArrayList.contains(name);
    }

    /****************************************************************************/
    /* Enter a variable, function, class type or array type to the symbol table */

    /****************************************************************************/
    public void enter(String name, TYPE t) {
        /*************************************************/
        /* [1] Compute the hash value for this new entry */
        /*************************************************/
        int hashValue = hash(name);

        /******************************************************************************/
        /* [2] Extract what will eventually be the next entry in the hashed position */
        /* NOTE: this entry can very well be null, but the behaviour is identical */
        /******************************************************************************/
        SYMBOL_TABLE_ENTRY next = table[hashValue];

        /**************************************************************************/
        /* [3] Prepare a new symbol table entry with name, type, next and prevtop */
        /**************************************************************************/
        SYMBOL_TABLE_ENTRY entry = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++);

        /**********************************************/
        /* [4] Update the top of the symbol table ... */
        /**********************************************/
        top = entry;

        /****************************************/
        /* [5] Enter the new entry to the table */
        /****************************************/
        table[hashValue] = entry;

        /**************************/
        /* [6] Print Symbol Table */
        /**************************/
        PrintMe();
    }

    public TYPE find(String name) {
        for (SYMBOL_TABLE_ENTRY entry = table[hash(name)]; entry != null; entry = entry.next) {
            if (name.equals(entry.name)) {
                return entry.type;
            }
        }

        return null;
    }

    /***************************************************************************/
    /* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */

    /***************************************************************************/
    public void beginScope(String scope) {
        /************************************************************************/
        /* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
        /* they are not really types. In order to be ablt to debug print them, */
        /* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This */
        /* class only contain their type name which is the bottom sign: _|_ */
        /************************************************************************/
        enter(
                "SCOPE-BOUNDARY",
                new TYPE_FOR_SCOPE_BOUNDARIES(scope));

        this.scope = scope;

        /*********************************************/
        /* Print the symbol table after every change */
        /*********************************************/
        PrintMe();
    }

    /********************************************************************************/
    /* end scope = Keep popping elements out of the data structure, */
    /*
     * from most recent element entered, until a <NEW-SCOPE> element is encountered
     */

    /********************************************************************************/
    public void endScope() {
        /**************************************************************************/
        /* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */
        /**************************************************************************/
        while (top.name != "SCOPE-BOUNDARY") {
            table[top.index] = top.next;
            top_index = top_index - 1;
            top = top.prevtop;
        }
        /**************************************/
        /* Pop the SCOPE-BOUNDARY sign itself */
        /**************************************/
        table[top.index] = top.next;
        top_index = top_index - 1;
        top = top.prevtop;

        /*********************************************/
        /* Print the symbol table after every change */
        /*********************************************/
        PrintMe();
    }

    public void beginClass(String scope) {
        enter(
                "class",
                new TYPE_FOR_SCOPE_BOUNDARIES(scope));

        this.scope = scope;
        PrintMe();
    }

    public String getScope() {
        for (SYMBOL_TABLE_ENTRY entry = table[hash("SCOPE-BOUNDARY")]; entry != null; entry = entry.next) {
            if (entry.type.getClass().getSimpleName().equals(TYPE_FOR_SCOPE_BOUNDARIES.class.getSimpleName())) {
                return entry.type.name;
            }
        }

        return null;
    }

    /********************************************************/
    /* Find the index of inner-most scope element with name */
    /********************************************************/
    public int getIndex(String name) {
        for (SYMBOL_TABLE_ENTRY entry = table[hash(name)]; entry != null; entry = entry.next) {
            if (name.equals(entry.name)) {
                return entry.prevtop_index;
            }
        }
        return -1;
    }

    /************************************************************/
    /* Find if the element with name is in the inner-most scope */
    /************************************************************/
    public boolean isInCurrentScope(String name) {
        if (getIndex(name) == -1) {
            return false;
        }
        if (getIndex(name) > getIndex("SCOPE-BOUNDARY")) {
            return true;
        }
        return false;
    }


    /************************************************************/
    /* Find if the element with name is in the inner-most class */
    /************************************************************/
    public boolean isInCurrentClass(String name) {
        if (getIndex(name) == -1) {
            return false;
        }

        if (getIndex(name) > getIndex("class")) {
            return true;
        }
        return false;
    }


    /***********************************************/
    /** Let you know and change the last function **/
    /***********************************************/
    public TYPE getLastFunctionType() {
        return lastFunctionType;
    }

    public void setLastFunction(TYPE functionType) {
        lastFunctionType = functionType;
    }

    /********************************************/
    /** Let you know and change the last class **/
    /********************************************/
    public TYPE_CLASS getLastClassType() {
        return lastClassType;
    }

    public void setLastClass(TYPE_CLASS lastClass) {
        lastClassType = lastClass;
    }

    public static int n = 0;

    public void PrintMe() {
        int i = 0;
        int j = 0;
        String dirname = "./output/";
        String filename = String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt", n++);
        PrintWriter fileWriter = null;

        try {
            /*******************************************/
            /* [1] Open Graphviz text file for writing */
            /*******************************************/
            fileWriter = new PrintWriter(dirname + filename);

            /*********************************/
            /* [2] Write Graphviz dot prolog */
            /*********************************/
            fileWriter.print("digraph structs {\n");
            fileWriter.print("rankdir = LR\n");
            fileWriter.print("node [shape=record];\n");

            /*******************************/
            /* [3] Write Hash Table Itself */
            /*******************************/
            fileWriter.print("hashTable [label=\"");
            for (i = 0; i < hashArraySize - 1; i++) {
                fileWriter.format("<f%d>\n%d\n|", i, i);
            }
            fileWriter.format("<f%d>\n%d\n\"];\n", hashArraySize - 1, hashArraySize - 1);

            /****************************************************************************/
            /* [4] Loop over hash table array and print all linked lists per array cell */
            /****************************************************************************/
            for (i = 0; i < hashArraySize; i++) {
                if (table[i] != null) {
                    /*****************************************************/
                    /* [4a] Print hash table array[i] -> entry(i,0) edge */
                    /*****************************************************/
                    fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n", i, i);
                }
                j = 0;
                for (SYMBOL_TABLE_ENTRY it = table[i]; it != null; it = it.next) {
                    /*******************************/
                    /* [4b] Print entry(i,it) node */
                    /*******************************/
                    fileWriter.format("node_%d_%d ", i, j);
                    fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
                            it.name,
                            it.type.name,
                            it.prevtop_index);

                    if (it.next != null) {
                        /***************************************************/
                        /* [4c] Print entry(i,it) -> entry(i,it.next) edge */
                        /***************************************************/
                        fileWriter.format(
                                "node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
                                i, j, i, j + 1);
                        fileWriter.format(
                                "node_%d_%d:f3 -> node_%d_%d:f0;\n",
                                i, j, i, j + 1);
                    }
                    j++;
                }
            }
            fileWriter.print("}\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static SYMBOL_TABLE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */

    /*****************************/
    protected SYMBOL_TABLE() {
    }

    /******************************/
    /* GET SINGLETON INSTANCE ... */

    /******************************/
    public static SYMBOL_TABLE getInstance() {
        if (instance == null) {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new SYMBOL_TABLE();

            /*****************************************/
            /* [1] Enter primitive types int, string */
            /*****************************************/
            instance.enter("int", new TYPE_INT(false));
            instance.enter("string", new TYPE_STRING(false));

            /*************************************/
            /* [2] How should we handle void ??? */
            /*************************************/

            /***************************************/
            /* [3] Enter library function PrintInt */
            /***************************************/
            instance.enter(
                    "PrintInt",
                    new TYPE_FUNCTION(
                            TYPE_VOID.getInstance(),
                            "PrintInt",
                            new TYPE_LIST(
                                    new TYPE_INT(false), null,
                                    null)));

            instance.enter(
                    "PrintString",
                    new TYPE_FUNCTION(
                            TYPE_VOID.getInstance(),
                            "PrintString",
                            new TYPE_LIST(
                                    new TYPE_STRING(false), null,
                                    null)));


        }
        return instance;
    }

    public static void debug() {

    }
}
