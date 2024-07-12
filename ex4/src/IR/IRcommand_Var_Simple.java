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
import MIPS.*;
import TEMP.*;
import SYMBOL_TABLE.scopeEnum;

public class IRcommand_Var_Simple extends IRcommand
{
	scopeEnum scope;
	int location;
	String name;

	
	public IRcommand_Var_Simple(TEMP destinaion, scopeEnum scope, int location, String name)
	{
		this.destination = destinaion;
		this.scope = scope;
		this.location = location;
		this.name = name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (scope == scopeEnum.Global) {//global
			MIPSGenerator.getInstance().varSimpleGlobalLocation(destination, name);
		}
		else if (scope == scopeEnum.Local) {//local
			MIPSGenerator.getInstance().varSimpleLocalLocation(destination, location);
		}
		else { //field
			MIPSGenerator.getInstance().varSimpleFieldLocation(destination, location);
		}
	}
}
