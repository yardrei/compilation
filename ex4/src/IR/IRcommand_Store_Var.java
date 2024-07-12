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

public class IRcommand_Store_Var extends IRcommand
{
	TEMP locTemp;
	TEMP src;
	
	public IRcommand_Store_Var(TEMP locTemp, TEMP src)
	{
		this.locTemp = locTemp;
		this.src      = src;
		sources = Arrays.asList(src, locTemp);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeVar(locTemp, src);
	}
}
