public class OrderedDictionary extends AbstractDictionary {

	public OrderedDictionary() {
		super(0);
	}



	public int size() {
		int s = 0;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null)
				s++;
		}
		return s;
	}

	public int indexOf(Object key) {
		for (int i = 0; i < keys.length; i++) {
			if (key.equals(keys[i]))
				return i;
		}
		return -1;
	}

	int newIndexOf(Object key) {
		int size = this.size();
		if (size == keys.length) {
			Object[] newKeys = new Object[keys.length + 1];
			Object[] newValues = new Object[keys.length + 1];

			for (int i = 0; i < keys.length; i++) {
				newKeys[i] = keys[i];
				newValues[i] = values[i];
			}

			keys = newKeys;
			values = newValues;
			return keys.length - 1;} 
		else return size;
		
	}

}
