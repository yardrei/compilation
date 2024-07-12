package AST;

import IR.IR;
import IR.IRcommand_Exp_String;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;
import TYPES.*;

public class AST_EXP_STRING extends AST_EXP
{
	public String value;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(String value, int line)
	{
		super(line);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
	}

	/************************************************/
	/* The printing message for an STR EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST STR EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STR(%s)",value));
	}
	public TYPE SemantMe(){
		return new TYPE_STRING(true);
	}

	@Override
	public TEMP IRme() {
		TEMP register = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Exp_String(register, value));

		return register;
	}
}