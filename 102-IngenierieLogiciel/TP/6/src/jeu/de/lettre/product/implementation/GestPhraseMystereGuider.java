package jeu.de.lettre.product.implementation;

import jeu.de.lettre.product.IAlphabet;

public class GestPhraseMystereGuider extends GestPhraseMystere {
	
	public GestPhraseMystereGuider() {
		super(0.7);
	}
	public GestPhraseMystereGuider(double pourcentageLettresCodes) {
		super(pourcentageLettresCodes);
		
	}
	public GestPhraseMystereGuider(IAlphabet alphabetEncodage) {
		super(0.7, alphabetEncodage);
		
	}
}