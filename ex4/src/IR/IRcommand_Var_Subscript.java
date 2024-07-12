/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.Arrays;

import MIPS.*;
import TEMP.*;

public class IRcommand_Var_Subscript extends IRcommand
{
	TEMP src;
	TEMP locTemp;
	int size;
	
	public IRcommand_Var_Subscript(TEMP destination, TEMP src, TEMP locTemp, int size)
	{
		this.destination = destination;
		this.src = src;
		this.locTemp = locTemp;
		this.size = size;
		this.sources = Arrays.asList(src, locTemp);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().varSubscriptLocation(destination, src, locTemp, size);
	}
}
