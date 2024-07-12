package IR;

import TEMP.*;

import java.util.Arrays;

import MIPS.*;

public class IRcommand_Method_Call extends IRcommand
{
	TEMP locTemp;
	int location;
	int numberOfParam;
	boolean isVoid;
	
	public IRcommand_Method_Call (TEMP destination, TEMP locTemp, int location, int numberOfParam, boolean isVoid)
	{
		this.locTemp = locTemp;
		this.location = location;
		this.numberOfParam = numberOfParam;
		this.isVoid = isVoid;
		if (!this.isVoid) {
			this.destination = destination;
		}
		sources = Arrays.asList(locTemp);
	}
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().comment("Method call");
		MIPSGenerator.getInstance().methodCall(destination, locTemp, location, isVoid);
		MIPSGenerator.getInstance().comment("number of params: " + numberOfParam);
		MIPSGenerator.getInstance().moveStackPointer(-1 * (numberOfParam + 1));
	}
}
