-- Question 2


SELECT nom FROM ville WHERE insee='34172';

/*

Plan d'execution
----------------------------------------------------------
Plan hash value: 2371920588

---------------------------------------------------------------------------
| Id  | Operation	  | Name  | Rows  | Bytes | Cost (%CPU)| Time	  |
---------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |	  |	3 |   168 |    68   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| VILLE |	3 |   168 |    68   (0)| 00:00:01 |
---------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   1 - filter("INSEE"='34172')

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	 73  recursive calls
	 12  db block gets
	327  consistent gets
	  0  physical reads
       2072  redo size
	554  bytes sent via SQL*Net to client
	367  bytes received via SQL*Net from client
	  2  SQL*Net roundtrips to/from client
	  5  sorts (memory)
	  0  sorts (disk)
	  1  rows processed

*/
-- Question 4

SELECT nom FROM ville WHERE insee='34172';

/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 2371920588

---------------------------------------------------------------------------
| Id  | Operation	  | Name  | Rows  | Bytes | Cost (%CPU)| Time	  |
---------------------------------------------------------------------------
|   0 | SELECT STATEMENT  |	  |	3 |   168 |    68   (0)| 00:00:01 |
|*  1 |  TABLE ACCESS FULL| VILLE |	3 |   168 |    68   (0)| 00:00:01 |
---------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   1 - filter("INSEE"='34172')

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	  0  recursive calls
	  0  db block gets
	192  consistent gets
	  0  physical reads
	  0  redo size
	788  bytes sent via SQL*Net to client
	367  bytes received via SQL*Net from client
	  3  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
	  1  rows processed


*/


-- Question 5

SELECT d.nom FROM ville v, departement d WHERE v.dep = d.id AND insee='34172';
/*

Plan d'execution
----------------------------------------------------------
Plan hash value: 3142094180

----------------------------------------------------------------------------------------------
| Id  | Operation		     		 | Name	     	 | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT	     	 |		     	 |	   3 |	 117 |	  71   (0)| 00:00:01 |
|   1 |  NESTED LOOPS		     	 |		     	 |	   3 |	 117 |	  71   (0)| 00:00:01 |
|   2 |   NESTED LOOPS		     	 |		     	 |	   3 |	 117 |	  71   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS FULL	     | VILLE	     |	   3 |	  24 |	  68   (0)| 00:00:01 |
|*  4 |    INDEX UNIQUE SCAN	     | SYS_C00164756 |	   1 |	     |	   0   (0)| 00:00:01 |
|   5 |   TABLE ACCESS BY INDEX ROWID| DEPARTEMENT   |	   1 |	  31 |	   1   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - filter("INSEE"='34172')
   4 - access("V"."DEP"="D"."ID")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
   - this is an adaptive plan


Statistiques
----------------------------------------------------------
	 32  recursive calls
	 16  db block gets
	267  consistent gets
	  0  physical reads
       3048  redo size
	551  bytes sent via SQL*Net to client
	401  bytes received via SQL*Net from client
	  2  SQL*Net roundtrips to/from client
	  2  sorts (memory)
	  0  sorts (disk)
	  1  rows processed



*/

-- Question 6
SELECT d.nom FROM ville v, departement d WHERE v.dep = d.id;
/*

Plan d'execution
----------------------------------------------------------
Plan hash value: 211249738

----------------------------------------------------------------------------------
| Id  | Operation	   	   | Name	 	| Rows  | Bytes | Cost (%CPU)| Time	 	|
----------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |		 	| 31236 |  1067K|    72   (2)| 00:00:01 |
|*  1 |  HASH JOIN	   	   |		 	| 31236 |  1067K|    72   (2)| 00:00:01 |
|   2 |   TABLE ACCESS FULL| DEPARTEMENT|   104 |  3224 |     3   (0)| 00:00:01 |
|   3 |   TABLE ACCESS FULL| VILLE	 	| 31236 |   122K|    68   (0)| 00:00:01 |
----------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   1 - access("V"."DEP"="D"."ID")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	 38  recursive calls
	  0  db block gets
       2712  consistent gets
	  0  physical reads
	  0  redo size
     653842  bytes sent via SQL*Net to client
      27225  bytes received via SQL*Net from client
       2442  SQL*Net roundtrips to/from client
	  3  sorts (memory)
	  0  sorts (disk)
      36601  rows processed



*/

-- Question 7
select v.nom, d.nom from ville v join departement d on v.dep = d.id WHERE id='91';

