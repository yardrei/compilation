/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.Arrays;

import MIPS.*;
import TEMP.*;

public class IRcommand_Var_Field extends IRcommand
{
	int location;
	TEMP src;
	boolean needLoad;
	
	public IRcommand_Var_Field(TEMP destination, TEMP src, int location)
	{
		this.destination = destination;
		this.src = src;
		this.location = location;
		sources = Arrays.asList(src);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().varFieldLocation(destination, src, location);
	}
}
