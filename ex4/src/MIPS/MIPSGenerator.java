/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import IR.IRcommand;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import TYPES.*;

public class MIPSGenerator {
	private final String exceptionDivByZero = "exception_divide_by_zero";
	private final String exceptionAccessViolation = "exception_Access_Violation";
	private final String exceptionInvalidPointer = "exception_Invalid_Pointer";
	private int WORD_SIZE = 4;
	private boolean codeSegment = false;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	public List<String> globalCommandsLabels = new ArrayList<String>();

	private void addLabel(String label) {
		fileWriter.format("%s:\n", label);
	}

	public void comment(String comment) {
		fileWriter.format("\t# %s\n", comment);
	}

	private void addLine(String line, Object... args) {
		if (line == null || line.isEmpty()) {
			fileWriter.println();
			return;
		}

		if (!line.startsWith("\t")) {
			fileWriter.format("\t");
		}
		fileWriter.format(line, args);

		if (!line.endsWith("\n")) {
			fileWriter.format("\n");
		}
	}

	/**
	 * Allocates heap memory
	 * 
	 * @param $a0 size of memory to allocate
	 * @return $v0 pointer to allocated memory
	 */
	private void malloc() {
		addLine("li $v0, 9");
		addLine("syscall");
	}

	private void toCodeSegment() {
		if (!codeSegment) {
			fileWriter.format(".text\n");
			codeSegment = true;
		}
	}

	private void toDataSegment() {
		if (codeSegment) {
			fileWriter.format(".data\n");
			codeSegment = false;
		}
	}

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile() {
		addInvalidPointerExceptionTargets();
		addAccessViolationExceptionTargets();
		addDivByZeroExceptionTarget();
		callGlobalLabels();
		addMain();
		fileWriter.close();
	}

	private void addInvalidPointerExceptionTargets() {
		addLabel(exceptionInvalidPointer);
		addLine("li $v0, 4");
		addLine("la $a0, string_invalid_ptr_dref");
		addLine("syscall");
		addLine("li $v0, 10");
		addLine("syscall");
	}

	private void addAccessViolationExceptionTargets() {
		addLabel(exceptionAccessViolation);
		addLine("li $v0, 4");
		addLine("la $a0, string_access_violation");
		addLine("syscall");
		addLine("li $v0, 10");
		addLine("syscall");
	}

	private void addDivByZeroExceptionTarget() {
		addLabel(exceptionDivByZero);
		addLine("li $v0, 4");
		addLine("la $a0, string_illegal_div_by_0");
		addLine("syscall");
		addLine("li $v0, 10");
		addLine("syscall");
	}

	private void addMain() {
		addLabel("main");
		addLine("jal init_global");
		addLine("jal user_main");
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
	}

	public void print_int(TEMP src) {
		toCodeSegment();

		fileWriter.format("\tmove $a0, $t%d\n", src.register);
		fileWriter.format("\tli $v0, 1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0, 32\n");
		fileWriter.format("\tli $v0, 11\n");
		fileWriter.format("\tsyscall\n");
	}

	public void print_string(TEMP src) {
		toCodeSegment();

		fileWriter.format("\tmove $a0, $t%d\n", src.register);
		fileWriter.format("\tli $v0, 4\n");
		fileWriter.format("\tsyscall\n");
	}

	public void allocate(String var_name, String data) {
		toDataSegment();
		fileWriter.format("%s: .word %s\n", var_name, data);
	}

	public void loadLabel(TEMP dst, String var_name, int offset) {
		toCodeSegment();
		fileWriter.format("\tla $s0, %s\n", dst.register, var_name);
		fileWriter.format("\tlw $t%d, %d($s0)\n", dst.register, offset);
	}

	public void loadVar(TEMP src) {
		toCodeSegment();
		comment("Load");
		fileWriter.format("\tlw $t%d, 0($t%d)\n", src.register, src.register);
	}

	public void storeGlobalLabel(String label) {
		toCodeSegment();
		addLabel(label);
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $ra, 0($sp)\n");
	}

