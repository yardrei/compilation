package IR;

import TEMP.*;

import java.util.Arrays;

import MIPS.*;

public class IRcommand_PrintString extends IRcommand
{
	TEMP src;
	
	public IRcommand_PrintString(TEMP src)
	{
		this.src = src;
		sources = Arrays.asList(src);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().print_string(src);
	}
}
