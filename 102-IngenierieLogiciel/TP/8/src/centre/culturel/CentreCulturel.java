package centre.culturel;

import java.util.ArrayList;
import cycle.seminaire.CycleSeminaires;

public class CentreCulturel {
	public ArrayList<Archives> archives;
	
	public CentreCulturel (ArrayList<Archives> archives) {
		this.archives = archives;
	}

	public CycleSeminaires creationCycleSeminaires(String titre, String resume, Creneau[] crenaux ) {
		
		return new CycleSeminaires(titre, resume, 0, 0);
	}
	
}