import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthentificationService } from '../authentification.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-inscription',
  templateUrl: './inscription.component.html',
  styleUrls: ['./inscription.component.scss']
})
export class InscriptionComponent implements OnInit {

  public futureUser = {"email":"", "password": "", "nom": "", "prénom" : ""}
  message: string = "";
  proposerConnexion = false;

  constructor(private authService: AuthentificationService, private router: Router) { }

  onSubmit(){
    this.proposerConnexion = false;
    console.log("in onSubmit of inscription futureUser is " + this.futureUser)
    if (Object.values(this.futureUser).includes("")){
      this.message= "Tous les champs doivent être remplis"
      return
    }
    let email = {"email": this.futureUser["email"]};
  	this.authService.verificationConnexion(email).subscribe((reponse : any) =>{
  		console.log(this.message);
  		if (reponse['resultat']){
        this.proposerConnexion = true;
        this.message = "Email déjà utilisé"
  		}
      else {
        console.log("Email n'existe pas on peut créer");
        this.authService.creerUtilisateur(this.futureUser).subscribe((reponse :any) => {
          this.message = reponse['message']
          console.log(reponse)
          if (reponse['resultat'] == 1){
            this.authService.connect(this.futureUser["email"])
            setTimeout(() => {
              this.router.navigate(['/produits'])
            }, 1500);

          }
        });



      }
  		//setTimeout( () => {this.router.navigate(['/categories']);}, 1000);
  	});
  }

  ngOnInit() : void{

  }


}
