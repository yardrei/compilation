package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;
import IR.*;
import TEMP.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;

	//AST Annotations
    TYPE_VAR vartype;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int line)
	{
		super(line);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR(%s)\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe() {
		TYPE type = SYMBOL_TABLE.getInstance().find(name);
		if(type == null || type.isFunction()) {
			throw new SemanticException(line);
		}
		if (type.isVar()) {
			vartype = (TYPE_VAR) type;
		}
		return type;
	}

	@Override
	public TEMP IRme() {
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Var_Simple(dst, vartype.scope, vartype.location, name));
		return dst;
	}
}
