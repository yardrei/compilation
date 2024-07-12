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

public class IRcommand_Binop_Mul_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	
	public IRcommand_Binop_Mul_Integers(TEMP destination,TEMP t1,TEMP t2)
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
		MIPSGenerator.getInstance().mul(destination,t1,t2);
	}
}
