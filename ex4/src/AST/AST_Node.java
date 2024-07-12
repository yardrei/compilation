package AST;

import TYPES.*;
import TEMP.*;

public abstract class AST_Node {

  /*******************************************/
  /* The serial number is for debug purposes */
  /* In particular, it can help in creating  */
  /* a graphviz dot format of the AST ...    */
  /*******************************************/
  public int SerialNumber;
  public int line;

  public AST_Node(int line) {
    SerialNumber = AST_Node_Serial_Number.getFresh();
    this.line = line;
  }

  /***********************************************/
  /* The default message for an unknown AST node */
  /***********************************************/
  public abstract void PrintMe();

  public abstract TYPE SemantMe();

  public TEMP IRme() {return null;};

  public String getId() {return null;}
}
