package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;
	public boolean constant;
	public int location = 0;

	public TYPE() {
		name = this.getClass().getSimpleName();
		constant = false;
	}

	public boolean isAssignableTo(TYPE t) {
		if (t == null) {
			return false;
		}
		if (t.isVar()) {
			return this.isAssignableTo(((TYPE_VAR)t).type);
		}
		return this.getClass().getSimpleName().equals(t.getClass().getSimpleName());
	}

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){
		return this instanceof TYPE_CLASS;
	}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){
		return this instanceof TYPE_ARRAY;
	}


	public boolean isInt(){
		return this instanceof TYPE_INT;
	}

	public boolean isString(){
		return this instanceof TYPE_STRING;
	}

	public boolean isVar(){
		return this instanceof TYPE_VAR;
	}

	public boolean isPrimitive() {
		return (this instanceof TYPE_INT)
				|| (this instanceof TYPE_STRING)
				|| (this instanceof TYPE_NIL)
		        || (this instanceof TYPE_VOID);
	}


	public boolean isFunction(){
		return this instanceof TYPE_FUNCTION;
	}

	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof TYPE)) {
			return false;
		}
		return ((TYPE)obj).name.equals(this.name);
	}
}
