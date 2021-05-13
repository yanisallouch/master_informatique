using System;
using System.Collections.Generic;
using System.Text;

namespace ClassLibrary1
{
    class Rectangle : IGeometrique
    {
        int longeur;
        int largeur;

        public double Aire
        {
            get => longeur * largeur;
        }

        public override string ToString()
        {
            return base.ToString() + $" {nameof(longeur)}" + longeur + $" { nameof(largeur)}" + largeur ;
        }

        public Rectangle(int longeur, int largeur)
        {
            this.longeur = longeur;
            this.largeur = largeur;
        }

        public double getAire()
        {
            throw new NotImplementedException();
        }

        public string getNom()
        {
            throw new NotImplementedException();
        }

        public double getPerimetre()
        {
            throw new NotImplementedException();
        }

        public double getVolume()
        {
            throw new NotImplementedException();
        }

        public void setNom()
        {
            throw new NotImplementedException();
        }
    }
}
