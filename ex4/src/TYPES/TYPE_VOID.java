package TYPES;

public class TYPE_VOID extends TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static TYPE_VOID instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_VOID() {
		this.constant = true;
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_VOID getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_VOID();
			instance.name = "void";
		}
		return instance;
	}

	@Override
	public boolean isAssignableTo(TYPE t) {
		return false;
	}
}
