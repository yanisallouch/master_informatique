package fake.compiler.product.implementation;

import fake.compiler.product.IParser;

public class ParserJAVA implements IParser {

	public ParserJAVA() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parse() {
		System.out.println("I am parsing a Java scaned text and I generate a Java AbstractSyntaxTree");
	}

}
