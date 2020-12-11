/*========================================================================
Fichier Canevas_TP.cc 
Cours d'algorithmique du texte HMSN116M - Université de Montpellier
Année 2017-18

Créé par Sèverine Bérard, modifié par VOTRE_NOM_ICI
=========================================================================*/
#include <iostream>
#include <fstream> //pour lire les fichiers
#include <string>
#include <map> //pour les tableaux associatifs

using namespace std;

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// NAÏF ///////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
void naif(string P, int m, string T, int n)
{
   
}

//////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// MP /////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
int * calcule_MP_next(string P, int m)
{
   
}

void MP(string P, int m, string T, int n)
{
   
}

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// KMP /////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
int * calcule_KMP_next(string P, int m)
{
   
}

void KMP(string P, int m, string T, int n)
{
   
}

//////////////////////////////////////////////////////////////////////////////
////////////////////////////// Boyer-Moore ///////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
int * calcule_suff(string P, int m)
{
   
}

int * calcule_D(string P, int m)
{
   
}

map<char,int> calcule_R(string P, int m)
{
   
}

void BM(string P, int m, string T, int n)
{
   
}

//////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// main ///////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
int main(int argc, char** argv)
{
   if (argc != 3)
      {
	 cout<<"Mauvaise utilisation du programme "<<argv[0]<<", on attend :"<<endl;
	 cout<<argv[0]<<" ficMotif ficTexte"<<endl;
      }
   else
      {
	 /* Lecture du motif */
	 string nomFicMotif (argv[1]);
	 ifstream IfFicMotif (nomFicMotif.c_str(), ios::in);
	 if (!IfFicMotif)
	    {
	       cout<<endl<<"Impossible d'ouvrir le fichier : "<<nomFicMotif<<endl<<endl;
	       return(0);
	    }
	 string P; char c;
	 //On lit le fichier caractère par caractère
	 while(IfFicMotif.get(c)){
	    P += c;
	 }
	 //On élimine l'éventuel retour à la ligne en fin de fichier
	 if (P.size() >1 && (int)P[P.size()-1]==10)
	    P=P.substr(0,P.size()-1);

	 P='#'+P; //Pour que la première lettre de P soit à l'indice 1
	 int m=P.size()-1;

	 /* Lecture du texte */
	 string nomFicTexte(argv[2]);
	 ifstream IfFicTexte (nomFicTexte.c_str(), ios::in);
	  if (!IfFicTexte)
	    {
	       cout<<endl<<"Impossible d'ouvrir le fichier : "<<nomFicTexte<<endl<<endl;
	       return(0);
	    }
	  string T;
	  //On lit le fichier caractère par caractère
	  while(IfFicTexte.get(c)){
	     T += c;
	  }
	  //On élimine l'éventuel retour à la ligne en fin de fichier
	  if (T.size() >1 && (int)T[T.size()-1]==10)
	     T=T.substr(0,T.size()-1);
    
	  T='#'+T; //Pour que la première lettre de T soit à l'indice 1
	  int n=T.size()-1;
	  
	  cout<<"********************************************************************"<<endl;
	  cout<<"Recherche du motif de taille "<<m<<" P="<<P.substr(1,P.size())<<endl;
	  //On n'affiche plus le texte car il peut être long
  	 cout<<"Dans un texte de taille "<<n<<endl;//<<" T="<<T.substr(1,T.size())<<endl;
	  cout<<"********************************************************************"<<endl<<endl<<endl;

	  cout<<"************* Recherche naïve *************"<<endl;
	  naif(P,m,T,n);
	  cout<<"###############################"<<endl<<endl;

	  cout<<"************* Recherche avec MP *************"<<endl;
	  MP(P,m,T,n);
	  cout<<"################################"<<endl<<endl;

	  cout<<"************* Recherche avec KMP *************"<<endl;
	  KMP(P,m,T,n);
	  cout<<"################################"<<endl<<endl;

	  cout<<"************* Recherche avec Boyer-Moore *************"<<endl;
	  BM(P,m,T,n);
	  cout<<"################################"<<endl;
      }
}
