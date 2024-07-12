package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_STMT_WHILE extends AST_STMT {
	public AST_EXP cond;
	public AST_LIST<AST_STMT> body;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond, AST_LIST<AST_STMT> body, int line) {
		super(line);

		this.cond = cond;
		this.body = body;
	}

	public void PrintMe() {
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT\nWHILE\n");

		if (cond != null) {
			cond.PrintMe();
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
		}

		if (body != null) {
			body.PrintMe();
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
		}
	}

	public TYPE SemantMe() {
		if (!(cond.SemantMe().isInt())) {
			throw new SemanticException(line);
		}
		SYMBOL_TABLE.getInstance().beginScope("while");
		body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}

	public TEMP IRme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("end");
		String label_start = IRcommand.getFreshLabel("start");
	
		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_start));

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));		

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.IRme();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_Label(label_start));		

		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_end));

		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}
}