package listing2;


public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Produit lgv = new ProduitSimple("La grande vadrouille", 10.0);
		Client cl = new Client("Dupont");
		CompteSimple cmt = new CompteSimple(cl);
		System.out.println("Compte : " + cmt.prixLocation(lgv));
		
		Compte cmt2 = new CompteAvecReduc(new CompteSimple(cl));
		System.out.println("CompteReduction : " + cmt2.prixLocation(lgv));
		Compte cmt3 = new CompteAvecSeuil(new CompteSimple(cl)); // le seuil est à 2 par défaut
		System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));
		System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv));
		System.out.println("CompteSeuil : " + cmt3.prixLocation(lgv)); // doit afficher 0
		
		Produit r4 = new ProduitSolde(new ProduitSimple("RockyIV", 10.0), 0.30);
		System.out.println("CompteNormal+ProduitSoldé : " + cmt.prixLocation(r4));
		System.out.println("CompteReduction+ProduitSoldé : " + cmt2.prixLocation(r4));
		
		Compte cmt4 = new CompteAvecSeuil(cmt2);
		
		System.out.println("CompteSeuil + Reduc : " + cmt4.prixLocation(lgv));
		System.out.println("CompteSeuil + Reduc  : " + cmt4.prixLocation(lgv));
		System.out.println("CompteSeuil + Reduc  : " + cmt4.prixLocation(lgv)); // doit afficher 0
	}

}
