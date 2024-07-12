/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import MIPS.*;

public class IRcommand_Const_String extends IRcommand
{
	String label;
	String str;
	
	public IRcommand_Const_String(String label, String str)
	{
		this.label = label;
		this.str = str.replace("\"", "");
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().constString(label, str);
	}
}
