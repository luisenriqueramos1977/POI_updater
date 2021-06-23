package fh_postdam.ramos.poiupdater;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File;
import java.io.IOException;


public class Updater_Main {
	
	/*
	 * this is a first attemp to parser and upload data
	 */
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		//creating a constructor of file class and parsing an XML file  
		File file = new File("C:\\Users\\luis.ramos\\Downloads\\ssl.backoffice2.reiseland-brandenburg.de.xml");  
		
		//an instance of factory that gives a document builder  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		doc.getDocumentElement().normalize();  
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); 
		//listing all classifification objects
		NodeList nodeList = doc.getElementsByTagName("classification");  
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);  
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;  
				try {
					 //System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
					 String language = eElement.getAttributes().getNamedItem("language").getNodeValue();
			         String revision= eElement.getAttributes().getNamedItem("revision").getNodeValue();
			         String  name = eElement.getAttributes().getNamedItem("name").getNodeValue();
			         String tstamp = eElement.getAttributes().getNamedItem("tstamp").getNodeValue();
			         String id = eElement.getAttributes().getNamedItem("id").getNodeValue();
			         if ((language != null)&& (revision != null) && (name != null) && (tstamp!= null) && (id!= null) ) {
						 System.out.println("\nNode Name :" + node.getNodeName()); 
			        	 System.out.println("classification id: "+id);
			        	 System.out.println("classification language: "+language);
				         System.out.println("classification revision: "+eElement.getAttributes().getNamedItem("revision").getNodeValue());
				         System.out.println("classification name: "+eElement.getAttributes().getNamedItem("name").getNodeValue());
				         System.out.println("classification tstamp: "+eElement.getAttributes().getNamedItem("tstamp").getNodeValue());
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		
		//getting all addresses
		
		
		
		
	}

}
