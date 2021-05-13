

import java.io.Serializable;

public interface Espece extends Serializable {
	public String getNom();
	public void setNom(String nom);
	public double getDureeDeVie();
}