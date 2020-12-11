
public class Bidon {
	int capacite = 0;
	int quantite = 0;
	
	public Bidon(){
		super();
		this.capacite = (int) ((Math.random() * (101 - 3)) + 3);
	}
	
	public Bidon(int capacite) {
		super();
		this.capacite = capacite;
	}
	
	public Bidon(int capacite, int quantite) {
		super();
		this.capacite = capacite;
		this.quantite = quantite;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	
	public int remplir() {
		this.setQuantite(this.getCapacite());
		return getCapacite();
	}
	
	public int remplir(int qte) {
		int tmp = qte + this.getQuantite();
		if(tmp >= this.getCapacite()) {
			tmp = this.getCapacite();
		}// on enleve le trop plein
		this.setQuantite(tmp);
		return getQuantite();
	}
	
	public void vider() {
		this.setQuantite(0);
	}
	
	public void transvaser(Bidon bidonDest) {
		bidonDest.remplir(this.getQuantite());
		this.vider();
	}
}
