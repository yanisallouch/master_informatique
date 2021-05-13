using System;
using System.Collections.Generic;

namespace Complexe
{
    class Complexe
    {
        public int A { get; set; }
        public int B { get; set; }

        public Complexe()
        {
            
        }
        public Complexe(int a, int b)
        {
            A = a;
            B = b;
        }

        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            Complexe a = new Complexe(1, 2);
            Complexe b = new Complexe(1, 2);

            Console.WriteLine("a = " + a + " \nb = " + b + "\na + b = " + (a + b));
            Console.WriteLine("a = " + a + " \nb = " + b + "\na * b = " + (a * b) );
        }
        public static Complexe operator +(Complexe cplxA, Complexe cplxB)
        {
            Complexe cplxC = new Complexe(cplxA.A + cplxB.A, cplxA.B + cplxB.B);
            return cplxC;
        }
        public static Complexe operator -(Complexe cplxA, Complexe cplxB)
        {
            Complexe cplxC = new Complexe(cplxA.A - cplxB.A, cplxA.B - cplxB.B);
            return cplxC;
        }
        public static Complexe operator *(Complexe cplxA, Complexe cplxB)
        {
            Complexe cplxC = new Complexe((cplxA.A * cplxB.A) - (cplxA.B * cplxB.B), ((cplxA.A * cplxB.B) + (cplxA.B * cplxB.A)));
            return cplxC;
        }
        public override string ToString()
        {
            return  A + " + " + B + "i";
        }
    }
}
