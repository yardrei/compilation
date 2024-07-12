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
import MIPS.*;

public class IRcommand_Load_Label extends IRcommand
{
	String var_name;
	int offset;
	
	public IRcommand_Load_Label(TEMP destination,String var_name, int offset)
	{
		this.destination  = destination;
		this.var_name = var_name;
		this.offset	= offset;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().loadLabel(destination,var_name, offset);
	}
}
