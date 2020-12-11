
package Dico;

import java.io.*;

public class Main {


    public static void main(String args[]) {

	System.out.println("\nTEST DE LA CLASSE ORDEREDDICTIONARY");	
	OrderedDictionary ordDico = new OrderedDictionary(3);
	ordDico.test();

	System.out.println("\nTEST DE LA CLASSE FASTDICTIONARY");	
	FastDictionary fastDico = new FastDictionary(3);
	fastDico.test();
	
    }

}
