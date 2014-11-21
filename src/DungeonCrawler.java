/*
 * DungeonCrawler.java
 * Main file for the DungeonCrawler dungeon solving program.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */

import java.util.Scanner;

import org.semanticweb.owlapi.model.OWLOntology;


public class DungeonCrawler {

   private static final String ontologyFileName = "./CSC481-Ontology-Bustamante_Farnum.owl";
   
   public static void main(String[] args) {
      OntologyManager ontologyManager;
      OWLOntology ontology;
      Character player;
      
      
      ontologyManager = new OntologyManager(ontologyFileName);
      ontology = ontologyManager.ontology;
      if(ontology == null)
         return;
      ontologyManager.testOntology();
      
      
      player = createCharacterFromPrompt();
      
   }
   
   /* createCharacterFromPrompt
    * Function to be called at start to initialize player by 
    * getting player prompted name, stats, weapon, etc.
    *
    * @return Character, the newly created character object
    *
    * TODO: 1) fill in with actual prompts to the player
    *       2) add weapon, type, element to Character class 
    */
   private static Character createCharacterFromPrompt() {
      String name = "";
      int hp, defense, speed;
      Scanner scanner = new Scanner(System.in);
      
      // placeholder for Nathan or someone else to to
      System.out.println("add prompt text and stuff:");
      hp = defense = speed = scanner.nextInt();
      
      // TODO: new Character function should have params for weapon, type
      //       and element once those get implemented.
      return new Character(name, hp, defense, speed);
   }
   
}
