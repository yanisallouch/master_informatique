package jeu.de.lettre.product.implementation;

import java.util.ArrayList;

import jeu.de.lettre.product.IAlphabet;

public abstract class Alphabet implements IAlphabet {
	String aDevinerEnClair;
	ArrayList<String> encodageCharacter = new ArrayList<>();
	ArrayList<Integer> encodageInteger = new ArrayList<Integer>(26);
	
	public Alphabet(String aDeviner) {
		this.aDevinerEnClair = aDeviner;		
		init();
	}

	@Override
	public abstract void init();

	public String getaDevinerEnClair() {
		return aDevinerEnClair;
	}

	public void setaDevinerEnClair(String aDeviner) {
		this.aDevinerEnClair = aDeviner;
	}

	public ArrayList<String> getEncodageCharacter() {
		return encodageCharacter;
	}

	public void setEncodageCharacter(ArrayList<String> encodage) {
		this.encodageCharacter = encodage;
	}

	public ArrayList<Integer> getEncodageInteger() {
		return encodageInteger;
	}
	
	public void setEncodageInteger(ArrayList<Integer> encodageInteger) {
		this.encodageInteger = encodageInteger;
	}
	public int getIndiceCharDeLaLettre(char lettre) {
		for( int i = 0; i < encodageCharacter.size(); i++) {
			if(encodageCharacter.get(i).equals(String.valueOf(lettre))) {
				return i;
			}
		}
		throw new RuntimeException("Bug dans la matrice");
	}
}
