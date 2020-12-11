package implementation;
import dico.*;;

public class FastDictionary extends AbstractDictionary {
	
	public FastDictionary(){
		super();
	}
	public FastDictionary(int size){
		super(size);
	}
	
	@Override
	public int indexOf(Object key) {
		int i = Math.abs(key.hashCode()%size);
		Object aKey = keyArray[i];
		if(aKey != null) {
			if(aKey.equals(key)) {
				return i;
			}else {
				int lap = 0;
				for(int k = i; k < size && lap < 2; k = (k+1)% size ) {
					if(keyArray[k] != null) {
						if(keyArray[k].equals(key)) {
							return k;
						}
					}
					if(k == size-1 ) {
						lap++;
					}
				}
			}
		}
		return -1;	
	}
	
	@Override
	public int newIndexOf(Object key) {
		int j = Math.abs(key.hashCode()%size);
		int lap = 0;
		for(int i = j; i < size && lap < 2; i = (i+1)%size) {
			if(keyArray[i] == null) {
				return i;
			}
			if(i == size -1) {
				lap++;
			}
		}
		return -1;
	}
	
	public String toString() {
		return "Fast" + super.toString();
	}
}
