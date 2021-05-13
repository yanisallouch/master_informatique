using Exercice3_Serveur.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Exercice3_Serveur.Controllers
{
    public class ComparateurController : ApiController
    {
        [Route("Trivago/Comparateur")]
        [Obsolete]
        public string Get(string ville, string dateDebut, string dateFin, int nombrePersonne, int nombreEtoile)
        {
            String url = "https://localhost:44311/";

            string offersLapeyronie = GetReleases(url + "HotelLapeyronie/GetListeOffre?dateDebut=" + dateDebut + "&dateFin=" + dateFin + "&nombrePersonne=" + nombrePersonne + "&ville=" + ville + "&nombreEtoile=" + nombreEtoile);
            string offersIbis = GetReleases(url + "HotelIbis/GetListeOffre?dateDebut=" + dateDebut + "&dateFin=" + dateFin + "&nombrePersonne=" + nombrePersonne + "&ville=" + ville + "&nombreEtoile=" + nombreEtoile);
            string offersKyriad = GetReleases(url + "HotelKyriad/GetListeOffre?dateDebut=" + dateDebut + "&dateFin=" + dateFin + "&nombrePersonne=" + nombrePersonne + "&ville=" + ville + "&nombreEtoile=" + nombreEtoile);

            return offersLapeyronie + offersIbis + offersKyriad;
        }

        [Route("Trivago/GetReleases")]
        [Obsolete]
        public String GetReleases(String url)
        {
            System.Net.ServicePointManager.CertificatePolicy = new TrustAllCertificatePolicy();

            var request = (HttpWebRequest)WebRequest.Create(url);

            request.Method = "GET";
            request.AutomaticDecompression = DecompressionMethods.Deflate | DecompressionMethods.GZip;

            var content = string.Empty;
            using (var response = (HttpWebResponse)request.GetResponse())
            {
                using (var stream = response.GetResponseStream())
                {
                    using (var sr = new StreamReader(stream))
                    {
                        content = sr.ReadToEnd();
                    }
                }
            }

            String resultat = content.ToString();
            return resultat.Replace("\"", "");
        }
    }
}
