import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthentificationService } from '../authentification.service'
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-connexion',
  templateUrl: './connexion.component.html',
  styleUrls: ['./connexion.component.scss']
})
export class ConnexionComponent implements OnInit {
	utilisateur = {"email":"", "password": ""};
	message: string = "";

  constructor(private authService: AuthentificationService, private router: Router) { }

  onSubmit(){
  	this.authService.verificationConnexion(this.utilisateur).subscribe((reponse : any) =>{
  		this.message = reponse['message'];
  		console.log(this.message);
  		if (reponse['resultat']){
  			this.authService.connect(this.utilisateur.email);
  			this.router.navigate(['/categories']);
  		}
  		//setTimeout( () => {this.router.navigate(['/categories']);}, 1000);
  	});
  }

  ngOnInit(): void {
  }

}
