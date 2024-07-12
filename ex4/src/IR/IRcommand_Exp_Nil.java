package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TYPES.TYPE_NIL;

public class IRcommand_Exp_Nil extends IRcommand{
  
  public IRcommand_Exp_Nil(TEMP destination) {
    this.destination = destination;
  }

  public void MIPSme() {
    MIPSGenerator.getInstance().li(destination, TYPE_NIL.VALUE);
  }
}
