package listing1;

public class Produit {
	
	double prix;
	String nom;
	
	public Produit (String nom, double prix){
		this.nom = nom;
		this.prix = prix;
	}
	
	public double getPrix(){
		return prix;
	}
	
}
