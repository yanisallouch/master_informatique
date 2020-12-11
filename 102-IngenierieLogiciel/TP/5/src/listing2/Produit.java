package listing2;

public abstract class Produit {
	
	double prix;
	String nom;
	
	public Produit (String nom, double prix){
		this.nom = nom;
		this.prix = prix;
	}
	
	public abstract double getPrix();
}