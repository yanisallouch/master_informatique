
package Dico;

/**La classe FastDictionary spécialise la classe AbstractDictionary<BR>
 *Cette classe est une spécialisation du framework défini pas l'interface Dictionary et la classe AbstractDictionary<BR>
 *Dans ce type de dictionnaire, les couples clé-valeur sont rangés et retrouvés à l'aide d'une technique de hachage*/

public class FastDictionary extends AbstractDictionary {
    
    /**Ce constructeur définit un objet FastDictionary avec une taille de dictionnaire de 10 par défaut*/
    public FastDictionary() {
	tailleTab = 10;	
	tabCle = new Object[tailleTab];
	tabValeur = new Object[tailleTab];
    }

    
    /**Ce constructeur définit un objet FastDictionary avec une taille de dictionaire passée en paramètre
     *@param t taille du dictionnaire*/
    public FastDictionary(int t) {
	tailleTab  = t;
	tabCle = new Object[tailleTab];
	tabValeur = new Object[tailleTab];
    }

    
    /**Cette méthode renvoie le nombre de couples clé-valeur contenus dans le dictionnaire<BR>
     *Cette méthode est une redéfinition de la méthode size() de la classe AbstractDictionary qui supposait les couples tassés à gauche dans le dictionnaire. Un dictionnaire de type FastDictionary doit être parcouru en entier car il contient des trous
     *@return Nombre d'éléments contenus dans le dictionnaire*/
    protected int size() {	
	int cpt = 0;
	for (int i = 0 ; i < tailleTab ; i++) {
	    if (tabCle[i] != null)
		cpt++;	
	}			
	return cpt;
    }


    /**Cette méthode renvoie l'index auquel est rangée la clé passée en paramètre dans le receveur<BR>
     *Cette méthode est une redéfinition de la méthode indexOf de la classe AbstractDictionary qui effectuait une recherche séquentielle. Pour un dictionnaire de type FastDictionary, l'index correspondant à la clé passée en paramètre est retrouvé à l'aide de la méthode de hachage
     *@param key clé recherchée dans le dictionnaire
     *@return index de la clé si elle a été trouvée, sinon -1*/
    protected int indexOf(Object key) {
	if (tailleTab == 0)
	    return -1;
	int index = key.hashCode() % tailleTab;
	if (index < 0)
	    index = (-1) * index;
	int i = index;
	if (tabCle[index] != key) {
	    do {
		i++; 
		if (i == tailleTab)
		    i =  0;
	    } while( (i != index) && (tabCle[i] != key));
	  
	    if (i == index)
		return -1;
	    else
		return i;
	}
	else
	    return index;
    } 


    /**Cette méthode renvoie l'index auquel sera rangé un nouveau couple clé-valeur dans le dictionnaire<BR>
     *Cet index est calculé à l'aide d'une technique de hachage : la méthode hashCode() est appliquée à la clé modulo la taille du dictionnaire<BR>
     *Si un couple clé-valeur est déjà rangé dans le dictionnaire à cette position, le dictionnaire est parcouru de manière séquentielle jusqu'à obtenir l'index d'une case vide<BR>  
     *Cette méthode ne doit être appelée que dans le cas ou le dictionnaire ne contient pas déjà cette clé<BR>
     *Si le dictionnaire est aux 3/4 plein, la taille du dictionnaire est augmentée de 25% par rapport à sa taille initiale, de manière à éviter au maximum les conflits de hachage
     *@param key clé de l'élément à insérer
     *@return index du nouvel élément dans le receveur*/
    protected int newIndexOf(Object key) {
	if (mustGrow())
	    grow();

	int index = key.hashCode() % tailleTab;
	if (index < 0)
	    index = (-1) * index;
	
	while (tabCle[index] != null) {
	    index++;
	    if (index == tailleTab)
		index =  0;
	}

	return index;
    }

   
    /**Cette méthode indique si le dictionnaire doit être agrandi ou non
     *@return vrai si le receveur est aux 3/4 plein, faux sinon*/ 
    protected boolean mustGrow() {
	if (size() + 1 > (tailleTab * 3) / 4) 
	    return true;       
	else
	    return false;
    }

    
    /**Cette méthode permet d'augmenter la taille du ditionnaire de 25%<BR>
     *Les clés et les valeurs sont réinsérées dans les nouveaux tableaux en utilisant une technique de hachage<BR>
     *L'index d'un couple est calculé en appliquant la méthode hashCode() à la clé modulo la taille du dictionnaire<BR>
     *Si un couple clé-valeur est déjà rangé dans le dictionnaire à cette position, le dictionnaire est parcouru de manière séquentielle jusqu'à obtenir l'index d'une case vide*/
    protected void grow() {
	int fin = tailleTab;
	if (tailleTab % 4 == 0)
	    tailleTab = tailleTab + tailleTab / 4;
	else
	    tailleTab = tailleTab + tailleTab / 4 + 1;
	if (tailleTab == 0)
	    tailleTab = 2;
  
	Object[] newTabCle = new Object[tailleTab];
	Object[] newTabValeur = new Object[tailleTab];	    

	for (int i = 0 ; i < fin ; i++) {
	    if (tabCle[i] != null) {
		int index = tabCle[i].hashCode() % tailleTab;
		if (index < 0)
		    index = (-1) * index;

		while (newTabCle[index] != null) {
		    index++;
		    if (index == tailleTab)
			index =  0;
		}

		newTabCle[index] = tabCle[i];
		newTabValeur[index] = tabValeur[i];
	    }
	}
	
	tabCle = newTabCle;
	tabValeur = newTabValeur;	
    }



