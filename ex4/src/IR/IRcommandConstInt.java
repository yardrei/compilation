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

public class IRcommandConstInt extends IRcommand
{
	int value;
	
	public IRcommandConstInt(TEMP destination,int value)
	{
		this.destination = destination;
		this.value = value;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		// MIPSGenerator.getInstance().comment("Const int, value: " + value);
		MIPSGenerator.getInstance().li(destination,value);
	}
}
