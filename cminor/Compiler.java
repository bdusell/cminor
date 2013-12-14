package cminor;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.PrintStream;
import cminor.lexer.Lexer;
import cminor.parser.Parser;
import cminor.parser.ParsingError;
import cminor.ast.AstNode;
import cminor.semantic.ErrorLogger;

public class Compiler {

	private static final String PIPE_MARKER = "-";
	private static final String OUTPUT_FLAG = "-o";
	private static final String HELP_FLAG   = "-h";
	private static final String TREE_FLAG   = "-t";
	private static final String SYMBOL_FLAG = "-s";
	private static final String CHECK_FLAG  = "-l";

	private static final String ASSEMBLY_SUFFIX = "s";
	private static final String DOT_SUFFIX      = "dot";

	private static final HashSet<String> flags = new HashSet<String>();
	static {
		flags.add(OUTPUT_FLAG);
		flags.add(HELP_FLAG);
		flags.add(TREE_FLAG);
		flags.add(SYMBOL_FLAG);
		flags.add(CHECK_FLAG);
	}

	private boolean helpMode;
	private boolean treeMode;
	private boolean symbolMode;
	private boolean checkMode;
	private String inputName;
	private String outputName;

	public Compiler(String[] args) throws UsageError {
		parseArgs(args);
	}

	public static class UsageError extends Exception {
		public UsageError(String message) {
			super(message);
		}
	}

	public static class CompilationError extends Exception {
		public CompilationError(String message) {
			super(message);
		}
	}

	public void parseArgs(String[] args) throws UsageError {

		String inputName = null;
		List<String> outputArgs = null;
		boolean helpFlag = false;
		boolean treeFlag = false;
		boolean symbolFlag = false;
		boolean checkFlag = false;

		for(String arg : args) {
			if(outputArgs != null && outputArgs.size() == 0) {
				if(isFlag(arg)) throw new UsageError(OUTPUT_FLAG + " flag missing argument");
				else outputArgs.add(arg);
			}
			else if(arg.equals(OUTPUT_FLAG)) {
				if(outputArgs == null) outputArgs = new ArrayList<String>();
				else throw new UsageError(OUTPUT_FLAG + " flag given more than once");
			}
			else if(arg.equals(HELP_FLAG)) {
				if(helpFlag) throw new UsageError(HELP_FLAG + " flag given more than once");
				else helpFlag = true;
			}
			else if(arg.equals(TREE_FLAG)) {
				if(treeFlag) throw new UsageError(TREE_FLAG + " flag given more than once");
				else treeFlag = true;
			}
			else if(arg.equals(SYMBOL_FLAG)) {
				if(symbolFlag) throw new UsageError(SYMBOL_FLAG + " flag given more than once");
				else symbolFlag = true;
			}
			else if(arg.equals(CHECK_FLAG)) {
				if(checkFlag) throw new UsageError(CHECK_FLAG + " flag given more than once");
				else checkFlag = true;
			}
			else if(isInputFile(arg)) {
				if(inputName == null) inputName = arg;
				else throw new UsageError("more than one input file given");
			}
			else throw new UsageError("cannot recognize argument \"" + arg + "\"");
		}

		if(outputArgs != null) {
			if(outputArgs.size() > 0) this.outputName = outputArgs.get(0);
			else throw new UsageError(OUTPUT_FLAG + " flag missing argument");
		}

		if(treeFlag && symbolFlag) {
			throw new UsageError(TREE_FLAG + " and " + SYMBOL_FLAG + " flags conflict");
		}

		if(checkFlag && (treeFlag || symbolFlag || outputArgs != null)) {
			throw new UsageError("too many arguments for " + CHECK_FLAG + " mode");
		}

		this.helpMode = helpFlag;
		this.treeMode = treeFlag;
		this.symbolMode = symbolFlag;
		this.checkMode = checkFlag;
		this.inputName = inputName;

	}

	public static void printUsage() {
		printUsage(System.out);
	}

	public static void printUsage(PrintStream output) {
		output.println("Usage: cminor [" + HELP_FLAG + "] [" + TREE_FLAG + "] [" + SYMBOL_FLAG + "] " +
			"[" + CHECK_FLAG + "] [" + OUTPUT_FLAG + " <output>] [<input.{cm,cminor}>]\n" +
		               "    <input>      Specifies the input file. If omitted or given as \"-\", the\n" +
		               "                 input is stdin.\n" +
		               "    " + OUTPUT_FLAG + " <output>  Specifies the output file. Upon error no output file is\n" +
		               "                 created. If omitted, the output file has the name of the input\n" +
		               "                 file with its suffix replaced. If omitted and the input is\n" +
		               "                 stdin, or if the output file is given as \"-\", the output is\n" +
		               "                 stdout.\n" +
		               "    " + TREE_FLAG + "           Abstract syntax tree mode. Instead of compiling the program,\n" +
		               "                 generates dot code representing the abstract syntax tree of\n" +
		               "                 the input program.\n" +
                               "    " + SYMBOL_FLAG + "           Symbol resolution mode. Generates dot code representing the\n" +
		               "                 abstract syntax tree of the input program, including nodes for\n" +
		               "                 resolved symbols.\n" +
		               "    " + CHECK_FLAG + "           Only validates the input program for semantic correctness;\n" +
		               "                 does not generate any code.\n" +
		               "    " + HELP_FLAG + "           Prints this help message.");
	}

