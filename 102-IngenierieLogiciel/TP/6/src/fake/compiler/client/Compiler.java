package fake.compiler.client;

import fake.compiler.factory.IFabriqueCompiler;
import fake.compiler.product.IGenerator;
import fake.compiler.product.ILexer;
import fake.compiler.product.IParser;
import fake.compiler.product.implementation.ProgramText;

public class Compiler {
	protected IFabriqueCompiler fabrique;
	protected ILexer lexer;
	protected IParser parser;
	protected IGenerator gen;
	
	public Compiler(String string) {
		fabrique = IFabriqueCompiler.getFactory(string);
		lexer = fabrique.createLexer();
		parser = fabrique.createParser();
		gen = fabrique.createGenerator();
	}
	
	public  void compile(ProgramText t) {
		System.out.println("Compiling a: " + fabrique + " program.");
		lexer.scan();
		parser.parse();
		gen.generate();
		System.out.println("Compiling finished");
	}
	
	public static void main(String[] args) {
		try{
			System.out.println("-----------------------");
			Compiler c1 = new Compiler("Java");
			c1.compile(new ProgramText("..."));
			System.out.println("-----------------------");
			Compiler c2 = new Compiler ("C++");
			c2.compile(new ProgramText("..."));
			System.out.println("-----------------------");
			@SuppressWarnings("unused")
			Compiler c3 = new Compiler("ADA");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}