

public class EspeceImplementation implements Espece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2312627145814498032L;
	private String nom;
	private double dureeDeVie;
	
	public EspeceImplementation() {
		nom = "";
		dureeDeVie = 0.0;
	}
				
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getDureeDeVie() {
		return dureeDeVie;
	}
	public String toString() {
		return nom + " et a pour duree de vie, " + dureeDeVie ;
	}
}