import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Router } from '@angular/router'

import { ActivatedRoute, Params } from '@angular/router';
import {FormsModule} from '@angular/forms';

import { AuthentificationService } from '../authentification.service'
import { ProduitsService } from '../produits.service';

const httpOptions = {
	headers: new HttpHeaders({
		"Access-Control-Allow-Methods": "GET,POST",
		"Access-Control-Allow-Headers": "Content-type",
		"Content-Type": "application/json",
		"Access-Control-Allow-Origin": "*"
	})
};

//

@Component({
  selector: 'app-recherche',
  templateUrl: './recherche.component.html',
  styleUrls: ['./recherche.component.scss']
})

export class RechercheComponent implements OnInit {

	message: string = "";
	criteres: any = {"text" : {"critere-1": ["foo", "bar"] }, "number" : ["critere-2"] };
	criteresChoisis: any = { "text" : {"critere-1":"foo"} , "number" :  {"critere-2": { 'min':"22", "max":"99" } } };
	// recup les criteres de la bdd
  constructor(private router: Router,
								private route: ActivatedRoute,
								private authService: AuthentificationService,
								private produitsService: ProduitsService) { }

  ngOnInit(): void {
		this.produitsService.getCriteres().subscribe((reponse : any ) => {
			console.log("hey subscribe recheche");
			this.criteres = reponse;
			console.log(this.criteres);
			this.criteresChoisis["text"] = {}
			this.criteresChoisis["number"] = {}
			for(var crit of Object.keys(this.criteres["text"]) ){
				this.criteresChoisis["text"][crit] = "";
			}
			console.log("printing numbers")
			console.log(this.criteres["number"]);
			this.criteresChoisis["number"] = {};
			for(var crit2 of this.criteres["number"]){
				console.log("creating min and max for " + crit2);
				this.criteresChoisis["number"][crit2] = {};
				this.criteresChoisis["number"][crit2]["min"] = null;
				this.criteresChoisis["number"][crit2]["max"] = null;
			}
			console.log(this.criteresChoisis)
		});
	}

	onSubmit(): void {
		console.log("hey onSubmit() recherche critere");

		let texte = this.criteresChoisis['text'];
		for (var crit in texte){
			if(texte.hasOwnProperty(crit) && texte[crit] == ''){
				delete texte[crit];
			}
		}
		let prix = this.criteresChoisis['number'];
		for (var crit in prix){
			if(prix.hasOwnProperty(crit) && prix[crit]['min'] === null){
				delete prix[crit]['min']
			}
			if(prix.hasOwnProperty(crit) && prix[crit]['max'] === null){
				delete prix[crit]['max']
			}
			if (Object.keys(prix[crit]).length == 0){
				delete prix[crit]
			}

		}
		this.produitsService.recherche(this.criteresChoisis);
		this.router.navigate(['/produits', 'recherche' ]);
	}

	getTextKeys() : any {
		return Object.keys(this.criteres["text"]);
	}

	getNumberKeys() : any {
		return (this.criteres["number"]);
	}
	//
	// isNaN(critere : any) : boolean {
	// 	return isNaN(critere);
	// }
	//
	// isNumber(critere : any) : boolean {
	// 	return !isNaN(critere);
	// }

}
