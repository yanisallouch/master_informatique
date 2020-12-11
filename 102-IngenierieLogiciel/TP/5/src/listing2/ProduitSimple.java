package listing2;

public class ProduitSimple extends Produit {

	public ProduitSimple(String nom, double prix) {
		super(nom, prix);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getPrix() {
		return prix;
	}
}