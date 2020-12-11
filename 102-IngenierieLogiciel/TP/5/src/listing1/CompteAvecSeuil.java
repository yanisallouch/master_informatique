package listing1;

public class CompteAvecSeuil extends Compte {
	
	int seuil = 2;
	private int nbLoc = 0;
	
	public CompteAvecSeuil(Client client, int seuil) {
		super(client);
		this.seuil = seuil;
	}

	public CompteAvecSeuil(Client client) {
		super(client);
	}
	
	@Override
	public double prixLocation(Produit lgv) {
		if(client != null) {
			if(nbLoc < seuil) {
				nbLoc++;
				return (lgv.getPrix());
			}else {
				nbLoc = 0;
				return(0);
			}
		}
		return(0);
	}
}