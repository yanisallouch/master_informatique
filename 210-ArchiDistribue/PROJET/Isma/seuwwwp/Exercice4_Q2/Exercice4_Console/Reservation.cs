using System;
using System.Collections.Generic;
using System.Text;

namespace Exercice4_Console
{
    public class Reservation
    {
        protected static int compteur = 0;

        protected int numeroReservation;
        protected String dateArrivee;
        protected String dateDepart;
        protected int nombrePersonne;
        protected Client client;
        protected Chambre chambre;


        public Reservation(String dateArrivee, String dateDepart, int nombrePersonne, Client client, Chambre chambre)
        {
            compteur = compteur + 1;
            this.NumeroReservation = compteur;
            this.DateArrivee = dateArrivee;
            this.DateDepart = dateDepart;
            this.NombrePersonne = nombrePersonne;
            this.Client = client;
            this.Chambre = chambre;
        }

        public int NumeroReservation { get => numeroReservation; set => numeroReservation = value; }
        public string DateArrivee { get => dateArrivee; set => dateArrivee = value; }
        public string DateDepart { get => dateDepart; set => dateDepart = value; }
        public int NombrePersonne { get => nombrePersonne; set => nombrePersonne = value; }
        public Client Client { get => client; set => client = value; }
        public Chambre Chambre { get => chambre; set => chambre = value; }
    }
}
