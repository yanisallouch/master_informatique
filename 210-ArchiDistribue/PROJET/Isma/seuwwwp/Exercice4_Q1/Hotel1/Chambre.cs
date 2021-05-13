using System;
using System.Collections.Generic;
using System.Text;

namespace Hotel1
{
    class Chambre
    {
        public int idChambre;
        public int numeroChambre;
        public int nbrLit;
        public double prix;
        public int nbrPersonneMax;

        public Chambre(int idChambre, int numeroChambre, int nbrLit, double prix, int nbrPersonneMax)
        {
            this.IdChambre = idChambre;
            this.NumeroChambre = numeroChambre;
            this.NbrLit = nbrLit;
            this.Prix = prix;
            this.NbrPersonneMax = nbrPersonneMax;
        }

        public int IdChambre { get => idChambre; set => idChambre = value; }
        public int NumeroChambre { get => numeroChambre; set => numeroChambre = value; }
        public int NbrLit { get => nbrLit; set => nbrLit = value; }
        public double Prix { get => prix; set => prix = value; }
        public int NbrPersonneMax { get => nbrPersonneMax; set => nbrPersonneMax = value; }

        public String afficher()
        {
            return "NumeroChambre : " + NumeroChambre + "   NbrLit : " + NbrLit + "    Prix : " + Prix;
        }
    }
}
