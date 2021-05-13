package listing2;

public class CompteSimple extends Compte {

	public CompteSimple(Client client) {
		super(client);
		
	}

	@Override
	public double prixLocation(Produit lgv) {
		
		return lgv.getPrix();
	}
}