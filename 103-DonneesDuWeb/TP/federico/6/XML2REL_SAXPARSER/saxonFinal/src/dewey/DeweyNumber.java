package dewey;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringJoiner;

public class DeweyNumber extends Stack<Integer>{
	
	public String toString() {
		if (this.isEmpty()) return "null";
		StringJoiner joiner = new StringJoiner(".");
		for (int i: this) {
			joiner.add(Integer.toString(i));
		}
		return joiner.toString();
		
	}
	public void incr() {
		if (this.isEmpty()) System.out.println("Trying to increment an empty stack");
		this.push(this.pop()+1);
	}
	
	public Integer peek() {
		if (this.isEmpty()) return null;
		return super.peek();
	}
	
	public static void main(String[] args) {
		DeweyNumber test = new DeweyNumber();
		test.push(1);
		test.push(2);
		test.push(3);
		test.incr();
		System.out.println(test);
		System.out.println(test.pop());
		String s = null;
		System.out.println("aa".equals(s));
	}
}
