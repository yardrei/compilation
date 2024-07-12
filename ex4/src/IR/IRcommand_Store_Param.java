package IR;


import TEMP.*;

import java.util.Arrays;

import MIPS.*;

public class IRcommand_Store_Param extends IRcommand
{
	TEMP src;
	int location;
	int size;
	boolean isFirst;
	
	public IRcommand_Store_Param(TEMP src, int location, int size, boolean isFirst)
	{
		this.src = src;
		this.location = location;
		this.size = size;
		this.isFirst = isFirst;
		sources = Arrays.asList(src);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (isFirst) {
			MIPSGenerator.getInstance().moveStackPointer(size);
			MIPSGenerator.getInstance().storeLocal(src, 0);
		}
		else {
			MIPSGenerator.getInstance().storeLocal(src, location);
		}
	}
}
