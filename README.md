# Spécialité [AIGLE](https://formations.umontpellier.fr/fr/formations/sciences-technologies-sante-STS/master-XB/master-informatique-program-fruai0342321nprme154/architectures-et-ingenierie-du-logiciel-et-du-web-aigle-subprogram-pr476.html): Architecture et Ingénierie du Logiciel

[https://informatique-fds.edu.umontpellier.fr/etudiants/offre-de-formation-master/parcours-aigle-architecture-et-ingenierie-du-logiciel-et-du-web/](https://formations.umontpellier.fr/fr/formations/sciences-technologies-sante-STS/master-XB/master-informatique-program-fruai0342321nprme154/architectures-et-ingenierie-du-logiciel-et-du-web-aigle-subprogram-pr476.html)

##	Conception et développement d’architectures logicielles

	-	architectures orientées services
	-	architectures web
	-	urbanisation (architectures des systèmes d’informations)

##	Maîtrise de la conception et du développement par objets
##	Maîtrise des technologies web
##	Maîtrise de l’informatique embarquée (pour mobiles...)

##	[102 - Ingénierie logicielle](./102-IngenierieLogiciel)
###	[Christophe Dony](http://www.lirmm.fr/~dony/) et [Clémentine Nebut](http://www.lirmm.fr/~nebut)

-	Conception logicielle avancée
-	[Réutilisation et de réutilisabilité du logiciel](./102-IngenierieLogiciel/TP/1)
-	[Validation par le test](./102-IngenierieLogiciel/TP/3)
-	[Schémas de réutilisation objet.](./102-IngenierieLogiciel/TP/4)
-	[Design patterns](./102-IngenierieLogiciel/CM/cpatterns.pdf) : analyse et conception UML, [compréhension et mise en oeuvre,](./102-IngenierieLogiciel/TP/5) et [ici](./102-IngenierieLogiciel/TP/6) et [ici](./102-IngenierieLogiciel/TP/8).
-	[Frameworks, plugins](./102-IngenierieLogiciel/TP/1), [lignes de produits](./102-IngenierieLogiciel/TP/7).
-	[Bases du test de logiciels.](./102-IngenierieLogiciel/TP/3)

###	Bibliographie :

##	[103 - Données du web](./103-DonneesDuWeb)
###	[Federico Ulliana](http://www.lirmm.fr/~ulliana/) et [Pierre Pompidor](http://www.lirmm.fr/~pompidor/)

-	[Partie 1 : Fondements des langages de données semi-structuées du Web](./103-DonneesDuWeb/CM/federico)
	1.	[Une suite de langages pour le Web : XML, DTD, XPath, XQuery.](./103-DonneesDuWeb/TP/federico/1)
	2.	[Exportation et publication de données relationnelles en XML.](./103-DonneesDuWeb/TP/federico/3)
	3.	[Stockage et interrogation de données XML dans les bases de données relationnelles.](./103-DonneesDuWeb/TP/federico/6)

-	[Partie 2 : Présentation des données du Web](./103-DonneesDuWeb/CM/pompidor/)
	1.	[médiatisation de données XML via des transformations XSLT](./103-DonneesDuWeb/TP/pompidor/)
	2.	[présentation du framework applicatif Angular au sein d’une architecture MEAN](./103-DonneesDuWeb/TP/pompidor/PROJET_MEAN/lecture/Angular_Pierre_Pompidor_2.pdf)

###	Bibliographie :

##	[104 - Compilation et Interprétation](./104-CompilationEtInterpretation)
###	[David Delahaye](http://www.lirmm.fr/~delahaye/) et [Mathieu Lafourcade](http://www.lirmm.fr/~lafourcade)

###	Articulation avec le cours de L3

Les cours de compilation s’intéressent en général à l’analyse lexico-syntaxique des langages.
Ce cours-ci prend le relais en se focalisant sur l’exécution des programmes.

###	Transformation de programmes

-	[transformation d’un langage source à un langage cible.](./104-CompilationEtInterpretation/CM/delahaye)
-	évaluation directe de programmes.
-	[transformation du langage LISP en :](./104-CompilationEtInterpretation/CM/lafourcade)
	1.	un langage intermédiaire (évaluation efficace)
	2.	un langage de machine virtuelle (processeurs physiques)
	3.	un langage de machine virtuelle à pile

###	[Projet](./104-CompilationEtInterpretation/TP/projet)

Un projet en groupe permet aux étudiants de programmer de façon réaliste la totalité des chaines d’exécution (évaluation de LISP ou du langage intermédiaire, compilation vers les langages intermédiaires et de machine virtuelle, évaluation des machines virtuelles).

###	Bibliographie :

*	Quelques références classiques :

	*	sur la compilation : [[ASS89]](./bibliographie/104/Structure_and_Interpretation_of_Computer_Programs_-_2nd_Edition.pdf),
	*	sur l’initiation à la programmation par le langage SCHEME (cousin de LISP) : [[ASS89]](./bibliographie/104/Structure_and_Interpretation_of_Computer_Programs_-_2nd_Edition.pdf) ;
	*	de haut niveau, sur LISP et SCHEME, et les problématiques d’évaluation et de compilation : [[SJ93](./bibliographie/104/La_programmation_applicative_by_Emmanuel_Saint-James_.djvu),Que94] ;
	*	sur LEXet YACC [[LMB94]](./bibliographie/104/lex_yacc.pdf);
	*	enfin, [Kar97] est un polycopié intéressant sur la compilation et l’interprétation (dont l’approche très différente du présent cours pourra déconcerter certains).

*	Bibliographie sur les langages :
	*	COMMON LISP [[Ste90]](http://www.cs.cmu.edu/Groups/AI/html/cltl/cltl2.html) [file:/net/local/doc/cltl/clm/clm.html](a_chercher_le_fichier) ou http://clisp.cons.org/
	*	On se reportera aussi au polycopié de LISP distribué en cours [[Duc13a]](./bibliographie/104/Petit_Imprecis_de_LISP.pdf).


	*	[[ASS89]](./bibliographie/104/Structure_and_Interpretation_of_Computer_Programs_-_2nd_Edition.pdf)		H. Abelson, G.J. Sussman, and J. Sussman. Structure et interprétation des programmes informatiques. InterÉditions, Paris, 1989.
	*	[[ASU89]](./bibliographie/104/Compilers_Principles,_Techniques,_and_Tools,_2nd_Ed.pdf)		A. Aho, R. Sethi, and J. Ullman.Compilateurs : principes, techniques et outils. InterEditions,Paris, 1989.
	*	[[DEMN98]](./bibliographie/104/Langages_et_modeles_a_objets,_Etat_des_recherches_et_perspectives._Collection_Didactique.pdf)		R. Ducournau, J. Euzenat, G. Masini, and A. Napoli, editors. Langages et modèles à objets : État des recherches et perspectives. Collection Didactique. INRIA, 1998.
	*	[[Duc13a]](./bibliographie/104/Petit_Imprecis_de_LISP.pdf)		R. Ducournau.  Petit Imprécis de LISP.  Université Montpellier 2, polycopié de Master Informatique, 85 pages, 2013.
	*	[[Duc13b]](./bibliographie/104/Programmation_par_Objets_les_concepts_fondamentaux.pdf)		R. Ducournau.  Programmation par Objets : les concepts fondamentaux.  Université Montpellier 2, polycopié de Master Informatique, 215 pages, 2013.
	*	[Kar97]		J. Karczmarczuk. Implantation des langages de programmation — compilateurs et interprètes,1997. Polycopié Licence d’Informatique, Caen.
	*	[[Kle71]](./bibliographie/104/Mathematical_Logic_by_Kleene,_Stephen_Cole.pdf)		S.C. Kleene. Logique mathématique. Collection U. Armand Colin, Paris, 1971.
	*	[[LMB94]](./bibliographie/104/lex_yacc.pdf)		J. R. Levine, T. Mason, and D. Brown.LEX & YACC. O’Reilly, 1994.
	*	[[MD97]](./bibliographie/104/Java_Virtual_Machine_by_Troy_Downing,_Jon_Meyer.pdf)		J. Meyer and T. Downing.JAVA Virtual Machine. O’Reilly, 1997.
	*	[Que90]		Ch. Queinnec. Le filtrage : une application de (et pour) Lisp. InterÉditions, Paris, 1990.
	*	[Que94]		Ch. Queinnec. Les langages Lisp. InterÉditions, Paris, 1994.
	*	[[SJ93]](./bibliographie/104/La_programmation_applicative_by_Emmanuel_Saint-James_.djvu)		E.  Saint-James. La  programmation  applicative  :  de LISP à  la  machine  en  passant  par  le lambda-calcul. Hermès, 1993.
	*	[[Ste90]](http://www.cs.cmu.edu/Groups/AI/html/cltl/cltl2.html)		G. L. Steele. Common Lisp, the Language. Digital Press, second edition, 1990.
	*	[[Ter96]](./bibliographie/104/Compilers_and_compiler_generators_an_introduction_with_C++_by_Patrick_D._Terry.pdf)		P.D. Terry. Compilers and Compiler Generators an introduction with C++. Pearson, 1996



##	[105M - Principes de la programmation concurrente et répartie](./105-ProgrammationConcurrenteEtRepartie)
###	Contenu : programmation concurrente et communications intra et inter systèmes
###	[Hinde Bouziane](http://www.lirmm.fr/~bouziane/)

-	[Communications inter-processus (IPC) : Files de messages](./105-ProgrammationConcurrenteEtRepartie/TP/2), [mémoire partagée et ensembles de sémaphores](./105-ProgrammationConcurrenteEtRepartie/TP/3)
-	[Activités dans les processus (threads), parallélisme et synchronisation](./105-ProgrammationConcurrenteEtRepartie/TP/1-3seances)
-	[Programmation client-serveur (utilisation avancée des sockets et serveurs itératifs et concurrents)](./105-ProgrammationConcurrenteEtRepartie/TP/4)
-	[RPC (appel de procédure à distance)](./105-ProgrammationConcurrenteEtRepartie/TP/)

###	A quoi ça sert ?

	-	Applications multi-média, jeux et traitement d’images / vidéos
	-	Application distribuées (P2P, réseaux sociaux, etc.)
	-	Simulations scientifiques (météo, etc.)
	-	Administration système et réseaux

###	Bibliographie :

##	[108 - Programmation par agents](./108-ProgAgent)
###	[Madalina Croitoru](http://www.lirmm.fr/~croitoru/)  et [François Suro](https://www.lirmm.fr/~suro/index.html)

	-	Concepts de base de la programmation par acteurs et agents :asynchronisme, parallélisme, distribution massive
	-	Usage des continuations locales vs gestion des tâches en cours
	-	Architectures classiques d’agents (réactives, BDI, subsomption)
	-	Langages de programmation d’agents
	-	Protocoles classiques de coordination et de négociation
	-	Utilisation de rôles et de groupes pour la réalisation d’applications
	-	Gestion de la distribution d’applications multi-agents
	-	Utilisation des techniques multi-agents pour faciliter la programmation d’applications ouvertes, évolutives et distribuées

####	Les TPs utiliseront l’environnement NetLogo pour la programation et simulation d'agents:

*	[http://ccl.northwestern.edu/netlogo/](http://ccl.northwestern.edu/netlogo/)
*	[http://ccl.northwestern.edu/netlogo/6.1.1/](http://ccl.northwestern.edu/netlogo/6.1.1/)

###	Bibliographie :

*	Ce cours est basé sur le livre An introduction to multiagent systems de Michael Woolridge: [http://www.cs.ox.ac.uk/people/michael.wooldridge/pubs/imas/IMAS2e.html](http://www.cs.ox.ac.uk/people/michael.wooldridge/pubs/imas/IMAS2e.html) ou [ici](./bibliographie/108/An_Introduction_to_MultiAgent_Systems_by_Michael_Wooldridge_(z-lib.org).pdf).

	*	Du matériel supplémentaire est disponible comme suit:
		*	Des transparents faites par Michael Woolridge et Simon Parsons: http://www.cs.ox.ac.uk/people/michael.wooldridge/pubs/imas/distrib/
		*	Des transparents faites par Terry Payne: https://cgi.csc.liv.ac.uk/~trp/COMP310.html
		*	Version ITunes University des transparents de Terry Payne: https://itunes.apple.com/gb/course/multi-agent-systems/id980647152

Une autre ressource possible à consulter sur les simulations multi agent sont les travaux de Jacques Ferber :
*	http://www.lirmm.fr/~ferber/publications/index.html
*	http://www.lirmm.fr/~ferber/publications/LesSMA_Ferber.pdf

##	[121M - Algorithmique du texte avancé](./121-AlgoDuTexte)
###	 [Sèverine Bérard](http://www.isem.univ-montp2.fr/en/personnel/teams/phylogeny-and-molecular-evolutions/berard-severine.index/ )

-	[Programme](./121-AlgoDuTexte/Admin_HMIN121M.pdf) : principalement centré autour de la recherche de motifs

	1.	Recherche de motifs exacts : [KMP](./121-AlgoDuTexte/CM/2) et [Boyer-Moore](./121-AlgoDuTexte/CM/3)
	2.	Recherche de motifs approchés et multiples : [Aho-Corasick](./121-AlgoDuTexte/CM/4)
	3.	Structures d’indexation des textes : [arbre des suffixes](./121-AlgoDuTexte/CM/6), [table dessuffixes](./121-AlgoDuTexte/CM/7), dictionnaires

###	Bibliographie :

*	[The Exact String Matching Problem: a Comprehensive Experimental Evaluation](./bibliographie/121/Faro-Lecroq_2010.pdf)
*	[The Exact Online String Matching Problem: a Review of the Most Recent Results](./bibliographie/121/Faro-Lecroq_2013.pdf)

##	[122 - Entrepôt de donnée et Big-Data](./122-EDBD)
###	 [Federico Ulliana](http://www.lirmm.fr/~ulliana/) et Anne-Muriel Chiffoleau

###	Programme

-	L'objectif du module est d'aborder les entrepôts de données et les plateformes pour l'analyse des données massives.

###	Partie 0 - [Rappels de Modélisation Relationnelle et SQL - Federico Ulliana](./122-EDBD/CM/1)
###	Partie 1 - [Optimisation de requête - Anne-Muriel Chifolleau](./122-EDBD/CM/2)

-	[Cas d'étude sur le SDGB ORACLE SQL](./122-EDBD/TP/data_warehouse/2).

###	Partie 2 - [Entrepôts de données relationnels](./122-EDBD/CM/3) et [ouverture au Big Data - Federico Ulliana, Christophe Menichetti (IBM)](./122-EDBD/CM/4)

-	Architecture des entrepôts de données
-	[Modélisation multidimensionnelle](./122-EDBD/TP/mini_projet_1)
-	[OLAP/OLTP](./122-EDBD/TP/mini_projet_1)
-	[Hadoop Map/Reduce](./122-EDBD/TP/map_reduce)
-	Solutions de Big-Data et Machine learning pour le Big-Data

###	Bibliographie :

*	[[BD-G]](http://georges.gardarin.free.fr/Livre_BD_Contenu/XX-TotalBD.pdf) Bases de données, Georges Gardarin, 5ème edition (2005)
*	[[BD-G]](http://georges.gardarin.free.fr/Livre_BD_Contenu/XX-TotalBD.pdf) Modèle relationnel et SQL. Chapitre VI et VII.

*	[[ORA]](http://docs.oracle.com/cd/B28359_01/server.111/b28286.pdf) Oracle® Database SQL Language, Reference 11g Release 1 (11.1) - (2013)
*	[[ORA]](http://docs.oracle.com/cd/B28359_01/server.111/b28286.pdf) Liste datatypes. Table 2.1.

*	[[UML]](./bibliographie/122/prolegomenes_uml.pdf) Prolegomenes_uml.pdf
*	[UML] Section : projection vers les bases de données.
*	[[UML2]](http://laurent-audibert.developpez.com/Cours-UML/?page=diagramme-classes) UML 2 : De l'apprentissage à la pratique
*	[[UML2]](http://laurent-audibert.developpez.com/Cours-UML/?page=diagramme-classes)] Associations n-aires. Section 3.3.

*	[[ADB]](http://dsf.berkeley.edu/papers/fntdb07-architecture.pdf) Architecture of a Database System. Hellerstein, Stonebraker, Hamilton
*	[[ADB]](http://dsf.berkeley.edu/papers/fntdb07-architecture.pdf) Architecture of a Database System. Chapitre 5

*	[big-data-survey](./bibliographie/122/big-data-survey.pdf)

*	[[DW1]](./bibliographie/122/The_Data_Warehouse_Toolkit_The_Definitive_Guide_to_Dimensional_Modeling_by_Ralph_Kimball,_Margy_Ross_(z-lib.org).pdf) The Datawarehouse Toolkit. Kimball, Ross. (traduction française disponible à la BU, demander aux enseignants)
*	[[DW2]](./bibliographie/122/Multidimensional_Databases_and_Data_Warehousing_(Synthesis_Lectures_on_Data_Management)_by_Christian_S._Jensen,_Torben_Bach_Pedersen,_Christian_Thomsen_(z-lib.org).pdf) Multidimensional Databases and Data Warehousing.  Jensen, Pedersen, Thomsen.
*	[[DW3]](http://www.financegirltoronto.com/wp-content/uploads/2017/07/getting-data-right.pdf) Getting Data Right

*	[[MR1]](./bibliographie/122/mapreduce-osdi04.pdf) MapReduce: [Simplified Data Processing on Large Clusters](https://static.googleusercontent.com/media/research.google.com/en//archive/mapreduce-osdi04.pdf) - Jeffrey Dean and Sanjay Ghemawat

*	[[MR2]](http://hadoop.apache.org/) Apache Hadoop

*	[[MR3]](http://grut-computing.com/HadoopBook.pdf) Hadoop : the definitive guideou [ici](./bibliographie/122/HadoopBook.pdf)

*	[Hadoop and the Data Warehouse When to Use Which](./bibliographie/122/Hadoop_and_the_Data_Warehouse_When_to_Use_Which.pdf)