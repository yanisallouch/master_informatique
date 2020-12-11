
package Dico;

/**La classe OrderedDictionary spécialise la classe AbstractDictionary<BR>
 *Cette classe est une spécialisation du framework défini pas l'interface Dictionary et la classe AbstractDictionary<BR>
 *Dans ce type de dictionnaire, les couples clé-valeur sont rangés pas ordre d'insertion*/

public class OrderedDictionary extends AbstractDictionary {

    /**Ce constructeur définit un objet OrderedDictionary avec une taille de dictionnaire de 10 par défaut.*/
    public OrderedDictionary() {
	tailleTab = 10;
	tabCle = new Object[tailleTab];
	tabValeur = new Object[tailleTab];
    }

    
    /**Ce constructeur définit un objet OrderedDictionary avec une taille de dictionaire passée en paramètre
     *@param t taille du dictionnaire*/
    public OrderedDictionary(int t) {
	tailleTab = t;
	tabCle = new Object[tailleTab];
	tabValeur = new Object[tailleTab];
    }

    
   /**Cette méthode renvoie l'index auquel sera rangé un nouveau couple clé-valeur dans le dictionnaire<BR>
    *Les couples d'un dictionnaire ordonné étant rangés pas ordre d'insertion, la valeur du nouvel index est égale à la taille du dictionnaire<BR>  
    *Cette méthode ne doit être appelée que dans le cas ou le dictionnaire ne contient pas déjà cette clé<BR>
    *S'il n'y a pas assez de place dans le dictionnaire pour l'insertion d'un nouvel élément, la taille du dictionnaire est augmentée de 25% par rapport à sa taille initiale
    *@param key clé de l'élément à insérer
    *@return index du nouvel élément dans le receveur*/
    protected int newIndexOf(Object key) {
	int index = size();
	
	if (index == tailleTab) 
	    grow();

	return index;
    }


    /**Cette méthode permet d'augmenter la taille du ditionnaire de 25%<BR>
     *Les clés et les valeurs sont réinsérées dans les nouveaux tableaux de manière séquentielle*/ 
    protected void grow() {	
	int fin = tailleTab;
	if (tailleTab % 4 == 0)
	    tailleTab = tailleTab + tailleTab / 4;
	else
	    tailleTab = tailleTab + tailleTab / 4 + 1; 
	if (tailleTab == 0)
	    tailleTab++;

	Object[] newTabCle = new Object[tailleTab];
	Object[] newTabValeur = new Object[tailleTab];
	 
	for(int i = 0 ; i < fin ; i++) {
	    newTabCle[i] = tabCle[i];
	    newTabValeur[i] = tabValeur[i];
	}		    
	 
	tabCle = newTabCle;
	tabValeur = newTabValeur;	
    }


    /*Test d'insertion d'un élément dans le dictionnaire
      1) CRITERES

      PRECONDITIONS:
      -dictionnaire de taille 0 (T0) / dictionnaire de taille 1 (T1) / dictionnaire de taille quelconque (TN) 
      -0 élément dans le dictionnaire (D0) / 1 élément dans le dictionnaire (D1) / n éléments dans le dictionnaire (DN)
      -élément dans dictionnaire (ED) / élément non dans dictionnaire (END)
      POSTCONDITION:
      -élément inséré (EI) / élément non inséré (ENI)

      2) CLASSES D'EQUIVALENCE

      POTENTIELLES : (T0 T1 TN) * (D0 D1 DN) * (ED END) * (EI ENI)
      EFFECTIVES : T0*D0*END*EI ; 
                   T1*D0*END*EI ; T1*D1*ED*ENI ; T1*D1*END*EI ;
		   TN*D0*END*EI ; TN*D1*ED*ENI ; TN*D1*END*EI ; TN*DN*ED*ENI ; TN*DN*END*EI 
    */

    /*Test de recherche d'un élément dans le dictionnaire
      1) CRITERES
      
      PRECONDITIONS: 
      -dictionnaire de taille 0 (T0) / dictionnaire de taille 1 (T1) / dictionnaire de taille quelconque (TN) 
      -0 élément dans le dictionnaire (D0) / 1 élément dans le dictionnaire (D1) / n éléments dans le dictionnaire (DN)
      -élément en position 0 (E0) / élément en position 1 (E1) / élément en position quelconque (EN) 
      POSTCONDITION:
      -élément trouvé (ET) / élément non trouvé (ENT)

      2) CLASSES D'EQUIVALENCE

      POTENTIELLES : (T0 T1 TN) * (D0 D1 DN) * (E0 E1 EN) * (ET ENT)
      EFFECTIVES : T0*D0*ENT ;
                   T1*D0*ENT ; T1*D1*E0*ET ;
		   TN*D0*ENT ; TN*D1*ENT ; TN*D1*E0*ET ; TN*DN*ENT ; TN*DN*E0*ET ; TN*DN*E1*ET ; TN*DN*EN*ET
    */
 
