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

public class IRcommand_Global_Store_End extends IRcommand
{
	String var_name;
	TEMP src;
	
	public IRcommand_Global_Store_End(String var_name,TEMP src)
	{
		this.src      = src;
		this.var_name = var_name;
		sources = Arrays.asList(src);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().storeGlobalEnd(var_name, src);
	}
}
