import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ProduitsService } from '../produits.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-produits',
	templateUrl: './produits.component.html',
	styleUrls: ['./produits.component.css']
})
export class ProduitsComponent implements OnInit {
	private produits: Object[];
	constructor(private produisService: ProduitsService) {
		console.log("Dans le constructeur du composant produits");
	}
	ngOnInit(): void {
		console.log("Dans ngOnInit() du composant produits");
		this.produisService.getProduits().subscribe(produits => {
			this.produits = produits;
		});
	}
}
