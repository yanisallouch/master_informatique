using System;
using System.Collections.Generic;

namespace Hotel1
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("\n ===== Bienvenue dans gestionhotel.com =====\n");

            Client c1 = new Client(1, "Benallal", "Chahinez", "02/03/1996", "00001111");
            Client c2 = new Client(2, "Omar", "Nasser", "09/10/1998", "00002222");

            Chambre ch1 = new Chambre(1, 1, 2, 50, 4);
            Chambre ch2 = new Chambre(2, 2, 1, 100, 2);
            Chambre ch3 = new Chambre(3, 3, 2, 150, 3);

            List<Chambre> listeChambre1 = new List<Chambre>();
            List<Chambre> listeChambre2 = new List<Chambre>();
            List<Chambre> listeChambre3 = new List<Chambre>();
            listeChambre1.Add(ch1);
            listeChambre1.Add(ch2);
            listeChambre2.Add(ch3);
            listeChambre3.Add(ch1);
            listeChambre3.Add(ch2);
            listeChambre3.Add(ch3);

            Adresse a1 = new Adresse(1, "France", "Montpellier", "Rue Augustine Fliche", 34, "00001");
            Adresse a2 = new Adresse(2, "France", "Montpellier", "Rue Augustine Fliche", 30, "00011");
            Adresse a3 = new Adresse(2, "France", "Paris", "Rue Augustine Fliche", 90, "00012");

            Hotel h1 = new Hotel(1, "Ibis Hotel", 3, listeChambre1);
            Hotel h2 = new Hotel(2, "Hilton Hotel", 2, listeChambre2);
            Hotel h3 = new Hotel(1, "Hotel Asterix", 4, listeChambre3);

            Console.WriteLine("\n Dans quelle ville voulez-vous séjourné ? (Montpellier)");
            Console.Write("\n ===> Votre choix : ");
            String ville = Console.ReadLine();
            Console.WriteLine("\n Votre date d'arrivée ? ");
            Console.Write("\n ===> Votre choix : "); 
            String arrive = Console.ReadLine();
            Console.WriteLine("\n Votre date de départ ? ");
            Console.Write("\n ===> Votre choix : "); 
            String depart = Console.ReadLine();

            Console.WriteLine("\n\n ------ Voici les hotels disponnibles ------\n");
            
            if(ville.Equals(a1.ville))
            {
                Console.WriteLine(" " +h1.idHotel + "  " + h1.nomHotel);
                Console.WriteLine(" " +h2.idHotel + "  " + h2.nomHotel);
            }
            else
            {
                Console.WriteLine(h3.idHotel + "  " + h3.nomHotel);
            }

            Console.Write("\n ===> Votre choix : ");
            String reponse = Console.ReadLine();
            Hotel hotelChoisi;
            if (reponse.Equals("1"))
            {
                hotelChoisi = h1;
            }
            else if (reponse.Equals("2"))
            {
                hotelChoisi = h2;
            }
            else
            {
                hotelChoisi = h3;
            }

            Console.Write("\n Prix Minimum (1) : ");
            int prixMin = int.Parse( Console.ReadLine());
            Console.Write("\n Prix Max (200) : ");
            int prixMax = int.Parse(Console.ReadLine());

            Console.WriteLine("\n\n ------ Voici les chambres disponnibles ------\n");

            foreach (Chambre chambre in hotelChoisi.listeChambre)
            {
                if (chambre.prix <= prixMax && chambre.prix >= prixMin)
                {
                    Console.WriteLine(" " +chambre.afficher());
                }
            }

            Random aleatoire = new Random();
            int prix = aleatoire.Next(prixMin, prixMin);

            Chambre ch4 = new Chambre(4, 4, 2, prix, 2);
            Console.WriteLine(" " + ch4.afficher());

            Console.Write("\n Choisissez une chambre : ");
            reponse = Console.ReadLine();
            Chambre chambreChoisi;

            if (reponse.Equals("1"))
            {
                chambreChoisi = ch1;
            }
            else if (reponse.Equals("2"))
            {
                chambreChoisi = ch2;
            }
            else
            {
                chambreChoisi = ch3;
            }

            Reservation reservation = new Reservation(1, arrive, depart, chambreChoisi.prix, chambreChoisi.nbrPersonneMax);

            Console.WriteLine("\n ===== Saisi des informations du client =====\n");
            Console.Write("   Prénom du client : ");
            string prenomClient = Console.ReadLine();
            Console.Write("   Nom du client    : ");
            string nomClient = Console.ReadLine();
            Console.Write("   Carte de crédit  : ");
            string carteCredit = Console.ReadLine();
            Client client = new Client(1, nomClient, prenomClient, "", carteCredit);

            Console.WriteLine("\n   " + "----- Réservation effectué avec sucées -----".ToUpper());
        }
    }
}
