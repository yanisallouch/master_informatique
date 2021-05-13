/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	25 octobre 2020
*	@attribute HMIN104
*/

package colorGrap;

public class Arete {
	Sommet sommetGauche;// 1
	Sommet sommetDroit;// 2
	boolean preference = false;
	//boolean etreIgnorer = false;

//	public Arete(Sommet sommetGauche, Sommet sommetDroit, boolean preference, boolean etreIgnorer) {
//		this.sommetGauche = sommetGauche;
//		this.sommetDroit = sommetDroit;
//		this.preference = preference;
//		this.etreIgnorer = etreIgnorer;
//	}
	public Arete(Sommet sommetGauche, Sommet sommetDroit, boolean preference) {
		this.sommetGauche = sommetGauche;
		this.sommetDroit = sommetDroit;
		this.preference = preference;
	}
	public Arete(Sommet sommetGauche, Sommet sommetDroit) {	
		this.sommetGauche = sommetGauche;
		this.sommetDroit = sommetDroit;
	}
	public Sommet getSommetGauche() {
		return sommetGauche;
	}
	public void setSommetGauche(Sommet sommetGauche) {
		this.sommetGauche = sommetGauche;
	}
	public Sommet getSommetDroit() {
		return sommetDroit;
	}
	public void setSommetDroit(Sommet sommetDroit) {
		this.sommetDroit = sommetDroit;
	}
	public boolean isPreference() {
		return preference;
	}
	public void setPreference(boolean preference) {
		this.preference = preference;
	}
//	public boolean isEtreIgnorer() {
//		return etreIgnorer;
//	}
//	public void setEtreIgnorer(boolean etreIgnorer) {
//		this.etreIgnorer = etreIgnorer;
//	}
	public boolean contient(Sommet smt) {
		return getSommetDroit().equals(smt) || getSommetGauche().equals(smt);
	}
	public int position(Sommet smt) {
		int pos = 0;
		if(getSommetGauche().equals(smt)) {
			pos = 1;
		}else if (getSommetDroit().equals(smt)) {
			pos = 2;
		}
		return pos;
	}
	public Sommet autreExtremiterDe(Sommet smt){ 
		if(this.position(smt)==1) {
			return getSommetDroit();
		}
		else if(this.position(smt) == 2){
			return getSommetGauche();
		}else {
			return null;
		}
	}
	@Override
	public String toString() {
		return "(" + sommetGauche + " <--> " +  sommetDroit + ")" + (preference?"(pref)":"");
	}
}