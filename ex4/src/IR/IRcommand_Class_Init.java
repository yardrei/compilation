package IR;

import MIPS.*;

// Creates the initialization for class variables
public class IRcommand_Class_Init extends IRcommand
{
	String className;
	String parentName;
	boolean hasFuncs;

	
	public IRcommand_Class_Init(String className, String parentName, boolean hasFuncs)
	{
		this.className = className;
		this.parentName = parentName;
		this.hasFuncs = hasFuncs;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().label("init_" + className);
		MIPSGenerator.getInstance().functionPrologue();
		if (parentName != null) {
			MIPSGenerator.getInstance().jumpAndLink("init_" + parentName);
		}
		if (hasFuncs) {
			MIPSGenerator.getInstance().addVtableToAlloc(className);
		}
	}
}
