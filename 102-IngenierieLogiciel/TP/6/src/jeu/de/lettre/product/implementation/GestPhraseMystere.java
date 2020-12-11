package jeu.de.lettre.product.implementation;

import java.util.*;

import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;

public abstract class GestPhraseMystere implements IGestPhraseMystere {
	
	public double pourcentageLettresCodes;
	IAlphabet alphabetEncode;
	String motMystere;

	public GestPhraseMystere() {}
	
	public GestPhraseMystere(double pourcentageLettresCodes) {
		this.pourcentageLettresCodes = pourcentageLettresCodes;
	}
	
	public GestPhraseMystere(IAlphabet alphabetEncode) {
		this.alphabetEncode = alphabetEncode;
	}

	public GestPhraseMystere(double pourcentageLettresCodes, IAlphabet alphabetEncode) {
		this.alphabetEncode = alphabetEncode;
		this.pourcentageLettresCodes = pourcentageLettresCodes;
		this.init();
	}
	
	
	
	public double getPourcentageLettresCodes() {
		return pourcentageLettresCodes;
	}

	public void setPourcentageLettresCodes(double pourcentageLettresCodes) {
		this.pourcentageLettresCodes = pourcentageLettresCodes;
	}

	public IAlphabet getAlphabetEncode() {
		return alphabetEncode;
	}

	public void setAlphabetEncode(IAlphabet alphabetEncode) {
		this.alphabetEncode = alphabetEncode;
	}

	public String getMotMystere() {
		return motMystere;
	}

	public void setMotMystere(String motMystere) {
		this.motMystere = motMystere;
	}

	public boolean estToujoursMystere() {
		return !this.motMystere.equals(alphabetEncode.getaDevinerEnClair());
	}
	
	public void initMotMystere() {
		String leMotADevinerEnClair = alphabetEncode.getaDevinerEnClair();
		int taille = (int)(leMotADevinerEnClair.length() * pourcentageLettresCodes);
		for(int i = 0; i < taille ; i++) {
			char lettreAEncodeEnClair = leMotADevinerEnClair.charAt(i);
			if((i < taille/3 || i > (taille/3) * 2 ) && lettreAEncodeEnClair > 64 && lettreAEncodeEnClair < 91) {
				// le mot : **********__________**********
				//           taille/3  taille/3  taille/3  
				// espace et autre caractere de ponctuation sont eviter
				
				char iemeChar = (char) (lettreAEncodeEnClair - 26) ;
				int indiceIemeChar = alphabetEncode.getIndiceCharDeLaLettre(iemeChar);
				this.motMystere += alphabetEncode.getEncodageCharacter().get(indiceIemeChar);
			}else{
				this.motMystere += String.valueOf(lettreAEncodeEnClair);
			}
		}
	}
	
	public boolean contientLaLettre(String lettre) {
		return alphabetEncode.getaDevinerEnClair().contains(lettre);
	}
	
	public void demasquerLettre(String lettre) {
		int indiceCorrespondanceEncodage = lettre.charAt(0) - 26;
		motMystere = motMystere.replace(alphabetEncode.getEncodageCharacter().get(indiceCorrespondanceEncodage).charAt(0)
				, lettre.charAt(0));
	}
	
	public int resteADecouvrir() {
		int taille = motMystere.length();
		int cpt = 0;
		for(int i = 0; i < taille; i++) {
			if(motMystere.charAt(i) < 65) {
				cpt++;
			}
		}
		return cpt;
	}
	
	public void init() {
		System.out.println("------------");
		System.out.println("Initialisation gestionnaire phrase mystere");
		this.initMotMystere();
		System.out.println("Fin d'initialisation gestionnaire phrase mystere");
		System.out.println("------------");
	}
	
	public void startGestion() {
		Scanner sc= new Scanner(System.in);
		while(this.estToujoursMystere()) {
			System.out.println("Saisir une lettre : ");
			String lettre = sc.nextLine();
			if(this.contientLaLettre(lettre)) {
				System.out.println("La lettre '" + lettre + "' est presente");
				this.demasquerLettre(lettre);
			}else {
				System.out.println("La lettre '" + lettre + "' n'est pas presente");
			}
		}
		sc.close();
	}
}