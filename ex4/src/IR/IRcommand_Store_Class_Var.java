package IR;

import TEMP.*;

import java.util.Arrays;

import MIPS.*;

public class IRcommand_Store_Class_Var extends IRcommand
{
	TEMP src;
	int location;
	
	public IRcommand_Store_Class_Var(TEMP src, int location)
	{
		this.src      = src;
		this.location = location;
		sources = Arrays.asList(src);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeClassVar(src, location);
	}
}
