package listing1;

public class CompteAvecReduction extends Compte {

	double reduction = 0.25;
	
	public CompteAvecReduction(Client client, double reduction) {
		super(client);
		this.reduction = reduction;
	}

	public CompteAvecReduction(Client client) {
		super(client);
		
	}

	@Override
	public double prixLocation(Produit lgv) {
		if(client != null) {
			return(lgv.getPrix()-(lgv.getPrix()*reduction));
		}
		return(0);
	}
}