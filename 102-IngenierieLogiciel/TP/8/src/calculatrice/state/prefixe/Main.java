package calculatrice.state.prefixe;

public class Main {
	public static void main(String[] args) throws CalculetteException {
		Calculette c = new Calculette();
		c.enter("plus");
		c.enter("123");
		c.enter("234");
		System.out.println(c.getResults());
	}
}
