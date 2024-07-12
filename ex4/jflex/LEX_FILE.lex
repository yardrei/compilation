/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
// This replaces %cup. See https://jflex.de/manual.html#CUPWork (CUP compatibility)
%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol
%eofval{
  return new java_cup.runtime.Symbol(TokenNames.EOF);
%eofval}
%eofclose

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)                       {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value)         {return new Symbol(type, yyline, yycolumn, value);}
	private Symbol symbol(TokenNames token)               { return symbol(token);}
	private Symbol symbol(TokenNames token, Object value) { return symbol(token, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 
	public int getCharPos() { return yycolumn;   } 


	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator			= \r | \n | \r\n
OneLineWhiteSpace		= " " | \t
WhiteSpace				= {LineTerminator} | {OneLineWhiteSpace}
InvalidInteger			= 0[0-9]+
INTEGER					= 0|[1-9][0-9]*
Letter					= [a-zA-Z]
LetterAndNumber			= [a-zA-Z0-9]
Parentheses 			= "(" | ")" | "[" | "]" | "{" | "}"
Punctuation				= "!" | "?" | "." | ";"
Minus					= "-"
Plus					= "+"
Asterisk				= "*"
ForwardSlash			= "/"
Character				= {Parentheses} | {Punctuation} | {Plus} | {Minus} | {Asterisk} | {ForwardSlash} | {LetterAndNumber} | {OneLineWhiteSpace}
CommentTwoCharacter     = {Parentheses} | {Punctuation} | {Plus} | {Minus} | {WhiteSpace} | {LetterAndNumber}
ID						= {Letter}{LetterAndNumber}*
STRING          		= "\"" {LetterAndNumber}* "\""
RegularTypeOneComment	= "//" {Character}* {LineTerminator}
InvalidTypeOneComment	= "//" {Character}* .
TypeOneCommentAtEof		= "//" {Character}*
TypeOneComment 			= {RegularTypeOneComment} | {TypeOneCommentAtEof}
TypeTwoCommentOpener	= "/*"
TypeTwoCommentCloser	= "*/"
TypeTwoComment  		= {TypeTwoCommentOpener} (({Asterisk}*{CommentTwoCharacter} | {ForwardSlash}))* {Asterisk}* {TypeTwoCommentCloser}

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"					{ return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
","					{ return symbol(TokenNames.COMMA);}
"."					{ return symbol(TokenNames.DOT);}
";"                 { return symbol(TokenNames.SEMICOLON);}
":="                { return symbol(TokenNames.ASSIGN);}
"="                 { return symbol(TokenNames.EQ);}
"<"                 { return symbol(TokenNames.LT);}
">"                 { return symbol(TokenNames.GT);}


/* keywords */
"if"				{ return symbol(TokenNames.IF);}
"extends"			{ return symbol(TokenNames.EXTENDS);}
"class"				{ return symbol(TokenNames.CLASS);}
"new"				{ return symbol(TokenNames.NEW);}
"nil"				{ return symbol(TokenNames.NIL);}
"return"            { return symbol(TokenNames.RETURN);}
"array"				{ return symbol(TokenNames.ARRAY);}
"while"				{ return symbol(TokenNames.WHILE);}
"int"				{ return symbol(TokenNames.TYPE_INT);}
"string"			{ return symbol(TokenNames.TYPE_STRING);}
"void"				{ return symbol(TokenNames.TYPE_VOID);}



{STRING}            { return symbol(TokenNames.STRING, new String(yytext()));}
{InvalidInteger}	{ return symbol(TokenNames.error);}
{INTEGER}			{
						Integer x = Integer.valueOf(yytext());
						if (x <= 32767) {
							return symbol(TokenNames.INT, x);
						}
						else {
							return symbol(TokenNames.error);
						}
					}
{ID}					{ return symbol(TokenNames.ID, new String( yytext()));}   
{WhiteSpace}			{ /* just skip what was found, do nothing */ }
{TypeOneComment}		{ /* just skip what was found, do nothing */}
{InvalidTypeOneComment}	{ return symbol(TokenNames.error);}
{TypeTwoComment}  	 	{/* just skip what was found, do nothing */}
<<EOF>>					{ return symbol(TokenNames.EOF);}
[^]						{ return symbol(TokenNames.error);}
}
