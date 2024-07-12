package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;
	public boolean constant;

	public TYPE() {
		name = this.getClass().getSimpleName();
		constant = false;
	}

	public boolean isAssignableTo(TYPE t) {
		if (t == null) {
			return false;
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
		return name.equals(TYPE_FUNCTION.class.getSimpleName());
	}

	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof TYPE)) {
			return false;
		}
		return ((TYPE)obj).name.equals(this.name);
	}
}
