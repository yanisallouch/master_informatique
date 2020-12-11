package centre.culturel;

import java.util.ArrayList;
import cycle.seminaire.CycleSeminaires;

public class Archives {
	public ArrayList<CycleSeminaires> anciensSeminaires;
	public ArrayList<Adherent> adherents;
	
	public Archives(ArrayList<CycleSeminaires> anciensSeminaires, ArrayList<Adherent> adherents) {
		this.anciensSeminaires = anciensSeminaires;
		this.adherents = adherents;
	}

	public ArrayList<CycleSeminaires> getAnciensSeminaires() {
		return anciensSeminaires;
	}

	public void setAnciensSeminaires(ArrayList<CycleSeminaires> anciensSeminaires) {
		this.anciensSeminaires = anciensSeminaires;
	}

	public ArrayList<Adherent> getAdherents() {
		return adherents;
	}

	public void setAdherents(ArrayList<Adherent> adherents) {
		this.adherents = adherents;
	}
}