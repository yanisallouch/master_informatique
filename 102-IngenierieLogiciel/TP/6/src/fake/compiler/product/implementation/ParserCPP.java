package fake.compiler.product.implementation;

import fake.compiler.product.IParser;

public class ParserCPP  implements IParser {

	public ParserCPP() {
		
	}

	@Override
	public void parse() {
		System.out.println("I am parsing a C++ scaned text a C++ AbstractSyntaxTree");
	}

}
