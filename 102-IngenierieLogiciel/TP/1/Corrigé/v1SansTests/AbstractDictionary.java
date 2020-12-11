public abstract class AbstractDictionary implements Idico {

	protected Object[] keys;
	protected Object[] values;
	
	protected AbstractDictionary(int n) {
		keys = new Object[n];
		values = new Object[n];
	}

	public int size() {
		return keys.length;
	}

	abstract int newIndexOf(Object key);

	abstract int indexOf(Object key);

	public Object get(Object key) throws Exception {
		int i = this.indexOf(key);
		if (i != -1)
			return values[i];
		else
			throw 
			new Exception("Cette clé n'est pas dans le dictionnaire: " + key);
	}

	public Object put(Object key, Object value) {
		int j = indexOf(key);
		if (j == -1) {
			int i = this.newIndexOf(key);
			keys[i] = key;
			values[i] = value;
		} else values[j] = value;
		return this;
	}

	public boolean isEmpty() {
		return (this.size() == 0);
	}

	public boolean containsKey(Object key) {
		return (indexOf(key) == -1);
	}

	public String toString() {
		String s = "taille du tableau: " + keys.length + "\n";
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) {
				s = s + keys[i].toString() + " -> " + values[i].toString()
						+ "\n";
			}
		}
		return s;
	}

	public static void main(String[] args) throws Exception {

		// AbstractDictionary OD = new SortedDictionary();
		AbstractDictionary OD = new FastDictionary();
		// AbstractDictionary OD = new OrderedDictionary(2);

		OD.put("Lavoisier", "Chimiste fran<E7>ais ...");
		System.out.println(OD.get("Lavoisier"));

		System.out.println("size " + OD.size());

		OD.put("Autre", "Autre Chimiste ...");
		System.out.println(OD.get("Autre"));

		OD.put("Autre", "Autre Chimiste 2 ...");
		System.out.println(OD.get("Autre"));

		OD.put("AutreBis", "Un Chimiste Bis ...");
		System.out.println(OD.get("AutreBis"));

		OD.put("D", "Autre Chimiste D ...");
		System.out.println(OD.get("D"));

		OD.put("Z", "Autre Chimiste Z ...");
		System.out.println(OD.get("Z"));

		OD.put("X", "Autre Chimiste X ...");
		System.out.println(OD.get("X"));

		OD.put("P", "Autre Chimiste P ...");
		System.out.println(OD.get("P"));

		OD.put("A", "Autre Chimiste A ...");
		System.out.println(OD.get("D"));

		OD.put("A", "Autre Chimiste A ...");
		System.out.println(OD.get("A"));

		System.out.println("size " + OD.size());

		System.out
				.println("*************************************************************");
		System.out.println(OD);

	}

}
