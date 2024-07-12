package IR;

import java.util.Arrays;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

public class IRcommand_Binop_Add_Strings extends IRcommand {

  	public TEMP t1;
	public TEMP t2;

	public IRcommand_Binop_Add_Strings(TEMP destination,TEMP t1,TEMP t2) {
		this.destination = destination;
		this.t1 = t1;
		this.t2 = t2;
		sources = Arrays.asList(t1, t2);
	}

	public void MIPSme() {
		MIPSGenerator.getInstance().concat(destination, t1, t2);
	}
}
