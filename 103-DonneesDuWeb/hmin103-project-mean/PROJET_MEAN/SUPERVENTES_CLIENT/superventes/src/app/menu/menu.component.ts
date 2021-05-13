import { Component, OnInit } from '@angular/core';
import {ProduitsService} from '../produits.service';
import { AuthentificationService } from '../authentification.service';
import { Observable } from 'rxjs';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

    public user: Observable<string>;
    public userName: string = "";
    constructor(private route: ActivatedRoute,
                  private authService: AuthentificationService,
                  private produitsService: ProduitsService) {
       console.log("Dans le constructeur du composant produits")
       this.user = this.authService.getUser();
       this.user.subscribe(user => {
         this.userName = user
       });
    }

  ngOnInit(): void {

  }

  deconnexion(){
  	this.authService.disconnect();
  }


  connexionForcee(){
  	console.log("connexion forcée");
  	this.authService.connect("hey");
  	console.log("user is now "+ this.authService.getUser());
  }

}
