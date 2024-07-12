package TYPES;

public class TYPE_LIST extends TYPE {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public TYPE head;
    public TYPE_LIST next;
    private TYPE_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */

    /******************/
    public TYPE_LIST(TYPE head, String name, TYPE_LIST next) {
        this.head = head;
        this.name = name;
        this.next = next;

        for (tail = this; tail.next != null; tail = tail.next)
            ;
    }

    public TYPE_LIST(TYPE head, String name) {
        this.head = head;
        this.name = name;
        this.next = null;
        this.tail = this;
    }

    public TYPE_LIST() {
        this.head = null;
        this.name = null;
        this.next = null;
        this.tail = this;
    }


    public void add(String name, TYPE type) {
        if (this.head == null) {
            this.head = type;
            this.name = name;
            this.tail = this;
        } else {
            this.tail.next = new TYPE_LIST(type, name);
            this.tail = this.tail.next;
        }
    }

    public TYPE_LIST(TYPE_LIST o) {
        this.head = o.head;
        this.name = o.name;

        if (o.next == null) {
            this.next = null;
        } else {
            this.next = new TYPE_LIST(o.next);
        }
    }

    public TYPE_LIST concat(TYPE_LIST r) {
        if(r == null) {
            return new TYPE_LIST(this);
        }

        TYPE_LIST l = new TYPE_LIST(this);

        TYPE_LIST curr;
        for (curr = l; curr.next != null; curr = curr.next)
            ;

        curr.next = new TYPE_LIST(r);

        return l;
    }

    public void selectiveAdd(String name, TYPE r) {
        if(r == null) {
            return;
        }

        if (this.head == null) {
            this.head = r;
            this.name = name;
            return;
        }
        
        System.out.println("this name: " + name); ///////////////
        for (TYPE_LIST curr = this; curr != null; curr = curr.next) {
            if(curr.name.equals(name)) {
                System.out.println("curr name: " + curr.name); ///////////////
                curr.head = r;
                return;
            }
        }

        TYPE_LIST newNode = new TYPE_LIST(r, name);
        TYPE_LIST curr;
        for (curr = this; curr.next != null; curr = curr.next) {
            curr.tail = newNode;
        }
        curr.tail = newNode;
        curr.next = newNode;
    }


    public int numberList(int start, int increment) {
        int size = 0;
        for (TYPE_LIST curr = this; curr != null && curr.head != null; curr = curr.next, size++) {
            System.out.println("Is null: " + (curr.head == null) + " size: " + size); /////////////
            curr.head.location = start + (increment * size);
        }
        return size;
    }

    @Override
    public boolean isAssignableTo(TYPE obj) {
        if (!(obj instanceof TYPE_LIST)) {
            return false;
        }

        TYPE_LIST castObj = (TYPE_LIST) obj;
        TYPE_LIST left, right;

        for (left = castObj, right = this; left != null && right != null; left = left.next, right = right.next) {
            if (!right.head.isAssignableTo(left.head)) {
                return false;
            }
        }

        // Check that both lists have reached the end
        if (left != null || right != null) {
            return false;
        }

        return true;
    }
}