package fake.compiler.factory.implementation;

import fake.compiler.factory.IFabriqueCompiler;
import fake.compiler.product.IGenerator;
import fake.compiler.product.ILexer;
import fake.compiler.product.IParser;
import fake.compiler.product.implementation.GeneratorJAVA;
import fake.compiler.product.implementation.LexerJAVA;
import fake.compiler.product.implementation.ParserJAVA;

public class FabriqueCompilerJAVA implements IFabriqueCompiler {

	public FabriqueCompilerJAVA() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ILexer createLexer() {
		return new LexerJAVA();
	}

	@Override
	public IParser createParser() {
		return new ParserJAVA();
	}

	@Override
	public IGenerator createGenerator() {
		return new GeneratorJAVA();
	}
	
	@Override
	public String toString() {
		return "Java";
	}
}