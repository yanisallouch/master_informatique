public class SortedDictionary extends OrderedDictionary {
	// protected Comparable[] keys; En Java, on ne peut pas red<E9>finir un
	// attribut. cf commentaire ci-dessous.

	/**
	 * Pour le SortedDictionary il faut que les <E9>lts soient comparables afin
	 * d'<E9>tablir un ordre. - Soit on red<E9>finit l'attribut keys
	 * "protected Comparable [] keys" et le param<E8>tre key de la m<E9>thode
	 * "int newIndexOf(Obkect key)" peut rester un Object en faisant les
	 * comparaisons entre cl<E9>s comme suit : keys[i].compareTo(key) (cf. #).
	 * 
	 * --> Attention : Apr<E8>s test, il appara<EE>t que les attributs ne sont
	 * pas red<E9>finissable en JAVA mais seulement surchargeable. Avec ce qui
	 * est <E9>nonc<E9> ci-dessus, on se retrouve avec deux tableaux "keys" : un
	 * tableau d'objets et un tableau de comparable. A priori, la solution
	 * ci-dessus aurait par contre <E9>t<E9> faisable en C++.
	 * 
	 * - Soit on ne red<E9>finit pas l'attribut "keys[]" et il faut <E9>crire la
	 * m<E9>thode "newIndexOf" ainsi : int newIndexOf(Object key) { Comparable
	 * comp = (Comparable) key; ... comp.compareTo(keys[i]); ... }
	 * 
	 * # int compareTo(Object); --> Interface Comparable
	 * 
	 */

	int newIndexOf(Object key) {
		//la clé doit être un élément de type comparable mais impossible de changer la signature de la méthode
		Comparable cKey = (Comparable) key;
		//création de nouveaux tableaux plus grands de 1
		Object[] newKeys = new Object[keys.length + 1];
		Object[] newValues = new Object[keys.length + 1];
		int i = 0, ret = 0;
		//recherche de la position de la dernière clé plus petite que ckey
		while (i < keys.length && cKey.compareTo(keys[i]) > 0) { // cKey > keys[i]
			newKeys[i] = keys[i];
			newValues[i] = values[i];
			i++;
		}
		//ret est l'indice auquel se fera l'insertion
		ret = i;
		newKeys[i] = null;
		newValues[i] = null;
		//recopie des autres éléments
		if (i != keys.length) {
			for (int j = i + 1; j < newKeys.length; j++) {
				newKeys[j] = keys[j - 1];
				newValues[j] = values[j - 1];
			}
		}
		keys = newKeys;
		values = newValues;
		return ret;
	}
}
