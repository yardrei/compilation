package IR;

import MIPS.*;
import TYPES.TYPE_LIST;

// Creates the Vtable for a class(name)
public class IRcommand_Class_Vtable extends IRcommand
{
	String name;
	TYPE_LIST funcList;
	
	public IRcommand_Class_Vtable(String name, TYPE_LIST funcList)
	{
		this.name = name;
		this.funcList = funcList;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().vtLabel(name);
		MIPSGenerator.getInstance().createClassVtable(name, funcList);
	}
}
