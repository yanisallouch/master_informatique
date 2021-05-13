package listing2;

public class ProduitSimple extends Produit {

	public ProduitSimple(String nom, double prix) {
		super(nom, prix);
		
	}

	@Override
	public double getPrix() {
		return prix;
	}
}