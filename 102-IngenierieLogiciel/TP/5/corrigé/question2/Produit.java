package produit;

public class Produit {

		double prixBase;
		double TVA = 19.6;
		double marge = 1.10;
		String titre;
		
		Produit(String titre, double pb) {
			this.titre = titre;
			this.prixBase = pb;}
		
		protected double prixHT(){return prixBase * marge;}

		double prixVente(){return (this.prixHT() * (1 + TVA));}
		
		double prixLocation(){
			return this.prixVente() * 5 /100;
		}
		
public static void main(String[] args){
			
			Produit lgv = new Produit("La grande vadrouille", 10.0);
			Client cl = new Client("Dupont");

			Compte cmt = new Normal(cl);
			System.out.println("Normal : " + cmt.prixLocation(lgv));
			
			Compte cmt2 = new Seuil (new Reduction (new Normal (cl)));
			System.out.println("Seuil+Reduction : " + cmt2.prixLocation(lgv));
			System.out.println("Seuil+Reduction : " + cmt2.prixLocation(lgv));
			System.out.println("Seuil+Reduction : " + cmt2.prixLocation(lgv));
			System.out.println("Seuil+Reduction : " + cmt2.prixLocation(lgv));
			System.out.println("Seuil+Reduction : " + cmt2.prixLocation(lgv));

			Produit r4 = new ProduitSoldé("RockyIV", 10.0);
			System.out.println("Seuil+Reduction+Soldé : " + cmt2.prixLocation(r4));
			System.out.println("Seuil+Reduction+Soldé : " + cmt2.prixLocation(r4));
			}
		}

	class ProduitSoldé extends Produit{
		ProduitSoldé(String titre, double pb) {
			super(titre, pb);}
		
		double prixVente(){return (super.prixVente() / 2);}
	}

	class Client{
		protected String nom;
		protected Compte compte;
		public void setCompte(Compte c){compte = c;}
		public Compte getCompte(){return compte;}
		
		public Client(String nom){
			this(nom, null);}
		
		public Client(String nom, Compte c){
			this.nom = nom;
			this.compte = c;}
	}

	abstract class Compte {
		public double prixLocation(Produit p){
			return p.prixLocation();}
	}

	class Normal extends Compte{
		static int compteur = 0;
		int numero;
		Client titulaire;
		
		public Normal (Client cl){
			titulaire = cl;
			cl.setCompte(this);
			this.numero = ++compteur;
		}
	}

	class Forfait extends Compte{
		Compte décoré;
		public Forfait(Compte c){
			décoré = c;
		}
		public double prixLocation(Produit p){
			return décoré.prixLocation(p);}
	}

	class Reduction extends Forfait{

		double reduction = 0.10;
		
		public Reduction(Compte c){super(c);}
		
		public double prixLocation(Produit p){
			return(super.prixLocation(p) * (1-reduction));}
	}

	class Seuil extends Forfait{
		//donne une location gratuite après $compteur payantes
		static int init = 2;
		int compteur = init;
		
		public Seuil(Compte c){super(c);}
		
		public double prixLocation(Produit p){
			if (compteur-- == 0) 
				{compteur = init; return 0.0;}
			else return super.prixLocation(p);
		}
	}
