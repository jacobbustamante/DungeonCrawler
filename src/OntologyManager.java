/*
 * OntologyManager.java
 * Class file to manage DungeonCrawler ontology.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */

import java.io.File;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

 /* NOTE: not sure how to structure this yet either.
  *
  * TODO: 1) figure out how to access classes, individuals, properties, etc.
  *
 */
public class OntologyManager {
   
   public OWLOntology ontology;
   
   public OntologyManager(String fileName) {
      try {
         ontology = loadOntology(fileName);
      }
      catch (OWLOntologyCreationException e) {
         System.out.println("could not load ontology at " + fileName);
      }
   }
   
   public static OWLOntology loadOntology(String fileName) throws OWLOntologyCreationException {
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      
      File file = new File(fileName);
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
      System.out.println("Loaded ontology: " + ontology);
      
      return ontology;
   }
   
   public void testOntology() {
      Set <OWLClass> classes = ontology.getClassesInSignature();
      Object[] classArray = classes.toArray();

      OWLOntologyID ontologyId = ontology.getOntologyID();
      IRI ontologyIri = ontologyId.getOntologyIRI();
      Set<OWLClassAxiom> ontologyAxioms = ontology.getAxioms((OWLClass)classArray[1]);

      System.out.println(classes);
      System.out.println(ontologyId);
      System.out.println(ontologyIri);
      System.out.println(ontologyAxioms);
      
      PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );
      reasoner.getKB().realize();
		reasoner.getKB().printClassTree();
   }
   
   
}
