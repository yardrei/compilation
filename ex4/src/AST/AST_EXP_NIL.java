package AST;

import IR.IR;
import IR.IRcommand_Exp_Nil;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;
import TYPES.*;

public class AST_EXP_NIL extends AST_EXP {
	public AST_EXP_NIL(int line) {
		super(line);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
	}

	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe() {
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE EXP NIL\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"EXP\nNIL");

	}

	public TYPE SemantMe() {
		return TYPE_NIL.getInstance();
	}

	@Override
	public TEMP IRme() {
		TEMP register = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Exp_Nil(register));
		
		return register;
	}
}
