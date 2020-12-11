public class Sommet {
	String label;
	int poid=0;
	int couleur=-1;

	public Sommet(String label, int poid) {
		this.label = label;
		this.poid = poid;
	}
	public Sommet(String label) {
		this.label = label;
	}
	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getPoid() {
		return poid;
	}
	public void setPoid(int poid) {
		this.poid = poid;
	}
	public int getCouleur() {
		return couleur;
	}
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	@Override
	public String toString() {
		return label + ":" + couleur;
	}
}