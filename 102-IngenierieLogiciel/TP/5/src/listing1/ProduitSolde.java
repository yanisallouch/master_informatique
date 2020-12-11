package listing1;

public class ProduitSolde extends Produit {

	double reduction = 0.3;
	
	public ProduitSolde(String nom, double prix) {
		super(nom, prix);
	}

	public ProduitSolde(String nom, double prix, double reduction) {
		super(nom, prix);
		// TODO Auto-generated constructor stub
		this.reduction = reduction;
	}
	
	@Override
	public double getPrix() {
		return prix - (prix*reduction);
	}
	
}
