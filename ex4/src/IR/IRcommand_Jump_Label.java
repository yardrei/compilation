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

public class IRcommand_Jump_Label extends IRcommand_Jump
{
	
	public IRcommand_Jump_Label(String label_name)
	{
		super(label_name);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jump(label_name);
	}
}
