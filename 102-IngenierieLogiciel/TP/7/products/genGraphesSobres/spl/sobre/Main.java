package spl.sobre;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Graph graphTest = new Graph();
		File folder = new File("./bin");
	    File[] listOfFiles = folder.listFiles();
	    for(int i = 0; i < listOfFiles.length; i++){
	        String filename = listOfFiles[i].getName();
	        if(filename.endsWith(".xml")||filename.endsWith(".XML")) {
//	            System.out.println(filename);
	        }
	    }
		graphTest.createNewGraph("MonGraphSobres");
		graphTest.printlnGraphviz();
	}
}