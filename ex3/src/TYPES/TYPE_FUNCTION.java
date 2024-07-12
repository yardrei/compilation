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

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params) {
		this.returnType = returnType;
		this.params = params;
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

	@Override
	public boolean isAssignableTo(TYPE obj) {
		if (obj == null || !obj.isFunction()) {
			return false;
		}
		TYPE_FUNCTION funcObj = (TYPE_FUNCTION) obj;
		if (returnType.isAssignableTo(funcObj.returnType)) {
			return areParamsAssignable(funcObj);
		}
		return false;
	}
}
