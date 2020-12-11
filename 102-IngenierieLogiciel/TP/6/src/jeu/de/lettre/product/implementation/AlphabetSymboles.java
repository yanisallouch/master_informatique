package jeu.de.lettre.product.implementation;

import java.util.Collections;

public class AlphabetSymboles extends Alphabet {

	public AlphabetSymboles() {
		super("");
	}

	public AlphabetSymboles(String aDeviner) {
		super(aDeviner);
	}

	@Override
	public void init() {
		System.out.println("----");
		System.out.println("Initialisation d'alphabet");
		
		for(int i = 38; i < 66; i++) {
			// j'ai fais un padding de 1 lettre de chaque cote
			encodageInteger.add(i);
		}
		
		Collections.shuffle(encodageInteger);
		
		for(int i = 0; i < 27; i++) {
			int tmp = encodageInteger.get(i);
			encodageCharacter.add(String.valueOf((char)tmp));
			/*
			 * une fois qu'elle est generer,
			 *  je fais l'association entre
			 *  mon alphabet de lettre de A a Z et ma suite aleatoire
			 */
		}

		System.out.println("Fin d'initialisation d'alphabet");
		System.out.println("----");
	}
}