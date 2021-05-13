package implementation;

import java.util.Collection;

import patternComposite.Composant;
import patternComposite.Composite;

public class Directory extends Composite {
	
	public void ls() {
	}
	
	@Override
	public int size() {
		
		return 0;
	}

	@Override
	public Collection<Composant> find(String name) {
		
		return null;
	}

	@Override
	public Collection<Composant> findR(String name) {
		
		return null;
	}

}
