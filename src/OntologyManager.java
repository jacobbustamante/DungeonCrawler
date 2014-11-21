/*
 * OntologyManager.java
 * Class file to manage DungeonCrawler ontology.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

 /* NOTE: not sure how to structure this yet either.
  *
  * TODO: 1) figure out how to access classes, individuals, properties, etc.
  *
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
