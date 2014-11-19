
import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/*
 * OntologyManager.java
 * Class file to manage DungeonCrawler ontology.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */


public class OntologyManager {
   
   public static OWLOntology loadOntology(String fileName) throws OWLOntologyCreationException {
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      
      File file = new File(fileName);
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
      System.out.println("Loaded ontology: " + ontology);
      
      return ontology;
   }
   
   
}
