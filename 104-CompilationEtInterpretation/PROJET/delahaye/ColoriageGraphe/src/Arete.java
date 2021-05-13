
public class Arete {
	Sommet elem1;
	Sommet elem2;
	boolean preference=false;
	
	public Arete(Sommet elem1, Sommet elem2, boolean preference) {
		this.elem1 = elem1;
		this.elem2 = elem2;
		this.preference = preference;
	}
	public Arete(Sommet elem1, Sommet elem2) {
		this.elem1 = elem1;
		this.elem2 = elem2;
	}
	
	public Sommet getElem1() {
		return elem1;
	}
	public void setElem1(Sommet elem1) {
		this.elem1 = elem1;
	}
	public Sommet getElem2() {
		return elem2;
	}
	public void setElem2(Sommet elem2) {
		this.elem2 = elem2;
	}
	public boolean isPreference() {
		return preference;
	}
	public void setPreference(boolean preference) {
		this.preference = preference;
	}
	
	@Override
	public String toString() {
		return "Arete (" + elem1 + "," +  elem2 + "):" + preference;
	}
}