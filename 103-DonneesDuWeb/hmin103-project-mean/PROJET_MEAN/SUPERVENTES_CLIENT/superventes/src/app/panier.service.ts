import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthentificationService } from './authentification.service';

const httpOptions = {
	headers: new HttpHeaders({
		"Access-Control-Allow-Methods": "GET,POST",
		"Access-Control-Allow-Headers": "Content-type",
		"Content-Type": "application/json",
		"Access-Control-Allow-Origin": "*"
	})
};

@Injectable({
	providedIn: 'root'
})
export class PanierService {
	private urlBase: string = 'http://localhost:8888/';
	private user: string = "undefined";




	constructor(private authService: AuthentificationService,
							private http: HttpClient) {
//
								this.authService.getUser().subscribe((user :string) => {
									this.user = user;
								});

	 }

	getPanier(email : string): Observable<any> {
		let emailObj = {"email": email};
		let url = this.urlBase+'panier/';
		console.log("Dans le service PanierService avec "+ url + "et " + email);
		return this.http.post(url, JSON.stringify(emailObj), httpOptions);
	}

	updatePanier(panier : any): Observable<any> {
		let url = this.urlBase+'panier/update';
		console.log("Dans le service PanierService avec "+ url + "et " + panier.email);
		return this.http.post(url, JSON.stringify(panier), httpOptions);
	}

	//methode pour ajouter depuis product
	addProductToPanier(nom : string, marque: string, quantite : number, prix : number){
		console.log("in service, ajouter au panier de " + this.user + " " + nom +" "+ prix +  " " + marque + " " + quantite);
		let url = this.urlBase + 'panier/addproduct';
		let obj = {"email":this.user, "ligneCommande" : {"nom":nom , "marque": marque, "quantite":quantite, "prix" : prix}};
		console.log(url);
		// console.log(obj);
		this.http.post(url, JSON.stringify(obj), httpOptions).subscribe();
		console.log("after call");


	}

	supprimerPanier(){
		console.log("in service panier, trying to delete")
		return this.http.post(this.urlBase+"panier/delete", JSON.stringify({"email":this.user}), httpOptions);

	}
}


/*
		getProduits(): Observable<any> {
				let url = this.urlBase+'produits';
	console.log("Dans le service ProduitsService avec "+url);
				return this.http.get(url);
		}

		getCategories(): Observable<any>{
			let url = this.urlBase+'categories';
			console.log("Dans le service ProduitsService avec "+url)
			return this.http.get(url);

		}

		getProduitsParCategorie(categorie : any) : Observable<any> {
				return this.http.get(this.urlBase+'produits/'+categorie);
		}


}
*/
