package listing1;

public class Compte {
	
	public Client client;
	
	public Compte(Client client) {
		this.client = client;
	}

	public double prixLocation(Produit lgv) {		
		if(client != null) {
			return (lgv.getPrix());
		}
		return 0;
	}
}
