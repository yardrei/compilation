/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

import java.util.Arrays;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Binop_Sub_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	
	public IRcommand_Binop_Sub_Integers(TEMP destination,TEMP t1,TEMP t2)
	{
		this.destination = destination;
		this.t1 = t1;
		this.t2 = t2;
		sources = Arrays.asList(t1, t2);
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().sub(destination,t1,t2);
	}
}
