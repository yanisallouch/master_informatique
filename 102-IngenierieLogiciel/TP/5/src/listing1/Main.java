package listing1;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args){
		Produit lgv = new Produit("La grande vadrouille", 10.0);
		Client cl = new Client("Dupont");
		Compte cmt = new Compte(cl);
		System.out.println("Compte : " + cmt.prixLocation(lgv));
		
		Compte cmt2 = new CompteAvecReduction(cl);
		System.out.println("CompteReduction : " + cmt2.prixLocation(lgv));
		Compte cmt3 = new CompteAvecSeuil(cl); // le seuil est à 2 par défaut
		System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));
		System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));
		System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv)); // doit afficher 0
		Produit r4 = new ProduitSolde("RockyIV", 10.0);
		System.out.println("CompteNormal+ProduitSoldé : " + cmt.prixLocation(r4));
		System.out.println("CompteReduction+ProduitSoldé : " + cmt2.prixLocation(r4));
	}	
}
