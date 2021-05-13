package fake.compiler.product.implementation;
import fake.compiler.product.IGenerator;

public class GeneratorJAVA implements IGenerator {

	public GeneratorJAVA() {
		
	}

	@Override
	public void generate() {
		System.out.println("I am generating a JVM program text from a Java AbstractSyntaxTree");
	}
}