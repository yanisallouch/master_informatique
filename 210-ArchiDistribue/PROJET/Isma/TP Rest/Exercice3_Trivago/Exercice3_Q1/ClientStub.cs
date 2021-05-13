using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Exercice3_Q1
{
    class ClientStub
    {
        static bool CheckInt(string a)
        {

            int num = 0;
            if (int.TryParse(a, out num))
            {
                return true;
            }

            return false;
        }

        static void pause()
        {
            TextWriter writer = new StringWriter();
            for (int i = 0; i < 3; i++)
            {
                Thread.Sleep(1000);
                Console.Write(".");
            }
            Thread.Sleep(1000);
        }

        static String GetReleases(String url)
        {
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

        [Obsolete]
        static void Main(string[] args)
        {
            System.Net.ServicePointManager.CertificatePolicy = new TrustAllCertificatePolicy();
            String url = "https://localhost:44311/";

            while (true)
            {
                Console.WriteLine("\n   ===== Bienvenue dans Trivago, le meilleur comparateur des offres =====\n");
                Console.WriteLine("      ***** Veuillez saisir les informations de l'hotel souhaité *****\n");
                Console.Write("\n      Nombre de personne : ");
                string nombrePersonne = Console.ReadLine();
                Console.Write("\n      Nombre d'étoile    : ");
                string nombreEtoile = Console.ReadLine();
                Console.Write("\n      Ville              : ");
                string ville = Console.ReadLine();
                Console.Write("\n      Date Debut         : ");
                string dateDebut = Console.ReadLine();
                Console.Write("\n      Date Fin           : ");
                string dateFin = Console.ReadLine();

                Console.Write("\n\n      Vérification des offres disponible en cours");
                pause();

                if (nombrePersonne.Equals("") || nombreEtoile.Equals("") || ville.Equals("") || dateDebut.Equals("") || dateFin.Equals("") || !CheckInt(nombrePersonne) || !CheckInt(nombreEtoile))
                {
                    Console.Clear();

                    Console.WriteLine("\n   ===== Bienvenue dans Trivago, le meilleur comparateur des offres =====\n");
                    Console.WriteLine("  ***** Voici la liste des meilleurs offres disponible pour le moment *****\n");

                    Console.WriteLine("\n      Aucune offre disponible avec ses informations");
                    Console.Write("\n\n  Appuyer sur une touche pour revenir en arrière...");
                    Console.ReadKey();
                    Console.Clear();
                    continue;
                }

                string offers = GetReleases(url + "Trivago/Comparateur?ville=" + ville + "&dateDebut=" + dateDebut + "&dateFin=" + dateFin + "&nombrePersonne=" + nombrePersonne + "&nombreEtoile=" + nombreEtoile);

                if (offers == null || offers.Equals(""))
                {
                    Console.Clear();

                    Console.WriteLine("\n   ===== Bienvenue dans Trivago, le meilleur comparateur des offres =====\n");
                    Console.WriteLine("  ***** Voici la liste des meilleurs offres disponible pour le moment *****\n");

                    Console.WriteLine("\n      Aucune offre disponible avec ses informations");
                    Console.Write("\n\n  Appuyer sur une touche pour revenir en arrière...");
                    Console.ReadKey();
                    Console.Clear();
                    continue;
                }

                string[] arrayOffre = offers.Split('@');

                Console.Clear();

                Console.WriteLine("\n   ===== Bienvenue dans Trivago, le meilleur comparateur des offres =====\n");
                Console.WriteLine("  ***** Voici la liste des meilleurs offres disponible pour le moment *****\n");

                for (int i = 0; i < arrayOffre.Length - 1; i++)
                {
                    string[] offreCourant = arrayOffre[i].Split('=');
                    Console.WriteLine("\n      - Nom de l'hotel     : " + offreCourant[0]);
                    Console.WriteLine("      - Adresse de l'hotel : " + offreCourant[1]);
                    Console.WriteLine("      - Nombre de lit      : " + offreCourant[2]);
                    Console.WriteLine("      - Prix de l'offre    : " + offreCourant[3]);
                    Console.WriteLine("      - Nombre d'étoile    : " + offreCourant[4] + "\n");
                }

                Console.Write("\n Appuyer sur une touche pour revenir en arrière...");
                Console.ReadKey();
                Console.Clear();
            }
        }
    }
}
