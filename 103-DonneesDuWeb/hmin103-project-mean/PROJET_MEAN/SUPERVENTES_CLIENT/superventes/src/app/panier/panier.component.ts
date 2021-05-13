import { Component, OnInit } from '@angular/core';
import { AuthentificationService } from '../authentification.service';
import { ActivatedRoute, Params } from '@angular/router';
import { PanierService} from '../panier.service';
import { Subject, BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  //styleUrls: ['./panier.component.css']
})
export class PanierComponent implements OnInit {
  public contenu: any

  public message : string = "";

  public user: BehaviorSubject<string>;

  constructor(
                private route: ActivatedRoute,
                private authService: AuthentificationService,
                private panierService: PanierService) {
     console.log("Dans le constructeur du composant panier")
     this.user = this.authService.getUser();
  }

  ngOnInit() {
        let email = this.user.value;
        console.log("Dans ngOnInit de PanierComponent");
        if (email!== "undefined"){
          console.log("fetching panier for "+ email);
          this.panierService.getPanier(email).subscribe(panier => {
          if (panier['resultat'] ==1){
            this.contenu = panier['panier'].produits;
            console.log(this.contenu)
          }
          });
        }
      }

  supprimer(indice: number){
    console.log("should delete product number "+ indice);
    this.contenu.splice(indice,1);
  }

  savePanier(){
    console.log("Should save panier");
    this.panierService.updatePanier({"email":this.user.value, "contenu":this.contenu}).subscribe(reponse=> {
      if (reponse['code'] == 0){
        console.log("Erreur in updating");
      }
      else console.log("Panier updated");

    })
  }

  submitPanier(){
    console.log("Submitting panier");
    this.panierService.supprimerPanier().subscribe((reponse : any)=> {
      console.log(reponse)
      if (reponse['resultat']){
        this.message = "Commande validée!"
      }
    });
  }
}

/*



*/
