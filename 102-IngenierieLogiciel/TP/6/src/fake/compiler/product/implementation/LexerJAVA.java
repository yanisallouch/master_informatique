package fake.compiler.product.implementation;

import fake.compiler.product.ILexer;

public class LexerJAVA implements ILexer {

	public LexerJAVA() {
		
	}

	@Override
	public void scan() {
		System.out.println("I am scanning a Java program text");
	}
}