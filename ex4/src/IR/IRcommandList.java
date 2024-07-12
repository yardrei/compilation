/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.LinkedList;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IRcommandList extends LinkedList<IRcommand>
{

	public IRcommandList()
	{
		super();
	}

	public IRcommandList(LinkedList<IRcommand> list)
	{
		super(list);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		forEach((IRcommand command) -> command.MIPSme());
	}
}
