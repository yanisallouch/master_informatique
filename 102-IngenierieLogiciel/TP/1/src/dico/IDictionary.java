package dico;

public interface IDictionary {
    public Object get(Object key);
    public IDictionary put(Object key, Object value);
    boolean isEmpty();
    boolean containsKey(Object key);
    int size();
}