    protected int testBoiteBlanche() {
	int retour = 0;

	//TEST N°1:
	//Insertion : T0*D0*END*EI 
	//Recherche : T0*D0*ENT
	System.out.println("\nTEST N°1:"); 
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire vide \nde taille 0");
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire vide \nde taille 0");
	OrderedDictionary dico = new OrderedDictionary(0);
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
	    if (dico.tailleTab != 1) {
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
	dico = new OrderedDictionary(1);
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
	    if (dico.tailleTab != 1) {
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
	//Recherche : T1*D1*E0*ET
	System.out.println("\nTEST N°3:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille 1\nLe dictionnaire contient 1 élément, l'élément à insérer est connu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille 1\nLe dictionnaire contient 1 élément\nL'élément se trouve en position 0");
	if (dico.get("Platon") == null) {
	    System.out.println("Erreur : L'élément aurait du être trouvé en position 0");
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
	System.out.println("\nTEST N°4:"); 
	System.out.println("Essai d'insertion d'un élément dans un dictionnaire de taille 1\nLe dictionnaire contient 1 élément, l'élément à insérer est inconnu");
	dico.put("Euclide", "Mathématicien grec");
	if (!dico.containsKey("Euclide")) {
	    System.out.println("Erreur : L'élément n'a pas été inséré");
	    System.out.println("TEST 4 ..............................................................[FAILED]");
	    retour = -1;
	}
	else {
	    boolean drapo = true;
	    if (dico.tailleTab != 2) {
		System.out.println("Erreur : La taille du dictionnaire est incorrecte");
		System.out.println("TEST 4 ..............................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if ((String)dico.tabCle[1] != "Euclide") {
		System.out.println("Erreur : La position d'insertion est incorrecte");
		System.out.println("TEST 4 ..............................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 4 ..............................................................[OK]");	
	}


	//TEST N°5:
	//Insertion : TN*D0*END*EI
	//Recherche : TN*D0*ENT
	System.out.println("\nTEST N°5:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire vide \nde taille n");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire vide \nde taille n");
	dico = new OrderedDictionary(4);
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
	    if ((String)dico.tabCle[0] != "Platon") {
		System.out.println("Erreur : La position d'insertion est incorrecte");
		System.out.println("TEST 5 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 5 Insertion ....................................................[OK]");	
	}


	//TEST N°6:
	//Insertion : TN*D1*ED*ENI
	//Recherche : TN*D1*E0*ET 
	System.out.println("\nTEST N°6:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient 1 élément, l'élément à insérer est connu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient 1 élément\nL'élément recherché se trouve en position 0");
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
	    if ((String)dico.tabCle[1] != "Euclide") {
		System.out.println("Erreur : La position d'insertion est incorrecte");
		System.out.println("TEST 7 Insertion ....................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 7 Insertion ....................................................[OK]");	
	}

	
	//TEST N°8:
	//Insertion : TN*DN*ED*ENI 
	//Recherche : TN*DN*E0*ET ; TN*DN*E1*ET ; TN*DN*EN*ET
	System.out.println("\nTEST N°8:"); 
	System.out.println("Insertion : Essai d'insertion d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient n éléments, l'élément à insérer est connu");
	System.out.println("Recherche : Essai de recherche d'un élément dans un dictionnaire \nde taille n\nLe dictionnaire contient n éléments\nL'élément se trouve en position 0, 1 ou n");	
	dico.put("Aristote", "Philosophe grec");
	dico.put("Archimède", "Savant grec");
	if (dico.get("Platon") == null) {
	    System.out.println("Erreur : L'élément aurait du être trouvé");
	    System.out.println("TEST 8 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
	if (dico.get("Euclide") == null) {
	    System.out.println("Erreur : L'élément aurait du être trouvé");
	    System.out.println("TEST 8 Recherche ....................................................[FAILED]"); 
	    retour = -1;	
	}
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
	    if (dico.tailleTab != 4) {	
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
	    if (dico.tailleTab != 5) {
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
	    if ((String)dico.tabCle[4] != "Thalès") {
		System.out.println("Erreur : La position d'insertion est incorrecte");
		System.out.println("TEST 9 ..............................................................[FAILED]");
		retour = -1;
		drapo = false;
	    }
	    if (drapo)
		System.out.println("TEST 9 Insertion ....................................................[OK]");	
	}
	

	return retour;
    }

	
}
