package listing2;

public abstract class CompteForfait extends Compte { // Wrapper | Decorateur
	
	Compte decore;
	
	public CompteForfait( Compte decore) {
		super(decore.client);
		this.decore = decore;
	}
	
	public double prixLocation(Produit lgv) {
		return decore.prixLocation(lgv);
	}
}