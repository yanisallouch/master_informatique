package patternComposite;

public abstract class Composant {
	public Composant parent;
	public int basicSize;
	public String name;
	
	public abstract int size();
	public abstract int nbElem();
	
	public String absoluteAdress() {
		if(parent == null) {
			return "";
		}else {
			Composant tmp = parent;
			return tmp.absoluteAdress()+"/"+name;
		}
	}
}