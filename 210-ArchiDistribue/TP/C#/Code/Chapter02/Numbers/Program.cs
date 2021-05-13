using System;

namespace Numbers
{
    class Program
    {
        static void Main(string[] args)
        {
            // >= 0
            uint naturalNumber = 23;
            // integers
            int integerNumber = -23;
            // single-precision
            float realNumber = 2.3F;
            // double-precision
            double anotherRealNumber = 2.3;
            // 2 million
            int decimalNotation = 2_000_000;
            int binaryNotation = 0b_0001_1110_1000_0100_1000_0000;
            int hexNotation = 0x_001E_8480;
            // check three variables have the same value
            Console.WriteLine($"{decimalNotation == binaryNotation}");
            Console.WriteLine($"{decimalNotation == hexNotation}");
        }
    }
}
