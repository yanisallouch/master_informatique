/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	25 octobre 2020
*	@attribute HMIN104
*/

package colorGrap;

public class Sommet {
	String etiquette = "";
	int couleur = 0; // couleur commence a 1
	int degre = 0;
	int ordreDesactivation = 0; // la desactivation commence a 1

	public Sommet(String etiquette, int couleur, int degre) {
		this.etiquette = etiquette;
		this.couleur = couleur;
		this.degre = degre;
	}
	public Sommet(String etiquette, int couleur) {
		this.etiquette = etiquette;
		this.couleur = couleur;
	}
	public Sommet(String etiquette) {
		this.etiquette = etiquette;
	}
	public String getEtiquette() {
		return etiquette;
	}
	public void setEtiquette(String etiquette) {
		this.etiquette = etiquette;
	}
	public int getCouleur() {
		return couleur;
	}
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	public int getDegre() {
		return degre;
	}
	public void setDegre(int degre) {
		this.degre = degre;
	}
	
	public int getOrdreDesactivation() {
		return ordreDesactivation;
	}
	public void setOrdreDesactivation(int ordreDesactivation) {
		this.ordreDesactivation = ordreDesactivation;
	}
	public boolean isDesactiver() {
		return getOrdreDesactivation() == 0;
	}
	public boolean memeEtiquette(Sommet smt) {
		return this.getEtiquette().equals(smt.getEtiquette());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sommet other = (Sommet) obj;
		if (couleur != other.couleur)
			return false;
		if (degre != other.degre)
			return false;
		if (etiquette == null) {
			if (other.etiquette != null)
				return false;
		} else if (!etiquette.equals(other.etiquette))
			return false;
		if (ordreDesactivation != other.ordreDesactivation)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "s:" + getEtiquette() + ", c:" + (getCouleur()==-1?"spill":getCouleur()) + ", d:" + getDegre() ;
	}
}