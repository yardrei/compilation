package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Function_Call extends IRcommand
{

	String name;
	int numberOfParam;
	boolean isVoid;
	
	public IRcommand_Function_Call (TEMP destination, String name, int numberOfParam, boolean isVoid)
	{
		this.name = name;
		this.numberOfParam = numberOfParam;
		this.isVoid = isVoid;
		if (!this.isVoid) {
			this.destination = destination;
		}
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().functionCall(destination, name, this.isVoid);
		MIPSGenerator.getInstance().moveStackPointer(-1 * numberOfParam);
	}
}
