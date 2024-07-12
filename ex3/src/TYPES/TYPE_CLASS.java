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
	public TYPE_LIST dataMembers;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST dataMembers) {
		this.name = name;
		this.father = father;
		if (dataMembers == null) {
			this.dataMembers = new TYPE_LIST();
		} else {
			this.dataMembers = dataMembers;
		}
	}

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

	@Override
	public boolean isAssignableTo(TYPE o) {
		if (!(o instanceof TYPE_CLASS)) {
			return false;
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
