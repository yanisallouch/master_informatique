using System;
using System.Collections.Generic;
using System.Text;

namespace Hotel1
{
    class Hotel
    {
        public int idHotel;
        public String nomHotel;
        public int nbrEtoile;
        public int idAdresse;
        public List<Chambre> listeChambre;

        public Hotel(int idHotel, string nomHotel, int nbrEtoile, List<Chambre> listeChambre)
        {
            this.IdHotel = idHotel;
            this.NomHotel = nomHotel;
            this.NbrEtoile = nbrEtoile;
            this.ListeChambre = listeChambre;
        }

        public int IdHotel { get => idHotel; set => idHotel = value; }
        public string NomHotel { get => nomHotel; set => nomHotel = value; }
        public int NbrEtoile { get => nbrEtoile; set => nbrEtoile = value; }
        public List<Chambre> ListeChambre { get => listeChambre; set => listeChambre = value; }
    }
}