    /*Test d'insertion d'un élément dans le dictionnaire
      1) CRITERES

      PRECONDITIONS:
      -dictionnaire de taille 0 (T0) / dictionnaire de taille 1 (T1) / dictionnaire de taille quelconque (TN) 
      -0 élément dans le dictionnaire (D0) / 1 élément dans le dictionnaire (D1) / n*3/4 éléments dans le dictionnaire (DN3/4)
      -élément dans dictionnaire (ED) / élément non dans dictionnaire (END)
      POSTCONDITION:
      -élément inséré (EI) / élément non inséré (ENI)

      2) CLASSES D'EQUIVALENCE

      POTENTIELLES : (T0 T1 TN) * (D0 D1 DN3/4) * (ED END) * (EI ENI)
      EFFECTIVES : T0*D0*END*EI ; 
                   T1*D0*END*EI ; T1*D1*ED*ENI ; T1*D1*END*EI ;
		   TN*D0*END*EI ; TN*D1*ED*ENI ; TN*D1*END*EI ; TN*DN3/4*ED*ENI ; TN*DN3/4*END*EI 
    */

    /*Test de recherche d'un élément dans le dictionnaire
      1) CRITERES
      
      PRECONDITIONS: 
      -dictionnaire de taille 0 (T0) / dictionnaire de taille 1 (T1) / dictionnaire de taille quelconque (TN) 
      -0 élément dans le dictionnaire (D0) / 1 élément dans le dictionnaire (D1) / n*3/4 éléments dans le dictionnaire (DN3/4)
       POSTCONDITION:
      -élément trouvé (ET) / élément non trouvé (ENT)

      2) CLASSES D'EQUIVALENCE

      POTENTIELLES : (T0 T1 TN) * (D0 D1 DN3/4) * (ET ENT)
      EFFECTIVES : T0*D0*ENT ;
                   T1*D0*ENT ; T1*D1*ET ; T1*D1*ENT ;
		   TN*D0*ENT ; TN*D1*ENT ; TN*D1*ET ; TN*DN3/4*ENT ; TN*DN3/4*ET
    */
 
