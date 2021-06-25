package fh_postdam.ramos.poiupdater;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.management.loading.PrivateClassLoader;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.Dataset;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.system.Txn;
import org.apache.jena.util.FileManager;



public class Updater_Main {
	//concepts
	
	private static OntClass  Classification;
	private static DatatypeProperty poi_revision;
	public static DatatypeProperty  poi_name;
	public static DatatypeProperty poi_tstamp;
	public static DatatypeProperty poi_id;
	public static DatatypeProperty poi_language;

	
	

	
	/*
	 * main variables of the system
	 */
	
	private static Model PoiModel;
	//poi dataset  
	private static Dataset PoiDataset;
	private static String Poi_DB_root = "C:\\Users\\luis.ramos\\TDBS\\POI_TDB";
	private static OntModel PoiOntModel;
	/*
	 * this is a first attemp to parser and upload data
	 */
	
	public static void main(String[] args) {
		
		String POI_URL = "https://www.reiseland-brandenburg.de/poi";
		String POI_NS = POI_URL+"#";
		
		//getting the file
		
		//final String inputOntology = "C:\\Users\\luis.ramos\\Desktop\\ontologies\\ontologies\\poi.rdf";
		
		final String aRoot = "C:\\Users\\luis.ramos\\TDBS\\POI_TDB";
		
		//first, we create the model specification
        //FileInputStream fileInputStream = new FileInputStream(inputOntology);
        //if (fileInputStream == null) {
         //   throw new IllegalArgumentException( "File: " + inputOntology + " not found");
        //}
     // create an empty model
        //Model model = ModelFactory.createDefaultModel();
        // read the RDF/XML file
        //model.read(fileInputStream, "");
        // write it to standard out
        //model.write(System.out); 
        //Dataset ds = TDB2Factory.connectDataset(aRoot);
       // Txn.executeWrite(ds, ()-> {RDFDataMgr.read(ds, inputOntology);});
        
        //Txn.executeRead(ds, ()->{
        //    RDFDataMgr.write(System.out, ds, Lang.TRIG) ;
        //}) ;
        //saving dataset 
      //closing writing
       // ds.close();
        
        //setting up ontmodel

        
        
	    OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);//add as parameter
	     
	    
	     /*
		* reading every available dataset
		*/
		//reading poi database
	     PoiDataset = TDB2Factory.connectDataset(Poi_DB_root);
	     PoiDataset.begin(ReadWrite.WRITE) ;
	   //onto model of gleif1

		PoiModel = PoiDataset.getDefaultModel() ;
		PoiOntModel = ModelFactory.createOntologyModel(spec, PoiModel);
		//PoiOntModel.write(System.out); flag for checking the model
		
		//getting every model classes and properties
		Classification = PoiOntModel.getOntClass(POI_NS + "Classification" );
		poi_revision = PoiOntModel.getDatatypeProperty(POI_NS + "revision");
		poi_name = PoiOntModel.getDatatypeProperty(POI_NS + "name");
		poi_tstamp = PoiOntModel.getDatatypeProperty(POI_NS + "tstamp");
		poi_id = PoiOntModel.getDatatypeProperty(POI_NS + "id");
		poi_language = PoiOntModel.getDatatypeProperty(POI_NS + "language");
		
		System.out.println(Classification);
		System.out.println(poi_revision);
		System.out.println(poi_name);
		System.out.println(poi_tstamp);
		System.out.println(poi_id);
		System.out.println(poi_language);

		/*
		 * with the ontmodel loaded in memory, we proceed to check wheter or not there are present element
		 */
		
		//creating a constructor of file class and parsing an XML file  
		File file = new File("C:\\Users\\luis.ramos\\Downloads\\ssl.backoffice2.reiseland-brandenburg.de.xml");  
		//an instance of factory that gives a document builder  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
		Document doc = null;
		try {
			doc = db.parse(file);
		} catch (SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
		
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
					 String str_language = eElement.getAttributes().getNamedItem("language").getNodeValue();
			         String str_revision= eElement.getAttributes().getNamedItem("revision").getNodeValue();
			         String  str_name = eElement.getAttributes().getNamedItem("name").getNodeValue();
			         String str_tstamp = eElement.getAttributes().getNamedItem("tstamp").getNodeValue();
			         String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
			         if ((str_language != null)&& (str_revision != null) && (str_name != null) && (str_tstamp!= null) && (str_id!= null) ) {
						 //System.out.println("\nNode Name :" + node.getNodeName()); 
			        	 System.out.println("classification id: "+str_id);
				         //checking if individual exist
				 		 Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
				 		 
				 		// System.out.println("this instance: "+PoiInstance);
				 		 
				 		 if (PoiInstance == null) {
					 		 System.out.println("null loop ");
				 			 //conversion to valid time stamp
					 		 try {
					 			 //doing tstamp to xsd format exchange
					 			XSDDateTime aDate_xsd = null;
					 			aDate_xsd=JenaUtilities.timestamptoJenaDate_xsd(str_tstamp);
					 			System.out.println("jena date: "+aDate_xsd);
					 			 //and add it to tdb2
					 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  Classification); 
					 			PoiInstance.addLiteral(poi_id, str_id);
					 			PoiInstance.addLiteral(poi_language, str_id);
					 			PoiInstance.addLiteral(poi_revision, str_revision);
					 			PoiInstance.addLiteral(poi_name, str_name);
					 			PoiInstance.addLiteral(poi_tstamp, aDate_xsd);
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println(e.toString());
							}
						} else {
							//check revision, if corresponds, then everything is okay
				 			 System.out.println("poi instance : "+PoiInstance);
				 			 //getting parents if any
				 			 NodeList parentList = (NodeList) JenaUtilities.getFirstChildNodeByName(node, "parents");
				 			 System.out.println("parentList: "+parentList.getLength());
				 			 for (int ichild = 0; ichild < parentList.getLength(); ichild++) {
				 				  System.out.println(parentList.item(ichild).getNodeName().toString());
				 				  //if item is classification!!!
				 				  Node node_parent = parentList.item(ichild);  
				 				  if (node.getNodeType() == Node.ELEMENT_NODE) {
				 					Element element_parent = (Element) node_parent; 
							        String str_parent_id = element_parent.getAttributes().getNamedItem("id").getNodeValue();
							        System.out.println("str_parent_id: "+str_parent_id);
							        //link classification to parents
				 					
				 				  }
				 			}//end for childNodes
				 		
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}//end for
		
		//closing comunication with origin databases
		PoiDataset.commit();
		PoiDataset.close();
		

		
		//getting all addresses
		
		
		
		
	}


	

}
