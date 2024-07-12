package TYPES;

public class TYPE_CLASS extends TYPE {
	/*********************************************************************/
	/* If this class does not extend a father class this should be null */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods */
	/**************************************************/
	public TYPE_LIST dataMembers; // ex3

	public TYPE_LIST varList; // ex4
	public TYPE_LIST funcList; // ex4
	public int size;

	/****************/
	/* CTROR(S) ... */
	/****************/
	//old
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST dataMembers) {
		this.name = name;
		this.father = father;
		if (dataMembers == null) {
			this.dataMembers = new TYPE_LIST();
		} else {
			this.dataMembers = dataMembers;
		}
	}

	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST varList, TYPE_LIST funcList, int numberOfVars) {
		this.name = name;
		this.father = father;
		this.varList = varList;
		this.funcList = funcList;
		// The size consists of the number of variables + 1 for the pointer to the father class
		this.size = numberOfVars + 1;
	}

	//old
	public TYPE findType(String id) {
		if (id == null) {
			return null;
		}
		for (TYPE_CLASS currClass = this; currClass != null; currClass = currClass.father) {
			for (TYPE_LIST temp = currClass.dataMembers; temp != null; temp = temp.next) {
				if (id.equals(temp.name)) {
					return temp.head;
				}
			}
		}

		return null;
	}

	public TYPE findVar(String id) {
		if (id == null) {
			return null;
		}
		for (TYPE_LIST temp = varList; temp != null; temp = temp.next) {
			if (id.equals(temp.name)) {
				return temp.head;
			}
		}
		return null;
	}

	public TYPE findFunc(String id) {
		if (id == null) {
			return null;
		}
		for (TYPE_LIST temp = funcList; temp != null; temp = temp.next) {
			if (id.equals(temp.name)) {
				return temp.head;
			}
		}
		return null;
	}

	@Override
	public boolean isAssignableTo(TYPE o) {
		if (!(o.isClass())) {
			return false;
		}
		if (o.isVar()) {
            return this.isAssignableTo(((TYPE_VAR)o).type);
        }

		TYPE_CLASS castO = (TYPE_CLASS) o;
		return castO.isAncestorOf(this);
	}

	public boolean isAncestorOf(TYPE_CLASS o) {
		for (TYPE_CLASS ancestor = o; ancestor != null; ancestor = ancestor.father) {
			if (ancestor.name == name) {
				return true;
			}
		}
		return false;
	}
}
