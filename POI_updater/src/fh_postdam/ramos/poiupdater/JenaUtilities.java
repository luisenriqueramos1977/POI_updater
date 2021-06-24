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
		
			//reading gleif database
		
		aDataset = TDB2Factory.connectDataset(aRoot);
	    
		aDataset.begin(ReadWrite.READ) ;
		
		//onto model of gleif1

		aModel = aDataset.getDefaultModel() ;
		
		aOntModel = ModelFactory.createOntologyModel(aSpec, aModel);
				
		return aOntModel;
		
	}

}
