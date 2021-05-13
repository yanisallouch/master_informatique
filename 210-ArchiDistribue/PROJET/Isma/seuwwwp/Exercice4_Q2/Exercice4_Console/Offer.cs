using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Exercice4_Console
{
    public class Offer
    {
        private string idOffre;
        private Chambre chambre;
        private string dateDisponibilite;
        private int prixOffre;
        private string imageChambreURL;
        private bool disponible;

        public Offer()
        {

        }

        public Offer(string idOffre, Chambre chambre, string dateDisponibilite, int prixOffre, string imageChambreURL, bool disponible)
        {
            this.IdOffre = idOffre;
            this.Chambre = chambre;
            this.DateDisponibilite = dateDisponibilite;
            this.PrixOffre = prixOffre;
            this.ImageChambreURL = imageChambreURL;
            this.Disponible = disponible;
        }

        public string IdOffre { get => idOffre; set => idOffre = value; }
        public Chambre Chambre { get => chambre; set => chambre = value; }
        public string DateDisponibilite { get => dateDisponibilite; set => dateDisponibilite = value; }
        public int PrixOffre { get => prixOffre; set => prixOffre = value; }
        public bool Disponible { get => disponible; set => disponible = value; }
        public string ImageChambreURL { get => imageChambreURL; set => imageChambreURL = value; }
    }
}
