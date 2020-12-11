package calculatrice.state.prefixe;

public class EOperateur extends EtatCalculette {

	public EOperateur(Calculette c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	int enter(String s) throws CalculetteException {
		calc.setOperateur(s);
		return (2);
	}
}