    protected int testBoiteBlanche() {
	int retour = 0;

	//TEST N°1:
	//Insertion : T0*D0*END*EI 
	//Recherche : T0*D0*ENT
	System.out.println("\nTEST N°1:"); 
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire vide \nde taille 0");
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire vide \nde taille 0");
	FastDictionary dico = new FastDictionary(0);
	if (dico.get("Platon") != null) {
	    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
	    System.out.println("TEST 1 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 1 Recherche ....................................................[OK]");    
	dico.put("Platon", "Philosophe grec");
	if (!dico.containsKey("Platon")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 1 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    if (dico.tailleTab != 2) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 1 Insertion ....................................................[FAILED]");
		retour = -1;
	    }
	    else
		System.out.println("TEST 1 Insertion ....................................................[OK]");	
	}


	//TEST N°2:
	//Insertion : T1*D0*END*EI  
	//Recherche : T1*D0*ENT
	System.out.println("\nTEST N°2:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire vide \nde taille 1");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire vide \nde taille 1");
	dico = new FastDictionary(1);
	if (dico.get("Platon") != null) {
	    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
	    System.out.println("TEST 2 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 2 Recherche ....................................................[OK]");   
	dico.put("Platon", "Philosophe grec");
	if (!dico.containsKey("Platon")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 2 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true; 
	    if (dico.tailleTab != 2) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 2 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (dico.size() != 1) {
		System.out.println("Erreur : Le dictionnaire devrait contenir un seul élément");
		System.out.println("TEST 2 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 2 Insertion ....................................................[OK]");	
	}

		
		
	//TEST N°3:
	//Insertion : T1*D1*ED*ENI
	//Recherche : T1*D1*ET
	System.out.println("\nTEST N°3:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille 1\nLe dictionnaire contient 1 élément, l'élément à insérer est connu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille 1\nLe dictionnaire contient 1 élément\nL'élément se trouve en position 0");
	if (dico.get("Platon") == null) {
	    System.out.println("Erreur : L'élément aurait du être trouvé");
	    System.out.println("TEST 3 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 3 Recherche ....................................................[OK]");   
	if (!dico.containsKey("Platon")) {
	    System.out.println("Erreur : L'élément devrait se trouver dans le ditionnaire");
	    System.out.println("TEST 3 ..............................................................[FAILED]");
	    retour = -1;
	}
	else
	    System.out.println("TEST 3 Insertion ....................................................[OK]");	


	//TEST N°4:
	//Insertion : T1*D1*END*EI
	//Recherche : T1*D1*ENT
	System.out.println("\nTEST N°4:"); 
	System.out.println("Essai d'insertion d'un élément dans un dictionnaire de taille 1\nLe dictionnaire contient 1 élément, l'élément à insérer est inconnu");
	System.out.println("Essai de recherche d'un élément dans un dictionnaire de taille 1\nLe dictionnaire contient 1 élément\nL'élément ne se trouve pas dans le dictionnaire");
	if (dico.get("Euclide") != null) {
	    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
	    System.out.println("TEST 4 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 4 Recherche ....................................................[OK]");   
	dico.put("Euclide", "Mathématicien grec");
	if (!dico.containsKey("Euclide")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 4 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true;
	    if (dico.tailleTab != 3) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 4 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (dico.size() != 2) {
		System.out.println("Erreur : Le nombre d'éléments est incorrect");
		System.out.println("TEST 4 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 4 Insertion ....................................................[OK]");	
	}


	//TEST N°5:
	//Insertion : TN*D0*END*EI
	//Recherche : TN*D0*ENT
	System.out.println("\nTEST N°5:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire vide \nde taille n");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire vide \nde taille n");
	dico = new FastDictionary(4);
	if (dico.get("Platon") != null) {
	    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
	    System.out.println("TEST 5 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 5 Recherche ....................................................[OK]");   
	dico.put("Platon", "Philosophe grec");
	if (!dico.containsKey("Platon")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 5 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true;
	    if (dico.tailleTab != 4) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 5 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (dico.size() != 1) {
		System.out.println("Erreur : Le nombre d'éléments est incorrect");
		System.out.println("TEST 5 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 5 Insertion ....................................................[OK]");	
	}


	//TEST N°6:
	//Insertion : TN*D1*ED*ENI
	//Recherche : TN*D1*ET 
	System.out.println("\nTEST N°6:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient 1 élément, l'élément à insérer est connu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient 1 élément\nL'élément recherché est connu");
	if (dico.get("Platon") == null) {
	    System.out.println("Erreur : L'élément aurait du être trouvé");
	    System.out.println("TEST 6 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 6 Recherche ....................................................[OK]");   

	if (!dico.containsKey("Platon")) {
	    System.out.println("Erreur : L'élément devrait se trouver dans le dictionnaire");
	    System.out.println("TEST 6 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else
	    System.out.println("TEST 6 Insertion ....................................................[OK]");	
    

	//TEST N°7:
	//Insertion : TN*D1*END*EI
	//Recherche : TN*D1*ENT 
	System.out.println("\nTEST N°7:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient 1 élément, l'élément à insérer est inconnu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient 1 élément");
	if (dico.get("Euclide") != null) {
	    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
	    System.out.println("TEST 7 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 7 Recherche ....................................................[OK]"); 
	dico.put("Euclide", "Mathématicien grec");  
	if (!dico.containsKey("Euclide")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 7 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true;
	    if (dico.tailleTab != 4) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 7 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (dico.size() != 2) {
		System.out.println("Erreur : Le nombre d'éléments est incorrect");
		System.out.println("TEST 7 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 7 Insertion ....................................................[OK]");	
	}

	
	//TEST N°8:
	//Insertion : TN*DN4/3*ED*ENI 
	//Recherche : TN*DN4/3*ET
	System.out.println("\nTEST N°8:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient n éléments, l'élément à insérer est connu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient n éléments\nL'élément recherché est connu");	
	dico.put("Aristote", "Philosophe grec");
	dico.put("Archimède", "Savant grec");
	if (dico.get("Archimède") == null) {
	    System.out.println("Erreur : L'élément aurait du être trouvé");
	    System.out.println("TEST 8 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	if (!dico.containsKey("Aristote")) {
	    System.out.println("Erreur : L'élément devrait se trouver dans le dictionnaire");
	    System.out.println("TEST 8 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true;
	    if (dico.tailleTab != 5) {	
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 8 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (dico.size() != 4) {
		System.out.println("Erreur : Le nombre d'éléments est incorrect");
		System.out.println("TEST 8 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 8 Insertion ....................................................[OK]");	
	}


	//TEST N°9:
	//Insertion : TN*DN*END*EI
	//Recherche : TN*DN*ENT
	System.out.println("\nTEST N°9:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient n éléments, l'élément à insérer est inconnu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient n éléments");
	if (dico.get("Thalès") != null) {
	    System.out.println("Erreur : L'élément n'aurait pas du être trouvé");
	    System.out.println("TEST 9 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	else
	    System.out.println("TEST 9 Recherche ....................................................[OK]"); 
	dico.put("Thalès", "Mathématicien grec");
	if (!dico.containsKey("Thalès")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 9 Insertion ....................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true;
	    if (dico.tailleTab != 7) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 9 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (dico.size() != 5) {
		System.out.println("Erreur : Le nombre d'éléments est incorrect");
		System.out.println("TEST 9 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }	    
	    if (drapo)
		System.out.println("TEST 9 Insertion ....................................................[OK]");	
	}
	
	return retour;    
    }

}


    