/*


Plan d'execution
----------------------------------------------------------
Plan hash value: 825730138

----------------------------------------------------------------------------------------------
| Id  | Operation		    	 	| Name	     	 | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT	    	 |		     	 |	   3 |	 261 |	  70   (2)| 00:00:01 |
|   1 |  NESTED LOOPS		    	 |		      	 |	   3 |	 261 |	  70   (2)| 00:00:01 |
|   2 |   TABLE ACCESS BY INDEX ROWID| DEPARTEMENT   |	   1 |	  31 |	   1   (0)| 00:00:01 |
|*  3 |    INDEX UNIQUE SCAN	     | SYS_C00164756 |	   1 |	     |	   1   (0)| 00:00:01 |
|*  4 |   TABLE ACCESS FULL	     	 | VILLE	     |	   3 |	 168 |	  69   (2)| 00:00:01 |
----------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - access("D"."ID"='91')
   4 - filter("V"."DEP"='91')

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	  5  recursive calls
	  0  db block gets
	257  consistent gets
	  0  physical reads
	  0  redo size
       6822  bytes sent via SQL*Net to client
	552  bytes received via SQL*Net from client
	 15  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
	196  rows processed


*/

-- Question 8

-- Requete Q6
select /*+ use_nl(ville departement)*/ departement.nom from ville join departement on dep=departement.id;

/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 1651012225

----------------------------------------------------------------------------------
| Id  | Operation	   | Name	 | Rows  | Bytes | Cost (%CPU)| Time	 |
----------------------------------------------------------------------------------
|   0 | SELECT STATEMENT   |		 	 | 31236 |  1067K|  6930   (1)| 00:00:01 |
|   1 |  NESTED LOOPS	   |		 	 | 31236 |  1067K|  6930   (1)| 00:00:01 |
|   2 |   TABLE ACCESS FULL| DEPARTEMENT |   104 |  3224 |     3   (0)| 00:00:01 |
|*  3 |   TABLE ACCESS FULL| VILLE	 	 |   300 |  1200 |    67   (2)| 00:00:01 |
----------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - filter("DEP"="DEPARTEMENT"."ID")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	 13  recursive calls
	  0  db block gets
      22458  consistent gets
	  0  physical reads
	  0  redo size
     650619  bytes sent via SQL*Net to client
      27270  bytes received via SQL*Net from client
       2442  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
      36601  rows processed

*/




-- Requête Q7

/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 825730138

----------------------------------------------------------------------------------------------
| Id  | Operation		     | Name	     | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT	     |		     |	   3 |	 261 |	  70   (2)| 00:00:01 |
|   1 |  NESTED LOOPS		     |		     |	   3 |	 261 |	  70   (2)| 00:00:01 |
|   2 |   TABLE ACCESS BY INDEX ROWID| DEPARTEMENT   |	   1 |	  31 |	   1   (0)| 00:00:01 |
|*  3 |    INDEX UNIQUE SCAN	     | SYS_C00164756 |	   1 |	     |	   1   (0)| 00:00:01 |
|*  4 |   TABLE ACCESS FULL	     | VILLE	     |	   3 |	 168 |	  69   (2)| 00:00:01 |
----------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - access("D"."ID"='91')
   4 - filter("V"."DEP"='91')

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	  4  recursive calls
	  0  db block gets
	208  consistent gets
	  0  physical reads
	  0  redo size
       6822  bytes sent via SQL*Net to client
	584  bytes received via SQL*Net from client
	 15  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
	196  rows processed



*/


-- Question 9

create index idx_dep_ville on ville (dep);

-- Requête Q5
Indentique, le couple tabe access full + index unique scan est optimal

/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 3142094180

----------------------------------------------------------------------------------------------
| Id  | Operation		     | Name	     | Rows  | Bytes | Cost (%CPU)| Time     |
----------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT	     |		     |	   3 |	 117 |	  71   (0)| 00:00:01 |
|   1 |  NESTED LOOPS		     |		     |	   3 |	 117 |	  71   (0)| 00:00:01 |
|   2 |   NESTED LOOPS		     |		     |	   3 |	 117 |	  71   (0)| 00:00:01 |
|*  3 |    TABLE ACCESS FULL	     | VILLE	     |	   3 |	  24 |	  68   (0)| 00:00:01 |
|*  4 |    INDEX UNIQUE SCAN	     | SYS_C00164756 |	   1 |	     |	   0   (0)| 00:00:01 |
|   5 |   TABLE ACCESS BY INDEX ROWID| DEPARTEMENT   |	   1 |	  31 |	   1   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - filter("INSEE"='34172')
   4 - access("V"."DEP"="D"."ID")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
   - this is an adaptive plan


