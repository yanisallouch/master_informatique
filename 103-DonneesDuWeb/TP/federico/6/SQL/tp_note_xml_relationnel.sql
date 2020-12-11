/*
1) Stockage Monet
*/

/*
1. Implémenter sous Oracle le schéma Monet permettant de stocker un document XML pour les bâtiments
que vous avez proposé pour le TP 1.
*/

DROP TABLE batiment;
DROP TABLE batiment_etage;
DROP TABLE batiment_etage_bureau;
DROP TABLE batiment_etage_bureau_code;
DROP TABLE batiment_etage_bureau_personne;
DROP TABLE batiment_etage_salle;
DROP TABLE batiment_etage_salle_nombrePlaces;



CREATE TABLE batiment ( node varchar(20));

CREATE TABLE batiment_etage ( node varchar(20) , txtval varchar(20));

CREATE TABLE batiment_etage_bureau ( node varchar(20));

CREATE TABLE batiment_etage_bureau_code ( node varchar(20), numval number(10));

CREATE TABLE batiment_etage_bureau_personne ( node varchar(20), txtval varchar(20));

CREATE TABLE batiment_etage_salle ( node varchar(20));

CREATE TABLE batiment_etage_salle_nombrePlaces ( node varchar(20), numval number(10));

INSERT INTO batiment (node)
VALUES ('n1');

INSERT INTO batiment_etage (node, txtval)
VALUES ('n2', 'foobarDescription1');

INSERT INTO batiment_etage_bureau (node)
VALUES ('n3');

INSERT INTO batiment_etage_bureau_code (node, numval)
VALUES ('n4', 'foobarCode1');

INSERT INTO batiment_etage_salle (node)
VALUES ('n5');

INSERT INTO batiment_etage_salle_nombrePlaces (node, numval)
VALUES ('n6', '421');

INSERT INTO batiment_etage (node, txtval)
VALUES ('n7', 'foobarDescription2');

INSERT INTO batiment_etage_bureau (node)
VALUES ('n8');

INSERT INTO batiment_etage_bureau_code (node, numval)
VALUES ('n9', '422');

INSERT INTO batiment_etage_bureau_personne (node, txtval)
VALUES ('n10', 'william');

INSERT INTO batiment_etage_salle (node)
VALUES ('n11');

INSERT INTO batiment_etage_salle_nombrePlaces (node, numval)
VALUES ('n12', '422');

/*
2. Exprimez en SQL trois requêtes XPath de votre choix sur ce document.
*/

-- //personne[self::node() = 'william']

SELECT bebp.txtval
FROM batiment_etage_bureau_personne bebp
WHERE bebp.txtval = 'william';

-- //etage[.//personne='william' or ()]

/*
Exemple de requête qui ne s'exprime pas en SQL sans ajouter des FK et une relation de parent sur la table.
*/

-- //etage/description

SELECT be.txtval
FROM batiment_etage be;


-- //nombrePlaces[. > 421]

SELECT besn.numval
FROM batiment_etage_salle_nombrePlaces besn
WHERE besn.numval > '421';

/*
3. Comment pourrait on étendre le schéma Monet pour gérer l’axe de navigation ancestor ?
*/

/*
Comme vu sur la seconde requête XPATH, certaines requêtes ne se traduisent pas en SQL sans étendre le shéma relationnel.

La solution consiste à ajouter une colonne parent et/ou ancètre pour naviguer dans l'arbre comme en XML.
*/


/*
3) Interval-encoding avec SAX
*/

/*
1. Donnez l’encodage begin/end de l’un des documents XML pour les bâtiments que vous avez proposé lors du TP 1, puis enregistrez-le dans la table NODE.
*/

/*
<!--1--><batiment>
	<!--2--><etage>
		<!--3--><description><!--4-->foobarDescription1<!--5-->
		        </description><!--6-->
		<!--7--><bureau>
			<!--8--><code><!--9-->foobarCode1<!--10-->
			    		</code><!--11-->
		        </bureau><!--12-->
		<!--13--><salle>
			<!--14--><nombrePlaces><!--15-->421<!--16-->
			        </nombrePlaces><!--17-->
		        </salle><!--18-->
	        </etage><!--19-->
   <!--20--><etage>
		<!--21--><description><!--22-->foobarDescription2<!--23-->
		        </description><!--24-->
		<!--25--><bureau>
			<!--26--><code><!--27-->422<!--28-->
			        </code><!--29-->
			<!--30--><personne><!--31-->william<!--32-->
			        </personne><!--33-->
		        </bureau><!--34-->
		<!--35--><salle>
			<!--36--><nombrePlaces><!--37-->422<!--38-->
			        </nombrePlaces><!--39-->
		        </salle><!--40-->
	        </etage><!--41-->
        </batiment><!--42-->
*/

CREATE TABLE NODE (
	begin_ number(10),
	end_ number(10),
	parent number(10),
	tag varchar(20),
	type varchar(20),
	txtval varchar(20),
	CONSTRAINT PK_NODE PRIMARY KEY (begin_)
);

INSERT INTO NODE (begin_, end_, parent, tag, type)
VALUES (1,42,'','batiment','ELT');
	INSERT INTO NODE (begin_, end_, parent, tag, type)
	VALUES (2,19,'1','etage','ELT');
		INSERT INTO NODE (begin_, end_, parent, tag, type)
		VALUES (3,6,'2','description','ELT');
			INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
			VALUES (4,5,'3','','TXT', 'foobarDescription1');
		INSERT INTO NODE (begin_, end_, parent, tag, type)
		VALUES (7,12,'2','bureau','ELT');
			INSERT INTO NODE (begin_, end_, parent, tag, type)
			VALUES (8,11,'7','code','ELT');
				INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
				VALUES (9,10,'8','','TXT', 'foobarCode1');
		INSERT INTO NODE (begin_, end_, parent, tag, type)
		VALUES (13,18,'2','salle','ELT');
			INSERT INTO NODE (begin_, end_, parent, tag, type, )
			VALUES (14,17,'13','nombrePlaces','ELT');
				INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
				VALUES (15,16,'14','','TXT', '421');
	INSERT INTO NODE (begin_, end_, parent, tag, type)
	VALUES (20,41,'1','etage','ELT');
		INSERT INTO NODE (begin_, end_, parent, tag, type)
		VALUES (21,24,'20','description','ELT');
			INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
			VALUES (22,23,'21','','TEXT', 'foobarDescription2');
		INSERT INTO NODE (begin_, end_, parent, tag, type)
		VALUES (25,34,'20','bureau','ELT');
			INSERT INTO NODE (begin_, end_, parent, tag, type)
			VALUES (26,29,'25','code','ELT');
				INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
				VALUES (27,28,'26','','TEXT', '422');
			INSERT INTO NODE (begin_, end_, parent, tag, type)
			VALUES (30,33,'25','personne','ELT');
				INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
				VALUES (31,32,'30','','TEXT', 'william');
		INSERT INTO NODE (begin_, end_, parent, tag, type)
		VALUES (35,40,'20','salle','ELT');
			INSERT INTO NODE (begin_, end_, parent, tag, type)
			VALUES (36,39,'35','nombrePlaces','ELT');
				INSERT INTO NODE (begin_, end_, parent, tag, type, txtval)
				VALUES (37,38,'35','','TEXT', '422');
