package calculatrice.state.prefixe;

public abstract class EtatCalculette {
	static protected enum operations {plus, moins, mult, div};
	abstract int enter(String s) throws CalculetteException;
	Calculette calc;
	
	public EtatCalculette(Calculette c) {
		this.calc = c;
	}

	public Calculette getCalc() {
		return calc;
	}

	public void setCalc(Calculette calc) {
		this.calc = calc;
	}
}