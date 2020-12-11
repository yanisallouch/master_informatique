 package dewey;

public class Element {
	public String tag;
	public String text;
	public String nodTyp;
	public String id;
	
	
	public Element(String id, String tag, String type, String text) {
		this.id = id;
		this.tag = tag;
		this.nodTyp = type;
		this.text = text;
	}





	public String toString() {
		return "INSERT INTO NODE VALUES(" + sanitize(id) + ", " + sanitize(tag)
				+ ", " + sanitize(nodTyp) + ", " + sanitize(text) + ");\n";
		
	}
	
	private static String sanitize(String s) {
		if (s== null) return s;
		return "'"+s.replace("'", "''")+"'";
	}


}
