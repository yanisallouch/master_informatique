using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Exercice3_Serveur.Models
{
    public class Hotel
    {
        protected static int compteur = 0;

        protected int numeroHotel;
        protected String nomHotel;
        protected String paysHotel;
        protected String villeHotel;
        protected String rueHotel;
        protected String lieuDitHotel;
        protected String positionGPSHotel;
        protected int nombreEtoile;
        protected List<Chambre> listeChambre;


        public Hotel(String nomHotel, String paysHotel, String villeHotel, String rueHotel, String lieuDitHotel,
                     String positionGPSHotel, int nombreEtoile, List<Chambre> listeChambre)
        {
            compteur = compteur + 1;
            this.NumeroHotel = compteur;
            this.NomHotel = nomHotel;
            this.PaysHotel = paysHotel;
            this.VilleHotel = villeHotel;
            this.RueHotel = rueHotel;
            this.LieuDitHotel = lieuDitHotel;
            this.PositionGPSHotel = positionGPSHotel;
            this.NombreEtoile = nombreEtoile;
            this.ListeChambre = listeChambre;
        }

        public int NumeroHotel { get => numeroHotel; set => numeroHotel = value; }
        public string NomHotel { get => nomHotel; set => nomHotel = value; }
        public string PaysHotel { get => paysHotel; set => paysHotel = value; }
        public string VilleHotel { get => villeHotel; set => villeHotel = value; }
        public string RueHotel { get => rueHotel; set => rueHotel = value; }
        public string LieuDitHotel { get => lieuDitHotel; set => lieuDitHotel = value; }
        public string PositionGPSHotel { get => positionGPSHotel; set => positionGPSHotel = value; }
        public int NombreEtoile { get => nombreEtoile; set => nombreEtoile = value; }
        public List<Chambre> ListeChambre { get => listeChambre; set => listeChambre = value; }
    }
}