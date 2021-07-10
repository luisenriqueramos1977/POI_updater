package fh_postdam.ramos.poiupdater;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;  
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.function.library.print;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResIterator;

/**
*@author Luis Ramos luis.ramos@fh-potsdam.de
*@version Creation time: 24.06.2021 08:53:52
*Class Description:
*this class contains specific methods which execute tasks that
* can be considered repetitives along any programming related to 
* jena API. 
*
*/
public class JenaUtilities {
	/**
	 * @param aDataset
	 * @param aRoot
	 * @param aModel
	 * @param aOntModel
	 * @param aSpec
	 * @return aOntModel of aDataset
	 * 
	 */
	public static OntModel readOntModelfromDataset(Dataset aDataset, String aRoot, Model aModel , OntModelSpec aSpec) {
		/*
		 * reading every available dataset
		 */
		//reading ontology dataset
		OntModel aOntModel;
		aDataset = TDB2Factory.connectDataset(aRoot);
		aDataset.begin(ReadWrite.READ) ;
		//onto model of gleif1
		aModel = aDataset.getDefaultModel() ;
		aOntModel = ModelFactory.createOntologyModel(aSpec, aModel);
		return aOntModel;
		
	}//ends readOntModelfromDataset
	
	/**
	 * @param ns
	 * @param IndClass
	 * @param ontModel
	 * @param ord
	 * @return an Individual
	 */
	public static Individual getIndividualbyID(String ns, String IndClass, OntModel ontModel, String ID) {
		OntClass  anIndClass;
		Individual PoiInstance = null;
		anIndClass = ontModel.getOntClass( ns + IndClass );
		String currentID = "";
		//System.out.println("anIndClass  "+anIndClass);//flag
		//System.out.println("String id to be searched "+ID);//flag
		//adapting ord
		String ID2 = "\""+ID+"\"";
		//System.out.println("String ID modified "+ID2);//flag
		 ExtendedIterator  model_items = anIndClass.listInstances();//here I get all instances in model
			//begins loop 
			 while (model_items.hasNext())
		      {
				 PoiInstance = (Individual) model_items.next();
				 	try {
				 		currentID = "";
				 		currentID  = PoiInstance.getLabel("id").toString();
			 			System.out.println("------current ID ---"+currentID);//flag
				 			if ((ID.equalsIgnoreCase(currentID)) | (ID2.equalsIgnoreCase(currentID))){
				 			System.out.println("------String found in jena utilities ---"+ID);//flag
				 			System.out.println("------Found poi instance ---"+PoiInstance);//flag
					 		return PoiInstance;
					 	}	
					 	else {
					 		PoiInstance = null;
					 	}
				 	}
				 	catch(java.lang.NullPointerException e) {
				 		currentID= null;
				 		PoiInstance = null;
				 		//System.out.println("error: "+e);
				 	}
				 	if (!(currentID==null)) {
				 		//we add quotation marks and a second option 
				 }
		      }
			 //GLEIF1_items.close();
		return PoiInstance;
	}//ends getIndividualbyID
	
	/**
	 * @param ns
	 * @param IndClass
	 * @param ontModel
	 * @param ord
	 * @return an Individual
	 */
	public static Individual getIndividualbyName(String ns, String IndClass, OntModel ontModel, String Name) {
		OntClass  anIndClass;
		Individual PoiInstance = null;
		anIndClass = ontModel.getOntClass( ns + IndClass );
		String currentName = "";
		System.out.println("anIndClass  "+anIndClass);//flag
		System.out.println("String Name to be searched "+Name);//flag
		//adapting ord
		String Name2 = "\""+Name+"\"";
		System.out.println("String ID modified "+Name2);//flag
		 ExtendedIterator  model_items = anIndClass.listInstances();//here I get all instances in model
			//begins loop 
			 while (model_items.hasNext())
		      {
				 PoiInstance = (Individual) model_items.next();
				 	try {
				 		currentName = "";
				 		currentName  = PoiInstance.getLabel("name").toString();
				 			if ((Name.equalsIgnoreCase(currentName)) | (Name2.equalsIgnoreCase(currentName))){
				 			System.out.println("------String found in jena utilities ---"+Name);//flag
				 			System.out.println("------Found poi instance ---"+PoiInstance);//flag
					 		return PoiInstance;
					 	}	
					 	else {
					 		PoiInstance = null;
					 	}
				 	}
				 	catch(java.lang.NullPointerException e) {
				 		currentName= null;
				 	}
				 	if (!(currentName==null)) {
				 		//we add quotation marks and a second option 
				 }
		      }
		return PoiInstance;
	}//ends getIndividualbyID

