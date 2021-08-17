package fh_postdam.ramos.poiupdater;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.management.loading.PrivateClassLoader;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.lang3.builder.Diff;
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryIteratorException;
import java.security.spec.DSAGenParameterSpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.jena.rdf.model.Literal;
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
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.CannotCreateException;
import org.apache.jena.sparql.function.library.print;
import org.apache.jena.system.Txn;
import org.apache.jena.util.FileManager;



public class Updater_Main {
	//concepts
	
	private static OntClass  Classification;
	private static OntClass  Coordinate;
	private static OntClass  District;
	private static OntClass  Department;
	private static OntClass  Region;
	private static OntClass  Excursionsregion;
	private static OntClass  Aquaticsdistrict;
	private static OntClass  State;
	private static OntClass  Location;
	
	
	
	//poi properties
	private static DatatypeProperty poi_location_alias;
	private static DatatypeProperty poi_revision;
	public static DatatypeProperty  poi_name;
	public static DatatypeProperty poi_tstamp;
	public static DatatypeProperty poi_id;
	public static DatatypeProperty poi_language;
	public static DatatypeProperty poi_equis;
	public static DatatypeProperty poi_ye;
	public static DatatypeProperty poi_coordinateType;
	
	public static ObjectProperty poi_parents;
	public static ObjectProperty address;
	public static ObjectProperty allowedController;
	public static ObjectProperty category;
	public static ObjectProperty classification_category_poi;
	public static ObjectProperty connection;
	public static ObjectProperty connectionType;
	public static ObjectProperty controller;
	public static ObjectProperty coordinates;
	public static ObjectProperty department;
	public static ObjectProperty district;
	public static ObjectProperty location;
	public static ObjectProperty openinghours;
	public static ObjectProperty weekday;
	public static ObjectProperty classification_excursionsregion_location;
	public static ObjectProperty classification_aquaticsdistrict_location;
	public static ObjectProperty poi_region;
	public static ObjectProperty poi_state;

	
	
	
	
	//rdf standard properties
	public static Property rdf_ns_type;
	
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
		String xs= "http://www.w3.org/2001/XMLSchema#";
		String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns";
		String rdf_ns = rdf+"#";
		String rdfs = "http://www.w3.org/2000/01/rdf-schema";
		String rdfs_ns = rdfs+"#";
		String xml="xml:";
		String xml_ns =xml+"#";

		
		//getting the file
		
		//final String inputOntology = "C:\\Users\\luis.ramos\\Desktop\\ontologies\\ontologies\\poi.rdf";
		
		final String aRoot = "C:\\Users\\luis.ramos\\TDBS\\POI_TDB";
		
		final Scanner input = new Scanner(System.in);//set up reader
		
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
		
		//creating specific class
		
		 try {
			    Aquaticsdistrict = PoiOntModel.getOntClass(POI_NS + "Aquaticsdistrict" );
			    Excursionsregion = PoiOntModel.getOntClass(POI_NS + "Excursionsregion" );
		    	
			} catch (Exception e) {
				// TODO: handle exception
				Aquaticsdistrict = PoiOntModel.createClass( POI_NS + "Aquaticsdistrict" );
				Excursionsregion = PoiOntModel.createClass( POI_NS + "Excursionsregion" );
			}
		 

