package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_PROG extends AST_Node
{
	public AST_LIST<AST_DEC> decList;

	public AST_PROG(AST_LIST<AST_DEC> decList, int line) {
		super(line);

		this.decList = decList;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		if(decList != null) {
			decList.PrintMe();
		}


		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PROG\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (decList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,decList.SerialNumber);
	}

	public TYPE SemantMe() {
		SYMBOL_TABLE.getInstance().beginScope("global");
		decList.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();

		return null;
	}
}
