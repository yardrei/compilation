package TYPES;

public class TYPE_NIL extends TYPE
{
    private static TYPE_NIL instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected TYPE_NIL() {
        this.constant = true;
    }

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static TYPE_NIL getInstance()
    {
        if (instance == null)
        {
            instance = new TYPE_NIL();
            instance.name = "nil";
        }
        return instance;
    }

    @Override
	public boolean isAssignableTo(TYPE o) {
        if(o.isClass() || o.isArray()) {
            return true;
        }
        return false;
    }
}
