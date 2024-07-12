package IR;

import MIPS.*;

public class IRcommand_Function_Epilogue extends IRcommand
{
	
	private String label_name;

	public IRcommand_Function_Epilogue(String function_name)
	{
		label_name = function_name + "_epilogue";
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().functionEpilogue(label_name);
	}
}
