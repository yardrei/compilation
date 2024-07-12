package AST;

import TYPES.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;

	//Ast annotation
	int location;
	int size;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int line)
	{
		super(line);

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe() {
		/* Check var */
		TYPE varType = var.SemantMe();
		TYPE_ARRAY arrayType;
		if(varType == null || !varType.isArray() || !varType.isVar()) {
			throw new SemanticException(line);
		}

		/* Check subscript */
		TYPE subscriptType = subscript.SemantMe();

		if(!(subscriptType.isInt())) {
			throw new SemanticException(line);
		}

		// Assert that subscript isn't a negative constant
		if((subscript instanceof AST_EXP_INT) && ((AST_EXP_INT) subscript).value < 0) {
			throw new SemanticException(line);
		}

		// location = ((AST_EXP_INT) subscript).value;
		
		arrayType =(TYPE_ARRAY) ((TYPE_VAR)varType).type;
		
		size = arrayType.size;

		System.out.println("size: " + arrayType.size);//////////////

		return arrayType.type;
	}

	@Override
	public TEMP IRme() { // TODO: made a mistake when arr[][], dont know how to deal with it.
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP src = var.IRme();
		TEMP locTemp = subscript.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Var_Subscript(dst, src, locTemp, size));
		return dst;
	}
}