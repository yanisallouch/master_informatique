import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ProduitsComponent} from './produits/produits.component';
import {CategoriesComponent} from './categories/categories.component';
import {PanierComponent} from './panier/panier.component';
import { ConnexionComponent } from './connexion/connexion.component';
import {RechercheComponent} from './recherche/recherche.component'
import { InscriptionComponent } from './inscription/inscription.component';

const routes: Routes = [
	{
		path: 'produits',
		component: ProduitsComponent
	},
	{
		path: 'produits/recherche',
		component: ProduitsComponent
	},
	{
		path: 'produits/:categorie',
		component: ProduitsComponent
	},
	{
		path: 'recherche',
		component: RechercheComponent
	},
	{
		path: 'categories',
		component: CategoriesComponent
	},
	{
		path: 'panier/ajout/:nom/:marque',
		component: PanierComponent
	},
	{
		path: 'panier/ajout/:nom/:marque/:quantite',
		component: PanierComponent
	},
	{
		path:'panier',
		component: PanierComponent
	},
	{
		path: 'connexion',
		component: ConnexionComponent

	},
	{
		path: 'inscription',
		component: InscriptionComponent
	}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
