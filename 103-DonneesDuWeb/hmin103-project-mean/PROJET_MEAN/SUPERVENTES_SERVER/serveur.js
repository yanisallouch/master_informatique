const express = require('express');
const app     = express();
app.use(express.json());
app.use(express.urlencoded({extended:true}));
app.use(function (req, res, next) {
		res.setHeader('Access-Control-Allow-Origin', '*');
		res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
		res.setHeader('Access-Control-Allow-Headers', '*');
		res.header("Content-Type", "application/json");
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


		/* Liste des criteres sur produits */
		app.get("/produits/criteres", (req,res) => {
			console.log("/produits/criteres");
			let criteres = {};
			let criteresNombre = [];
			let criteresText = [];

			//pour trouver les clés
			db.collection("produits").find().limit(1).toArray( (err, doc) => {
			console.log("callback find");
			console.log(doc)
			for (var key of Object.keys((doc[0])) ){
				console.log("key ="  +key);
				if(key!="_id"){
					if(isNaN(doc[0][key])){
						criteresText.push(key);
					}else{
						criteresNombre.push(key);
					}
				}
			}
			console.log(criteresText)
			console.log(criteresNombre)
			criteres["number"] = criteresNombre;
			criteres["text"] = {};
			(function (criteresText){
				console.log("critere fnt");
				if(criteresText.length == 0){
					console.log("lenght is 0")
					console.log("\nCriteres is finally")
					console.log(criteres)
					res.end(JSON.stringify(criteres));
					return
				}
				else{
					let critere = criteresText.pop();
					console.log("fonction critere pour critere " + critere);
					try{
						db.collection("produits").distinct( critere, (err2, doc2) => {
							console.log("doc2 =");
							console.log(doc2);
							criteres["text"][critere] = doc2;
							arguments.callee(criteresText);

						});
					}catch(e) {
						console.log("Erreur sur /produits/criteres : " + e);
					}
			}
			})(criteresText);
			});
			console.log("fin de fnt");
		});


		app.post("/produits/recherche", (req,res) => {
			 console.log("here /produit/recherche");
			 console.log(req.body);
			 let criteresText = req.body["text"];
			 let criteresNumber = req.body["number"];

			 let filter = criteresText;
			 for (let key of Object.keys(criteresNumber)) {
				 let minMaxFilter = {} ;
				 if (criteresNumber[key].hasOwnProperty('min')){
					 minMaxFilter['$gte'] = criteresNumber[key]['min'];
				 }
				 if (criteresNumber[key].hasOwnProperty('max')) {
					 minMaxFilter['$lte'] = criteresNumber[key]['max'];
				 }
				 //let min = criteresNumber[key]['min'];
				 //let max = criteresNumber[key]['max'];
				 //{$lt:max,$gt:min}
				 filter[key] = minMaxFilter;
			 }
			 console.log(filter)
			 try {
				 db.collection("produits").find(filter).toArray((err, documents) => {
					 console.log(documents)
					 res.end(JSON.stringify(documents));
				 });
			 } catch(e) {
				 console.log("Erreur sur produits/rechecher"+ e);
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

		/* Connexion */
		app.post("/membre/connexion", (req,res) => {
				try {
						console.log(req.body);
						db.collection("membres")
						.find(req.body)
						.toArray((err, documents) => {
								if (documents != undefined && documents.length == 1)
										res.end(JSON.stringify({"resultat": 1, "message": "Authentification réussie"}));
								else res.end(JSON.stringify({"resultat": 0, "message": "Email et/ou mot de passe incorrect"}));
						});
				} catch (e) {
						res.end(JSON.stringify({"resultat": 0, "message": e}));
				}
		});

		/* Inscription */
		app.post("/membre/inscription", (req,res) => {
				try {
						//console.log(req.body);
						let promise = db.collection("membres")
						.insertOne(req.body, (err, res1) => {
							if (err) console.log(err)
							else {
								console.log("inserted record into membres")
								console.log(res1.ops)
								res.end(JSON.stringify({"resultat": 1, "message": "Inscription réussie"}))
							}
						});
				} catch (e) {
					console.log("exception caught")
					console.log(e)
						res.end(JSON.stringify({"resultat": 0, "message": "Inscription échouée: erreur"}));
				}
		});

		app.post("/panier", (req,res) => {
				console.log(req.body);
				try {
						db.collection("paniers")
						.find(req.body)
						.toArray((err, documents) => {
								if (documents != undefined && documents.length == 1){
										let reponse  = {"resultat":1, "panier":documents[0]}
										res.end(JSON.stringify(reponse));
										console.log(reponse['panier']);
								}
								else {
										res.end(JSON.stringify({"resultat":0}));
										console.log("Erreur, plusieurs paniers trouvés");
								}
						});
				} catch (e) {
						res.end(JSON.stringify({"resultat": 0, "message": e}));
				}
		});

		app.post("/panier/update", (req,res) => {
			let obj = req.body;
			console.log(obj);
			try {
					db.collection("paniers").updateOne(
						{"email":obj["email"]},
						{
							$set:{
								"produits": obj['contenu']
							}
						}).toArray((err, result) => {
							console.log(result);
							console.log("stringify goes here");

						});
					}
			catch (e) {
					res.end(JSON.stringify({"resultat": 0, "message": e}));
			}
		});

		app.post("/panier/addproduct", (req,res) => {
			let obj = req.body;
			console.log("/panier/addProduct");
			console.log(obj);
			let produit = obj["ligneCommande"];
			try{
				db.collection("paniers").find({"email":obj["email"]}).toArray((err, documents) => {
							if (documents != undefined && documents.length==0){
									console.log("zero");
									db.collection("paniers").updateOne(
										{"email":obj["email"]},
										{
											$set:{
												"email": obj["email"],
												"produits": [produit]
											}
										}
									,{upsert:true}).then(result => {
										console.log("in result");
									});

							}
							else if (documents.length == 1){
									res.end(JSON.stringify({"resultat":0}));
									console.log("1 panier trouvé trouvés");
									db.collection("paniers").updateOne(
										{"email":obj["email"]},
										{
											$addToSet:{
												"produits": { $each: [produit]}
											}
										}
									,{upsert:true}).then(result => {
										console.log("in result after appending to panier");
									});
							}
							else {
								console.log("problem")
							}
					});
			} catch (e) {
					res.end(JSON.stringify({"resultat": 0, "message": e}));
			}

		});


		app.post("/panier/delete", (req,res) => {
			console.log("removing")
			console.log(req.body)
			try{
				db.collection("paniers").deleteOne(req.body, (err, result) => {
					console.log("deleted " + result.deletedCount + " records")
					res.end(JSON.stringify({"resultat":1}));

			});
		} catch (e) {
		res.end(JSON.stringify({"resultat": 0, "message": e}));
		}




		});

});

app.listen(8888);
