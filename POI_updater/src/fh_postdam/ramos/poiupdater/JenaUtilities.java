package fh_postdam.ramos.poiupdater;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.util.iterator.ExtendedIterator;

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
	public static OntModel readOntModelfromDataset(Dataset aDataset, String aRoot, Model aModel, OntModel aOntModel, OntModelSpec aSpec) {
		
		/*
		 * reading every available dataset
		 */
		
		//reading ontology dataset
		
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
		System.out.println("anIndClass  "+anIndClass);//flag
		System.out.println("String id to be searched "+ID);//flag
		//adapting ord
		String ID2 = "\""+ID+"\"";
		System.out.println("String ID modified "+ID2);//flag
		 ExtendedIterator  model_items = anIndClass.listInstances();//here I get all instances in model
			//begins loop 
			 while (model_items.hasNext())
		      {
				 PoiInstance = (Individual) model_items.next();
				 	try {
				 		currentID = "";
				 		currentID  = PoiInstance.getLabel("id").toString();
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
			 //GLEIF1_items.close();
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

	

}//JenaUtilities