	public void execute() throws Exception, CompilationError {
		if(helpMode) printUsage();
		else {

			boolean useStdin = false, useStdout = false;
			if(this.inputName == null || this.inputName.equals(PIPE_MARKER)) {
				useStdin = true;
			}
			if(this.outputName == null && useStdin || this.outputName != null && this.outputName.equals(PIPE_MARKER)) {
				useStdout = true;
			}
			String inputName = useStdin ? "stdin" : this.inputName;
			FileReader input = useStdin ? new FileReader(java.io.FileDescriptor.in) : new FileReader(this.inputName);

			// Parse the input program into an AST
			ErrorLogger logger = new ErrorLogger();
			AstNode rootNode = getRootNode(input, inputName, logger);

			if(this.checkMode) {
				if(logger.hasErrors()) throw new ParsingError("parsing phase failed");
				rootNode.resolveSymbols(logger);
				if(logger.hasErrors()) throw new CompilationError("symbol resolution phase failed");
				else {
					rootNode.typeCheck(logger);
					if(logger.hasErrors()) throw new CompilationError("type checking phase failed");
				}
			}
			else {

				if(logger.hasErrors()) {
					throw new ParsingError("compilation aborted");
				}

				String outputName;
				if(!useStdout && this.outputName == null) {
					outputName = replaceSuffix(this.inputName, this.treeMode ? DOT_SUFFIX : ASSEMBLY_SUFFIX);
				}
				else {
					outputName = this.outputName;
				}

				if(this.treeMode) {
					PrintStream output = useStdout ? System.out : new PrintStream(new FileOutputStream(outputName));
					rootNode.printDotCode(output);
				}
				else if(this.symbolMode) {
					rootNode.resolveSymbols(logger);
					PrintStream output = useStdout ? System.out : new PrintStream(new FileOutputStream(outputName));
					rootNode.printDotCode(output);
					output.close();
				}
				else {
					/* This is where the normal compilation behavior occurs. */
					rootNode.resolveSymbols(logger);
					if(logger.hasErrors()) throw new CompilationError("compilation aborted"); 
					rootNode.typeCheck(logger);
					if(logger.hasErrors()) throw new CompilationError("compilation aborted");
					PrintStream output = useStdout ? System.out : new PrintStream(new FileOutputStream(outputName));
					rootNode.generateCode(output);
					output.close();
				}
			}
		}
	}

	public static void main(String[] args) {

		try {
			Compiler compiler = new Compiler(args);
			compiler.execute();
		}
		catch(UsageError e) {
			System.err.println(e.getMessage());
			printUsage();
			System.exit(1);
		}
		catch(CompilationError|ParsingError e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		catch(java.io.FileNotFoundException e) {
			System.err.println("error when using file: " + e.getMessage());
			System.exit(1);
		}
		catch(Exception e) {
			System.err.println("internal compiler error (crap!): " + e.getMessage());
			e.printStackTrace(System.err);
			System.exit(1);
		}

	}

	private static AstNode getRootNode(FileReader input, String inputName, ErrorLogger logger) throws ParsingError, java.io.IOException {

		Lexer lexer = new Lexer(input);
		lexer.setFilename(inputName);
		lexer.setErrorLogger(logger);
		Parser parser = new Parser(lexer);
		try {
			AstNode result = ((AstNode) parser.parse().value);
			return result;
		}
		catch(Exception e) {
			throw new ParsingError(e.getMessage() == null ? "parsing error" : e.getMessage());
		}
		finally {
			input.close();
		}
	}

	private static boolean hasSuffix(String s, String suffix) {
		return s.endsWith("." + suffix);
	}

	private static String replaceSuffix(String s, String newSuffix) {
		return s.substring(0, s.lastIndexOf('.') + 1) + newSuffix;
	}

	private static boolean isFlag(String arg) {
		return flags.contains(arg);
	}

	private static boolean isInputFile(String arg) {
		return hasSuffix(arg, "cm") || hasSuffix(arg, "cminor") || arg.equals(PIPE_MARKER);
	}

}
