/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

import java.util.Arrays;

import MIPS.*;

public class IRcommand_Store_Local extends IRcommand
{
	TEMP src;
	boolean isInit;
	int location;
	
	public IRcommand_Store_Local(TEMP src, boolean isInit, int loaction)
	{
		this.isInit = isInit;
		this.location = loaction;
		if (isInit) {
			this.src = src;
			sources = Arrays.asList(src);
		}
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().moveStackPointerFromFrame(location);
		if (isInit) {
			MIPSGenerator.getInstance().storeLocal(src, 0);
		}
	}
}
