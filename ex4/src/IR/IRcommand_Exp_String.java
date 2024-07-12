package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Exp_String extends IRcommand{
  private String str;
  
  public IRcommand_Exp_String(TEMP destination, String str) {
    this.destination = destination;
    this.str = str.replace("\"", "");
  }

  @Override
  public void MIPSme() {
    MIPSGenerator.getInstance().allocString(destination, str);
  }
}
