package centre.culturel;


public class Creneau {
	public String debut;
	public String fin;
	
	public Creneau(String debut, String fin) {
		this.debut = debut;
		this.fin = fin;
	}

	public String getDebut() {
		return debut;
	}

	public void setDebut(String debut) {
		this.debut = debut;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}
}
