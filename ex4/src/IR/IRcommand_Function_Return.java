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
import MIPS.*;
import TEMP.*;

public class IRcommand_Function_Return extends IRcommand
{
	private String function_name;
	private TEMP src;
	
	public IRcommand_Function_Return (TEMP src, String function_name)
	{
		this.function_name = function_name;
		// if (function_name.equals("main")) {
		// 	this.function_name = "user_main";
		// }
		this.src = src;
		if (src != null) {
			sources = Arrays.asList(src);
		}
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().functionReturn(src, function_name);
	}
}
