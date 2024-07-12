package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;
import TEMP.*;
import IR.*;

public class AST_STMT_IF extends AST_STMT {

  public AST_EXP cond;
  public AST_LIST<AST_STMT> body;

  /*******************/
  /* CONSTRUCTOR(S) */
  /*******************/
  public AST_STMT_IF(AST_EXP cond, AST_LIST<AST_STMT> body, int line) {
    super(line);

    this.cond = cond;
    this.body = body;
  }

  public void PrintMe() {
    AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT\nIF\n");

    if (cond != null) {
      cond.PrintMe();
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
    }

    if (body != null) {
      body.PrintMe();
      AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }
  }

  @Override
  public TYPE SemantMe() {
    TYPE condType = cond.SemantMe();
    if(!(condType.isInt())) {
      throw new SemanticException(line);
    }

    SYMBOL_TABLE.getInstance().beginScope(String.format("IF %d", line));
    body.SemantMe();
    SYMBOL_TABLE.getInstance().endScope();

    return null;
  }

  public TEMP IRme() {

		String label_end = IRcommand.getFreshLabel("end");

    TEMP cond_temp = cond.IRme();

    IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));

    body.IRme();

    IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_end));

    return null;
  }
}
