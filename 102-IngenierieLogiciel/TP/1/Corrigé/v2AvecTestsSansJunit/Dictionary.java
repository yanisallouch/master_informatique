
package Dico;

/**L'interface Dictionary décrit la structure commune à tous les types de dictionnaires.<BR>
 *Ses méthodes sont les opérations de base disponibles pour la manipulation d'un dictionnaire.*/

public interface Dictionary {

    /**Cette méthode recherche un élément du dictionnaire
     *@param key clé de l'élément à rechercher
     *@return élément ou null si la clé est inconnue du receveur*/
    public Object get(Object key);

    /**Cette méthode insère un nouveau couple clé-valeur dans le receveur, si la clé est inconnue dans le receveur
     *@param key clé de l'élément à insérer
     *@param value valeur de l'élément à insérer
     *@return receveur de la méthode*/
    public Object put(Object key, Object value);

    /**Cette méthode indique si le receveur est vide
     *@return vrai si le receveur est vide, faux sinon*/
    public boolean isEmpty();

    /**Cette méthode indique si une clé est connue dans le receveur
     *@param key clé à recherche
     *@return vrai si la clé a été trouvée, faux sinon*/
    public boolean containsKey(Object key);
}