Statistiques
----------------------------------------------------------
	  8  recursive calls
	  0  db block gets
	197  consistent gets
	  0  physical reads
	  0  redo size
	551  bytes sent via SQL*Net to client
	403  bytes received via SQL*Net from client
	  2  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
	  1  rows processed

*/

-- Requête Q6

/*Plus de table access full mais un index fast full scan.
Le nombre de consistent get est similaire mais le cout en %CPU est bien plus faible. De plus
un bloc peut contenir plus d index qu'il ne peut contenir de lignes. On voit egalement moins d'appels recursifs */


/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 3151218067

---------------------------------------------------------------------------------------
| Id  | Operation	      	  | Name	      | Rows  | Bytes | Cost (%CPU)| Time     |
---------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT      | 	      	  | 31236 |  1067K|    26	(0)| 00:00:01 |
|*  1 |  HASH JOIN	      	  | 	      	  | 31236 |  1067K|    26	(0)| 00:00:01 |
|   2 |   TABLE ACCESS FULL   | DEPARTEMENT   |   104 |  3224 |     3	(0)| 00:00:01 |
|   3 |   INDEX FAST FULL SCAN| IDX_DEP_VILLE | 31236 |   122K|    23	(0)| 00:00:01 |
---------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   1 - access("V"."DEP"="D"."ID")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
   - this is an adaptive plan


Statistiques
----------------------------------------------------------
	  8  recursive calls
	  0  db block gets
       2530  consistent gets
	 70  physical reads
	  0  redo size
     650614  bytes sent via SQL*Net to client
      27225  bytes received via SQL*Net from client
       2442  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
      36601  rows processed


*/

-- Requête Q7
/*
idx_dep_ville est utilisé au lieu de table acces full. Moins de get. TABLE ACCES BY INDEX ROWID BATCHED:
le SBGD récupère un batch de paires (index,bloc) et les ordonne par bloc afin de regouper les acces aux lignes situées dans un même bloc
*/
/*

Plan d'execution
----------------------------------------------------------
Plan hash value: 2538414451

------------------------------------------------------------------------------------------------------
| Id  | Operation			         		 | Name	     	 | Rows  | Bytes | Cost (%CPU)| Time     |
------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT		     		 |		     	 |	 196 | 17052 |	   4   (0)| 00:00:01 |
|   1 |  NESTED LOOPS			     		 |		     	 |	 196 | 17052 |	   4   (0)| 00:00:01 |
|   2 |   TABLE ACCESS BY INDEX ROWID		 | DEPARTEMENT   |	   1 |	  31 |	   1   (0)| 00:00:01 |
|*  3 |    INDEX UNIQUE SCAN		 		 | SYS_C00164756 |	   1 |	     |	   1   (0)| 00:00:01 |
|   4 |   TABLE ACCESS BY INDEX ROWID BATCHED| VILLE	     |	 196 | 10976 |	   3   (0)| 00:00:01 |
|*  5 |    INDEX RANGE SCAN		     		 | IDX_DEP_VILLE |	 196 |	     |	   1   (0)| 00:00:01 |
------------------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   3 - access("DEPARTEMENT"."ID"='91')
   5 - access("VILLE"."DEP"='91')

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	  0  recursive calls
	  0  db block gets
	 33  consistent gets
	  0  physical reads
	  0  redo size
       6822  bytes sent via SQL*Net to client
	574  bytes received via SQL*Net from client
	 15  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
	196  rows processed

*/

-- Question 10
SELECT v.nom, d.nom, r.nom FROM ville v, region r, departement d WHERE v.dep=d.id AND r.id=d.reg;

/*
Double hash join pour double jointure. Puis des table access full. Les hash sont logiques puisque nous avons
des tables de tailles très différentes. Faire un hash sur Region est assez efficace comparé à la taille de departement.
Pour ville et sa jointure avec la jointure de departement et region le hash est là aussi efficace.
*/


