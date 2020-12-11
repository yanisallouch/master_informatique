package fake.compiler.product.implementation;

import fake.compiler.product.IParser;

public class ParserCPP  implements IParser {

	public ParserCPP() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parse() {
		System.out.println("I am parsing a C++ scaned text a C++ AbstractSyntaxTree");
	}

}
