package calculatrice.state.prefixe;

public class EOperateur extends EtatCalculette {

	public EOperateur(Calculette c) {
		super(c);
		
	}

	@Override
	int enter(String s) throws CalculetteException {
		calc.setOperateur(s);
		return (2);
	}
}