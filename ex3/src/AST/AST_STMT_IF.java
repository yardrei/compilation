package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import exceptions.*;

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
    if(!(condType instanceof TYPE_INT)) {
      throw new SemanticException(line);
    }

    SYMBOL_TABLE.getInstance().beginScope(String.format("IF %d", line));
    body.SemantMe();
    SYMBOL_TABLE.getInstance().endScope();

    return null;
  }
}
