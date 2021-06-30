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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	private static DatatypeProperty poi_revision;
	public static DatatypeProperty  poi_name;
	public static DatatypeProperty poi_tstamp;
	public static DatatypeProperty poi_id;
	public static DatatypeProperty poi_language;
	public static DatatypeProperty poi_equis;
	public static DatatypeProperty poi_ye;
	public static DatatypeProperty poi_coordinateType;
	
	public static ObjectProperty poi_parents;
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
		    	
			} catch (Exception e) {
				// TODO: handle exception
				Aquaticsdistrict = PoiOntModel.createClass( POI_NS + "Aquaticsdistrict" );
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
		
		rdf_ns_type = PoiOntModel.getProperty(rdf_ns + "type");
		
		System.out.println(Classification);
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
			System.out.println("number of districts: "+districtList.getLength());
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
			System.out.println("number of locations: "+locationList.getLength());
			for (int i = 0; i < locationList.getLength(); i++) {
				Node node = locationList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;  
					try {
						 String str_type = eElement.getAttributes().getNamedItem("type").getNodeValue();
						 String str_id = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 String str_name = eElement.getAttributes().getNamedItem("name").getNodeValue();
						 String str_language = eElement.getAttributes().getNamedItem("language").getNodeValue();
						 String str_revision = eElement.getAttributes().getNamedItem("revision").getNodeValue();
						 String str_tstamp = eElement.getAttributes().getNamedItem("tstamp").getNodeValue();
						 //getting list of node for every tag
						 
						 System.out.println("location name: "+str_name);
						 
						 NodeList nodedistrict = eElement.getElementsByTagName("district");
						 System.out.println("nodedistrict: "+nodedistrict.getLength());
						 
						 //getting all ids of this districts list
						 for (int i_list = 0; i_list < nodedistrict.getLength(); i_list++) {
								Node this_node = nodedistrict.item(i_list);  
								System.out.println("\nCurrent Element :" + this_node.getNodeName());
								
						        
								if (this_node.getNodeType() == Node.ELEMENT_NODE) {
									System.out.println("node is element");
									Element listElement = (Element) this_node;  
									try {
										//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
								         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
								         System.out.println("str_id_node: "+str_id_node);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}//endif for every node
						 }//endfor of the node list
										 
						 
						 NodeList nodedepartment = eElement.getElementsByTagName("department");
						 System.out.println("nodedepartment: "+nodedepartment.getLength());
						 
						//getting all ids of this districts list
						 for (int i_list = 0; i_list < nodedistrict.getLength(); i_list++) {
								Node this_node = nodedistrict.item(i_list);  
								if (this_node.getNodeType() == Node.ELEMENT_NODE) {
									Element listElement = (Element) this_node;  
									try {
										//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
								         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
								         System.out.println("str_id_nodedepartment: "+str_id_node);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}//endif for every node
						 }//endfor of the node list
						 
						 
						 NodeList noderegion = eElement.getElementsByTagName("region");
						 System.out.println("noderegion: "+noderegion.getLength());
						 
						//getting all ids of this districts list
						 for (int i_list = 0; i_list < nodedistrict.getLength(); i_list++) {
								Node this_node = nodedistrict.item(i_list);  
								if (this_node.getNodeType() == Node.ELEMENT_NODE) {
									Element listElement = (Element) this_node;  
									try {
										//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
								         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
								         System.out.println("str_id_noderegion: "+str_id_node);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}//endif for every node
						 }//endfor of the node list
						 NodeList nodecoordinates = eElement.getElementsByTagName("coordinates");
						 System.out.println("nodecoordinates: "+nodecoordinates.getLength());
						 
						 for (int i_list = 0; i_list < nodedistrict.getLength(); i_list++) {
								Node this_node = nodedistrict.item(i_list);  
								if (this_node.getNodeType() == Node.ELEMENT_NODE) {
									Element listElement = (Element) this_node;  
									try {
										//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
								         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
								         System.out.println("str_id_nodecoordinates: "+str_id_node);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}//endif for every node
						 }//endfor of the node list
						 
						 NodeList nodeexcursionsRegion = eElement.getElementsByTagName("excursionsRegion");
						 System.out.println("nodeexcursionsRegion: "+nodeexcursionsRegion.getLength());
						 
						 for (int i_list = 0; i_list < nodedistrict.getLength(); i_list++) {
								Node this_node = nodedistrict.item(i_list);  
								if (this_node.getNodeType() == Node.ELEMENT_NODE) {
									Element listElement = (Element) this_node;  
									try {
										//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
								         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
								         System.out.println("str_id_nodeexcursionsRegion: "+str_id_node);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}//endif for every node
						 }//endfor of the node list
						 
						 NodeList nodeaquaticsDistrict = eElement.getElementsByTagName("aquaticsDistrict");
						 System.out.println("nodeaquaticsDistrict: "+nodeaquaticsDistrict.getLength());
						 
						 
						 for (int i_list = 0; i_list < nodedistrict.getLength(); i_list++) {
								Node this_node = nodedistrict.item(i_list);  
								if (this_node.getNodeType() == Node.ELEMENT_NODE) {
									Element listElement = (Element) this_node;  
									try {
										//System.out.println("classification id: "+eElement.getAttributes().getNamedItem("id").getNodeValue());
								         String str_id_node= listElement.getAttributes().getNamedItem("id").getNodeValue();
								         System.out.println("str_id_nodeaquaticsDistrict: "+str_id_node);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}//endif for every node
						 }//endfor of the node list
						 
						 Element stateElement = (Element) eElement.getElementsByTagName("state");
						 //get id of state
						 String str_id_state = eElement.getAttributes().getNamedItem("id").getNodeValue();
						 
						 System.out.println("str_id_state: "+str_id_state);
			             
						 
						 if ((str_language != null)&& (str_revision != null) && (str_name != null) && (str_tstamp!= null) && (str_id!= null) ){
								
								//checking if individual exist
						 		Individual PoiInstance = JenaUtilities.getIndividualbyPropertyvalue(PoiOntModel, poi_id, str_id);
						 		if (PoiInstance == null) {
							 		 System.out.println("no location ");
							 		 //reading all attributes
							 		 
							 		 //getting every related object
							 		 
							 		 
							 		 
						 			
								}//if poi instance 
						 }
						 else {
					 			System.out.println("exist object "+str_id+ " type "+str_type);
					 		}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}//endif node
			}//end for districList
		}//endif 
		
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
	            //PoiOntModel.write(System.out,"RDF/XML");
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
				
	    
		
		//closing comunication with origin databases
		PoiDataset.commit();
		
		
		PoiDataset.close();
		
        System.out.println("parsed printed Done");

		
		//getting all addresses
		
		
		
		
	}


	

}
