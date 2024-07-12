package AST;

import TYPES.*;
import exceptions.*;

public class AST_EXP_BINOP extends AST_EXP {

	public AST_BINOP_OP OP;
	public AST_EXP left;
	public AST_EXP right;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, AST_BINOP_OP OP, int line) {
		super(line);
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null)
			left.PrintMe();
		if (OP != null)
			OP.PrintMe();
		if (right != null)
			right.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "BINOP\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left != null)
			AST_GRAPHVIZ
					.getInstance()
					.logEdge(SerialNumber, left.SerialNumber);
		if (OP != null)
			AST_GRAPHVIZ
					.getInstance()
					.logEdge(SerialNumber, OP.SerialNumber);
		if (right != null)
			AST_GRAPHVIZ
					.getInstance()
					.logEdge(SerialNumber, right.SerialNumber);
	}

	@Override
	public TYPE SemantMe() {
		TYPE typeExp1 = left.SemantMe();
		TYPE typeExp2 = right.SemantMe();

		switch (OP.OP) {
			case 1:
			case 2:
			case 4:
			case 5:
				if (typeExp1.name == "int" && typeExp2.name == "int") {
					return new TYPE_INT(false);
				} else {
					throw new SemanticException(line);
				}
			case 3:
				if (right instanceof AST_EXP_INT && ((AST_EXP_INT) right).value == 0) {
					throw new SemanticException(line);
				}
				if (typeExp1.name == "int" && typeExp2.name == "int") {
					return new TYPE_INT(false);
				} else {
					throw new SemanticException(line);
				}
			case 0:
				if(typeExp1 instanceof TYPE_STRING && typeExp2 instanceof TYPE_STRING) {
					return new TYPE_STRING(false);
				} else if (typeExp1 instanceof TYPE_INT && typeExp2 instanceof TYPE_INT) {
					return new TYPE_INT(false);
				}
				throw new SemanticException(line);
			case 6:
				if (typeExp2.isAssignableTo(typeExp1) || typeExp1.isAssignableTo(typeExp2)) {
					return new TYPE_INT(false);
				}
				throw new SemanticException(line);
		}
		return new TYPE_INT(false);
	}
}
