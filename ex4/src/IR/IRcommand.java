/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.ArrayList;
import java.util.List;
import TEMP.TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public abstract class IRcommand
{

	public TEMP destination = null;
	public List<TEMP> sources = new ArrayList<>();

	/*****************/
	/* Label Factory */
	/*****************/

	protected static int label_counter=0;
	public    static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s",label_counter++,msg);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public abstract void MIPSme();
}
