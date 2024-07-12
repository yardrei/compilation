package IR;

import TEMP.*;

import java.util.Arrays;

import MIPS.*;

public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand_Jump
{
	TEMP t;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label_name)
	{
		super(label_name);
		this.t = t;
		sources = Arrays.asList(t);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().beqz(t,label_name);
	}
}
