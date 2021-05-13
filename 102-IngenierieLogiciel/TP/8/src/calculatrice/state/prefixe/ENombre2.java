package calculatrice.state.prefixe;

public class ENombre2 extends EtatCalculette {

	public ENombre2(Calculette c) {
		super(c);
		
	}

	@Override
	int enter(String s) throws CalculetteException {
		float temp = 0;
		try {
			temp = Float.parseFloat(s);
		} catch (NumberFormatException e) {
			throw new CalculetteNumberException(s);
		}
		
		switch (operations.valueOf(calc.getOperateur())) {
		case plus: {
			calc.setAccumulateur((calc.getAccumulateur() + temp));
			break;
		}
		case mult: {
			calc.setAccumulateur((calc.getAccumulateur() * temp));
			break;
		}
		default:
			throw new CalculetteUnknownOperator((calc.getOperateur()));
		}
		return (1);
	}

}
