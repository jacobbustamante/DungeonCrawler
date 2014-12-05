/*
 * OntologyManager.java
 * Class file to manage DungeonCrawler ontology.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */

import com.clarkparsia.owlapiv3.OWL;
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

import java.util.Iterator;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;


 /* NOTE: not sure how to structure this yet either.
  *
  * TODO: 1) figure out how to access classes, individuals, properties, etc.
  *
 */
public class OntologyManager {
   
   public OWLOntology ontology;
   public static OWLOntologyManager manager;
   public static OWLDataFactory dataFactory;
   public static OWLOntologyID ontologyId;
   public static IRI ontologyIri;
   public static IRI characterIRI;
   public static String characterName;
   public static IRI curRoomIRI;
   
   public static int hp, attack, defense, speed;
   public static double luck;
   
   public  int uhp, uattack, udefense, uspeed;
   public  double uluck;
   
   
   
   public OntologyManager(String fileName) {
      try {
         ontology = loadOntology(fileName);
      }
      catch (OWLOntologyCreationException e) {
         System.out.println("could not load ontology at " + fileName);
      }
   }
   
   public static OWLOntology loadOntology(String fileName) throws OWLOntologyCreationException {
      manager = OWLManager.createOWLOntologyManager();
      dataFactory = manager.getOWLDataFactory();
      
      File file = new File(fileName);
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
      System.out.println("Loaded ontology: " + ontology + "\n\n");
      
      ontologyId = ontology.getOntologyID();
      ontologyIri = ontologyId.getOntologyIRI();
      
      return ontology;
   }
   
   public void initializeDungeon(String characterName) {
      this.characterName = characterName;
      
      String room_iri_string = ontologyIri + "#Room-Fire-8e";
      
      Set<OWLEntity> entities = ontology.getSignature();
      for (OWLEntity e : entities) {
         if (e.isOWLNamedIndividual()) {
            OWLNamedIndividual ni = e.asOWLNamedIndividual();
               if (e.toStringID().contains("Room-Fire")) {
                    if (ni.getDataPropertyValues(ontology).toString().contains("isStartRoom")) {
                       //System.out.println("Lonk is in " + e.toStringID());
                       room_iri_string = e.toStringID();
                    }
               }
         }
      }
      
      IRI character_iri = IRI.create(ontologyIri + "#" + characterName);
      this.characterIRI = character_iri;
      IRI room_iri = IRI.create(room_iri_string);
      IRI contains_iri = IRI.create(ontologyIri + "#contains");
      
      OWLNamedIndividual room = dataFactory.getOWLNamedIndividual(room_iri);
      OWLNamedIndividual character = dataFactory.getOWLNamedIndividual(character_iri);
      OWLObjectProperty contains = dataFactory.getOWLObjectProperty(contains_iri);
      
      OWLObjectPropertyAssertionAxiom assertion = dataFactory
                .getOWLObjectPropertyAssertionAxiom(contains, room, character);
      AddAxiom addAxiomChange = new AddAxiom(ontology, assertion);
      manager.applyChange(addAxiomChange);
      
      OWLDataProperty visited = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#visited"));
      OWLDatatype booleanDatatype = dataFactory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
      
      OWLLiteral literal = dataFactory.getOWLLiteral("false", booleanDatatype);
      OWLAxiom ax = dataFactory.getOWLDataPropertyAssertionAxiom(visited, room, literal);
      manager.removeAxiom(ontology, ax);
      
      
      
      literal = dataFactory.getOWLLiteral("true", booleanDatatype);
      
      ax = dataFactory.getOWLDataPropertyAssertionAxiom(visited, room, literal);
      manager.addAxiom(ontology, ax);
      
      this.curRoomIRI = room_iri;
   }
   
   
   public void enterRoom(String next_room_string) {
      OWLNamedIndividual next_room = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#" + next_room_string));
      OWLNamedIndividual prev_room = dataFactory.getOWLNamedIndividual(curRoomIRI);
      OWLNamedIndividual character = dataFactory.getOWLNamedIndividual(characterIRI);
      OWLObjectProperty contains_prop = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#contains"));
      
      OWLObjectPropertyAssertionAxiom assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(contains_prop, prev_room, character);
      RemoveAxiom removeAxiomChange = new RemoveAxiom(ontology, assertion);
      manager.applyChange(removeAxiomChange);
      
      assertion = manager.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(contains_prop, next_room, character);
      AddAxiom addAxiomChange = new AddAxiom(ontology, assertion);
      manager.applyChange(addAxiomChange);
      
      OWLDataProperty visited = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#visited"));
      OWLDatatype booleanDatatype = dataFactory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
      
      //System.out.println("before: " + next_room.getDataPropertyValues(ontology).toString());
      
      
      OWLLiteral literal = dataFactory.getOWLLiteral("false", booleanDatatype);
      OWLAxiom ax = dataFactory.getOWLDataPropertyAssertionAxiom(visited, next_room, literal);
      manager.removeAxiom(ontology, ax);
      
      literal = dataFactory.getOWLLiteral("true", booleanDatatype);
      ax = dataFactory.getOWLDataPropertyAssertionAxiom(visited, next_room, literal);
      manager.addAxiom(ontology, ax);
      
      //System.out.println("after: " + next_room.getDataPropertyValues(ontology).toString());
      
   }
   
