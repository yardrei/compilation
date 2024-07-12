package AST;

import TYPES.*;
import TEMP.*;

public class AST_LIST<T extends AST_Node> extends AST_Node {

    public T data;
    public AST_LIST<T> next;

    public AST_LIST(T data, AST_LIST<T> next, int line) {
        super(line);

        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.data = data;
        this.next = next;
    }

    public void PrintMe() {
        if (data != null) {
            data.PrintMe();
        }
        if (next != null) {
            next.PrintMe();
        }

        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s\nLIST", data.getClass().getSimpleName()));

        if (data != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, data.SerialNumber);
        }
        if (next != null) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, next.SerialNumber);
        }
    }

    public TYPE_LIST SemantMe() {
        return new TYPE_LIST(data.SemantMe(),
                data.getId(),
                next == null ? null : next.SemantMe());
    }

    public TEMP IRme() {
        data.IRme();
        if (next != null) {
            next.IRme();
        }
        return null;
    }

    public int size() {
        int size = 0;
        for(AST_LIST<T> l = this; l != null; l = l.next) {
            size++;
        }

        return size;
    }
}