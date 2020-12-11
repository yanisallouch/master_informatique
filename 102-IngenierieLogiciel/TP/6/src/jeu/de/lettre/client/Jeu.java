package jeu.de.lettre.client;
import jeu.de.lettre.factory.IFabriqueJeu;
import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;

public class Jeu {
	protected IFabriqueJeu fabrique;
	protected IGestPhraseMystere gestPhraseMystere;
	protected IAlphabet alphabetEncode;

//	public Jeu(String string) {
//		fabrique = IFabriqueJeu.getFactory(string);
//		alphabet = fabrique.createAlphabet();
//		gestPhraseMystere = fabrique.createGestPhraseMystere();
//	}

	public Jeu(String difficulter, String aDeviner) {
		fabrique = IFabriqueJeu.getFactory(difficulter.toUpperCase());
		alphabetEncode = fabrique.createAlphabetEncodage(aDeviner.toUpperCase());
		gestPhraseMystere = fabrique.createGestionnairePhraseMystere(alphabetEncode);
	}

	public static void main(String[] args) {
		try {
			System.out.println("-----------------------");
			Jeu uneSorteDePendu = new Jeu("Facile", "Le Petit Chaperon Rouge");
			uneSorteDePendu.start(3);
			System.out.println("-----------------------");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void start(int nbJoueurs) {
		// @argument nbJoueurs ==> nombre de joueurs
		// pour le moment est ignoré
		// TODO Auto-generated method stub
		System.out.println("Initialisation du jeu terminer !");
		System.out.println("Le jeu a ete lancer avec " + nbJoueurs + " joueur" + (nbJoueurs==1?"":"s")
				+ " en difficulte " + fabrique);
		gestPhraseMystere.startGestion();
	}

}