	/**
	 * @param aDataset
	 * @param aRoot
	 */
	public static void closeReadOntModelfromDataset(Dataset aDataset, String aRoot) {
		/*
		 * reading every available dataset
		 */
		aDataset = TDB2Factory.connectDataset(aRoot);	    
		aDataset.close();
	}//end of closeReadOntModelfromDataset

	public static void closeWriteOntModelfromDataset(Dataset aDataset, String aRoot) {
		/*
		 * reading every available dataset
		 */
		aDataset = TDB2Factory.connectDataset(aRoot);		
		aDataset.commit();
		aDataset.close();
	}

	/**
	 * @param aDataset
	 * @param aRoot
	 * @param aModel
	 * @param aOntModel
	 * @param aSpec
	 * @return aOntModel of aDataset
	 * 
	 */
	public static OntModel writeOntModeltoDataset(Dataset aDataset, String aRoot, Model aModel, OntModel aOntModel, OntModelSpec aSpec) {
		/*
		 * reading every available dataset
		 */
		aDataset = TDB2Factory.connectDataset(aRoot);		
		aDataset.begin(ReadWrite.WRITE) ;
		aModel = aDataset.getDefaultModel();
		aOntModel = ModelFactory.createOntologyModel(aSpec, aModel);
		return aOntModel;
		
	}

	/**
	 * @param aDate
	 * @return aDate_xsd
	 * @throws ParseException
	 */
	
	public static XSDDateTime jenaDate_xsd(String aDate) throws ParseException {
		XSDDateTime aDate_xsd = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//-uncomment for test
		//displaying current date and time
		Calendar cal = Calendar.getInstance();
		Date current_day = sdf.parse(aDate);
		cal.setTime(current_day);
		aDate_xsd = new XSDDateTime(cal);
		return aDate_xsd;
	}
	
	
	/**
	 * This function returns an item from the model, which has a value
	 * @param aModel
	 * @param aProperty
	 * @param aValue
	 * @return an Individual
	 */
	
	
	public static Individual getIndividualbyPropertyvalue(OntModel aModel, DatatypeProperty aProperty, String aValue) {
		Individual anIndividual = null;
    	Resource aResource = null;
		ResIterator dmbIte = aModel.listResourcesWithProperty(aProperty, aValue);
        // list the individuals
           while (dmbIte.hasNext()) {
        	   aResource = dmbIte.next();
           }
           
           try {
               anIndividual =aModel.getIndividual(aResource.toString());
           }
           catch (Exception e) {
        	   
        	   anIndividual = null;
           }
		
		return anIndividual;
	}//end of getIndividual

	/**
	 * @param directory where model is located
	 * @return the dataset located in the directory
	 */
	
	public static Dataset get_dataset(String directory) {
		
		Dataset dataset = TDB2Factory.connectDataset(directory) ;

		return dataset;
		
	}//end of 


	public static XSDDateTime timestamptoJenaDate_xsd(String aDate) {
		XSDDateTime aDate_xsd = null;
		Long long_tstamp = Long.parseLong(aDate);
		java.util.Date date_tstamp=new java.util.Date(long_tstamp*1000);
		//displaying current date and time
		Calendar cal = Calendar.getInstance();
		cal.setTime(date_tstamp);
		aDate_xsd = new XSDDateTime(cal);
		return aDate_xsd;
	}


	/**
	 * @param parent
	 * @param child
	 * @return
	 */
	
