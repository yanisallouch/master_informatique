using System;
using System.Collections.Generic;
using System.Text;

namespace Hotel1
{
    class Client
    {
        public int idClient;
        public String nomClient;
        public String prenomClient;
        public String dateNaissanceClient;
        public String carteCreditClient;

        public Client(int idClient, string nomClient, string prenomClient, String dateNaissanceClient, string carteCreditClient)
        {
            this.IdClient = idClient;
            this.NomClient = nomClient;
            this.PrenomClient = prenomClient;
            this.dateNaissanceClient = dateNaissanceClient;
            this.CarteCreditClient = carteCreditClient;
        }

        public int IdClient { get => idClient; set => idClient = value; }
        public string NomClient { get => nomClient; set => nomClient = value; }
        public string PrenomClient { get => prenomClient; set => prenomClient = value; }
        public String DateNaissanceClient { get => dateNaissanceClient; set => dateNaissanceClient = value; }
        public string CarteCreditClient { get => carteCreditClient; set => carteCreditClient = value; }
    }
}
