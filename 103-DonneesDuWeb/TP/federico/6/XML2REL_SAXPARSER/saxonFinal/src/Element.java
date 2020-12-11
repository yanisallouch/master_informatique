
public class Element {
	public String begin;
	public String parent;
	public String tag;
	
	public String text;
	
	
	
	public String end;
	public String nodTyp;
	
	public Element(String begin, String parent, String tag) {
		super();
		this.begin = begin;
		this.parent = parent;
		this.tag = tag;
		this.nodTyp = "element";
		this.text = null;
		
		
	}
	
	public Element(String begin, String end, String parent, String tag, String text) {
		super();
		this.begin = begin;
		this.parent = parent;
		this.tag = tag;
		this.text = text;
		this.nodTyp = "text";
		
		this.end = end;
		
		
	}
	
	public String toString() {
		
		
		return "INSERT INTO NODE VALUES(" + begin + ", " + end
				+ ", " + parent + ", " + tag
				+ ", '" + nodTyp + "', '" + text + "');\n";
		
	}


}
