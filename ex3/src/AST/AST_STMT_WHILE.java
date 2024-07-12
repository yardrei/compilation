package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;

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
		if (!(cond.SemantMe() instanceof TYPE_INT)) {
			throw new SemanticException(line);
		}
		SYMBOL_TABLE.getInstance().beginScope("while");
		body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}
}