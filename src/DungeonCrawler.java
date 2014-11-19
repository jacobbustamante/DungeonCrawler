/*
 * DungeonCrawler.java
 * Main file for the DungeonCrawler dungeon solving program.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;


public class DungeonCrawler {

   private static final String ontologyFileName = "./CSC481-Ontology-Bustamante_Farnum.owl";
   
   public static void main(String[] args) {
      OWLOntology ontology;
      
      try {
         ontology = OntologyManager.loadOntology(ontologyFileName);
      }
      catch(OWLOntologyCreationException e) {
         System.out.println("could not load ontology at " + ontologyFileName);
      }
      
   }
   
}