	public static Node getFirstChildNodeByName (Node parent, String child) {
		  NodeList childNodes = parent.getChildNodes();
		  Node aNodeChild = null;
		  for (int i = 0; i < childNodes.getLength(); i++) {
		    if (childNodes.item(i).getNodeName().equals(child)) {
		      aNodeChild = childNodes.item(i);
		    }
		  }
		return aNodeChild;
	}//
	
	//get all individuals linked to this object by this property, if any
	
	/**
	 * @param aModel
	 * @param anIndividual
	 * @param aProperty
	 * @return list of individuals with a rdf property
	 */
	public static List<Individual> getLinkedIndividualsbyProperty(OntModel aModel, Individual anIndividual, Property aProperty) {
	    List<Individual> myindividuals = new ArrayList<>();
        	NodeIterator dmbIte = anIndividual.listPropertyValues(aProperty);
            // list the individuals
               while (dmbIte.hasNext()) {
                   RDFNode node = dmbIte.next();
                   //getting the individual
                   anIndividual =aModel.getIndividual(node.toString());
                   myindividuals.add(anIndividual);
               }
		return myindividuals;
	}//end of getIndividual
	
	
	/**
	 * @param myindividuals
	 * @param aProperty
	 * @param aValue
	 * @return this_individual if there is an individual with such a value
	 */
	public static boolean checkIndividualinListbyLiteralValue(List<Individual> myindividuals, Property aProperty, String aValue) {
		
		//iterate over individuals to get id
    	for (int ind = 0; ind < myindividuals.size(); ind++) {
            Individual this_individual = myindividuals.get(ind);
            Literal thisValue = (Literal) this_individual.getPropertyValue(aProperty);
            if (aValue.equalsIgnoreCase(thisValue.getValue().toString())) {
                //System.out.println("there is an individual that match with this value "+thisValue.getValue().toString());
        		return true;
			}
        }
		return false;
	}
	
	
	public static boolean checkIndividualProperties(String ns, String IndClass, OntModel ontModel, Hashtable<String, String> properties_List) {
		OntClass  anIndClass;
		boolean Ind_exist;
		Ind_exist=false;
		Individual thisInstance = null;
		anIndClass = ontModel.getOntClass( ns + IndClass );
		//System.out.println("String ID modified "+ID2);//flag
		ExtendedIterator  model_items = anIndClass.listInstances();//here I get all instances in model
		 //checking if there is any individual
		if (model_items.hasNext()) {
			//begins loop 
			 while (model_items.hasNext())
		      {
				 thisInstance = (Individual) model_items.next();
				 List<Boolean>property_exist = null;
				 	try {
				 		//get property
				 	// getting keySet() into Set
				        Set<String> setOfProperties = properties_List.keySet();
				     // for-each loop
				        for(String key : setOfProperties) {
				        	Property aProperty = ontModel.getProperty(ns+key);
					 		String property_value = (thisInstance.getPropertyValue(aProperty)).toString();
					 		//comparing values
					 		if (property_value.equalsIgnoreCase(properties_List.get(key))) {
					 			property_exist.add(true);
							}//endif comparing properties
				        }//end for properties
				        //check size of property list dictionary
				        try {
				        	if (property_exist.size()>0) {
					        	//if any is false, the object do not exist
						        if (checkBooleanList(property_exist)) {
									return true;
								}//checking if exist
							}//if not continue
						} catch (Exception e) {
							System.out.println("exception in property_exist.size");
						}
				 	}
				 	catch(java.lang.NullPointerException e) {
						System.out.println("setOfProperties");
				 		e.printStackTrace();
						return true;
				 	}
				 	
		      }//endif while
			 
			 //if all are true

		} else {
			return false;
		}//endif model_items
		return Ind_exist;
	}//ends getIndividualbyID
	
	
	//checking all is true in an array
	public static boolean checkBooleanList(List<Boolean>myArray) {
		for (int i = 0; i < myArray.size(); i++) {
			  if (!myArray.get(i)) return false;
			}
		return true;
	}//end of checkBooleanList

	
}//JenaUtilities
