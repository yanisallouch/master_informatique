package mockito;

public class Pair<T1, T2> {
	public T1 e1;
	public T2 e2;
	
	public Pair() {
		e1=null;
		e2=null;
	}
	
	public Pair(T1 e1, T2 e2) {
		this.e1=e1;
		this.e2=e2;
	}

	public T1 getKey() {
		// TODO Auto-generated method stub
		return e1;
	}

	public T2 getValue() {
		// TODO Auto-generated method stub
		return e2;
	}
}
