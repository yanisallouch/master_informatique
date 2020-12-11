package listing2;

public class CompteSimple extends Compte {

	public CompteSimple(Client client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double prixLocation(Produit lgv) {
		// TODO Auto-generated method stub
		return lgv.getPrix();
	}
}