package TYPES;

public class TYPE_ARRAY extends TYPE {
    public TYPE type;

    /****************/
    /* CTROR(S) ... */

    /****************/
    public TYPE_ARRAY(String name, TYPE type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isAssignableTo(TYPE o) {
        if (o == null) {
            return false;
        }
        if (o instanceof TYPE_NIL) {
            return true;
        }

        if (name == o.name) {
            return true;
        }
        return (o.isArray()
                && this.name.equals("NEW")
                && this.type.isAssignableTo(((TYPE_ARRAY) o).type));

        // if(!super.isAssignableTo(o)) {
        //     return false;
        // }

        // TYPE_ARRAY castO = (TYPE_ARRAY) o;

        // if(castO.type instanceof TYPE_ARRAY && this.type instanceof TYPE_ARRAY) {
        //     return this.type.isAssignableTo(castO.type);
        // }

        // if(castO.type instanceof TYPE_CLASS && this.type instanceof TYPE_CLASS) {
        //     return ((TYPE_CLASS) this.type).isAssignableTo(castO.type);
        // }

        // return this.type.isAssignableTo(castO.type);
    }
}