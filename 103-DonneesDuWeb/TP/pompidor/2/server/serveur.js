const express = require('express');
const app     = express();
app.use(express.json());
app.use(express.urlencoded({extended:true}));
app.use(function (req, res, next) {
		res.setHeader('Access-Control-Allow-Origin', '*');
		res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
		res.setHeader('Access-Control-Allow-Headers', '*');
		next();
});

const MongoClient = require('mongodb').MongoClient;
const ObjectID    = require('mongodb').ObjectId;
const url         = "mongodb://localhost:27017";

MongoClient.connect(url, {useNewUrlParser: true}, (err, client) => {
		let db = client.db("SUPERVENTES");

		/* Liste des produits */
		app.get("/produits", (req,res) => {
				console.log("/produits");
				try {
						db.collection("produits").find().toArray((err, documents) => {
								res.end(JSON.stringify(documents));
						});
				} catch(e) {
						console.log("Erreur sur /produits : " + e);
						res.end(JSON.stringify([]));
				}
		});

		/* Liste des produits suivant une catégorie */
		app.get("/produits/:categorie", (req,res) => {
	let categorie = req.params.categorie;
				console.log("/produits/"+categorie);
				try {
						db.collection("produits").find({type:categorie}).toArray((err, documents) => {
								res.end(JSON.stringify(documents));
						});
				} catch(e) {
						console.log("Erreur sur /produits/"+categorie+" : "+ e);
						res.end(JSON.stringify([]));
				}
		});

		/* Liste des catégories de produits */
		app.get("/categories", (req,res) => {
				console.log("/categories");
			categories = [];
				try {
						db.collection("produits").find().toArray((err, documents) => {
				for (let doc of documents) {
								if (!categories.includes(doc.type)) categories.push(doc.type);
				}
						res.end(JSON.stringify(categories));
						});
				} catch(e) {
						console.log("Erreur sur /categories : " + e);
						res.end(JSON.stringify([]));
				}
		});

		/* Prix moyen des produits */
		app.get("/produits/prixmoyen", (req,res) => {
			console.log("route : /prixmoyen");
			db.collection("produits").find().toArray((err, documents) => {
				prixmoyen = 0;
				if(documents.length > 0){
					sum = 0;
					for (let d of documents) {
						sum += d.prix;
					}
					prixmoyen = sum / documents.length;
				}
				res.end(JSON.stringify({"prixmoyen":prixmoyen}));
			});
		});

		/* Prix superieur au prix moyen des produits */
		app.get("/produits/superieurprixmoyen", (req,res) => {
			console.log("route : /superieurprixmoyen");
			prixmoyen = 0;
			db.collection("produits").find().toArray((err, documents) => {
				if(documents.length > 0){
					sum = 0;
					for (let d of documents) {
						sum += d.prix;
					}
					prixmoyen = sum / documents.length;
					console.log(prixmoyen);
					db.collection("produits").find({"prix":{"$gt":prixmoyen}}).toArray((err, documents) => {
						res.end(JSON.stringify(documents));
					});
				}
			});
		});

		/* Connexion */
		app.post("/membre/connexion", (req,res) => {
				try {
						db.collection("membres")
						.find(req.body)
						.toArray((err, documents) => {
								if (documents != undefined && documents.length == 1)
										res.end(JSON.stringify({"resultat": 1, "message": "Authentification réussie\n"}));
								else res.end(JSON.stringify({"resultat": 0, "message": "Email et/ou mot de passe incorrect\n"}));
						});
				} catch (e) {
						res.end(JSON.stringify({"resultat": 0, "message": e}));
				}
		});

		// /* Ajout de produit */
		// app.post("/produit/ajout", (req,res) => {
		// 	console.log("\n");
		// 	console.log("route /produit/ajout avec " + JSON.stringify(req.body));
		// 		try {
		// 			db.collection("produits").insertOne(req.body);
		// 			res.end(JSON.stringify({"message":"ajout de produit effectuer\n"}));
		// 		} catch (e) {
		// 			res.end(JSON.stringify({"message":"probleme sur l'ajout de produit"+e+"\n"}));
		// 		}
		// });

		/* Ajout de produit */
		app.put("/produit/ajout", (req,res) => {
			console.log("\n");
			console.log("route /produit/ajout avec " + JSON.stringify(req.body));
				try {
					db.collection("produits").insertOne(req.body);
					res.end(JSON.stringify({"message":"ajout de produit effectuer\n"}));
				} catch (e) {
					res.end(JSON.stringify({"message":"probleme sur l'ajout de produit"+e+"\n"}));
				}
		});
});

// Convention CRUD
//	C,	create	<==	PUT()
//	R,	read	<==	GET()
//	U,	update	<==	POST()
//	D,	delete	<==	DELETE()

app.listen(8888);
