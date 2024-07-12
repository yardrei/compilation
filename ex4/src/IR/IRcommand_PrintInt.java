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

public class IRcommand_PrintInt extends IRcommand
{
	TEMP t;
	
	public IRcommand_PrintInt(TEMP t)
	{
		this.t = t;
		sources = Arrays.asList(t);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().print_int(t);
	}
}
