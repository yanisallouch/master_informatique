package jeu.de.lettre.factory.implementation;

import jeu.de.lettre.factory.IFabriqueJeu;
import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;
import jeu.de.lettre.product.implementation.GestPhraseMystereNonGuider;

public class FabriqueJeuMoyen implements IFabriqueJeu {
	
	public FabriqueJeuMoyen() {
		// TODO Auto-generated constructor stub
	}
	public FabriqueJeuMoyen(String aDeviner) {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public IGestPhraseMystere createGestionnairePhraseMystere(IAlphabet alphabetEncodage) {
		return new GestPhraseMystereNonGuider();
	}
	@Override
	public IAlphabet createAlphabetEncodage(String aDeviner) {
		// TODO Auto-generated method stub
		return null;
	}
}
