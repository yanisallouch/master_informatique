package fake.compiler.product.implementation;

public class ProgramText {
	String text;
	public ProgramText(String string) {
		this.text = string;
	}
	@Override
	public String toString() {
		return text;
	}
}
