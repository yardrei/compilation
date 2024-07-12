package TYPES;

public class TYPE_FUNCTION extends TYPE {
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;
	public int numOfParams = 0;

	//For methods
	public String className = null;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params, int numOfParams) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.numOfParams = numOfParams;
	}

	private boolean areParamsAssignable(TYPE_FUNCTION o) {
		if (params == null && o.params == null) {
			return true;
		}
		return params.isAssignableTo(o.params);
	}

	public boolean canOverwriteParent(TYPE parent) {
		if (parent == null || !parent.isFunction()) {
			return false;
		}
		TYPE_FUNCTION castParent = (TYPE_FUNCTION) parent;
		return isAssignableTo(parent)
				|| (this.returnType instanceof TYPE_VOID
						&& castParent.returnType instanceof TYPE_VOID
						&& areParamsAssignable(castParent));
	}

	public void setClassName(String name) {
		this.className = name;
	}

	@Override
	public boolean isAssignableTo(TYPE obj) {
		if (obj == null || !obj.isFunction()) {
			return false;
		}
		if (obj.isVar()) {
            return this.isAssignableTo(((TYPE_VAR)obj).type);
        }
		TYPE_FUNCTION funcObj = (TYPE_FUNCTION) obj;
		if (returnType.isAssignableTo(funcObj.returnType)) {
			return areParamsAssignable(funcObj);
		}
		return false;
	}
}
