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
    public produits: Object[];
    
    constructor(public produitsService: ProduitsService) {
       console.log("Dans le constructeur du composant produits");
    }
    
    ngOnInit() {
       console.log("Dans ngOnInit() du composant produits");    
       this.produitsService.getProduits().subscribe(produits => {
            this.produits = produits.slice();
       });
    }
}
