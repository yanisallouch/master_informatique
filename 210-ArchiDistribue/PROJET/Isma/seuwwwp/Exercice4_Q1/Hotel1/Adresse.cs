using System;
using System.Collections.Generic;
using System.Text;

namespace Hotel1
{
    class Adresse
    {
        public int idAdresse;
        public String pays;
        public String ville;
        public String rue;
        public int numero;
        public String positionGPS;

        public Adresse(int idAdresse, string pays, string ville, string rue, int numero, string positionGPS)
        {
            this.IdAdresse = idAdresse;
            this.Pays = pays;
            this.Ville = ville;
            this.Rue = rue;
            this.Numero = numero;
            this.PositionGPS = positionGPS;
        }

        public int IdAdresse { get => idAdresse; set => idAdresse = value; }
        public string Pays { get => pays; set => pays = value; }
        public string Ville { get => ville; set => ville = value; }
        public string Rue { get => rue; set => rue = value; }
        public int Numero { get => numero; set => numero = value; }
        public string PositionGPS { get => positionGPS; set => positionGPS = value; }
    }
}
