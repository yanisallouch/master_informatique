using System;
using System.Collections.Generic;
using System.Text;

namespace Hotel1
{
    class Reservation
    {
        public int idReservation;
        public String dateArrive;
        public String dateDepart;
        public double prix;
        public int nbrPersonne;
        public int idClient;
        public int idChambre;

        public Reservation(int idReservation, String dateArrive, String dateDepart, double prix, int nbrPersonne)
        {
            this.IdReservation = idReservation;
            this.dateArrive = dateArrive;
            this.dateDepart = dateDepart;
            this.Prix = prix;
            this.NbrPersonne = nbrPersonne;
        }

        public int IdReservation { get => idReservation; set => idReservation = value; }
        public String DateArrive { get => dateArrive; set => dateArrive = value; }
        public String DateDepart { get => dateDepart; set => dateDepart = value; }
        public double Prix { get => prix; set => prix = value; }
        public int NbrPersonne { get => nbrPersonne; set => nbrPersonne = value; }
    }
}
