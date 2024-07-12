package TYPES;

public class TYPE_VAR extends TYPE
{
	TYPE type;
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_VAR(String name, TYPE type)
	{
		this.name = name;
		this.type = type;
	}


	public boolean isAssignableTo(TYPE o) {
		if (o == null) {
			return false;
		}
		if (o.isVar()) {
			return type.isAssignableTo(((TYPE_VAR)o).type);
		}
        return type.isAssignableTo(o);
	}
}
