import { Component, OnInit } from '@angular/core';
import { AuthentificationService } from '../authentification.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ProduitsService } from '../produits.service';
import { Observable } from 'rxjs';
import { PanierService } from '../panier.service';

@Component({
  selector: 'app-produits',
  templateUrl: './produits.component.html',
  //styleUrls: ['./produits.component.css']
})
export class ProduitsComponent implements OnInit {
    public produits: any;

    public user: Observable<string | undefined >;

    public inACategory : boolean = false;
    public quantites : Array<number> = [];

    constructor(
                  private router: Router,
                  private panierService : PanierService,
                  private route: ActivatedRoute,
                  private authService: AuthentificationService,
                  private produitsService: ProduitsService) {
       console.log("Dans le constructeur du composant produits")
       this.user = this.authService.getUser();
    }

    ngOnInit() {
      this.inACategory = false;
      this.route.params.subscribe((params: Params) => {
        console.log("--Invocation du composant produits avec "+ params["recherche"]);
        console.log("--" + this.router.url )
        if (this.router.url === "/produits/recherche"){
          console.log("in ngOninit depuis une recherhce")
          this.produitsService.getResultatsRecherche().subscribe((reponse : any) => {
            this.produits = reponse;
          });
        }
        else if (params["categorie"] !== undefined){
          // params["categorie"] renvoie => "recherche" ?
          this.inACategory = true;
          // console.log("/produits/"+params['categorie']);
          this.produitsService.getProduitsParCategorie(params['categorie']).subscribe((produits : any) => {
            this.produits = produits;
          });
        }
        else {
          this.inACategory = false;
          this.produitsService.getProduits().subscribe(produits => {
          this.produits = produits.slice();});
        }
      });
    }

    getKeys(produit : any) : any {
      // je coupe _id
      return Object.keys(produit).slice(1,produit.length);
    }

    ajouterPanier(nom : string, marque: string, quantite : number, prix: number){
      if (quantite == undefined) quantite = 1;
      this.panierService.addProductToPanier(nom, marque, quantite, prix);
    }
}
