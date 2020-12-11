package jeu.de.lettre.factory;

import jeu.de.lettre.factory.implementation.FabriqueJeuDifficile;
import jeu.de.lettre.factory.implementation.FabriqueJeuFacile;
import jeu.de.lettre.factory.implementation.FabriqueJeuMoyen;
import jeu.de.lettre.product.IAlphabet;
import jeu.de.lettre.product.IGestPhraseMystere;

public interface IFabriqueJeu {
	// Abstraite
	public static IFabriqueJeu getFactory(String difficulter) {
		switch (difficulter) {
		case "FACILE" : {
			return new FabriqueJeuFacile();
		}
		case "MOYEN": {
			return new FabriqueJeuMoyen();
		}
		case "DIFFICILE": {
			return new FabriqueJeuDifficile();
		}
		default:
			throw new IllegalArgumentException("Non supported difficulty : " + difficulter + ", Extend the framework to support it */");
		}
	}
	
	public IAlphabet createAlphabetEncodage(String aDeviner); 
	public IGestPhraseMystere createGestionnairePhraseMystere(IAlphabet alphabetEncodage);
	

// to be implemented next ===> ?
//
//		public static IFabriqueJeu getFactory(String difficulter) {
//			switch (difficulter.toUpperCase()) {
//			case "FACILE" : {
//				return new FabriqueJeuFacile();
//			}
//			case "MOYEN": {
//				return new FabriqueJeuMoyen();
//			}
//			case "DIFFICILE": {
//				return new FabriqueJeuDifficile();
//			}
//			default:
//				throw new IllegalArgumentException("Non supported difficulty : " + difficulter + ", Extend the framework to support it */");
//			}
//		}
}