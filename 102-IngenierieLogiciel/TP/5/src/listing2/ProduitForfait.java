package listing2;

public abstract class ProduitForfait extends Produit {

	Produit decore;
	
	public ProduitForfait(Produit decore) {
		super(decore.nom, decore.prix);
		this.decore = decore;
	}
}