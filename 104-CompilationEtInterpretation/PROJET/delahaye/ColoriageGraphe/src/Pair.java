
public class Pair<T1, T2> {
	T1 sommet;
	T2 entier;

	public Pair(T1 element1, T2 element2) {
		super();
		this.sommet = element1;
		this.entier = element2;
	}

	public T1 getSommet() {
		return sommet;
	}
	public void setSommet(T1 element1) {
		this.sommet = element1;
	}
	public T2 getEntier() {
		return entier;
	}
	public void setEntier(T2 element2) {
		this.entier = element2;
	}
}