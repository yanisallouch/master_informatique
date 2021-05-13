using System;
using ClassLibrary1;
public class SaleItem
{
    string _name;
    decimal _cost;

    public SaleItem(string name, decimal cost)
    {
        _name = name;
        _cost = cost;
    }

    public string Name
    {
        get => _name;
        set => _name = value;
    }

    public decimal Price
    {
        get => _cost;
        set => _cost = value;
    }
}

namespace ConsoleApp1
{
    class Program
    {
        private const double Rayon = 10.0;
        private const string Name = "fooabr";
        private const int X = 10;
        private const int Y = 10;

        static void Main(string[] args)
        {
            var item = new SaleItem("Shoes", 19.955m);
            Console.WriteLine($"{item.Name}: sells for {item.Price:C2}");
            Console.WriteLine("Hello World!");

            IGeometrique cercle = new Cercle(Name, X, Y, Rayon);

            ClassLibrary1.Cercle notACercle = new ClassLibrary1.Cercle(Name, X, Y, Rayon);

            Console.WriteLine(cercle);
            Console.WriteLine(notACercle);
        }
    }
}