	public void storeGlobalEnd(String var_name, TEMP src) {
		toCodeSegment();
		fileWriter.format("\tsw $t%d, global_%s\n", src.register, var_name);
		fileWriter.format("\tlw $ra, 0($sp)\n");
		fileWriter.format("\taddu $sp, $sp, 4\n");
		addLine("jr $ra");
	}

	public void storeLocal(TEMP src, int location) {
		toCodeSegment();
		fileWriter.format("\tsw $t%d, %d($sp)\n", src.register, location * WORD_SIZE);
	}

	public void storeVar(TEMP dst, TEMP src) {
		toCodeSegment();
		comment("store");
		fileWriter.format("\tsw $t%d, 0($t%d)\n", src.register, dst.register);
	}

	public void storeClassVar(TEMP src, int location) {
		toCodeSegment();

		addLine("sw $t%d, %d($a0)", src.register, location * WORD_SIZE);
	}

	public void li(TEMP t, int value) {
		toCodeSegment();
		fileWriter.format("\tli $t%d, %d\n", t.register, value);
	}

	private void overflowUnderflowCheck(TEMP dst) {
		addLine("slti $s0, $t%d, 32767", dst.register); // $s0 = $t%d < 32,767
		String noOverflowLabel = IRcommand.getFreshLabel("NoOverflow");
		String noUnderflowLabel = IRcommand.getFreshLabel("NoUnderflow");
		addLine("bnez $s0, %s", noOverflowLabel);
		addLine("li $t%d, 32767", dst.register);
		addLabel(noOverflowLabel);
		addLine("slti $s0, $t%d, -32768", dst.register); // $s0 = $t%d < -32,768
		addLine("beqz $s0, %s", noUnderflowLabel);
		addLine("li $t%d, -32768", dst.register);
		addLabel(noUnderflowLabel);
	}

	public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		toCodeSegment();

