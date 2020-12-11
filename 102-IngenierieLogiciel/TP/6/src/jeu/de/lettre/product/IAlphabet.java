package jeu.de.lettre.product;

import java.util.ArrayList;

public interface IAlphabet {

	public void init();
	public String getaDevinerEnClair();
	public void setaDevinerEnClair(String string);
	public ArrayList<String> getEncodageCharacter();
	public void setEncodageCharacter(ArrayList<String> encodage);
	public ArrayList<Integer> getEncodageInteger();
	public void setEncodageInteger(ArrayList<Integer> encodage);
	public int getIndiceCharDeLaLettre(char lettre);
}
