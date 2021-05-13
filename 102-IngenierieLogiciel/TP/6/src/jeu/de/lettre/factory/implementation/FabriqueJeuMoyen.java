package jeu.de.lettre.factory.implementation;

import jeu.de.lettre.factory.IFabriqueJeu;
import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;
import jeu.de.lettre.product.implementation.GestPhraseMystereNonGuider;

public class FabriqueJeuMoyen implements IFabriqueJeu {
	
	public FabriqueJeuMoyen() {
		
	}
	public FabriqueJeuMoyen(String aDeviner) {
		
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
