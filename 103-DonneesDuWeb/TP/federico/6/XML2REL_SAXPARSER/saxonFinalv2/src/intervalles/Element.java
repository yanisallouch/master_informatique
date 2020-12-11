package intervalles;

public class Element {
	public Integer begin;
	public Integer parent;
	public String tag;
	
	public String text;
	
	
	
	public Integer end;
	public String nodTyp;
	
	
	public Element(Integer begin, Integer parent, String tag) {
		super();
		this.begin = begin;
		this.parent = parent;
		this.tag = tag;
		this.nodTyp = "element";
		this.text = null;
	}
	
	public Element(String begin, String parent, String tag) {
		this(Integer.parseInt(begin), Integer.parseInt(parent), tag);
	}
	
	
	public Element(Integer begin, Integer end, Integer parent, String tag, String text) {
		this(begin, parent, tag);
		this.text = text;
		this.nodTyp = "text";
		this.end = end;
	}
	
	public Element(String begin, String end, String parent, String tag, String text) {
		this(Integer.parseInt(begin), Integer.parseInt(parent),Integer.parseInt(parent), tag, text);
		}
	





	public String toString() {
//		String text = null;
//		if (this.text != null) {
//			text = this.text.replace("'","''"); //doubling quotes is the escape method for SQL
//			text = "'"+text+"'";
//		}
//		String tag = null;
//		if (this.tag != null) {
//			tag = this.tag.replace("'","''"); //doubling quotes is the escape method for SQL
//			tag = "'"+tag+"'";
//		}
//		
//		String nodTyp = null;
//		if (this.nodTyp != null) {
//			tag = this.nodTyp.replace("'","''"); //doubling quotes is the escape method for SQL
//			tag = "'"+nodTyp+"'";
//		}
		return "INSERT INTO NODE VALUES(" + begin + ", " + end
				+ ", " + parent + ", " + sanitize(tag)
				+ ", " + sanitize(nodTyp) + ", " + sanitize(text) + ");\n";
		
	}
	
	private static String sanitize(String s) {
		if (s== null) return s;
		return "'"+s.replace("'", "''")+"'";
	}


}
