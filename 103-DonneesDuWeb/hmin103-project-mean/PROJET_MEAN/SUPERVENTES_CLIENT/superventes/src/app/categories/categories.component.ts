import { Component, OnInit } from '@angular/core';
import { ProduitsService } from '../produits.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {

  public categories : any;

  constructor(private produitsService : ProduitsService) { }

  ngOnInit(): void {

         this.produitsService.getCategories().subscribe(categories => {
            this.categories = categories.slice();
       });

  }

}
