import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
import AST.*;
import exceptions.CompilerException;
import exceptions.LexicalException;

public class Main {
    static public void main(String argv[]) {
        Lexer l;
        Parser p;
        Symbol s;
        AST_PROG AST;
        FileReader file_reader;
        PrintWriter file_writer = null;
        String inputFilename = argv[0];
        String outputFilename = argv[1];

        try {
            /********************************/
            /* [1] Initialize a file reader */
            /********************************/
            file_reader = new FileReader(inputFilename);

            /********************************/
            /* [2] Initialize a file writer */
            /********************************/
            file_writer = new PrintWriter(outputFilename);

            /******************************/
            /* [3] Initialize a new lexer */
            /******************************/
            l = new Lexer(file_reader);

            /*******************************/
            /* [4] Initialize a new parser */
            /*******************************/
            p = new Parser(l);

            /***********************************/
            /* [5] 3 ... 2 ... 1 ... Parse !!! */
            /***********************************/
            AST = (AST_PROG) p.parse().value;

            /*************************/
            /* [6] Print the AST ... */
            /*************************/
            AST.PrintMe();

            /**************************/
            /* [7] Semant the AST ... */
            /**************************/
            AST.SemantMe();

            file_writer.write("OK");

            /*************************************/
            /* [9] Finalize AST GRAPHIZ DOT file */
            /*************************************/
            AST_GRAPHVIZ.getInstance().finalizeFile();
        } catch (LexicalException e) {
            e.printStackTrace();
            file_writer.write("ERROR");
            System.err.println(e.message + ", line:" + e.line);
        }
        
         catch (CompilerException e) {
            e.printStackTrace();
            file_writer.write(String.format("ERROR(%d)", e.line + 1));
            System.err.println(e.message + ", line:" + e.line);
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(file_writer != null) {
                file_writer.close();
            }
        }
    }
}


