package jeu.de.lettre.factory.implementation;

import jeu.de.lettre.factory.IFabriqueJeu;
import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;
import jeu.de.lettre.product.implementation.AlphabetSymboles;
import jeu.de.lettre.product.implementation.GestPhraseMystereGuider;

public class FabriqueJeuFacile implements IFabriqueJeu {
	
	public FabriqueJeuFacile() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IAlphabet createAlphabetEncodage(String aDeviner) {
		return new AlphabetSymboles(aDeviner);
	}
	
	@Override
	public IGestPhraseMystere createGestionnairePhraseMystere(IAlphabet alphabetEncodage) {
		return new GestPhraseMystereGuider(alphabetEncodage);
	}

	@Override
	public String toString() {
		return "Facile";
	}
}