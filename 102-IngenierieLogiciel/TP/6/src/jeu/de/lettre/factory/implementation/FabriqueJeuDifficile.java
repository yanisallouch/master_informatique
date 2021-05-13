package jeu.de.lettre.factory.implementation;

import jeu.de.lettre.factory.IFabriqueJeu;
import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;
import jeu.de.lettre.product.implementation.GestPhraseMystereNonGuider;

public class FabriqueJeuDifficile implements IFabriqueJeu {

	public FabriqueJeuDifficile() {
		
	}

	@Override
	public IGestPhraseMystere createGestionnairePhraseMystere(IAlphabet alphabetEncodage) {
		return new GestPhraseMystereNonGuider();
	}

	@Override
	public IAlphabet createAlphabetEncodage(String aDeviner) {
		
		return null;
	}

}