/*

Plan d'execution
----------------------------------------------------------
Plan hash value: 424771235

-----------------------------------------------------------------------------------
| Id  | Operation	    	| Name	  	  | Rows  | Bytes | Cost (%CPU)| Time	  |
-----------------------------------------------------------------------------------
|   0 | SELECT STATEMENT    |		  	  | 31236 |  4270K|    75   (2)| 00:00:01 |
|*  1 |  HASH JOIN	    	|		  	  | 31236 |  4270K|    75   (2)| 00:00:01 |
|*  2 |   HASH JOIN	    	|		  	  |   104 |  8736 |		6   (0)| 00:00:01 |
|   3 |    TABLE ACCESS FULL| REGION	  |    27 |  1080 |		3   (0)| 00:00:01 |
|   4 |    TABLE ACCESS FULL| DEPARTEMENT |   104 |  4576 |		3   (0)| 00:00:01 |
|   5 |   TABLE ACCESS FULL | VILLE	  	  | 31236 |  1708K|    68   (0)| 00:00:01 |
-----------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   1 - access("V"."DEP"="D"."ID")
   2 - access("R"."ID"="D"."REG")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
   - this is an adaptive plan


Statistiques
----------------------------------------------------------
	 84  recursive calls
	 17  db block gets
       2711  consistent gets
	  5  physical reads
       2992  redo size
    1114897  bytes sent via SQL*Net to client
      27262  bytes received via SQL*Net from client
       2442  SQL*Net roundtrips to/from client
	  7  sorts (memory)
	  0  sorts (disk)
      36601  rows processed


*/


-- Question 11
create index idx_reg_department on departement(reg)

Encore une fois le hash join est intéressant puisque la table ville est bien plus grande que la jointure de deaprtement et ville.
Le merge join est un peu plus mystérieux.
/*
La documentation d'Oracle nous donne un indice possible: Sort Merge Join est préférable lorsque la mémoire n'est pas suffisante pour
contenir la taille d'une table de hash qui devrait alors nécessiter des écritures sur disque. Etant donné la taille de Ville, cela semble bien possibe.
Un autre détail est que le SORT JOIN (tri) n'est effectué que sur la deuxième table. La, encore la documentation Oracle nous dit:
If an index exists, then the database can avoid sorting the first data set. However, the database always sorts the second data set, regardless of indexes.
Les raisons pour lesquelles un tri peut être évité ne sont malheureusement pas expliquées...
*/

/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 3225228285

----------------------------------------------------------------------------------------------------
| Id  | Operation		      		  | Name			   | Rows  | Bytes | Cost (%CPU)| Time	   |
----------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT	      	  | 		   	   	   | 31236 |  4270K|	75   (3)| 00:00:01 |
|*  1 |  HASH JOIN		      		  | 		   	   	   | 31236 |  4270K|	75   (3)| 00:00:01 |
|   2 |   MERGE JOIN		      	  | 		   	   	   |   104 |  8736 |	 6  (17)| 00:00:01 |
|   3 |    TABLE ACCESS BY INDEX ROWID| DEPARTEMENT	   	   |   104 |  4576 |	 2   (0)| 00:00:01 |
|   4 |     INDEX FULL SCAN	      	  | IDX_REG_DEPARTMENT |   104 |	   |	 1   (0)| 00:00:01 |
|*  5 |    SORT JOIN		      	  | 		   		   |	27 |  1080 |	 4  (25)| 00:00:01 |
|   6 |     TABLE ACCESS FULL	      | REGION		   	   |	27 |  1080 |	 3   (0)| 00:00:01 |
|   7 |   TABLE ACCESS FULL	      	  | VILLE		   	   | 31236 |  1708K|	68   (0)| 00:00:01 |
----------------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   1 - access("V"."DEP"="D"."ID")
   5 - access("R"."ID"="D"."REG")
       filter("R"."ID"="D"."REG")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
   - this is an adaptive plan


Statistiques
----------------------------------------------------------
	 11  recursive calls
	  0  db block gets
       2642  consistent gets
	  0  physical reads
	  0  redo size
    1114897  bytes sent via SQL*Net to client
      27262  bytes received via SQL*Net from client
       2442  SQL*Net roundtrips to/from client
	  1  sorts (memory)
	  0  sorts (disk)
      36601  rows processed



*/
-- Question 12
select v.nom, d.nom, r.nom from ville v JOIN departement d ON v.dep=d.id JOIN region r on r.id=d.reg WHERE r.id=91;
/*
Uniquement des nested loops. Cependant tous les acces se font sur des index. Les index range scan correspondent aux index secondanires
qui n'ont pas de garantie d'être unique. Ce n'est d'ailleurs pas le cas ici puisque les departement et les régions ne sont pas uniquie dans les tables des index.
D'après certaines sources, il semblerait que tant que la cardinalité est assez faible (moins de 4% des lignes pour chaque valeur d'index) INDEX RANGE SCAN reste plus efficace qu'un FULL TALE ACCES.
Etant donnée la distribution des villes dans les departement il est bien posssible que ce chiffre de 4% soit dépassé dans bien des cas.

*/

