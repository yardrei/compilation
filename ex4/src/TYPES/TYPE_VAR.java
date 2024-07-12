package TYPES;

import SYMBOL_TABLE.*;

public class TYPE_VAR extends TYPE
{
	public TYPE type;
	
	/***************/
	/* AST anntate */
	/***************/
	public scopeEnum scope;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_VAR(String name, TYPE type)
	{
		this.name = name;
		this.type = type;
		this.scope = scopeEnum.Undefined;
		this.location = 0;
	}

	public TYPE_VAR(String name, TYPE type, scopeEnum scope, int location)
	{
		this.name = name;
		this.type = type;
		this.scope = scope;
		this.location = location;
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

	@Override
	public boolean isArray() {
		if (type == null){
			return false;
		}
		return type.isArray();
	}

	@Override
	public boolean isVar() {
		return true;
	}

	@Override
	public boolean isClass() {
		return type.isClass();
	}

	@Override
	public boolean isInt() {
		return type.isInt();
	}

	@Override
	public boolean isString() {
		return type.isString();
	}
}
