package IR;

import TYPES.*;
import TEMP.*;
import MIPS.*;

public class IRcommand_Allocate_New extends IRcommand
{
	TYPE type;
	
	public IRcommand_Allocate_New(TEMP destination, TYPE type)
	{
		this.destination = destination;
		this.type = type;

	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (type instanceof TYPE_ARRAY) {
			MIPSGenerator.getInstance().allocateArray(destination, ((TYPE_ARRAY) type).size);
		}
		else {
			TYPE_CLASS typeClass = (TYPE_CLASS) type;
			MIPSGenerator.getInstance().allocateClass(destination, typeClass.name, typeClass.size);
		}
	}
}
