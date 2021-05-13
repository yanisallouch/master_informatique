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
                string choix;

                do
                {
                    Console.WriteLine("\n ===== Bienvenue dans gestionhotel.com =====\n");
                    Console.WriteLine("   1 - Hôtel Lapeyronie");
                    Console.WriteLine("   2 - Hôtel Ibis");
                    Console.WriteLine("   3 - Hôtel Kyriad");

                    Console.WriteLine("\n Vous voulez accéder à quel serveur d'hôtel ?");
                    Console.Write("\n ===> Votre choix : ");
                    choix = Console.ReadLine();

                    if (!choix.Equals("1") && !choix.Equals("2") && !choix.Equals("3"))
                    {
                        Console.Clear();
                        Console.WriteLine("\n Erreur : Choisir entre '1', '2' ou '3'");
                    }
                } while (!choix.Equals("1") && !choix.Equals("2") && !choix.Equals("3"));

                if (choix.Equals("1"))
                {
                    Console.Clear();

                    while (true)
                    {
                        Console.WriteLine("\n ===== Connexion au serveur de l'hôtel Lapeyronie =====\n");
                        Console.Write("   Login : ");
                        string login = Console.ReadLine();
                        Console.Write("   Password : ");
                        string password = Console.ReadLine();

                        if (login.ToLower().Equals("retour") && password.ToLower().Equals("retour"))
                        {
                            Console.Clear();
                            break;
                        }

                        Console.Write("\n   Connexion au serveur en cours");
                        pause();

                        if (GetReleases(url + "HotelLapeyronie/Authtification?login="+login+"&password="+password).Equals("true"))
                        {
                            string choixOption;

                            while (true)
                            {
                                Console.Clear();

                                do
                                {
                                    Console.WriteLine("\n ===== Bienvenue dans le service web de l'hôtel Lapeyronie =====\n");
                                    Console.WriteLine("   1 - Consulter les offres");
                                    Console.WriteLine("   2 - Effectuer une réservation");
                                    Console.WriteLine("   0 - Se déconnecter");

                                    Console.Write("\n ===> Votre choix : ");
                                    choixOption = Console.ReadLine();

                                    if (!choixOption.Equals("0") && !choixOption.Equals("1") && !choixOption.Equals("2"))
                                    {
                                        Console.Clear();
                                        Console.WriteLine("\n Erreur : Choisir entre '0', '1' ou '2'");
                                    }
                                } while (!choixOption.Equals("0") && !choixOption.Equals("1") && !choixOption.Equals("2"));

                                if (choixOption.Equals("0"))
                                {
                                    Console.Clear();
                                    break;
                                }
                                else if (choixOption.Equals("1"))
                                {
                                    Console.Clear();
                                    Console.WriteLine("\n ===== Consultation des offres =====\n");
                                    Console.Write("   Nombre de personnes : ");
                                    int nombrePersonne = int.Parse(Console.ReadLine());
                                    Console.Write("   Date de début       : ");
                                    string dateDebut = Console.ReadLine();
                                    Console.Write("   Date de fin         : ");
                                    string dateFin = Console.ReadLine();

                                    Console.Write("\n   Vérification des offres disponibles en cours");
                                    pause();

                                    Console.Clear();
                                    Console.WriteLine("\n ===== Liste des offres disponible =====\n");

                                    string offres = GetReleases(url + "HotelLapeyronie/GetListeOffre?dateDebut="+dateDebut+"&dateFin="+dateFin+"&nombrePersonne="+nombrePersonne);

                                    if (offres == null)
                                    {
                                        Console.WriteLine(" Vous n'etes pas connecté pour pouvoir consulter la liste des offres disponibles");
                                    }
                                    else if (offres.Equals(""))
                                    {
                                        Console.WriteLine(" Aucune offre disponible pour ses informations");
                                    }
                                    else
                                    {
                                        string[] arrayOffre = offres.Split('@');

                                        for (int i = 0; i < arrayOffre.Length - 1; i++)
                                        {
                                            string[] offreCourant = arrayOffre[i].Split('=');
                                            Console.WriteLine("   - Offre " + (i + 1) + "\n");
                                            Console.WriteLine("      - Identifiant           : " + offreCourant[0]);
                                            Console.WriteLine("      - Date de disponibilité : " + offreCourant[1]);
                                            Console.WriteLine("      - Numéro de chambre     : " + offreCourant[2]);
                                            Console.WriteLine("      - Nombre de lit         : " + offreCourant[3]);
                                            Console.WriteLine("      - Prix de l'offre       : " + offreCourant[4] + "\n");

                                            String info = "      - Identifiant           : " + offreCourant[0]
                                                        + "\n      - Date de disponibilité : " + offreCourant[1]
                                                        + "\n      - Numéro de chambre     : " + offreCourant[2]
                                                        + "\n      - Nombre de lit         : " + offreCourant[3]
                                                        + "\n      - Prix de l'offre       : " + offreCourant[4] + "\n";
                                            Form1 form = new Form1(offreCourant[5], "   - Offre " + (i + 1) + "\n", info, "");
                                            form.ShowDialog();
                                        }
                                    }

                                    Console.Write("\n Appuyer sur une touche pour revenir en arrière...");
                                    Console.ReadKey();
                                }
                                else if (choixOption.Equals("2"))
                                {
                                    Console.Clear();
                                    while (true)
                                    {
                                        Console.WriteLine("\n ===== Vérification de l'offre =====\n");
                                        Console.Write("   Identifiant de l'offre : ");
                                        string idOffre = Console.ReadLine();

                                        if (idOffre.ToLower().Equals("retour"))
                                            break;

                                        Console.Write("\n   Vérification de l'identifiant de l'offre en cours");
                                        pause();

                                        string offerORmsgError = GetReleases(url + "HotelLapeyronie/VerifyOffer?idOffer=" +idOffre);

                                        if (offerORmsgError.Equals("Cette offre n'est pas disponible, veuillez consulter la liste des offres"))
                                        {
                                            Console.Clear();
                                            Console.WriteLine("\n Erreur : " + offerORmsgError);
                                            Console.WriteLine("\n Vous pouvez saisir 'Retour' pour revenir en arrière");
                                        }
                                        else
                                        {
                                            Console.Clear();
                                            Console.WriteLine("\n ===== Saisi des informations du client =====\n");
                                            Console.Write("   Prénom du client : ");
                                            string prenomClient = Console.ReadLine();
                                            Console.Write("   Nom du client    : ");
                                            string nomClient = Console.ReadLine();
                                            Console.Write("   Carte de crédit  : ");
                                            string carteCredit = Console.ReadLine();
                                            Client client = new Client(nomClient, prenomClient, carteCredit);

                                            Console.Clear();
                                            string[] arrayOffer = offerORmsgError.Split('=');
                                            string choixValidation;

                                            do
                                            {
                                                Console.WriteLine("\n ===== Récaputilative des données =====\n");

                                                Console.WriteLine("   - Information de l'offre \n");
                                                Console.WriteLine("      - Identifiant           : " + arrayOffer[0]);
                                                Console.WriteLine("      - Date de disponibilité : " + arrayOffer[1]);
                                                Console.WriteLine("      - Numéro de chambre     : " + arrayOffer[2]);
                                                Console.WriteLine("      - Nombre de lit         : " + arrayOffer[3]);
                                                Console.WriteLine("      - Prix de l'offre       : " + arrayOffer[4] + "\n");

                                                Console.WriteLine("   - Information du client \n");
                                                Console.WriteLine("      - Prénom du client      : " + client.PrenomClient);
                                                Console.WriteLine("      - Nom du client         : " + client.NomClient);
                                                Console.WriteLine("      - Carte de crédit       : " + client.CarteCredit + "\n");

                                                String info = "      - Identifiant           : " + arrayOffer[0]
                                                        + "\n      - Date de disponibilité : " + arrayOffer[1]
                                                        + "\n      - Numéro de chambre     : " + arrayOffer[2]
                                                        + "\n      - Nombre de lit         : " + arrayOffer[3]
                                                        + "\n      - Prix de l'offre       : " + arrayOffer[4] + "\n";

                                                String info2 = "   - Information du client \n"
                                                        + "\n      - Prénom du client      : " + client.PrenomClient
                                                        + "\n      - Nom du client         : " + client.NomClient
                                                        + "\n      - Carte de crédit       : " + client.CarteCredit + "\n";

                                                Form1 form = new Form1(arrayOffer[5], "   - Information de l'offre \n", info, info2);
                                                form.ShowDialog();

                                                Console.WriteLine("   Voulez-vous confirmez la réservation ?");
                                                Console.WriteLine("\n    1 - OUI\n    2 - NON");
                                                Console.Write("\n    ===> Votre choix : ");
                                                choixValidation = Console.ReadLine();

                                                if (!choixValidation.Equals("1") && !choixValidation.Equals("2"))
                                                {
                                                    Console.Clear();
                                                    Console.WriteLine("\n Erreur : Choisir entre '1' ou '2'");
                                                }

                                            } while (!choixValidation.Equals("1") && !choixValidation.Equals("2"));

                                            if (choixValidation.Equals("1"))
                                            {
                                                Console.Write("\n   Validation de l'offre en cours");
                                                pause();

                                                Console.Clear();
                                                Console.WriteLine("\n ===== Récaputilative des données =====\n");

                                                Console.WriteLine("   - Information de l'offre \n");
                                                Console.WriteLine("      - Identifiant           : " + arrayOffer[0]);
                                                Console.WriteLine("      - Date de disponibilité : " + arrayOffer[1]);
                                                Console.WriteLine("      - Numéro de chambre     : " + arrayOffer[2]);
                                                Console.WriteLine("      - Nombre de lit         : " + arrayOffer[3]);
                                                Console.WriteLine("      - Prix de l'offre       : " + arrayOffer[4] + "\n");

                                                Console.WriteLine("   - Information du client \n");
                                                Console.WriteLine("      - Prénom du client      : " + client.PrenomClient);
                                                Console.WriteLine("      - Nom du client         : " + client.NomClient);
                                                Console.WriteLine("      - Carte de crédit       : " + client.CarteCredit + "\n");

                                                Console.WriteLine("   " + GetReleases(url + "HotelLapeyronie/BookingRegistration?idOffre=" + idOffre+ "&client=" + client.IdClient));

                                                Console.Write("\n Appuyer sur une touche pour revenir en arrière...");

                                                String info = "      - Identifiant           : " + arrayOffer[0]
                                                        + "\n      - Date de disponibilité : " + arrayOffer[1]
                                                        + "\n      - Numéro de chambre     : " + arrayOffer[2]
                                                        + "\n      - Nombre de lit         : " + arrayOffer[3]
                                                        + "\n      - Prix de l'offre       : " + arrayOffer[4] + "\n";

                                                String info2 = "   - Information du client \n"
                                                        + "\n      - Prénom du client      : " + client.PrenomClient
                                                        + "\n      - Nom du client         : " + client.NomClient
                                                        + "\n      - Carte de crédit       : " + client.CarteCredit + "\n";

                                                Form1 form = new Form1(arrayOffer[5], "   - RESERVATION EFFECTUEE AVEC SUCCEES \n", info, info2);
                                                form.ShowDialog();

                                                break;
                                            }
                                            else
                                            {
                                                Console.Clear();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            Console.Clear();
                            Console.WriteLine("\n Erreur : Login ou Password incorrecte, Veuillez le résaisir");
                            Console.WriteLine("\n Vous pouvez saisir 'Retour' comme login et password pour revenir en arrière");
                        }
                    }
                } 
                else if (choix.Equals("2"))
                {
                    Console.Clear();

                    while (true)
                    {
                        Console.WriteLine("\n ===== Connexion au serveur de l'hôtel Ibis =====\n");
                        Console.Write("   Login : ");
                        string login = Console.ReadLine();
                        Console.Write("   Password : ");
                        string password = Console.ReadLine();

                        if (login.ToLower().Equals("retour") && password.ToLower().Equals("retour"))
                        {
                            Console.Clear();
                            break;
                        }

                        Console.Write("\n   Connexion au serveur en cours");
                        pause();

                        if (GetReleases(url + "HotelIbis/Authtification?login=" + login + "&password=" + password).Equals("true"))
                        {
                            string choixOption;

                            while (true)
                            {
                                Console.Clear();

                                do
                                {
                                    Console.WriteLine("\n ===== Bienvenue dans le service web de l'hôtel Ibis =====\n");
                                    Console.WriteLine("   1 - Consulter les offres");
                                    Console.WriteLine("   2 - Effectuer une réservation");
                                    Console.WriteLine("   0 - Se déconnecter");

                                    Console.Write("\n ===> Votre choix : ");
                                    choixOption = Console.ReadLine();

                                    if (!choixOption.Equals("0") && !choixOption.Equals("1") && !choixOption.Equals("2"))
                                    {
                                        Console.Clear();
                                        Console.WriteLine("\n Erreur : Choisir entre '0', '1' ou '2'");
                                    }
                                } while (!choixOption.Equals("0") && !choixOption.Equals("1") && !choixOption.Equals("2"));

                                if (choixOption.Equals("0"))
                                {
                                    Console.Clear();
                                    break;
                                }
                                else if (choixOption.Equals("1"))
                                {
                                    Console.Clear();
                                    Console.WriteLine("\n ===== Consultation des offres =====\n");
                                    Console.Write("   Nombre de personnes : ");
                                    int nombrePersonne = int.Parse(Console.ReadLine());
                                    Console.Write("   Date de début       : ");
                                    string dateDebut = Console.ReadLine();
                                    Console.Write("   Date de fin         : ");
                                    string dateFin = Console.ReadLine();

                                    Console.Write("\n   Vérification des offres disponibles en cours");
                                    pause();

                                    Console.Clear();
                                    Console.WriteLine("\n ===== Liste des offres disponible =====\n");

                                    string offres = GetReleases(url + "HotelIbis/GetListeOffre?dateDebut=" + dateDebut + "&dateFin=" + dateFin + "&nombrePersonne=" + nombrePersonne);

                                    if (offres == null)
                                    {
                                        Console.WriteLine(" Vous n'etes pas connecté pour pouvoir consulter la liste des offres disponibles");
                                    }
                                    else if (offres.Equals(""))
                                    {
                                        Console.WriteLine(" Aucune offre disponible pour ses informations");
                                    }
                                    else
                                    {
                                        string[] arrayOffre = offres.Split('@');

                                        for (int i = 0; i < arrayOffre.Length - 1; i++)
                                        {
                                            string[] offreCourant = arrayOffre[i].Split('=');
                                            Console.WriteLine("   - Offre " + (i + 1) + "\n");
                                            Console.WriteLine("      - Identifiant           : " + offreCourant[0]);
                                            Console.WriteLine("      - Date de disponibilité : " + offreCourant[1]);
                                            Console.WriteLine("      - Numéro de chambre     : " + offreCourant[2]);
                                            Console.WriteLine("      - Nombre de lit         : " + offreCourant[3]);
                                            Console.WriteLine("      - Prix de l'offre       : " + offreCourant[4] + "\n");

                                            String info = "      - Identifiant           : " + offreCourant[0]
                                                        + "\n      - Date de disponibilité : " + offreCourant[1]
                                                        + "\n      - Numéro de chambre     : " + offreCourant[2]
                                                        + "\n      - Nombre de lit         : " + offreCourant[3]
                                                        + "\n      - Prix de l'offre       : " + offreCourant[4] + "\n";
                                            Form1 form = new Form1(offreCourant[5], "   - Offre " + (i + 1) + "\n", info, "");
                                            form.ShowDialog();
                                        }
                                    }

                                    Console.Write("\n Appuyer sur une touche pour revenir en arrière...");
                                    Console.ReadKey();
                                }
                                else if (choixOption.Equals("2"))
                                {
                                    Console.Clear();
                                    while (true)
                                    {
                                        Console.WriteLine("\n ===== Vérification de l'offre =====\n");
                                        Console.Write("   Identifiant de l'offre : ");
                                        string idOffre = Console.ReadLine();

                                        if (idOffre.ToLower().Equals("retour"))
                                            break;

                                        Console.Write("\n   Vérification de l'identifiant de l'offre en cours");
                                        pause();

                                        string offerORmsgError = GetReleases(url + "HotelIbis/VerifyOffer?idOffer=" + idOffre);

                                        if (offerORmsgError.Equals("Cette offre n'est pas disponible, veuillez consulter la liste des offres"))
                                        {
                                            Console.Clear();
                                            Console.WriteLine("\n Erreur : " + offerORmsgError);
                                            Console.WriteLine("\n Vous pouvez saisir 'Retour' pour revenir en arrière");
                                        }
                                        else
                                        {
                                            Console.Clear();
                                            Console.WriteLine("\n ===== Saisi des informations du client =====\n");
                                            Console.Write("   Prénom du client : ");
                                            string prenomClient = Console.ReadLine();
                                            Console.Write("   Nom du client    : ");
                                            string nomClient = Console.ReadLine();
                                            Console.Write("   Carte de crédit  : ");
                                            string carteCredit = Console.ReadLine();
                                            Client client = new Client(nomClient, prenomClient, carteCredit);

                                            Console.Clear();
                                            string[] arrayOffer = offerORmsgError.Split('=');
                                            string choixValidation;

                                            do
                                            {
                                                Console.WriteLine("\n ===== Récaputilative des données =====\n");

                                                Console.WriteLine("   - Information de l'offre \n");
                                                Console.WriteLine("      - Identifiant           : " + arrayOffer[0]);
                                                Console.WriteLine("      - Date de disponibilité : " + arrayOffer[1]);
                                                Console.WriteLine("      - Numéro de chambre     : " + arrayOffer[2]);
                                                Console.WriteLine("      - Nombre de lit         : " + arrayOffer[3]);
                                                Console.WriteLine("      - Prix de l'offre       : " + arrayOffer[4] + "\n");

                                                Console.WriteLine("   - Information du client \n");
                                                Console.WriteLine("      - Prénom du client      : " + client.PrenomClient);
                                                Console.WriteLine("      - Nom du client         : " + client.NomClient);
                                                Console.WriteLine("      - Carte de crédit       : " + client.CarteCredit + "\n");

                                                String info = "      - Identifiant           : " + arrayOffer[0]
                                                        + "\n      - Date de disponibilité : " + arrayOffer[1]
                                                        + "\n      - Numéro de chambre     : " + arrayOffer[2]
                                                        + "\n      - Nombre de lit         : " + arrayOffer[3]
                                                        + "\n      - Prix de l'offre       : " + arrayOffer[4] + "\n";

                                                String info2 = "   - Information du client \n"
                                                        + "\n      - Prénom du client      : " + client.PrenomClient
                                                        + "\n      - Nom du client         : " + client.NomClient
                                                        + "\n      - Carte de crédit       : " + client.CarteCredit + "\n";

                                                Form1 form = new Form1(arrayOffer[5], "   - Information de l'offre \n", info, info2);
                                                form.ShowDialog();

                                                Console.WriteLine("   Voulez-vous confirmez la réservation ?");
                                                Console.WriteLine("\n    1 - OUI\n    2 - NON");
                                                Console.Write("\n    ===> Votre choix : ");
                                                choixValidation = Console.ReadLine();

                                                if (!choixValidation.Equals("1") && !choixValidation.Equals("2"))
                                                {
                                                    Console.Clear();
                                                    Console.WriteLine("\n Erreur : Choisir entre '1' ou '2'");
                                                }

                                            } while (!choixValidation.Equals("1") && !choixValidation.Equals("2"));

                                            if (choixValidation.Equals("1"))
                                            {
                                                Console.Write("\n   Validation de l'offre en cours");
                                                pause();

                                                Console.Clear();
                                                Console.WriteLine("\n ===== Récaputilative des données =====\n");

                                                Console.WriteLine("   - Information de l'offre \n");
                                                Console.WriteLine("      - Identifiant           : " + arrayOffer[0]);
                                                Console.WriteLine("      - Date de disponibilité : " + arrayOffer[1]);
                                                Console.WriteLine("      - Numéro de chambre     : " + arrayOffer[2]);
                                                Console.WriteLine("      - Nombre de lit         : " + arrayOffer[3]);
                                                Console.WriteLine("      - Prix de l'offre       : " + arrayOffer[4] + "\n");

                                                Console.WriteLine("   - Information du client \n");
                                                Console.WriteLine("      - Prénom du client      : " + client.PrenomClient);
                                                Console.WriteLine("      - Nom du client         : " + client.NomClient);
                                                Console.WriteLine("      - Carte de crédit       : " + client.CarteCredit + "\n");

                                                Console.WriteLine("   " + GetReleases(url + "HotelIbis/BookingRegistration?idOffre=" + idOffre + "&client=" + client.IdClient));

                                                Console.Write("\n Appuyer sur une touche pour revenir en arrière...");

                                                String info = "      - Identifiant           : " + arrayOffer[0]
                                                        + "\n      - Date de disponibilité : " + arrayOffer[1]
                                                        + "\n      - Numéro de chambre     : " + arrayOffer[2]
                                                        + "\n      - Nombre de lit         : " + arrayOffer[3]
                                                        + "\n      - Prix de l'offre       : " + arrayOffer[4] + "\n";

                                                String info2 = "   - Information du client \n"
                                                        + "\n      - Prénom du client      : " + client.PrenomClient
                                                        + "\n      - Nom du client         : " + client.NomClient
                                                        + "\n      - Carte de crédit       : " + client.CarteCredit + "\n";

                                                Form1 form = new Form1(arrayOffer[5], "   - RESERVATION EFFECTUEE AVEC SUCCEES \n", info, info2);
                                                form.ShowDialog();

                                                break;
                                            }
                                            else
                                            {
                                                Console.Clear();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            Console.Clear();
                            Console.WriteLine("\n Erreur : Login ou Password incorrecte, Veuillez le résaisir");
                            Console.WriteLine("\n Vous pouvez saisir 'Retour' comme login et password pour revenir en arrière");
                        }
                    }
                } 
                else if (choix.Equals("3"))
                {
                    Console.Clear();

                    while (true)
                    {
                        Console.WriteLine("\n ===== Connexion au serveur de l'hôtel Kyriad =====\n");
                        Console.Write("   Login : ");
                        string login = Console.ReadLine();
                        Console.Write("   Password : ");
                        string password = Console.ReadLine();

                        if (login.ToLower().Equals("retour") && password.ToLower().Equals("retour"))
                        {
                            Console.Clear();
                            break;
                        }

                        Console.Write("\n   Connexion au serveur en cours");
                        pause();

                        if (GetReleases(url + "HotelKyriad/Authtification?login=" + login + "&password=" + password).Equals("true"))
                        {
                            string choixOption;

                            while (true)
                            {
                                Console.Clear();

                                do
                                {
                                    Console.WriteLine("\n ===== Bienvenue dans le service web de l'hôtel Kyriad =====\n");
                                    Console.WriteLine("   1 - Consulter les offres");
                                    Console.WriteLine("   2 - Effectuer une réservation");
                                    Console.WriteLine("   0 - Se déconnecter");

                                    Console.Write("\n ===> Votre choix : ");
                                    choixOption = Console.ReadLine();

                                    if (!choixOption.Equals("0") && !choixOption.Equals("1") && !choixOption.Equals("2"))
                                    {
                                        Console.Clear();
                                        Console.WriteLine("\n Erreur : Choisir entre '0', '1' ou '2'");
                                    }
                                } while (!choixOption.Equals("0") && !choixOption.Equals("1") && !choixOption.Equals("2"));

                                if (choixOption.Equals("0"))
                                {
                                    Console.Clear();
                                    break;
                                }
                                else if (choixOption.Equals("1"))
                                {
                                    Console.Clear();
                                    Console.WriteLine("\n ===== Consultation des offres =====\n");
                                    Console.Write("   Nombre de personnes : ");
                                    int nombrePersonne = int.Parse(Console.ReadLine());
                                    Console.Write("   Date de début       : ");
                                    string dateDebut = Console.ReadLine();
                                    Console.Write("   Date de fin         : ");
                                    string dateFin = Console.ReadLine();

                                    Console.Write("\n   Vérification des offres disponibles en cours");
                                    pause();

                                    Console.Clear();
                                    Console.WriteLine("\n ===== Liste des offres disponible =====\n");

                                    string offres = GetReleases(url + "HotelKyriad/GetListeOffre?dateDebut=" + dateDebut + "&dateFin=" + dateFin + "&nombrePersonne=" + nombrePersonne);

                                    if (offres == null)
                                    {
                                        Console.WriteLine(" Vous n'etes pas connecté pour pouvoir consulter la liste des offres disponibles");
                                    }
                                    else if (offres.Equals(""))
                                    {
                                        Console.WriteLine(" Aucune offre disponible pour ses informations");
                                    }
                                    else
                                    {
                                        string[] arrayOffre = offres.Split('@');

                                        for (int i = 0; i < arrayOffre.Length - 1; i++)
                                        {
                                            string[] offreCourant = arrayOffre[i].Split('=');
                                            Console.WriteLine("   - Offre " + (i + 1) + "\n");
                                            Console.WriteLine("      - Identifiant           : " + offreCourant[0]);
                                            Console.WriteLine("      - Date de disponibilité : " + offreCourant[1]);
                                            Console.WriteLine("      - Numéro de chambre     : " + offreCourant[2]);
                                            Console.WriteLine("      - Nombre de lit         : " + offreCourant[3]);
                                            Console.WriteLine("      - Prix de l'offre       : " + offreCourant[4] + "\n");

                                            String info = "      - Identifiant           : " + offreCourant[0]
                                                        + "\n      - Date de disponibilité : " + offreCourant[1]
                                                        + "\n      - Numéro de chambre     : " + offreCourant[2]
                                                        + "\n      - Nombre de lit         : " + offreCourant[3]
                                                        + "\n      - Prix de l'offre       : " + offreCourant[4] + "\n";
                                            Form1 form = new Form1(offreCourant[5], "   - Offre " + (i + 1) + "\n", info, "");
                                            form.ShowDialog();
                                        }
                                    }

                                    Console.Write("\n Appuyer sur une touche pour revenir en arrière...");
                                    Console.ReadKey();
                                }
                                else if (choixOption.Equals("2"))
                                {
                                    Console.Clear();
                                    while (true)
                                    {
                                        Console.WriteLine("\n ===== Vérification de l'offre =====\n");
                                        Console.Write("   Identifiant de l'offre : ");
                                        string idOffre = Console.ReadLine();

                                        if (idOffre.ToLower().Equals("retour"))
                                            break;

                                        Console.Write("\n   Vérification de l'identifiant de l'offre en cours");
                                        pause();

                                        string offerORmsgError = GetReleases(url + "HotelKyriad/VerifyOffer?idOffer=" + idOffre);

                                        if (offerORmsgError.Equals("Cette offre n'est pas disponible, veuillez consulter la liste des offres"))
                                        {
                                            Console.Clear();
                                            Console.WriteLine("\n Erreur : " + offerORmsgError);
                                            Console.WriteLine("\n Vous pouvez saisir 'Retour' pour revenir en arrière");
                                        }
                                        else
                                        {
                                            Console.Clear();
                                            Console.WriteLine("\n ===== Saisi des informations du client =====\n");
                                            Console.Write("   Prénom du client : ");
                                            string prenomClient = Console.ReadLine();
                                            Console.Write("   Nom du client    : ");
                                            string nomClient = Console.ReadLine();
                                            Console.Write("   Carte de crédit  : ");
                                            string carteCredit = Console.ReadLine();
                                            Client client = new Client(nomClient, prenomClient, carteCredit);

                                            Console.Clear();
                                            string[] arrayOffer = offerORmsgError.Split('=');
                                            string choixValidation;

                                            do
                                            {
                                                Console.WriteLine("\n ===== Récaputilative des données =====\n");

                                                Console.WriteLine("   - Information de l'offre \n");
                                                Console.WriteLine("      - Identifiant           : " + arrayOffer[0]);
                                                Console.WriteLine("      - Date de disponibilité : " + arrayOffer[1]);
                                                Console.WriteLine("      - Numéro de chambre     : " + arrayOffer[2]);
                                                Console.WriteLine("      - Nombre de lit         : " + arrayOffer[3]);
                                                Console.WriteLine("      - Prix de l'offre       : " + arrayOffer[4] + "\n");

                                                Console.WriteLine("   - Information du client \n");
                                                Console.WriteLine("      - Prénom du client      : " + client.PrenomClient);
                                                Console.WriteLine("      - Nom du client         : " + client.NomClient);
                                                Console.WriteLine("      - Carte de crédit       : " + client.CarteCredit + "\n");

                                                String info = "      - Identifiant           : " + arrayOffer[0]
                                                        + "\n      - Date de disponibilité : " + arrayOffer[1]
                                                        + "\n      - Numéro de chambre     : " + arrayOffer[2]
                                                        + "\n      - Nombre de lit         : " + arrayOffer[3]
                                                        + "\n      - Prix de l'offre       : " + arrayOffer[4] + "\n";

                                                String info2 = "   - Information du client \n"
                                                        + "\n      - Prénom du client      : " + client.PrenomClient
                                                        + "\n      - Nom du client         : " + client.NomClient
                                                        + "\n      - Carte de crédit       : " + client.CarteCredit + "\n";

                                                Form1 form = new Form1(arrayOffer[5], "   - Information de l'offre \n", info, info2);
                                                form.ShowDialog();

                                                Console.WriteLine("   Voulez-vous confirmez la réservation ?");
                                                Console.WriteLine("\n    1 - OUI\n    2 - NON");
                                                Console.Write("\n    ===> Votre choix : ");
                                                choixValidation = Console.ReadLine();

                                                if (!choixValidation.Equals("1") && !choixValidation.Equals("2"))
                                                {
                                                    Console.Clear();
                                                    Console.WriteLine("\n Erreur : Choisir entre '1' ou '2'");
                                                }

                                            } while (!choixValidation.Equals("1") && !choixValidation.Equals("2"));

                                            if (choixValidation.Equals("1"))
                                            {
                                                Console.Write("\n   Validation de l'offre en cours");
                                                pause();

                                                Console.Clear();
                                                Console.WriteLine("\n ===== Récaputilative des données =====\n");

                                                Console.WriteLine("   - Information de l'offre \n");
                                                Console.WriteLine("      - Identifiant           : " + arrayOffer[0]);
                                                Console.WriteLine("      - Date de disponibilité : " + arrayOffer[1]);
                                                Console.WriteLine("      - Numéro de chambre     : " + arrayOffer[2]);
                                                Console.WriteLine("      - Nombre de lit         : " + arrayOffer[3]);
                                                Console.WriteLine("      - Prix de l'offre       : " + arrayOffer[4] + "\n");

                                                Console.WriteLine("   - Information du client \n");
                                                Console.WriteLine("      - Prénom du client      : " + client.PrenomClient);
                                                Console.WriteLine("      - Nom du client         : " + client.NomClient);
                                                Console.WriteLine("      - Carte de crédit       : " + client.CarteCredit + "\n");

                                                Console.WriteLine("   " + GetReleases(url + "HotelKyriad/BookingRegistration?idOffre=" + idOffre + "&client=" + client.IdClient));

                                                Console.Write("\n Appuyer sur une touche pour revenir en arrière...");

                                                String info = "      - Identifiant           : " + arrayOffer[0]
                                                        + "\n      - Date de disponibilité : " + arrayOffer[1]
                                                        + "\n      - Numéro de chambre     : " + arrayOffer[2]
                                                        + "\n      - Nombre de lit         : " + arrayOffer[3]
                                                        + "\n      - Prix de l'offre       : " + arrayOffer[4] + "\n";

                                                String info2 = "   - Information du client \n"
                                                        + "\n      - Prénom du client      : " + client.PrenomClient
                                                        + "\n      - Nom du client         : " + client.NomClient
                                                        + "\n      - Carte de crédit       : " + client.CarteCredit + "\n";

                                                Form1 form = new Form1(arrayOffer[5], "   - RESERVATION EFFECTUEE AVEC SUCCEES \n", info, info2);
                                                form.ShowDialog();

                                                break;
                                            }
                                            else
                                            {
                                                Console.Clear();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            Console.Clear();
                            Console.WriteLine("\n Erreur : Login ou Password incorrecte, Veuillez le résaisir");
                            Console.WriteLine("\n Vous pouvez saisir 'Retour' comme login et password pour revenir en arrière");
                        }
                    }
                }
            }
        }
    }
}
