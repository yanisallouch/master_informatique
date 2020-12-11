
public interface Idico {

        Object get (Object key) throws Exception;
        
        Object put(Object key, Object value);
        
        boolean isEmpty();
        
        boolean containsKey(Object key);
        
}
