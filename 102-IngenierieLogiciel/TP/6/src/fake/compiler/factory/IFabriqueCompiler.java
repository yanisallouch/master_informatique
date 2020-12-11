package fake.compiler.factory;

import fake.compiler.factory.implementation.FabriqueCompilerCPP;
import fake.compiler.factory.implementation.FabriqueCompilerJAVA;
import fake.compiler.product.IGenerator;
import fake.compiler.product.ILexer;
import fake.compiler.product.IParser;

public interface IFabriqueCompiler {
	//Abstraite
	public static IFabriqueCompiler getFactory(String string) {
		switch (string.toUpperCase()) {
//		case "ADA": {
//		should not be implemented	
//		}
		case "C++" : {
			return new FabriqueCompilerCPP();
		}
		case "JAVA": {
			return new FabriqueCompilerJAVA();
		}
		default:
			throw new IllegalArgumentException("Non supported Language : " + string + ", Extend the framework to support it */");
		}
	}

	public ILexer createLexer();
	public IParser createParser();
	public IGenerator createGenerator();
}