
package Dico;

/**La classe AbstractDictionary implémente l'interface Dictionary.<BR>
 *Elle implémente les méthodes de l'interface Dictionary et introduit trois autres méthodes.<BR>
 *La méthode newIndexOf est abstraite car elle est spécifique aux éventuelles sous-classes de AbstractDictionary.<BR>
 *Les méthodes indexOf et size sont implémentées par défaut et pourront être redéfinies au besoin dans les éventuelles sous-classes de AbstractDictionary.*/

public abstract class AbstractDictionary implements Dictionary {

    /**Tableau contenant les clés du dictionnaire*/    
    protected Object[] tabCle;
    /**Tableau contenant les valeurs correpondant aux clés du dictionnaire*/
    protected Object[] tabValeur;
    /**Taille des tableaux des clés et des valeurs*/
    protected int tailleTab;


    /**Ce constructeur vide définit un objet AbstractDictionary.*/
    public AbstractDictionary() {}
    
    
    /**Cette méthode renvoie le nombre de couples clé-valeur contenus dans le dictionnaire<BR>
     *Elle parcours le tableau des clés jusqu'à trouver une case à NULL
     *@return Nombre d'éléments contenus dans le dictionnaire*/
    protected int size() {
	int cpt = 0;
	int i = 0;
	while ( (i < tailleTab) && (tabCle[i] != null) ) {
	    cpt++;	
	    i++;
	}			
	return cpt;
    }

   
    /**Cette méthode renvoie l'index auquel est rangée la clé passée en paramètre dans le receveur<BR>
     *Elle effectue une recherche séquentielle<BR>
     *@param key clé recherchée dans le dictionnaire
     *@return index de la clé si elle a été trouvée, sinon -1*/
    protected int indexOf(Object key) {
	int ind = 0;
	while ( (ind < tailleTab) && (tabCle[ind] != key) ) 
	    ind++;	
	if (ind == tailleTab)
	    return -1;
	else
	    return ind;
    }


    /**Cette méthode renvoie l'index auquel sera rangé un nouveau couple clé-valeur dans le dictionnaire<BR>
     *Cet index est calculé en fonction de la clé passée en paramètre<BR>
     *Cette méthode ne doit être appelée que dans le cas ou le dictionnaire ne contient pas déjà cette clé<BR>
     *S'il n'y a pas assez de place dans le dictionnaire pour l'insertion d'un nouvel élément, newIndexOf remplace les tableaux des clés et des valeurs pas des tableaux plus grands
     *@param key clé de l'élément à insérer
     *@return index du nouvel élément dans le receveur*/
    abstract protected int newIndexOf(Object key);	

   
    /**Cette méthode recherche un élément du dictionnaire
     *@param key clé de l'élément à rechercher
     *@return élément ou null si la clé est inconnue du receveur*/
    public Object get(Object key) {		
	int ind = indexOf(key);
	if (ind == -1)
	    return null;
	else
	    return tabValeur[ind]; 
    }


    /**Cette méthode insère un nouveau couple clé-valeur dans le receveur, si la clé est inconnue dans le receveur
     *@param key clé de l'élément à insérer
     *@param value valeur de l'élément à insérer
     *@return receveur de la méthode*/
    public Object put(Object key, Object value) {	
	if (!containsKey(key)) {
	    int i = newIndexOf(key);
	    tabCle[i] = key;
	    tabValeur[i] = value;
	}				
	return this;
    }

    
    /**Cette méthode indique si le receveur est vide
     *@return vrai si le receveur est vide, faux sinon*/
    public boolean isEmpty() {
	if (size() == 0)
	    return true;
	else 
	    return false;
    }

  
    /**Cette méthode indique si une clé est connue dans le receveur
     *@param key clé à recherche
     *@return vrai si la clé a été trouvée, faux sinon*/
    public boolean containsKey(Object key) {
	for (int i = 0 ; i < tailleTab ; i++)
	    if (tabCle[i] == key)
		return true;
	return false;
    }	



    public void affiche() { 
	System.out.println("\n*************************************************************************");
	System.out.println("Nombre de définitions : " + size());
	System.out.println("Taille du dictionnaire : " + tailleTab);
	for (int i = 0 ; i < tailleTab ; i++)
	    System.out.println("Index " + i + " : (" + tabCle[i] + " ; " + tabValeur[i] + ")");	
	System.out.println("*************************************************************************");
    }



    /*Test d'insertion d'un élément dans le dictionnaire
      1) CRITERES

      PRECONDITIONS:
      -dictionnaire vide (DV) / dictionnaire non vide (DNV) 
      -élément dans dictionnaire (ED) / élément non dans dictionnaire (END)
      POSTCONDITION:
      -élément inséré (EI) / élément non inséré (ENI)

      2) CLASSES D'EQUIVALENCE

      POTENTIELLES : (DV DNV) * (ED END) * (EI ENI) 
      EFFECTIVES : DV*END*EI ; DNV*END*EI ; DNV*ED*ENI 
    */

