using System;
using System.Collections.Generic;
using System.Text;

namespace Exercice4_Console
{
    public class Client
    {
        protected static int compteur = 0;

        protected int idClient;
        protected String nomClient;
        protected String prenomClient;
        protected String carteCredit;

        public Client()
        {

        }
        public Client(String nomClient, String prenomClient, String carteCredit)
        {
            compteur = compteur + 1;
            this.IdClient = compteur;
            this.NomClient = nomClient;
            this.PrenomClient = prenomClient;
            this.CarteCredit = carteCredit;
        }

        public int IdClient { get => idClient; set => idClient = value; }
        public string NomClient { get => nomClient; set => nomClient = value; }
        public string PrenomClient { get => prenomClient; set => prenomClient = value; }
        public string CarteCredit { get => carteCredit; set => carteCredit = value; }
    }
}
