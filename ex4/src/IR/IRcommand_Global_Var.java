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
import MIPS.*;

public class IRcommand_Global_Var extends IRcommand
{
	String var_name;
	String data = "0";
	
	public IRcommand_Global_Var(String var_name)
	{
		this.var_name = var_name;
	}

	public IRcommand_Global_Var(String var_name, String data)
	{
		this.var_name = var_name;
		this.data = data;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocate(String.format("global_%s", var_name), data);
	}
}
