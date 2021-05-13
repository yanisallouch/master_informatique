package patternComposite;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Composite extends Composant {
	public ArrayList<Composant> listeComposite;
	
	public abstract Collection<Composant> find(String name);
	public abstract Collection<Composant> findR(String name);
	
	@Override
	public int nbElem() {
		
		return 0;
	}
}