		fileWriter.format("\tadd $t%d,$t%d,$t%d\n", dst.register, oprnd1.register, oprnd2.register);
		overflowUnderflowCheck(dst);
	}

	/*
	 * Loads the address of the vaiable to register s0
	 */
	public void varSimpleLocalLocation(TEMP dst, int location) {
		addLine("subu $t%d, $fp, %d", dst.register, location * WORD_SIZE);
	}

	public void varSimpleGlobalLocation(TEMP dst, String label) {
		addLine("la $t%d, global_%s", dst.register, label);
	}

	public void varSimpleFieldLocation(TEMP dst, int location) {
		addLine("lw $s0, 8($fp)");
		addLine("addi $t%d, $s0, %d", dst.register, location * WORD_SIZE);
	}

	public void varSubscriptLocation(TEMP dst, TEMP src, TEMP locTemp, int size) {
		comment("var subscript");
		testInvalidPointer(src);
		testOutOfBoundAccess(locTemp, src);
		addLine("lw $s0, 0($t%d)", src.register);
		addLine("addu $t%d, $t%d, 1", locTemp.register, locTemp.register);
		addLine("sll $t%d, $t%d, 2", locTemp.register, locTemp.register);
		addLine("add $t%d, $s0, $t%d", dst.register, locTemp.register);
	}
	
	public void varFieldLocation(TEMP dst, TEMP src, int location) {
		comment("var field");
		testInvalidPointer(src);
		addLine("lw $s0, 0($t%d)", src.register);
		addLine("addi $t%d, $s0, %d", dst.register, location * WORD_SIZE);
		System.out.println("location: " + location);
	}

	private void testInvalidPointer(TEMP src) {
		addLine("li $s1, %d", TYPE_NIL.VALUE);
		addLine("lw $s2, 0($t%d)", src.register);
		addLine("beq $s2, $s1, %s", exceptionInvalidPointer);
	}

	private void testOutOfBoundAccess(TEMP locTemp, TEMP src) {
		addLine("lw $s1, 0($t%d)", src.register);
		addLine("lw $s1, 0($s1)");
		addLine("slt $s1, $t%d, $s1", locTemp.register); // $s0 = $t%d < size
		addLine("beqz $s1, %s", exceptionAccessViolation);
	}

	/**
	 * Returns the length of a string
	 * 
	 * @param str The given string
	 * @return $v0 length of string (Excluding null terminator)
	 */
	private void str_len(TEMP str) {
		toCodeSegment();
		addLine("move $v0, $zero");
		// $s0 will hold the address of the current character
		addLine("move $s0, $t%d", str.register);

		String startLoopLabel = IRcommand.getFreshLabel("str_len_start");
		String endLoopLabel = IRcommand.getFreshLabel("str_len_end");

		addLabel(startLoopLabel);
		addLine("lb $s1, 0($s0)"); // Load character
		addLine("beq $s1, $zero, %s", endLoopLabel); // If character is null terminator, end loop
		addLine("addi $v0, $v0, 1"); // Increment length
		addLine("addi $s0, $s0, 1"); // Increment address
		addLine("j %s", startLoopLabel); // Start loop again

		addLabel(endLoopLabel);
	}

	/**
	 * Copies a string from src to $a0, not including the null terminator
	 * 
	 * @param src The given string
	 * @return $a0 Pointer to the end of the copied string
	 */
	private void str_copy(TEMP src) {
		toCodeSegment();
		String startLoopLabel = IRcommand.getFreshLabel("str_copy_start");
		String endLoopLabel = IRcommand.getFreshLabel("str_copy_end");

		addLine("move $s0, $t%d", src.register); // $s0 will hold the current address
		addLabel(startLoopLabel);
		addLine("lb $s1, 0($s0)"); // Load character
		addLine("beq $s1, $zero, %s", endLoopLabel); // If character is null terminator, end loop
		addLine("sb $s1, 0($a0)"); // Copy character
		addLine("addi $a0, $a0, 1"); // Increment destination address
		addLine("addi $s0, $s0, 1"); // Increment source address
		addLine("j %s", startLoopLabel); // Start loop again

		addLabel(endLoopLabel);
	}

	public void concat(TEMP dst, TEMP left, TEMP right) {
		toCodeSegment();

		// Save string length in $a0 (Including null terminator)
		str_len(left);
		addLine("move $a0, $v0");
		str_len(right);
		addLine("add $a0, $a0, $v0");
		addLine("addi $a0, $a0, 1"); // Add null terminator

		malloc();

		addLine("move $a0, $v0");
		str_copy(left);
		str_copy(right);
		addLine("sb $zero, 0($a0)"); // Add null terminator

		addLine("move $t%d, $v0", dst.register);
	}

	public void str_eq(TEMP dst, TEMP left, TEMP right) {
		toCodeSegment();

		addLine("move $s0, $t%d", left.register); // get left's address
		addLine("move $s1, $t%d", right.register); // get right's address
		String startLoopLabel = IRcommand.getFreshLabel("str_eq_start");
		String endLoopLabel = IRcommand.getFreshLabel("str_eq_end");
		String equalLabel = IRcommand.getFreshLabel("str_eq_equal");
		String notEqualLabel = IRcommand.getFreshLabel("str_eq_not_equal");

		addLabel(startLoopLabel);
		addLine("lb $s2, 0($s0)"); // Load left character
		addLine("lb $s3, 0($s1)"); // Load right character
		addLine("bne $s2, $s3, %s", notEqualLabel); // If characters are not equal, return not equal
		addLine("beq $s2, $zero, %s", equalLabel); // If character is null terminator, we've reached the end of the
													// string. Return equal
		addLine("addi $s0, $s0, 1"); // Increment left address
		addLine("addi $s1, $s1, 1"); // Increment right address
		addLine("j %s", startLoopLabel); // Start loop again

		addLabel(notEqualLabel);
		addLine("li $t%d, 0", dst.register); // Set dst to false
		addLine("j %s", endLoopLabel);

		addLabel(equalLabel);
		addLine("li $t%d, 1", dst.register); // Set dst to true

		addLabel(endLoopLabel);
	}

	public void sub(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		toCodeSegment();

		fileWriter.format("\tsub $t%d,$t%d,$t%d\n", dst.register, oprnd1.register, oprnd2.register);
		overflowUnderflowCheck(dst);
	}

	public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		toCodeSegment();

		fileWriter.format("\tmul $t%d,$t%d,$t%d\n", dst.register, oprnd1.register, oprnd2.register);
		overflowUnderflowCheck(dst);
	}

	public void div(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		toCodeSegment();
		isEqualToZero(oprnd2);
		fileWriter.format("\tdiv $t%d, $t%d, $t%d\n", dst.register, oprnd1.register, oprnd2.register);
		overflowUnderflowCheck(dst);
	}

	private void isEqualToZero(TEMP temp) {
		addLine("beqz $t%d, %s", temp.register, exceptionDivByZero);
	}

	public void vtLabel(String label) {
		toDataSegment();
		fileWriter.format("vt_%s:\n", label);
	}

	public void label(String inlabel) {
		toCodeSegment();

		// if (inlabel.equals("main") || inlabel.equals("main_epilogue")) {
		// 	fileWriter.format("user_%s:\n", inlabel);
		// } else {
		// 	// TODO: Add "user_" before user labels so there aren't any conflicts between
		// 	// exception and user labels
		fileWriter.format("%s:\n", inlabel);
		// }
	}

	public void jump(String inlabel) {
		toCodeSegment();

		fileWriter.format("\tj %s\n", inlabel);
	}

	public void jumpAndLink(String inlabel) {
		toCodeSegment();
		comment("jump and link " + inlabel); /////////////////
		fileWriter.format("\tjal %s\n", inlabel);
	}

	public void blt(TEMP oprnd1, TEMP oprnd2, String label) {
		toCodeSegment();

		fileWriter.format("\tblt $t%d,$t%d,%s\n", oprnd1.register, oprnd2.register, label);
	}

	public void bge(TEMP oprnd1, TEMP oprnd2, String label) {
		toCodeSegment();

		fileWriter.format("\tbge $t%d,$t%d,%s\n", oprnd1.register, oprnd2.register, label);
	}

	public void bne(TEMP oprnd1, TEMP oprnd2, String label) {
		toCodeSegment();

		fileWriter.format("\tbne $t%d,$t%d,%s\n", oprnd1.register, oprnd2.register, label);
	}

	public void beq(TEMP oprnd1, TEMP oprnd2, String label) {
		toCodeSegment();

		fileWriter.format("\tbeq $t%d,$t%d,%s\n", oprnd1.register, oprnd2.register, label);
	}

	public void beqz(TEMP oprnd1, String label) {
		toCodeSegment();

		fileWriter.format("\tbeq $t%d,$zero,%s\n", oprnd1.register, label);
	}

	// Function Prologue - ra, fp, t0-9
	public void functionPrologue() {
		toCodeSegment();

		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $ra, 0($sp)\n");
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $fp, 0($sp)\n");
		fileWriter.format("\tmove $fp, $sp\n");
		fileWriter.format("\tsubu $sp, $sp, 40\n");
		for (int i = 0; i < 10; i++) {
			fileWriter.format("\tsw $t%d, %d($sp)\n", 9 - i, i * WORD_SIZE);
		}
	}

	// Function Epilogue - ra, fp, t0-9
	public void functionEpilogue(String inlabel) {
		toCodeSegment();

		label(inlabel);
		fileWriter.format("\tsubu $sp, $fp, 40\n");
		for (int i = 0; i < 10; i++) {
			fileWriter.format("\tlw $t%d, %d($sp)\n", 9 - i, i * WORD_SIZE);
		}
		fileWriter.format("\taddu $sp, $sp, 40\n");
		fileWriter.format("\tlw $fp, 0($sp)\n");
		fileWriter.format("\taddu $sp, $sp, 4\n");
		fileWriter.format("\tlw $ra, 0($sp)\n");
		fileWriter.format("\taddu $sp, $sp, 4\n");
		fileWriter.format("\tjr $ra\n");
	}

	// Function Return
	public void functionReturn(TEMP src, String functionName) {
		toCodeSegment();
		if (src != null) {
			fileWriter.format("\tmove $v0, $t%d\n", src.register);
		}
		jump(functionName + "_epilogue");
	}

	public void functionCall(TEMP src, String name, boolean isVoid) {
		toCodeSegment();
		comment("function call " + name); /////////////////
		addLine("jal %s", name);
		if (!isVoid) {
			addLine("move $t%d, $v0", src.register);
		}
	}

	public void methodCall(TEMP src, TEMP locTemp, int location, boolean isVoid) {
		toCodeSegment();
		testInvalidPointer(locTemp);
		addLine("lw $s0, 0($t%d)", locTemp.register);
		addLine("subu $sp, $sp, 4");
		addLine("sw $s0, 0($sp)");
		addLine("lw $s0, 0($s0)");
		addLine("lw $s0, %d($s0)", location * WORD_SIZE);
		addLine("jalr $s0");
		if (!isVoid) {
			addLine("move $t%d, $v0", src.register);
		}
	}

	public void constString(String label, String str) {
		toDataSegment();
		fileWriter.format("\t%s: .asciiz \"%s\"\n", label, str);
	}

	public void moveStackPointer(int move) {
		toCodeSegment();
		if (move != 0) {
			addLine("subu $sp, $sp, %d", move * WORD_SIZE);
		}
	}

	public void moveStackPointerFromFrame(int move) {
		toCodeSegment();
		addLine("subu $sp, $fp, %d", move * WORD_SIZE);
	}

	public void allocString(TEMP dst, String str) {
		toDataSegment();
		String label = IRcommand.getFreshLabel(str);
		fileWriter.format("%s: .asciiz \"%s\"\n", label, str);

		if (dst != null) {
			toCodeSegment();
			addLine("la $t%d, %s", dst.register, label);
		}
	}

	// Creates the Vtable for a class(name)
	public void createClassVtable(String name, TYPE_LIST funcList) {
		toDataSegment();

		TYPE_FUNCTION funcType;

		for (TYPE_LIST curr = funcList; curr != null; curr = curr.next) {
			funcType = (TYPE_FUNCTION) curr.head;
			addLine(".word %s", funcType.name + "_" + funcType.className);
		}
	}

	public void addVtableToAlloc(String name) {
		toCodeSegment();

		addLine("la $s0, vt_%s", name);
		addLine("sw $s0 0($a0)");
	}

	public void allocateArray(TEMP dst, int size) {
		toCodeSegment();

		addLine("li $a0, %d", (size + 1) * WORD_SIZE);
		malloc();
		addLine("move $t%d, $v0", dst.register);
		addLine("li $s0, %d", size);
		addLine("sw $s0 0($t%d)", dst.register);

	}

	public void allocateClass(TEMP dst, String name, int size) {
		toCodeSegment();

		addLine("li $a0, %d", size * WORD_SIZE);
		malloc();
		addLine("move $t%d, $v0", dst.register);
		addLine("move $a0, $v0", dst.register);
		comment("allocate class " + name); /////////////////
		addLine("jal init_%s", name);
	}

	public void callGlobalLabels() {
		addLabel("init_global");
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $ra, 0($sp)\n");
		globalCommandsLabels.forEach((String label) -> addLine("jal %s", label));
		fileWriter.format("\tlw $ra, 0($sp)\n");
		fileWriter.format("\taddu $sp, $sp, 4\n");
		addLine("jr $ra");
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance() {
		if (instance == null) {
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try {
				/*********************************************************************************/
				/*
				 * [1] Open the MIPS text file and write data section with error message strings
				 */
				/*********************************************************************************/
				String dirname = "./output/";
				String filename = String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname + filename);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