/*

Plan d'execution
----------------------------------------------------------
Plan hash value: 4049322760

-------------------------------------------------------------------------------------------------------------
| Id  | Operation			       			   | Name		    	| Rows  | Bytes | Cost (%CPU)| Time     |
-------------------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT		     		   |		    		|  2892 |	395K|	 21   (0)| 00:00:01 |
|   1 |  NESTED LOOPS			     		   |		    		|  2892 |	395K|	 21   (0)| 00:00:01 |
|   2 |   NESTED LOOPS			      		   |		    		|  2892 |	395K|	 21   (0)| 00:00:01 |
|   3 |    NESTED LOOPS 		      		   |		    		|	  5 |	420 |	  3   (0)| 00:00:01 |
|   4 |     TABLE ACCESS BY INDEX ROWID        | REGION 	    	|	  1 |	 40 |	  2   (0)| 00:00:01 |
|*  5 |      INDEX UNIQUE SCAN		       	   | SYS_C00164755	    |	  1 |	    |	  1   (0)| 00:00:01 |
|   6 |     TABLE ACCESS BY INDEX ROWID BATCHED| DEPARTEMENT	    |	  5 |	220 |	  1   (0)| 00:00:01 |
|*  7 |      INDEX RANGE SCAN		       	   | IDX_REG_DEPARTMENT |	  5 |	    |	  0   (0)| 00:00:01 |
|*  8 |    INDEX RANGE SCAN		     		   | IDX_DEP_VILLE	    |	578 |	    |	  1   (0)| 00:00:01 |
|   9 |   TABLE ACCESS BY INDEX ROWID	       | VILLE		    	|	578 | 32368 |	  6   (0)| 00:00:01 |
-------------------------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   5 - access("R"."ID"=91)
   7 - access("D"."REG"=91)
   8 - access("V"."DEP"="D"."ID")

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)
   - this is an adaptive plan


Statistiques
----------------------------------------------------------
	 12  recursive calls
	  0  db block gets
	296  consistent gets
	  1  physical reads
	  0  redo size
      47619  bytes sent via SQL*Net to client
       1562  bytes received via SQL*Net from client
	104  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
       1543  rows processed



*/

-- Question 13

select nom from ville WHERE dep LIKE '7%';

/*
Pas de soucis avec une chaine dont le début est fixé, l'index peut être utilisé. Par contre si on voulait LIKE '%7' cela forcerait un TABLE ACCESS FULL
puisque l'index ne pourrait pas être utilisé correctement. En pratique utiliser un index et son REVERSE peut être utile pour éviter les wildcards au début d'une chaine
*/

/*
Plan d'execution
----------------------------------------------------------
Plan hash value: 1694568309

-----------------------------------------------------------------------------------------------------
| Id  | Operation			    | Name	    | Rows  | Bytes | Cost (%CPU)| Time     |
-----------------------------------------------------------------------------------------------------
|   0 | SELECT STATEMENT		    |		    |  2500 |	136K|	 26   (0)| 00:00:01 |
|   1 |  TABLE ACCESS BY INDEX ROWID BATCHED| VILLE	    |  2500 |	136K|	 26   (0)| 00:00:01 |
|*  2 |   INDEX RANGE SCAN		    | IDX_DEP_VILLE |  2500 |	    |	  8   (0)| 00:00:01 |
-----------------------------------------------------------------------------------------------------

Predicate Information (identified by operation id):
---------------------------------------------------

   2 - access("DEP" LIKE '7%')
       filter("DEP" LIKE '7%')

Note
-----
   - dynamic statistics used: dynamic sampling (level=2)


Statistiques
----------------------------------------------------------
	  0  recursive calls
	  0  db block gets
	608  consistent gets
	  0  physical reads
	  0  redo size
     171957  bytes sent via SQL*Net to client
       3502  bytes received via SQL*Net from client
	287  SQL*Net roundtrips to/from client
	  0  sorts (memory)
	  0  sorts (disk)
       4278  rows processed

*/

--Question 14
Avant l'exécution de la commande il n'y a pas de statistiques pour les tables crées récemment.
Après l'exécution il y a des statistiques sur les minimas et maximas des colonnes, la densité, le nombre de buckets (probablement en rapport avec les index), et d'autres statistiques