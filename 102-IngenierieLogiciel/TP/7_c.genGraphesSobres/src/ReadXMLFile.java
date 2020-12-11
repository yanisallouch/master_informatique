import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException; 

public class ReadXMLFile {
	File filename= new File(".");

	public ReadXMLFile() {
	}
	
	public ReadXMLFile(File filename) {
		this.filename = filename;
	}

	public File getFilename() {
		return filename;
	}

	public void setFilename(File filename) {
		this.filename = filename;
	}
	
	public String getNameAttribute() throws SAXException, IOException, ParserConfigurationException {
		String name= "";
		//an instance of factory that gives a document builder  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Document doc = db.parse(this.getFilename());
		//System.out.println(this.getFilename());
		doc.getDocumentElement().normalize();  
		//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
		NodeList nodeList = doc.getElementsByTagName("feature"); 
		//System.out.println(nodeList.getLength());
		// nodeList is not iterable, so we are using for loop  
		Node node = nodeList.item(0);
		//System.out.println("\nNode Name :" + node.getNodeName());  
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) node;
			name =  eElement.getAttribute("name");
			//System.out.println("name: "+ name);
		} 
		return name;
	}
}