			try {
				poi_location_alias = PoiOntModel.getDatatypeProperty(POI_NS + "alias");
				if (poi_location_alias==null) {
					poi_location_alias = PoiOntModel.createDatatypeProperty(POI_NS + "alias");
				}
				
				classification_excursionsregion_location = PoiOntModel.getObjectProperty(POI_NS + "classification_excursionsregion_location");
				if (classification_excursionsregion_location==null) {
					classification_excursionsregion_location = PoiOntModel.createObjectProperty(POI_NS + "classification_excursionsregion_location");
				}
				
				classification_aquaticsdistrict_location= PoiOntModel.getObjectProperty(POI_NS + "classification_aquaticsdistrict_location");
				if (classification_aquaticsdistrict_location==null) {
					classification_aquaticsdistrict_location = PoiOntModel.createObjectProperty(POI_NS + "classification_aquaticsdistrict_location");
				}
				
				poi_region = PoiOntModel.getObjectProperty(POI_NS + "region");
				if (poi_region==null) {
					poi_region = PoiOntModel.createObjectProperty(POI_NS + "region");
				}
				
			
				poi_state = PoiOntModel.getObjectProperty(POI_NS + "state");
				if (poi_state==null) {
					poi_state = PoiOntModel.createObjectProperty(POI_NS + "state");
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("poi_location_alias not created");
			}
		 
		 
		
		//getting every model classes and properties
		Classification = PoiOntModel.getOntClass(POI_NS + "Classification" );
		Coordinate = PoiOntModel.getOntClass(POI_NS + "Coordinate" );
		
		District = PoiOntModel.getOntClass(POI_NS + "District" );
		Department = PoiOntModel.getOntClass(POI_NS + "Department" );
		Region = PoiOntModel.getOntClass(POI_NS + "Region" );
		Excursionsregion = PoiOntModel.getOntClass(POI_NS + "Excursionsregion" );
		Aquaticsdistrict = PoiOntModel.getOntClass(POI_NS + "Aquaticsdistrict" );
		State = PoiOntModel.getOntClass(POI_NS + "State" );
		Location = PoiOntModel.getOntClass(POI_NS + "Location" );
		
		poi_revision = PoiOntModel.getDatatypeProperty(POI_NS + "revision");
		poi_name = PoiOntModel.getDatatypeProperty(POI_NS + "name");
		poi_tstamp = PoiOntModel.getDatatypeProperty(POI_NS + "tstamp");
		poi_id = PoiOntModel.getDatatypeProperty(POI_NS + "id");
		poi_language = PoiOntModel.getDatatypeProperty(POI_NS + "language");
		poi_equis = PoiOntModel.getDatatypeProperty(POI_NS + "x_type");
		poi_ye = PoiOntModel.getDatatypeProperty(POI_NS + "y_type");
		poi_coordinateType = PoiOntModel.getDatatypeProperty(POI_NS + "coordinateType");
		
		//searching for alias, or creating it
		
		
		//getting object properties
		
		address= PoiOntModel.getObjectProperty(POI_NS + "address");
		allowedController = PoiOntModel.getObjectProperty(POI_NS + "allowedController");
		category = PoiOntModel.getObjectProperty(POI_NS + "category");
		classification_category_poi = PoiOntModel.getObjectProperty(POI_NS + "classification_category_poi");
		connection= PoiOntModel.getObjectProperty(POI_NS + "connection");
		connectionType= PoiOntModel.getObjectProperty(POI_NS + "connectionType");
		controller= PoiOntModel.getObjectProperty(POI_NS + "controller");
		coordinates= PoiOntModel.getObjectProperty(POI_NS + "coordinates");
		department= PoiOntModel.getObjectProperty(POI_NS + "department");
		district= PoiOntModel.getObjectProperty(POI_NS + "district");
		location= PoiOntModel.getObjectProperty(POI_NS + "location");
		openinghours= PoiOntModel.getObjectProperty(POI_NS + "openinghours");
		weekday= PoiOntModel.getObjectProperty(POI_NS + "weekday");
		
		rdf_ns_type = PoiOntModel.getProperty(rdf_ns + "type");
		
		System.out.println(Classification);
		System.out.println(Aquaticsdistrict);
		System.out.println(Excursionsregion);
		System.out.println(Coordinate);
		System.out.println(poi_revision);
		System.out.println(poi_name);
		System.out.println(poi_tstamp);
		System.out.println(poi_id);
		System.out.println(poi_language);
		System.out.println(poi_equis);
		System.out.println(poi_ye);
		System.out.println(poi_coordinateType);
		System.out.println(rdf_ns_type);
		System.out.println(address);
		System.out.println(allowedController);
		System.out.println(category);
		System.out.println(classification_category_poi);
		System.out.println(connection);
		System.out.println(connectionType);
		System.out.println(controller);
		System.out.println(coordinates);
		System.out.println(department);
		System.out.println(district);
		System.out.println(location);
		System.out.println(openinghours);
		System.out.println(weekday);
		System.out.println(poi_location_alias);
		System.out.println(classification_excursionsregion_location);
		System.out.println(classification_aquaticsdistrict_location);
		System.out.println(poi_region);
		System.out.println(poi_state);
	


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
		
		/*
		 * updating classification
		 */
		
		System.out.println("Do you want to update classification? ");
		String aResponse = input.nextLine();    //ans wer for first general loop
		if (aResponse.equalsIgnoreCase("Yes")) {
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
					         //checking if individual exist
					 		 Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
					 		 if (PoiInstance == null) {
						 		 //System.out.println("null loop ");
					 			 //conversion to valid time stamp
						 		 try {
						 			 //doing tstamp to xsd format exchange
						 			XSDDateTime aDate_xsd = null;
						 			aDate_xsd=JenaUtilities.timestamptoJenaDate_xsd(str_tstamp);
						 			//System.out.println("jena date: "+aDate_xsd);
						 			 //and add it to tdb2
						 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  Classification); 
						 			PoiInstance.addLiteral(poi_id, str_id);
						 			PoiInstance.addLiteral(poi_language, str_language);
						 			PoiInstance.addLiteral(poi_revision, str_revision);
						 			PoiInstance.addLiteral(poi_name, str_name);
						 			PoiInstance.addLiteral(poi_tstamp, aDate_xsd);
								} catch (Exception e) {
									// TODO: handle exception
									System.out.println(e.toString());
								}
							} else {
								//check revision, if corresponds, then everything is okay
					 			 //System.out.println("poi instance : "+PoiInstance);
					 			 //getting parents if any
					 			 NodeList parentList = (NodeList) JenaUtilities.getFirstChildNodeByName(node, "parents");
					 			 //System.out.println("parentList size: "+parentList.getLength());
					 			 //if some parents we iterate
					 			 if (parentList.getLength()>0) {
					 				 //the we iterate over the list
					 				for (int ichild = 0; ichild < parentList.getLength(); ichild++) {
						 				  //System.out.println(parentList.item(ichild).getNodeName().toString());
						 				  //if node is classification type, then we continue
						 				  if (parentList.item(ichild).getNodeName().toString()=="classification") {
						 					//if item is classification!!!
							 				  Node node_parent = parentList.item(ichild);  
							 				  if (node.getNodeType() == Node.ELEMENT_NODE) {
							 					Element element_parent = (Element) node_parent; 
										        String str_parent_id = element_parent.getAttributes().getNamedItem("id").getNodeValue();
										        //System.out.println("str_parent_id: "+str_parent_id);
										        //check if object with such relation is present
											    List<Individual> myindividuals = new ArrayList<>();
											    try {
											    	myindividuals= JenaUtilities.getLinkedIndividualsbyProperty(PoiOntModel, PoiInstance, rdf_ns_type);
											    	boolean ind_present = JenaUtilities.checkIndividualinListbyLiteralValue(myindividuals, poi_id, str_parent_id);
											    	//iterate over individuals to get id
											        if (! ind_present) {//if no individual linked with this property value, we create it
														//then we proceed to get the indvidual with this id
											        	try {
											        		Individual linking_individual = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_parent_id);
												 			PoiInstance.addProperty(rdf_ns_type, linking_individual);
														} catch (Exception e) {
															// TODO: handle exception
															//Ojo to log system
															System.out.println("warning: no individual with this id: "+str_parent_id);
														}
													}
												    
												} catch (Exception e) {
													e.printStackTrace();
												}
							 				  }
						 				  }
						 			 }//end for childNodes
					 			 }//end if parentList size
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}//end for classification
		}//endif update classification
		
		
		//***************  getting every available coordinate instance ******************
		System.out.println("Do you want to update coordinate? ");
		aResponse="";
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList coordinateList = doc.getElementsByTagName("coordinate");  
			//System.out.println("number of coordinates: "+coordinateList.getLength());
			for (int i = 0; i < coordinateList.getLength(); i++) {
				Node node = coordinateList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					// get text
					try {
						String equis = eElement.getElementsByTagName("x").item(0).getTextContent();
		                String ye = eElement.getElementsByTagName("y").item(0).getTextContent();
		                String coordinateType = eElement.getElementsByTagName("type").item(0).getTextContent();
		                String coordinate_x_y = "coordinate_"+equis+"_"+ye;
		                //check if exist individual with x and y coordinates
		                Individual anIndividual = null;
		            	Resource aResource = null;
		        		ResIterator dmbIte = PoiOntModel.listResourcesWithProperty(poi_equis, Float.parseFloat(equis));
		        
		                // if there is any individual, we search for y, if not, we create the individual
		        		if (dmbIte.hasNext()) {
		        			//System.out.println(" some resource with value "+equis);
		        			boolean existing_resource = false;
		        			while (dmbIte.hasNext()) {
			                	aResource = dmbIte.next();
				                anIndividual =PoiOntModel.getIndividual(aResource.toString());
				                //System.out.println("individual to be compared: "+anIndividual);
				                Literal y_value = (Literal) anIndividual.getPropertyValue(poi_ye);
				                //System.out.println("individuals literal float value: "+y_value.getFloat());
				                if (Float.toString(y_value.getFloat()).contains(ye)) {
				                	existing_resource=true;
									//System.out.println("coordinate resource "+coordinate_x_y+ " just exist");
									break;
								} else {
									existing_resource=false;
								}
				                //checking if result exist
			                }//end of while
		        			
		        			 if (! existing_resource) {
									//System.out.println("no y coordinate");
									coordinate_x_y = "coordinate_"+equis+"_"+ye;
									//System.out.println("must create object as: "+coordinate_x_y);
									//creating coordinate individual
									 //and add it to tdb2
									Individual PoiCoordinate = PoiOntModel.createIndividual( POI_NS +  coordinate_x_y,  Coordinate); 
									PoiCoordinate.addLiteral(poi_equis, Float.parseFloat(equis));
									PoiCoordinate.addLiteral(poi_ye, Float.parseFloat(ye));
									PoiCoordinate.addLiteral(poi_coordinateType, coordinateType);
							}//if resource does not exist
		        			 
						} else {
							coordinate_x_y = "coordinate_"+equis+"_"+ye;
							//must create generic method, individual with n properties
							//creating coordinate individual
							 //and add it to tdb2
							Individual PoiCoordinate = PoiOntModel.createIndividual( POI_NS +  coordinate_x_y,  Coordinate); 
							PoiCoordinate.addLiteral(poi_equis, Float.parseFloat(equis));
							PoiCoordinate.addLiteral(poi_ye, Float.parseFloat(ye));
							PoiCoordinate.addLiteral(poi_coordinateType, coordinateType);
						}//end if to check existence
		        	
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
	                
				}//if node
				
			}//end for coordinateList

		}//endif coordinate
		
				
		//getting all tags linked to locations
		System.out.println("Do you want to update district? ");
		
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList districtList = doc.getElementsByTagName("district");  
			//System.out.println("number of districts: "+districtList.getLength());
			for (int i = 0; i < districtList.getLength(); i++) {
				Node node = districtList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 if ((str_type != null)&& (str_id != null)) {
								System.out.println("str_id: "+str_id);
								//checking if individual exist
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 System.out.println("null districtList ");
						 			 //conversion to valid time stamp
							 		 try {
							 			 //and add it to tdb2
							 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  District); 
							 			PoiInstance.addLiteral(poi_id, str_id);
							 			System.out.println("created object type "+ str_type+" with id "+ str_id);
							 			
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.toString());
									}
								}//if poi instance 
						 		else {
						 			System.out.println("exist object "+str_id+ " type "+str_type);
						 		}
						 		
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList
		}//endif district
		
		System.out.println("Do you want to update department? ");
		
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList departmentList = doc.getElementsByTagName("department");  
			System.out.println("number of departments: "+departmentList.getLength());
			for (int i = 0; i < departmentList.getLength(); i++) {
				Node node = departmentList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 if ((str_type != null)&& (str_id != null)) {
								System.out.println("str_type: "+str_type);
								System.out.println("str_id: "+str_id);
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 //System.out.println("null loop ");
						 			 //conversion to valid time stamp
							 		 try {
							 			 //and add it to tdb2
							 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  Department); 
							 			PoiInstance.addLiteral(poi_id, str_id);
							 			System.out.println("created object type "+ str_type+" with id "+ str_id);
							 			
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.toString());
									}
								}//if poi instance 
						 		else {
						 			System.out.println("exist object "+str_id+ " type "+str_type);
						 		}
								
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList
		}//endif department
		
		
		System.out.println("Do you want to update region? ");
		
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList regionList = doc.getElementsByTagName("region");  
			System.out.println("number of regions: "+regionList.getLength());
			for (int i = 0; i < regionList.getLength(); i++) {
				Node node = regionList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 if ((str_type != null)&& (str_id != null)) {
								System.out.println("str_id: "+str_id);
								//checking if individual exist
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 //System.out.println("null loop ");
						 			 //conversion to valid time stamp
							 		 try {
							 			 //and add it to tdb2
							 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  Region); 
							 			PoiInstance.addLiteral(poi_id, str_id);
							 			System.out.println("created object type "+ str_type+" with id "+ str_id);
							 			
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.toString());
									}
								}//if poi instance 
						 		else {
						 			System.out.println("exist object "+str_id+ " type "+str_type);
						 		}
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList

		}//endif region
		
		System.out.println("Do you want to update excursionsregion? ");
		
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList classification_excursionsregion_locationList = doc.getElementsByTagName("classification_excursionsregion_location");  
			System.out.println("number of classification_excursionsregion_locations: "+classification_excursionsregion_locationList.getLength());
			for (int i = 0; i < classification_excursionsregion_locationList.getLength(); i++) {
				Node node = classification_excursionsregion_locationList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 if ((str_type != null)&& (str_id != null)) {
								System.out.println("str_type: "+str_type);
								System.out.println("str_id: "+str_id);
								//checking if individual exist
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 //System.out.println("null loop ");
						 			 //conversion to valid time stamp
							 		 try {
							 			 //and add it to tdb2
							 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  Excursionsregion); 
							 			PoiInstance.addLiteral(poi_id, str_id);
							 			System.out.println("created object type "+ str_type+" with id "+ str_id);
							 			
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.toString());
									}
								}//if poi instance 
						 		else {
						 			System.out.println("exist object "+str_id+ " type "+str_type);
						 		}
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList
		}//endif 
				
				
		System.out.println("Do you want to update aquaticsdistrict? ");
		
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList classification_aquaticsdistrict_locationList = doc.getElementsByTagName("classification_aquaticsdistrict_location");  
			System.out.println("number of classification_aquaticsdistrict_locations: "+classification_aquaticsdistrict_locationList.getLength());
			for (int i = 0; i < classification_aquaticsdistrict_locationList.getLength(); i++) {
				Node node = classification_aquaticsdistrict_locationList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 if ((str_type != null)&& (str_id != null)) {
								System.out.println("str_type: "+str_type);
								System.out.println("str_id: "+str_id);
								//checking if individual exist
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 //System.out.println("null loop ");
						 			 //conversion to valid time stamp
							 		 try {
							 			 //and add it to tdb2
							 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  Aquaticsdistrict); 
							 			PoiInstance.addLiteral(poi_id, str_id);
							 			System.out.println("created object type "+ str_type+" with id "+ str_id);
							 			
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.toString());
									}
								}//if poi instance 
						 		else {
						 			System.out.println("exist object "+str_id+ " type "+str_type);
						 		}
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList
		}//aquatic district
		
		System.out.println("Do you want to update state? ");
		
		aResponse = input.nextLine();    //ans wer for first general loop
		
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList stateList = doc.getElementsByTagName("state");  
			System.out.println("number of states: "+stateList.getLength());
			for (int i = 0; i < stateList.getLength(); i++) {
				Node node = stateList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 if ((str_type != null)&& (str_id != null)) {
								System.out.println("str_type: "+str_type);
								System.out.println("str_id: "+str_id);
								//checking if individual exist
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 //System.out.println("null loop ");
						 			 //conversion to valid time stamp
							 		 try {
							 			 //and add it to tdb2
							 			PoiInstance = PoiOntModel.createIndividual( POI_NS +  str_id,  State); 
							 			PoiInstance.addLiteral(poi_id, str_id);
							 			System.out.println("created object type "+ str_type+" with id "+ str_id);
							 			
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println(e.toString());
									}
								}//if poi instance 
						 		else {
						 			System.out.println("exist object "+str_id+ " type "+str_type);
						 		}
						 }
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList
		}//endif state
		
				
		System.out.println("Do you want to update location? ");
		aResponse = input.nextLine();    //ans wer for first general loop
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList locationList = doc.getElementsByTagName("location");  
			//System.out.println("number of locations: "+locationList.getLength());
			
			//getting name of every location
			
			for (int i = 0; i < locationList.getLength(); i++) {
				Node node = locationList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 String str_language = eElement.getAttributes().getNamedItem("language").getNodeValue();
						 String str_revision = eElement.getAttributes().getNamedItem("revision").getNodeValue();
						 String str_name = eElement.getAttributes().getNamedItem("name").getNodeValue();
						 String str_tstamp = eElement.getAttributes().getNamedItem("tstamp").getNodeValue();
						 //getting list of node for every tag
						 System.out.println("location name: "+str_name);
						
						 //checking if location exist in kb
						 Individual aLocation = PoiOntModel.getIndividual(POI_NS + str_id );
						 
						 if ((aLocation == null) && (str_language != null)&& (str_revision != null) && (str_name != null) && (str_tstamp!= null) && (str_id!= null)) {
							 //if no location and all attributes are present, we fill out the objekt
							//doing tstamp to xsd format exchange
					 		XSDDateTime aDate_xsd = null;
					 		aDate_xsd=JenaUtilities.timestamptoJenaDate_xsd(str_tstamp);
							 //creating individual and adding attributes
							 Individual PoiLocation = PoiOntModel.createIndividual( POI_NS +  str_id,  Location);
					 		 PoiLocation.addLiteral(poi_id, str_id);
					 		 PoiLocation.addLiteral(poi_language, str_language);
					 		 PoiLocation.addLiteral(poi_revision, str_revision);
					 		 PoiLocation.addLiteral(poi_name, str_name);
					 		 PoiLocation.addLiteral(poi_tstamp, aDate_xsd);
							 
							 //we surrounded district getting with try catch to avoid null exception
							 try {
								 NodeList nodedistrict = eElement.getElementsByTagName("district");
								 NodeList childDistrict = nodedistrict.item(0).getChildNodes();
								 //System.out.println("node child district length: "+childDistrict.getLength());
								 //getting all ids of this districts list
								 for (int i_list = 0; i_list < childDistrict.getLength(); i_list++) {
										Node this_node = childDistrict.item(i_list);  
										System.out.println("\nCurrent Element :" + this_node.getNodeName());
										if (this_node.getNodeType() == Node.ELEMENT_NODE) {
											System.out.println("node is element");
											Element listElement = (Element) this_node;  
											try {
										         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
										         System.out.println("str_id_node: "+str_id_node);
										       //searching for department individual in ontmodel
											 	Individual PoiInstance = PoiOntModel.getIndividual(POI_NS + str_id_node );
											 	PoiLocation.addLiteral(district, PoiInstance);
											    System.out.println("instance district: "+PoiInstance.toString());
											} catch (Exception e) {
												// TODO: handle exception
												System.out.println("no str_id_node: ");
											}
										}//endif for every node
								 }//endfor of the node list
								} catch (Exception e) {
										System.out.println("no district information available");
								}//end try catch district
							//try catch for department
							 try {
								 NodeList nodedepartment = eElement.getElementsByTagName("department");
								 NodeList childDepartment = nodedepartment.item(0).getChildNodes();
								 System.out.println("nodedepartment length: "+childDepartment.getLength());
								 
								//getting all ids of this districts list
								 for (int i_list = 0; i_list < childDepartment.getLength(); i_list++) {
										Node this_node = childDepartment.item(i_list);  
										System.out.println("\nCurrent Element :" + this_node.getNodeName());

										if (this_node.getNodeType() == Node.ELEMENT_NODE) {
											Element listElement = (Element) this_node;  
											try {
												//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
										         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
										         System.out.println("str_id_nodedepartment: "+str_id_node);
										         //searching for department individual in ontmodel
											 		Individual PoiInstance = PoiOntModel.getIndividual(POI_NS + str_id_node );
											 		PoiLocation.addLiteral(department, PoiInstance);
											        System.out.println("instance department: "+PoiInstance.toString());

											} catch (Exception e) {
												// TODO: handle exception
												System.out.println("error adding department ");
											}
										}//endif for every node
								 }//endfor of the node list
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no department information available");
							}//ends try catch for nodelist ifnormation

							 try {
								 NodeList noderegion = eElement.getElementsByTagName("region");
								 NodeList childRegion = noderegion.item(0).getChildNodes();
								 System.out.println("node region length: "+childRegion.getLength());
								 
								//getting all ids of this districts list
								 for (int i_list = 0; i_list < childRegion.getLength(); i_list++) {
										Node this_node = childRegion.item(i_list);  
										System.out.println("\nCurrent Element :" + this_node.getNodeName());

										if (this_node.getNodeType() == Node.ELEMENT_NODE) {
											Element listElement = (Element) this_node;  
											try {
												//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
										         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
										         System.out.println("str_id_noderegion: "+str_id_node);
											 	 Individual PoiInstance = PoiOntModel.getIndividual(POI_NS + str_id_node );
											 	 PoiLocation.addLiteral(poi_region, PoiInstance);
											     System.out.println("instance region: "+PoiInstance.toString());
											} catch (Exception e) {
												// TODO: handle exception
												//add error to log
												System.out.println("error while searching instance region ");
											}
										}//endif for every node
								 }//endfor of the node list
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no region information available");
							}//ends try catch for region
							 	
							 try {
								 NodeList nodecoordinates = eElement.getElementsByTagName("coordinates");
								 System.out.println("nodecoordinates length: "+nodecoordinates.getLength());
								 NodeList childCoordinates = nodecoordinates.item(0).getChildNodes();
								 
								 for (int i_list = 0; i_list < childCoordinates.getLength(); i_list++) {
										Node this_node = childCoordinates.item(i_list);  
										System.out.println("\nCurrent coordinate Element :" + this_node.getNodeName());
										//getting node list of coordinates node

										if (this_node.getNodeType() == Node.ELEMENT_NODE) {
											Element listElement = (Element) this_node;  
											try {
												String equis = listElement.getElementsByTagName("x").item(0).getTextContent();
								                String ye = listElement.getElementsByTagName("y").item(0).getTextContent();
								                String coordinateType = listElement.getElementsByTagName("type").item(0).getTextContent();
								                String coordinate_x_y = "coordinate_"+equis+"_"+ye;
								                //System.out.println("must search coordinate as: "+coordinate_x_y);
								                Individual PoiInstance = PoiOntModel.getIndividual(POI_NS+coordinate_x_y);
								                PoiLocation.addLiteral(coordinates, PoiInstance);
										        System.out.println("instance Coordinate: "+PoiInstance.toString());
										        
											} catch (Exception e) {
												// TODO: handle exception
												System.out.println("no str_id_nodecoordinates ");
											}
										}//endif for every node
								 }//endfor of the node list

							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no coordinates information available");
							}//end try catch for coordinates
							 						 
							 try {
								 NodeList nodeexcursionsRegion = eElement.getElementsByTagName("excursionsRegion");
								 NodeList childExcursionsRegion = nodeexcursionsRegion.item(0).getChildNodes();
								 System.out.println("node childExcursionsRegion length: "+childExcursionsRegion.getLength());
								 for (int i_list = 0; i_list < childExcursionsRegion.getLength(); i_list++) {
										Node this_node = childExcursionsRegion.item(i_list);  
										System.out.println("\nCurrent Element :" + this_node.getNodeName());

										if (this_node.getNodeType() == Node.ELEMENT_NODE) {
											Element listElement = (Element) this_node;  
											try {
												//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
										         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
										         System.out.println("str_id_nodeexcursionsRegion: "+str_id_node);
											     Individual PoiInstance = PoiOntModel.getIndividual(POI_NS + str_id_node );
											     PoiLocation.addLiteral(classification_excursionsregion_location, PoiInstance);
											     System.out.println("instance excursionsRegion: "+PoiInstance.toString());
											} catch (Exception e) {
												// TODO: handle exception
												System.out.println("error while searching instance excursionsRegion: ");
											}
										}//endif for every node
								 }//endfor of the node list
								 
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no excursionsRegion information available");
							}//end try catch excursion
							 
							 try {
								 
								 NodeList nodeaquaticsDistrict = eElement.getElementsByTagName("aquaticsDistrict");
								 NodeList childAquaticsDistrict = nodeaquaticsDistrict.item(0).getChildNodes();
								 System.out.println("childAquaticsDistrict length: "+childAquaticsDistrict.getLength());
								 for (int i_list = 0; i_list < childAquaticsDistrict.getLength(); i_list++) {
										Node this_node = childAquaticsDistrict.item(i_list);  
										System.out.println("\nCurrent Element :" + this_node.getNodeName());

										if (this_node.getNodeType() == Node.ELEMENT_NODE) {
											Element listElement = (Element) this_node;  
											try {
												//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
										         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
										         System.out.println("str_id_nodeaquaticsDistrict: "+str_id_node);
											 	 Individual PoiInstance = PoiOntModel.getIndividual(POI_NS + str_id_node );
											 	 PoiLocation.addLiteral(classification_aquaticsdistrict_location, PoiInstance);
											     System.out.println("instance aquaticsDistrict: "+PoiInstance.toString());
											} catch (Exception e) {
												// TODO: handle exception
												System.out.println("no aquaticsDistrict instance: ");
											}
										}//endif for every node
								 }//endfor of the node list
								
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no aquaticsDistrict information available");
							}//ends try catch aquaticdistrict
							 						 
							 try {
								 NodeList stateNode = eElement.getElementsByTagName("state");
								 String str_id_state = stateNode.item(0).getAttributes().getNamedItem("id").getNodeValue();
								 System.out.println("str_id_state length: "+str_id_state);
							 	Individual PoiInstance = PoiOntModel.getIndividual(POI_NS + str_id_state );
							 	PoiLocation.addLiteral(poi_state, PoiInstance);
							     System.out.println("instance state: "+PoiInstance.toString());
							     
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no state instance available");
							}//ends try catch 
							
							//trying two option aliases
							 
							 try {
								 NodeList aliasNodes = eElement.getElementsByTagName("alias");
								 Node aliasNode = aliasNodes.item(0);
								 Element aliasElement = (Element) aliasNode;
								 System.out.println("str_alias: "+aliasElement.getTextContent());
					             PoiLocation.addProperty(poi_location_alias, aliasElement.getTextContent());
								 //add alias property value
								 
							} catch (Exception e) {
								// TODO: handle exception
								System.out.println("no alias information available");

							}//end try catch alias
							 
						} else {
							System.out.println("exist object "+str_id+ " type "+str_type);
						}//endif aLocation exist	 
					} catch (Exception e) {
						// TODO: handle exception
					}//try for Location
				}//endif Location
			}//endfor?
		}//endif for update Location
		
		
		/*
		 * getting opening hours information
		 */
		
		System.out.println("Do you want to update opening hours? ");
		aResponse = input.nextLine();    //ans wer for first general loop
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList openinghourList = doc.getElementsByTagName("openingtimedate");
			System.out.println("openingHoursList size: "+openinghourList.getLength());
			//iterate over the list 
			for (int i = 0; i < openinghourList.getLength(); i++) {
				Node node = openinghourList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node; 
					NodeList childNodes = eElement.getChildNodes();
					// creating a My HashTable Dictionary
					Hashtable<String, String> openinghours_dict = new Hashtable<String, String>();
					//iterate over every child
					for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
					    Node n = childNodes.item(i2);
					    if (n.getNodeType() == Node.ELEMENT_NODE) {
							Element childElement = (Element) n;
							openinghours_dict.put(childElement.getNodeName(), childElement.getTextContent());
					    }//end if for child
					}//for child
					//checking if an opening hour exist
					if (!(JenaUtilities.checkIndividualProperties(POI_NS, "OpeningHours", PoiOntModel, openinghours_dict))) {
						try {
							System.out.println("opening hour object to be created: "+openinghours_dict);
						} catch (Exception e) {
							System.out.println("opening hours is null");
						}
					}
					
				}//endif poiList
			}//end for poiList
		}//end if for 
		
		
		/*
		 * getting poi information
		 */
		
		System.out.println("Do you want to update POI? ");
		aResponse = input.nextLine();    //ans wer for first general loop
		if (aResponse.equalsIgnoreCase("Yes")) {
			NodeList poiList = doc.getElementsByTagName("poi");
			for (int i = 0; i < 3; i++) {//poiList.getLength()
				Node node = poiList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					NodeList childNodes = eElement.getChildNodes();
					try {
						String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 String str_language = eElement.getAttributes().getNamedItem("language").getNodeValue();
						 String str_revision = eElement.getAttributes().getNamedItem("revision").getNodeValue();
						 String str_name = eElement.getAttributes().getNamedItem("name").getNodeValue();
						 String str_tstamp = eElement.getAttributes().getNamedItem("tstamp").getNodeValue();
						 //getting list of node for every tag
						 System.out.println("poi name: "+str_name);
						 System.out.println("getting corresponding poi values and printing it (must created object) and link it");
						 for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
							    Node n = childNodes.item(i2);
								    if (n.getNodeType() == Node.ELEMENT_NODE) {
										Element childElement = (Element) n;
										NodeList childSubNodes = childElement.getChildNodes();
										System.out.println("node name: "+ childElement.getNodeName());
										System.out.println("node value: "+ childElement.getTextContent());
										
										
										
										for (int i3 = 0; i3 < childSubNodes.getLength(); i3++) {
											Node SubNode = childSubNodes.item(i3);
											if (SubNode.getNodeType() == Node.ELEMENT_NODE) {
												Element childSubElement = (Element) SubNode;
												System.out.println("\t node child name: "+ childSubElement.getNodeName());
												System.out.println("\t node child value: "+ childSubElement.getTextContent());
												
												//for the case of classification_targetgroup_poi
												if (childSubElement.getNodeName()=="classification_targetgroup_poi") {
													String targetgroup_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
													String targetgroup_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
													System.out.println("\t\t node targetgroup_type id: "+ targetgroup_id);
													System.out.println("\t\t node targetgroup_type type: "+ targetgroup_type);
													
													
												}//if childSubElement.getNodeName()=="pricerangecomplex"
												
												//for the case of classification_metainformation_poi
												if (childSubElement.getNodeName()=="classification_metainformation_poi") {
													String metainformation_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
													String metainformation_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
													System.out.println("\t\t node metainformation_type id: "+ metainformation_id);
													System.out.println("\t\t node metainformation_type type: "+ metainformation_type);
													
													
												}//if childSubElement.getNodeName()=="pricerangecomplex"
												
												//for the case of classification
												if (childSubElement.getNodeName()=="classification") {
													String classification_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
													String classification_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
													System.out.println("\t\t node classification id: "+ classification_id);
													System.out.println("\t\t node classification type: "+ classification_type);
													
													
												}//if childSubElement.getNodeName()=="pricerangecomplex"
												
												//for the case of pricerangecomplex
												if (childSubElement.getNodeName()=="pricerangecomplex") {
													NodeList connectionsChildNodes = childSubElement.getChildNodes();
													for (int i4 = 0; i4 < connectionsChildNodes.getLength(); i4++) {
														Node connectionNode = connectionsChildNodes.item(i4);
														if (connectionNode.getNodeType() == Node.ELEMENT_NODE) {
														Element childConnectionElement = (Element) connectionNode;
														System.out.println("\t\t node pricerangecomplex child name: "+ childConnectionElement.getNodeName());
														System.out.println("\t\t node pricerangecomplex child value: "+ childConnectionElement.getTextContent());
														//getting details of opening hours
														
														}//if connectionNode
													}//for i4
												}//if childSubElement.getNodeName()=="pricerangecomplex"
												
												//for the case of opening hours
												if (childSubElement.getNodeName()=="openingtimedate") {
													NodeList connectionsChildNodes = childSubElement.getChildNodes();
													for (int i4 = 0; i4 < connectionsChildNodes.getLength(); i4++) {
														Node connectionNode = connectionsChildNodes.item(i4);
														if (connectionNode.getNodeType() == Node.ELEMENT_NODE) {
														Element childConnectionElement = (Element) connectionNode;
														System.out.println("\t\t node openingtimedate child name: "+ childConnectionElement.getNodeName());
														System.out.println("\t\t node openingtimedate child value: "+ childConnectionElement.getTextContent());
														//getting details of opening hours
														
														}//if connectionNode
													}//for i4
												}//if connection
												
												//for the case of connections
												if (childSubElement.getNodeName()=="connection") {
													NodeList connectionsChildNodes = childSubElement.getChildNodes();
													for (int i4 = 0; i4 < connectionsChildNodes.getLength(); i4++) {
														Node connectionNode = connectionsChildNodes.item(i4);
														if (connectionNode.getNodeType() == Node.ELEMENT_NODE) {
														Element childConnectionElement = (Element) connectionNode;
														System.out.println("\t\t node a connection child name: "+ childConnectionElement.getNodeName());
														System.out.println("\t\t node a connection child value: "+ childConnectionElement.getTextContent());
														}//if SubAddressNode
													}//for i4
												}//if connection
												
												//for the case of address
												if (childSubElement.getNodeName()=="address") {
													NodeList addresChildNodes = childSubElement.getChildNodes();
													for (int i4 = 0; i4 < addresChildNodes.getLength(); i4++) {
														Node SubAddressNode = addresChildNodes.item(i4);
														if (SubAddressNode.getNodeType() == Node.ELEMENT_NODE) {
														Element childSubAddressElement = (Element) SubAddressNode;
														System.out.println("\t\t node address child name: "+ childSubAddressElement.getNodeName());
														System.out.println("\t\t node address child value: "+ childSubAddressElement.getTextContent());
														//for the case of coordinates
														if (childSubAddressElement.getNodeName()=="coordinates") {
															NodeList coordinatesChildNodes = childSubAddressElement.getChildNodes();
															for (int i5 = 0; i5 < coordinatesChildNodes.getLength(); i5++) {
																Node coordinateNode = coordinatesChildNodes.item(i5);
																if (coordinateNode.getNodeType() == Node.ELEMENT_NODE) {
																	Element coordinateElement = (Element) coordinateNode;
																	System.out.println("\t\t\t node address coordinates name: "+ coordinateElement.getNodeName());																
																	//for the case of a coordinate
																	if (coordinateElement.getNodeName()=="coordinate") {
																		NodeList coordinateChildAttributes = coordinateElement.getChildNodes();
																		for (int i6 = 0; i6 < coordinateChildAttributes.getLength(); i6++) {
																			Node coordinateNodeAtt = coordinateChildAttributes.item(i6);
																			if (coordinateNodeAtt.getNodeType() == Node.ELEMENT_NODE) {
																			Element coordinateElementAtt = (Element) coordinateNodeAtt;
																			System.out.println("\t\t\t\t node coordinate attribute name: "+ coordinateElementAtt.getNodeName());
																			System.out.println("\t\t\t\t node coordinate attribute value: "+ coordinateElementAtt.getTextContent());
																			}//if coordinateNodeAtt
																		}//for coordinateChildAttributes
																	}// coordinateElement.getNodeName()=="coordinate" 
																}//coordinateNode.getNodeType() == Node.ELEMENT_NODE
															}//for coordinatesChildNodes.getLength()
														}//if coordinates
														
														}//if SubAddressNode
													}//for i4
												}//if childSubElement address
				
											}//if SubNode
										}//for i3
									
									
									//System.out.println("node value: "+childElement.getTextContent());
									//depending of the node we proceed to do a given operation
							    }//end if for child
						 }//for child
						 
						 
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif poiList
			}//end for poiList
		}//end if for poi
		
		
		System.out.println("Do you want to update rdf file? ");
		aResponse = input.nextLine();    //ans wer for first general loop
		if (aResponse.equalsIgnoreCase("Yes")) {
			//printing result
			//defining basic variables
			FileOutputStream poi_out_file = null;
	        File poi_file;
	        //reading data
			try {
				poi_file = new File("C:\\Users\\luis.ramos\\Desktop\\ontologies\\ontologies\\poi_parsed.rdf");
				poi_out_file = new FileOutputStream(poi_file);
				// if file doesnt exists, then create it
	            if (!poi_file.exists()) {
	            	poi_file.createNewFile();
	            }
	            //adding content
	         // get the content in bytes
	            byte[] contentInBytes = PoiOntModel.toString().getBytes();
	            poi_out_file.write(contentInBytes);
	            poi_out_file.flush();
	            poi_out_file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            try {
	                if (poi_out_file != null) {
	                	poi_out_file.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }//finish file writing

		}//endif file writing

						
	    
		
		//saving and closing comunication with origin databases
		PoiDataset.commit();
		
		
		PoiDataset.close();
		
        System.out.println("parsed printed Done");

		
		//getting all addresses
		
		
		
		
	}


	

}
