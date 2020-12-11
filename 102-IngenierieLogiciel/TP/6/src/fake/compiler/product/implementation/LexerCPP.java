package fake.compiler.product.implementation;

import fake.compiler.product.ILexer;

public class LexerCPP implements ILexer {

	public LexerCPP() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void scan() {
		System.out.println("I am scanning a C++ program text");
	}

}