    /*Test de recherche d'un élément dans le dictionnaire
      1) CRITERES
      
      PRECONDITIONS:
      -dictionnaire vide (DV) / dictionnaire plein (DNV)      
      POSTCONDITION:
      -élément trouvé (ET) / élément non trouvé (ENT)

      2) CLASSES D'EQUIVALENCE

      POTENTIELLES : (DV DV) * (ET ENT)  
      EFFECTIVES : DV*ENT ; DNV*ET ; DNV*ENT 
    */

    protected int testBoiteNoire() {	
	int retour = 0;

	//TEST N°1:
	//Recherche : DV * ENT
	//Insertion : DV * END * EI
	System.out.println("\nTEST N°1:"); 
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire vide");
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire vide");	
	if (this.isEmpty()) {
	    if (this.containsKey("Platon")) {
		System.out.println("Erreur : Le dictionnaire est vide et ne peut donc pas contenir l'élément");
		System.out.println("TEST 1 ..............................................................[FAILED]");
		retour =  -1;
	    }
	    else {
		if (this.get("Platon") != null) {
		    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
		    System.out.println("TEST 1 Recherche ....................................................[FAILED]"); 
		    retour = -1;	
		}		
		else
		    System.out.println("TEST 1 Recherche ....................................................[OK]"); 		    
		this.put("Platon", "Philosophe grec");
		if (this.containsKey("Platon")) 
		    System.out.println("TEST 1 Insertion ....................................................[OK]");	
		else {
		    System.out.println("Erreur : L'élément n'a pas été inséré");
		    System.out.println("TEST 1 Insertion ....................................................[FAILED]"); 
		    retour = -1;	
		}
	    }
	}
	else {
	    System.out.println("Erreur : Le dictionnaire devrait être vide");
	    System.out.println("TEST 1 ..............................................................[FAILED]");  
	    retour = -1;	
	}

	//TEST N°2:
	//Recherche : DNV * ENT 
	//Insertion : DNV * END * EI
	System.out.println("\nTEST N°2 :");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire non vide");
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire non vide\nL'élément est inconnu du dictionnaire");
	if (this.isEmpty()) {
	    System.out.println("Erreur : Le dictionnaire ne devrait pas être vide");
	    System.out.println("TEST 2 ..............................................................[FAILED]");  
	    retour = -1;	
	}
	else {
	    if (this.containsKey("Euclide")) {
		System.out.println("Erreur : Le dictionnaire ne devrait pas contenir l'élément");
		System.out.println("TEST 2 ..............................................................[FAILED]");
		retour = -1;
	    }
	    else {
		if (this.get("Euclide") != null) {
		    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
		    System.out.println("TEST 2 Recherche ....................................................[FAILED]"); 
		    retour = -1;	
		}
		else
		    System.out.println("TEST 2 Recherche ....................................................[OK]"); 	
		this.put("Euclide", "Mathématicien grec");
		if (this.containsKey("Euclide")) 
		    System.out.println("TEST 2 Insertion ....................................................[OK]");	
		else {
		    System.out.println("Erreur : L'élément n'a pas été inséré");
		    System.out.println("TEST 2 Insertion ....................................................[FAILED]");	
		    retour = -1;
		}
	    }
	}

	
	//TEST N°3 : 
	//Recherche : DNV * ET
	//Insertion : DNV * ED * ENI
	System.out.println("\nTEST N°3 :");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire non vide");
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire non vide\nL'élément est connu du dictionnaire");
	if (this.isEmpty()) {
	    System.out.println("Erreur : Le dictionnaire ne devrait pas être vide");
	    System.out.println("TEST 3 ..............................................................[FAILED]");  
	    retour = -1;	
	}
	else {
	    if (this.containsKey("Platon")) {
		if ((String)this.get("Platon") != "Philosophe grec") {
		    System.out.println("Erreur : L'élément aurait du être trouvé");
		    System.out.println("TEST 3 Recherche ....................................................[FAILED]"); 
		    retour = -1;	
		}
		else
		    System.out.println("TEST 3 ..............................................................[OK]");	
	    }
	    else {
		System.out.println("Erreur : Le dictionnaire devrait contenir l'élément");
		System.out.println("TEST 3 ..............................................................[FAILED]");	
		retour = -1;
	    }
	}
	    
	return retour;
    }	


    abstract protected int testBoiteBlanche();

    public void test() {
	System.out.println("\n*************************************************************************");
	System.out.println("TESTS BOITE NOIRE...");
	if (testBoiteNoire() == 0)
	    System.out.println("TESTS BOITE NOIRE ...................................................[OK]");
	else
	    System.out.println("TESTS BOITE NOIRE ...................................................[FAILED]");
	System.out.println("*************************************************************************");
		System.out.println("\n*************************************************************************");
	System.out.println("TESTS BOITE BLANCHE ...");
	if (testBoiteBlanche() == 0)
	    System.out.println("TESTS BOITE BLANCHE .................................................[OK]");
	else
	    System.out.println("TESTS BOITE BLANCHE .................................................[FAILED]");
	System.out.println("*************************************************************************");
    }
}
