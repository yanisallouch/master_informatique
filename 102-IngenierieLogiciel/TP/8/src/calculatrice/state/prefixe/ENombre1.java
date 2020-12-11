package calculatrice.state.prefixe;

public class ENombre1 extends EtatCalculette {

	public ENombre1(Calculette c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	int enter(String s) throws CalculetteException {
		try {
			calc.setAccumulateur(Float.parseFloat(s));
		} catch (NumberFormatException e) {
			throw new CalculetteNumberException(s);
		}
		return (3);
	}
}
