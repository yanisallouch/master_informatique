package listing2;

public class CompteAvecReduc extends CompteForfait {

	double reduction = 0.25;
	
	public CompteAvecReduc( Compte decore, double reduction) {
		super(decore);
		this.reduction = reduction;
	}

	public CompteAvecReduc( Compte decore) {
		super(decore);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double prixLocation(Produit lgv) {
		return decore.prixLocation(lgv) - (decore.prixLocation(lgv) * reduction);
	}
}