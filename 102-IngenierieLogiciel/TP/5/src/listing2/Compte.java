package listing2;

public abstract class Compte {
	
	public Client client;
	
	public Compte(Client client) {
		this.client = client;
	}

	public abstract double prixLocation(Produit lgv);
}