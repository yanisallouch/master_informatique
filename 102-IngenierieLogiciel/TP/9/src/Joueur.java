import java.util.ArrayList;

public class Joueur {
	public int taille;
	public ArrayList<Bidon> listesBidons;
	
	public Joueur() {
		super();
	}
	
	public Joueur(int taille) {
		super();
		this.taille = taille;
		this.listesBidons = new ArrayList<Bidon>(taille); 
	}

	public Joueur(ArrayList<Bidon> listesBidons) {
		super();
		this.listesBidons = listesBidons;
	}	
	
	public ArrayList<Bidon> getListesBidons() {
		return listesBidons;
	}

	public void setListesBidons(ArrayList<Bidon> listesBidons) {
		this.listesBidons = listesBidons;
	}
	
	public void remplir(int i) {
		Bidon tmp = this.listesBidons.get(i-1);
		tmp.remplir();
		this.listesBidons.set(i-1, tmp);
	}

	public void vider(int i) {
		Bidon tmp = this.listesBidons.get(i-1);
		tmp.vider();
		this.listesBidons.set(i-1, tmp);
	}

	public void transvaser(int i, int j) {
		// <i> vers <j>
		Bidon b1 = this.listesBidons.get(i-1);
		Bidon b2 = this.listesBidons.get(j-1);
		b2.remplir(b1.getQuantite());
	}
}