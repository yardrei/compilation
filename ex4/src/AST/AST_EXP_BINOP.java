package AST;

import TYPES.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_EXP_BINOP extends AST_EXP {

	public int OP;
	public AST_EXP left;
	public AST_EXP right;

	//Ast annotation
	private TYPE type;
	private boolean isString = false;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, int OP, int line) {
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
		// if (OP != null)
		// 	OP.PrintMe();
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
		// if (OP != null)
		// 	AST_GRAPHVIZ
		// 			.getInstance()
		// 			.logEdge(SerialNumber, OP.SerialNumber);
		if (right != null)
			AST_GRAPHVIZ
					.getInstance()
					.logEdge(SerialNumber, right.SerialNumber);
	}

	@Override
	public TYPE SemantMe() {
		TYPE typeExp1 = left.SemantMe();
		TYPE typeExp2 = right.SemantMe();

		if (typeExp1 == null || typeExp2 == null) {
			throw new SemanticException(line);
		}

		switch (OP) {
			case 1: // Minus
			case 2: // Multiply
			case 4: // LT
			case 5: // GT
				if (typeExp1.isInt() && typeExp2.isInt()) {
					type = new TYPE_INT(false);
					return new TYPE_VAR("int", type);
				} else {
					System.out.println(String.format("%b, %b", typeExp1.isInt(), typeExp2.isInt()));/////
					throw new SemanticException(line, String.format("Type mismatch: %s %s %s", typeExp1.name, OP, typeExp2.name));
				}
			case 3: // Divide
				if (right instanceof AST_EXP_INT && ((AST_EXP_INT) right).value == 0) {
					throw new SemanticException(line);
				}
				if (typeExp1.isInt() && typeExp2.isInt()) {
					type = new TYPE_INT(false);
					return new TYPE_VAR("int", type);
				} else {
					throw new SemanticException(line);
				}
			case 0: // Plus
				if(typeExp1.isString() && typeExp2.isString()) {
					isString = true;
					type = new TYPE_STRING(false);
					return new TYPE_VAR("string", type);
				} else if (typeExp1.isInt() && typeExp2.isInt()) {
					type = new TYPE_INT(false);
					return new TYPE_VAR("int", type);
				}
					throw new SemanticException(line);
			case 6: // EQ
				if(typeExp1.isString() && typeExp2.isString()) {
					isString = true;
				}
				if (typeExp2.isAssignableTo(typeExp1) || typeExp1.isAssignableTo(typeExp2)) {
					type = new TYPE_INT(false);
					return new TYPE_VAR("int", type);
				}
				throw new SemanticException(line);
		}
		type = new TYPE_INT(false);
		return new TYPE_VAR("int", type);
	}

	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();

		System.out.println("Operation: " + OP);
		
		switch (OP) {
			case 0: // Plus
				if(isString) {
					IR.getInstance()
					  .Add_IRcommand(new IRcommand_Binop_Add_Strings(dst, t1, t2));
				} else {
					IR.getInstance()
					  .Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, t1, t2));
				}
				break;
			case 1: // Minus
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
				break;
			case 2: // Multiply
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
				break;
			case 3: // Divide
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
				break;
			case 4: // LT
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
				break;
			case 5: // GT
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t2,t1));
				break;
			case 6: // EQ
				if(isString) {
					IR.getInstance()
						.Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst, t1, t2));
				} else {
					//Its for arrays and class too, comparing addresses
					IR.getInstance()
						.Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
				}
				break;
		}

		return dst;
	}
}