   public void removeMonster(String monster_string) {
      OWLNamedIndividual cur_room = dataFactory.getOWLNamedIndividual(curRoomIRI);
      OWLNamedIndividual monster = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#" + monster_string));
      OWLObjectProperty contains_prop = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#contains"));
      
      //System.out.println("before: " + cur_room.getObjectPropertyValues(ontology).toString());
      
      OWLObjectPropertyAssertionAxiom assertion = dataFactory
                .getOWLObjectPropertyAssertionAxiom(contains_prop, cur_room, monster);
      RemoveAxiom removeAxiomChange = new RemoveAxiom(ontology, assertion);
      //manager.removeAxiom(ontology, removeAxiomChange);
      //manager
      manager.applyChange(removeAxiomChange);
      //System.out.println("after: " + cur_room.getObjectPropertyValues(ontology).toString());
   }
   
   public String[] getNextMove() {
      //try { Thread.sleep(1000);} catch(Exception e) {}
      PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
      reasoner.getKB().realize();
      //try { Thread.sleep(1000);} catch(Exception e) {}
      
      OWLNamedIndividual nextMove = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#nextMove"));
      OWLObjectProperty next_room = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#nextMoveRoom"));
      OWLObjectProperty next_object = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#nextMoveObject"));
      OWLObjectProperty next_monster = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#nextMoveMonster"));
      
      OWLDataProperty hp_prop = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#HP"));
      OWLDataProperty defense_prop = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#Defense"));
      OWLDataProperty speed_prop = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#Speed"));
      OWLDataProperty attack_prop = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#Attack"));
      OWLDataProperty luck_prop = dataFactory.getOWLDataProperty(IRI.create(ontologyIri + "#Luck"));
      OWLDatatype intDatatype = dataFactory.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
      OWLDatatype decimalDatatype = dataFactory.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
      
      
      
      String[] ar = new String[2];
      ar[0] = ar[1] = "";
      
      IRI next_room_iri = IRI.create(ontologyIri + "#Room-Fire-8e");
      //System.out.println(reasoner.getObjectPropertyValues(nextMove, next_room));
      for (Node<OWLNamedIndividual> n : reasoner.getObjectPropertyValues(nextMove, next_room)) {
         IRI cur_iri = n.getRepresentativeElement().getIRI();
         //System.out.println(n.getRepresentativeElement().getIRI().toString());
         if (!cur_iri.toString().equals(curRoomIRI.toString())) {
            ar[0] = "room";
            ar[1] = cur_iri.toString().split("#")[1];
            next_room_iri = cur_iri;
         }
      }
      //System.out.println(reasoner.getObjectPropertyValues(nextMove, next_monster));
      for (Node<OWLNamedIndividual> n : reasoner.getObjectPropertyValues(nextMove, next_monster)) {
         IRI cur_iri = n.getRepresentativeElement().getIRI();
         //System.out.println(n.getRepresentativeElement().getIRI().toString());
         ar[0] = "monster";
         ar[1] = cur_iri.toString().split("#")[1];
         
         hp = Integer.parseInt(reasoner.getDataPropertyValues( n.getRepresentativeElement(), hp_prop ).iterator().next().getLiteral());
         defense = Integer.parseInt(reasoner.getDataPropertyValues( n.getRepresentativeElement(), defense_prop ).iterator().next().getLiteral());
         attack = Integer.parseInt(reasoner.getDataPropertyValues( n.getRepresentativeElement(), attack_prop ).iterator().next().getLiteral());
         speed = Integer.parseInt(reasoner.getDataPropertyValues( n.getRepresentativeElement(), speed_prop ).iterator().next().getLiteral());
         luck = Double.parseDouble(reasoner.getDataPropertyValues( n.getRepresentativeElement(), luck_prop ).iterator().next().getLiteral());
         
         
         
         OWLNamedIndividual character = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#" + characterName));
      
         int a,b,c,d;
         double l;
         uhp = Integer.parseInt(reasoner.getDataPropertyValues( character, hp_prop ).iterator().next().getLiteral());
         udefense = Integer.parseInt(reasoner.getDataPropertyValues( character, defense_prop ).iterator().next().getLiteral());
         uattack = Integer.parseInt(reasoner.getDataPropertyValues( character, attack_prop ).iterator().next().getLiteral());
         uspeed = Integer.parseInt(reasoner.getDataPropertyValues( character, speed_prop ).iterator().next().getLiteral());
         uluck = Double.parseDouble(reasoner.getDataPropertyValues( character, luck_prop ).iterator().next().getLiteral());
         
         
      }
      
      
      //System.out.println("should do " + ar[0]);
      switch(ar[0]) {
         case "room": 
            curRoomIRI = next_room_iri;
            break;
         case "monster":
            break;
         case "object":
            break;
         default:
            ar[0] = ar[1] = "";;
            break;
      }

      return ar;
   }
   
