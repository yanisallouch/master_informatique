package fake.compiler.product.implementation;
import fake.compiler.product.IGenerator;

public class GeneratorCPP implements IGenerator {

	public GeneratorCPP() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generate() {
		System.out.println("I am generating an assembler program text from a C++ AbstractSyntaxTree");
	}
}