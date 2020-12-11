import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		Graph graphTest = new Graph();
	    File folder = new File("./bin/");
	    File[] listOfFiles = folder.listFiles();
	    ReadXMLFile xmlFile = null;
        String filename = listOfFiles[0].getName();
        if(filename.endsWith(".xml")||filename.endsWith(".XML")) {
        	xmlFile = new ReadXMLFile(listOfFiles[0]);
        }
        //System.out.println(xmlFile.getNameAttribute());
		graphTest.createNewGraph(xmlFile.getNameAttribute());
		graphTest.printlnGraphviz();
	}
}
