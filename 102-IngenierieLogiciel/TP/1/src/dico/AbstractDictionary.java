package dico;

public abstract class AbstractDictionary implements IDictionary {
	public Object[] keyArray;
	public Object[] valueArray;
	public int size = 0;
	
	public AbstractDictionary() {
		this.size = 1;
		keyArray = new Object[size];
		valueArray = new Object[size];
	}
	public AbstractDictionary(int size) {
		this.size = size;
		keyArray = new Object[size];
		valueArray = new Object[size];
	}
	
	private int nbElements() {
		int resultat = 0; //
		for(int i = 0; i < size; i++) {
			if(!(keyArray[i] == null)) {
				resultat++;
			}				
		}
		return resultat;
	}
	
	public boolean mustGrow() {
		return (nbElements()>=0.75*size);
	}
	
	public String toString() {
		return "Dictionary total:real " + size + ":" + nbElements();
	}
	
	public boolean isEmpty() {
		return nbElements() == 0;
	}
	
	public int size() {
		return nbElements();
	}
	
	public int indexOf(Object key) {
		for(int i = 0; i < size; i++) {
			if(keyArray[i] != null) {
				if(keyArray[i].equals(key)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public int newIndexOf(Object key) {
		for(int i = 0; i < size; i++) {
			if(keyArray[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean grow() {
		if(mustGrow()) {
			int biggerSize = size *2;
			Object[] tmpKeyArray = new Object[size];
			Object[] tmpValueArray = new Object[size];
			for(int i = 0; i< size; i++) {
				if(keyArray[i] != null) {
					tmpKeyArray[i] = keyArray[i];
					tmpValueArray[i] = valueArray[i];
				}
			}
			keyArray = new Object[biggerSize];
			valueArray = new Object[biggerSize];
			for(int i = 0; i < size; i++) {
				if(tmpKeyArray[i] != null) {
					put(tmpKeyArray[i],tmpValueArray[i]);
				}
			}
			size = biggerSize;
			return true;
		}
		return false;
	}
	
	public Object get(Object key) {
		int i = indexOf(key);
		if(i >= 0) {
			if(key.equals(keyArray[i])) {
				return valueArray[i];
			}else {
				int lap = 0;
				for(int k = 0; k < size && lap < 2; k = (k+1)% size ) {
					if(keyArray[k].equals(key)) {
						return valueArray[k];
					}else if(k == size-1 ) {
						lap++;
					}
				}
			}
		}
		return null;
	}
	
	public Object get(int i) {
		return valueArray[i];
	}
	
	public boolean containsKey(Object key) {
		Object tmp = get(key);
		// je pourrais me passer du equals quand je m'assure que l'index correspond a la clé
		return (tmp != null);
	}
	
	public IDictionary put(Object key, Object value) {
		if(!containsKey(key)) {
			grow();
			int i = newIndexOf(key);
			keyArray[i] = key;
			valueArray[i] = value;
			return this;
		}
		return this;
	}
}
