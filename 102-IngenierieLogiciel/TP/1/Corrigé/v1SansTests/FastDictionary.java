public class FastDictionary extends AbstractDictionary {

	public FastDictionary() {
		super(4);
	}

	public FastDictionary(int n) {
		super(n);
	}

	public int size() {
		int s = 0;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) s++;
		}
		return s;
	}

	boolean mustGrow() {
		float i = this.size();
		float j = keys.length;
		if (j == 0)
			return true;
		else
			return ((i / j) > 0.7);
	}

	public void grow() {
		Object[] oldKeys = keys;
		Object[] oldValues = values;
		keys = new Object[oldKeys.length + 5];
		values = new Object[oldKeys.length + 5];

		for (int i = 0; i < oldKeys.length; i++) {
			if (oldKeys[i] != null) {
				this.put(oldKeys[i], oldValues[i]);
			}
		}
		System.out.println("taille du tableau: " + keys.length + "\n");
	}

	public int indexOf(Object key) {
		int hash = key.hashCode();
		if (hash < 0) hash = -1 * hash;
		int i = hash % keys.length;
		while ((!(key.equals(keys[i]))) && (keys[i] != null)) {
			i = (i + 1) % keys.length;
		}
		if (keys[i] == null) return -1;
		else return i;
	}

	public int newIndexOf(Object key) {
		if (this.mustGrow()) this.grow();
		int hash = key.hashCode();
		if (hash < 0) hash = -1 * hash;
		int i = hash % keys.length;
		if (keys[i] == null) return i;
		else {
			do {
				if (i + 1 < keys.length)
					i++;
				else
					i = 0;
			} while (keys[i] != null);
			return i;
		}
	}

}