   public void testOntology() {
      Set <OWLClass> classes = ontology.getClassesInSignature();
      Object[] classArray = classes.toArray();

      Set<OWLClassAxiom> ontologyAxioms = ontology.getAxioms((OWLClass)classArray[1]);

      System.out.println(classes);
      System.out.println(ontologyId);
      System.out.println(ontologyIri);
      System.out.println(ontologyAxioms);
      
      
      PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );
      reasoner.getKB().realize();
		reasoner.getKB().printClassTree();
      System.out.println("\n\n");
      
      Set<OWLEntity> entities = ontology.getSignature();
      for (OWLEntity e : entities) {
         System.out.println("\nentity");
         System.out.println(e.toStringID());
         if (e.isOWLNamedIndividual()) {
            OWLNamedIndividual ni = e.asOWLNamedIndividual();
               if (e.toStringID().contains("Room-Fire")) {
                    if (ni.getDataPropertyValues(ontology).toString().contains("isStartRoom")) {
                       System.out.println("Lonk is in " + e.toStringID());
                    }
               }
                 
         }
      }
      
      
      OWLNamedIndividual named = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#Lonk"));
      OWLNamedIndividual room = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#Room-Fire-8e"));
      System.out.println(named.toString());
      
      OWLClass a = dataFactory.getOWLClass(IRI.create(ontologyIri + "#Dungeon"));
      
      OWLObjectProperty contains_prop = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#contains"));
      
      NodeSet<OWLNamedIndividual> props = reasoner.getObjectPropertyValues(room, contains_prop);
      System.out.println(props.toString());
      
      
      OWLObjectProperty nm_monster = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#nextMoveMonster"));
      OWLObjectProperty nm_room = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#nextMoveRoom"));
      OWLObjectProperty nm_object = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#nextMoveObject"));
      OWLNamedIndividual nextMove = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#nextMove"));
      NodeSet<OWLNamedIndividual> move_props = reasoner.getObjectPropertyValues(nextMove, nm_monster);
      System.out.println(move_props.toString());
      move_props = reasoner.getObjectPropertyValues(nextMove, nm_room);
      System.out.println(move_props.toString());
      move_props = reasoner.getObjectPropertyValues(nextMove, nm_object);
      System.out.println(move_props.toString());
      
      OWLNamedIndividual room_1 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#Room-Fire-8e"));
      OWLNamedIndividual room_2 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#Room-Fire-5c"));
      OWLNamedIndividual ad_1 = dataFactory.getOWLNamedIndividual(IRI.create(ontologyIri + "#Lonk"));
      OWLObjectProperty obj_prop_1 = dataFactory.getOWLObjectProperty(IRI.create(ontologyIri + "#contains"));
      
      OWLObjectPropertyAssertionAxiom assertion = manager.getOWLDataFactory()
                .getOWLObjectPropertyAssertionAxiom(obj_prop_1, room_1, ad_1);
      AddAxiom addAxiomChange = new AddAxiom(ontology, assertion);
      manager.applyChange(addAxiomChange);
      
      assertion = manager.getOWLDataFactory()
                .getOWLObjectPropertyAssertionAxiom(obj_prop_1, room_2, ad_1);
      RemoveAxiom removeAxiomChange = new RemoveAxiom(ontology, assertion);
      manager.applyChange(removeAxiomChange);
      
      reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );
      reasoner.getKB().realize();
      
      
      move_props = reasoner.getObjectPropertyValues(nextMove, nm_monster);
      System.out.println(move_props.toString());
      move_props = reasoner.getObjectPropertyValues(nextMove, nm_room);
      System.out.println(move_props.toString());
      move_props = reasoner.getObjectPropertyValues(nextMove, nm_object);
      System.out.println(move_props.toString());
      
      
   }
   
}
