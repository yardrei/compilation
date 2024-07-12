package AST;

import TYPES.*;

public class AST_BINOP_OP extends AST_Node {

  public int OP;

  public AST_BINOP_OP(int OP) {
    super(0);
    /********************************/
    /* COPY INPUT DATA MEMBERS ... */
    /* 0 == + */
    /* 1 == - */
    /* 2 == * */
    /* 3 == / */
    /* 4 == < */
    /* 5 == > */
    /* 6 == == */
    /********************************/
    this.OP = OP;
  }

  /*************************************************/
  /* The printing message for a binop exp AST node */
  /*************************************************/
  public void PrintMe() {
    String sOP = "";

    /*********************************/
    /* CONVERT OP to a printable sOP */
    /*********************************/
    if (OP == 0) {
      sOP = "+";
    }
    if (OP == 1) {
      sOP = "-";
    }
    if (OP == 2) {
      sOP = "*";
    }
    if (OP == 3) {
      sOP = "/";
    }
    if (OP == 4) {
      sOP = "<";
    }
    if (OP == 5) {
      sOP = ">";
    }
    if (OP == 6) {
      sOP = "==";
    }

    /*************************************/
    /* AST NODE TYPE = AST BINOP EXP */
    /*************************************/
    System.out.print("AST NODE BINOP OP " + sOP + "\n");

    AST_GRAPHVIZ
        .getInstance()
        .logNode(SerialNumber, String.format("BINOP\n(%s)", sOP));
  }

  public TYPE SemantMe() {
    return null;
  }
}
