package listing2;

public class ProduitSolde extends ProduitForfait {
	
	public double solde;
	
	public ProduitSolde(Produit decore, double solde) {
		super(decore);
		this.solde = solde;
		
	}

	@Override
	public double getPrix() {
		return decore.getPrix() - (decore.getPrix() * solde);
	}
}