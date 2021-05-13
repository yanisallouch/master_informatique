package fake.compiler.factory.implementation;

import fake.compiler.factory.IFabriqueCompiler;
import fake.compiler.product.IGenerator;
import fake.compiler.product.ILexer;
import fake.compiler.product.IParser;
import fake.compiler.product.implementation.GeneratorCPP;
import fake.compiler.product.implementation.LexerCPP;
import fake.compiler.product.implementation.ParserCPP;

public class FabriqueCompilerCPP implements IFabriqueCompiler {

	public FabriqueCompilerCPP() {
		
	}
	
	@Override
	public ILexer createLexer() {
		return new LexerCPP();
	}

	@Override
	public IParser createParser() {
		return new ParserCPP();
	}

	@Override
	public IGenerator createGenerator() {
		return new GeneratorCPP();
	}

	@Override
	public String toString() {
		return "C++";
	}

}