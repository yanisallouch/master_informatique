package spl.sobre;

public class Node {
	public String label = "undefined";
	public String color = "none";
	public String shape = "";
		
	public Node() {
		// TODO Auto-generated constructor stub
	}
	
	public Node(String label) {
		this.label = label;
	}
	
	public Node(String label, String color) {
		this.label = label;
		this.color = color;
	}
	
	public Node(String label, String color, String shape) {
		this.label = label;
		this.color = color;
		this.shape = shape;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}

	@Override
	public String toString() {
		return "\"" + label + "\" [color=\"" + color + "\", shape=\"" + shape + "\"] ;";
	}
}