import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

const httpOptions = {
	headers: new HttpHeaders({
		"Access-Control-Allow-Methods": "GET,POST",
		"Access-Control-Allow-Headers": "Content-type",
		"Content-Type": "application/json",
		"Access-Control-Allow-Origin": "*"
	})
};

@Injectable({ providedIn: 'root' })
export class ProduitsService {
    private urlBase: string = 'http://localhost:8888/';

		private resultatRecherche: Observable<any> = new Observable();

    constructor(private http: HttpClient) { }

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

		recherche(critere : any) {
			this.resultatRecherche = this.http.post(this.urlBase+'produits/recherche',JSON.stringify(critere), httpOptions);
		}

		getCriteres() : Observable<any> {
			return this.http.get(this.urlBase+'produits/criteres');
		}

		getResultatsRecherche() : Observable<any> {
			return this.resultatRecherche;
		}

}
