 package prePost;

public class Element {
	public Integer pre;
	public Integer post;
	public String tag;
	
	public String text;
	
	public String nodTyp;
	
	
	public Element(Integer pre, String tag) {
		super();
		this.pre = pre;
		this.tag = tag;
		this.nodTyp = "element";
		this.text = null;
	}
	
	public Element(String pre, String tag) {
		this(Integer.parseInt(pre),tag);		
	}
	

	
	public Element(String pre, String post, String tag, String text) {
		this(Integer.parseInt(pre), Integer.parseInt(post), tag, text);
	}
	
	public Element(Integer pre, Integer post, String tag, String text) {
		this(pre, tag);
		this.text = text;
		this.nodTyp = "text";
		
		this.post = post;
	}
	





	public String toString() {
		return "INSERT INTO NODE VALUES(" + pre + ", " + post
				+ ", " + sanitize(tag)
				+ ", " + sanitize(nodTyp) + ", " + sanitize(text) + ");\n";
		
	}
	
	private static String sanitize(String s) {
		if (s== null) return s;
		return "'"+s.replace("'", "''")+"'";
	}


}
