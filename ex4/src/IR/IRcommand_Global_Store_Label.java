/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.*;

public class IRcommand_Global_Store_Label extends IRcommand
{
	String var_name;
	
	public IRcommand_Global_Store_Label(String var_name)
	{
		this.var_name = var_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		String label = IRcommand.getFreshLabel(var_name);
		MIPSGenerator.getInstance().globalCommandsLabels.add(label);
		MIPSGenerator.getInstance().storeGlobalLabel(label);
	}
}
