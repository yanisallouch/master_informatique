using System;
using System.Collections.Generic;
using System.Text;

namespace ConsoleApp1
{
    public class Cercle : IGeometrique
    {
        string name;
        int x;
        public int Y { get; set; }

        double rayon;

        public Cercle(string name, int x, int y, double rayon)
        {
            this.name = name;
            this.x = x;
            Y = y;
            this.rayon = rayon;
        }

        public double getAire()
        {
            return Math.PI * Math.Sqrt(this.rayon) ;
        }
        
        public string Name
        {
            get => name;
            set => name = value;
        }

        public int X
        {
            get => x;
            set { this.x = value; }
        }

        public double getPerimetre()
        {
            return 2 * Math.PI * this.rayon;
        }

        public double getVolume()
        {
            throw new NotImplementedException();
        }
        public override string ToString()
        {
            return base.ToString() + " le cercle " + this.name + " de positition " + x + "," + Y ;
        }

        public string getNom()
        {
            return Name;
        }

    }
}
