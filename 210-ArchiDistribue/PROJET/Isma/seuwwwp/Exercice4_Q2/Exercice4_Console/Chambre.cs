using System;
using System.Collections.Generic;
using System.Text;

namespace Exercice4_Console
{
    public class Chambre
    {
        private static int compteur = 0;

        protected int numeroChambre;
        protected int nombrePersonne;
        protected int nombreLit;
        protected int prixChambre;
        protected string imageURL;

        public Chambre(int nombrePersonne, int nombreLit, int prixChambre, string imageURL)
        {
            Compteur = Compteur + 1;
            this.NumeroChambre = Compteur;
            this.NombrePersonne = nombrePersonne;
            this.NombreLit = nombreLit;
            this.PrixChambre = prixChambre;
            this.ImageURL = imageURL;
        }

        protected static int Compteur { get => compteur; set => compteur = value; }
        public int NumeroChambre { get => numeroChambre; set => numeroChambre = value; }
        public int NombrePersonne { get => nombrePersonne; set => nombrePersonne = value; }
        public int NombreLit { get => nombreLit; set => nombreLit = value; }
        public int PrixChambre { get => prixChambre; set => prixChambre = value; }
        public string ImageURL { get => imageURL; set => imageURL = value; }
    }
}
