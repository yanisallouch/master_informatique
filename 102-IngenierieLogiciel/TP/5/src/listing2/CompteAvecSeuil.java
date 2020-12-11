package listing2;

public class CompteAvecSeuil extends CompteForfait {

	private int seuil = 2;
	private int nbLoc = 0;
	
	public CompteAvecSeuil( Compte decore, int seuil, int nbLoc) {
		super(decore);
		this.seuil = seuil;
		this.nbLoc = nbLoc;
	}

	public CompteAvecSeuil(Compte decore) {
		super(decore);
	}

	@Override
	public double prixLocation(Produit lgv) {
		if(nbLoc < seuil) {
			nbLoc++;
			return decore.prixLocation(lgv);
		}else {
			nbLoc = 0;
			return 0;
		}
	}
}