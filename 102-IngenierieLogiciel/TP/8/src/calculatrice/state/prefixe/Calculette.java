package calculatrice.state.prefixe;

public class Calculette {
	protected EtatCalculette etatCourant;
	protected EtatCalculette[] etats = new EtatCalculette[3];
	double accumulateur;
	String operateur;
	
	public Calculette() {
		etats[1] =  new ENombre1(this);
		etats[0] =  new EOperateur(this);
		etats[2] =  new ENombre2(this);
		etatCourant = etats[0];
		accumulateur = 0;
 	}

	public EtatCalculette getEtatCourant() {
		return etatCourant;
	}

	public void setEtatCourant(EtatCalculette etatCourant) {
		this.etatCourant = etatCourant;
	}

	public EtatCalculette[] getEtats() {
		return etats;
	}

	public void setEtats(EtatCalculette[] etats) {
		this.etats = etats;
	}

	public double getAccumulateur() {
		return accumulateur;
	}
	
	public double getResults() {
		return accumulateur;
	}

	public void setAccumulateur(double accumulateur) {
		this.accumulateur = accumulateur;
	}

	public String getOperateur() {
		return operateur;
	}

	public void setOperateur(String operateur) {
		this.operateur = operateur;
	}
	
	public void enter(String s) throws CalculetteException {
		etatCourant = etats[etatCourant.enter(s) - 1];
	}
}