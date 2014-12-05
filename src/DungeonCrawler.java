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
   
   public static class Player {
      int attack, defense, hp, speed;
      double luck;
      
      public Player(int a, int b, int c, int d, double l) {
         attack = a;
         defense = b;
         hp = c;
         speed = d;
         luck = l;
      }
   }
   
   public static void main(String[] args) {
      OntologyManager ontologyManager;
      OWLOntology ontology;
      Character player;
      String[] next_move_ar;
      String next_room_string;
      String monster_string;
      String player_name;
      String name;
      Player adv = new Player(100, 20, 20, 20, 1.0);
      boolean player_set = false;
      
      ontologyManager = new OntologyManager(ontologyFileName);
      ontology = ontologyManager.ontology;
      if(ontology == null)
         return;
      
      
      Scanner scanner = new Scanner(System.in);
      System.out.println("Hey Adventurer!");
      System.out.println("Look out! Many dangerous dungeons wreak havoc here in this land.");
      System.out.println("But I think you'll be just fine - I can tell that your gonna do great things, just be the look in your eyes");
      System.out.println("So then! Adventurer! What's' your name?");
      System.out.println("Link, Groose, Isaac, Lyra, Talon, Lonk, or ToonLink?");
      name = scanner.next();
      while(!name.equals("Link") && !name.equals("Groose")&& !name.equals("Isaac") && !name.equals("Lyra") && !name.equals("Talon") && !name.equals("Lonk") && !name.equals("ToonLink")) {
         System.out.println("Hey! Listen! That wasn't one of the options!! Don't try to trick me, I know you're one of them! So who are ya?");
         System.out.println("Link, Groose, Isaac, Lyra, Talon, Lonk, or ToonLink?");
         name = scanner.next();
      }
      System.out.println("Onward! Toward great adventure " + name + "!");
      
      
      //player = createCharacterFromPrompt();
      //player_name = "Lonk";
      player_name = name;
      
      ontologyManager.initializeDungeon(player_name);
      
      
      
      System.out.println(player_name + ". You are in " + ontologyManager.curRoomIRI.toString().split("#")[1] + " of the dungeon.");
      
      for (int i = 15; i > 0; i--) {
         next_move_ar = ontologyManager.getNextMove();

         switch(next_move_ar[0]) {
            case "room": 
               next_room_string = next_move_ar[1];
               ontologyManager.enterRoom(next_room_string);
               System.out.println(player_name + " moves to " + next_room_string + ".");
               break;
            case "monster":
               monster_string = next_move_ar[1];
               System.out.println(player_name + " is in battle with " + monster_string + ".");
               
               if (!player_set) {
                  adv = new Player(ontologyManager.uattack, ontologyManager.udefense, ontologyManager.uhp, ontologyManager.uspeed, ontologyManager.uluck);
                  player_set = true;
               }
               Player monster = new Player(ontologyManager.attack, ontologyManager.defense, ontologyManager.hp, ontologyManager.speed, ontologyManager.luck);
               
               double crit = 1.0;
                 while(monster.hp > 0 && adv.hp > 0) {
                     if(adv.speed > monster.speed) {
                         if(Math.random() %100 < adv.luck*10)
                             crit = 1.5;
                         else
                             crit = 1.0;
                         monster.hp -= (int) (adv.attack * crit - monster.defense);
                         if(Math.random() %100 < monster.luck*10)
                             crit = 1.5;
                         else
                             crit = 1.0;
                         adv.hp -= (int) (monster.attack * crit - adv.defense);
                     } else {
                         if(Math.random() %100 < monster.luck*110)
                             crit = 1.5;
                         else
                             crit = 1.0;
                         adv.hp -= (int) (monster.attack * crit - adv.defense);
                         if(Math.random() %100 < adv.luck*10)
                             crit = 1.5;
                         else
                             crit = 1.0;
                         monster.hp -= (int) (adv.attack * crit - monster.defense);
                     
                     }
                     
                     if (adv.hp > 0)
                        System.out.println(player_name + " has HP: " + adv.hp);
                     else {
                        System.out.println("Hey Listen! Game over...");
                        return;
                     }
                     if (monster.hp > 0)
                        System.out.println(monster_string + " has HP: " + monster.hp);
                     else
                        System.out.println(monster_string + " has HP: 0");
                 }
               
               
               System.out.println(player_name + " has defeated the " + monster_string + "!");
               
               ontologyManager.removeMonster(monster_string);
               break;
            case "object":
               break;
            default:
               i = 0;
               break;
         }
      }
      
      System.out.println("Congratulations " + player_name + "! You beat the dungeon!");
      
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
      int hp = 100, defense = 10, speed = 10, weaponAttack = 10;
      int promptChoice = 0;
      Scanner scanner = new Scanner(System.in);
      
      // placeholder for Nathan or someone else to to
      //System.out.println("");
      System.out.println("Hey Adventurer!");
      System.out.println("Look out! Many dangerous dungeons wreak havoc here in this land.");
      System.out.println("But I think you'll be just fine - I can tell that your gonna do great things, just be the look in your eyes");
      System.out.println("So then! Adventurer! What's' your name?");
      name = scanner.next();
      System.out.println("Hey " + name + "! I'll help you on your journey! But first things first, what do like best?");
      System.out.println("(1) Attack, (2) Defense, (3) Speed, (4) Health");
      promptChoice = scanner.nextInt();

///////////Needs prompt checking to make sure that they choose a correct number

      switch (promptChoice) {
        case 1: 
          weaponAttack += 3;
          defense -= 1;
          speed -= 1;
          hp -= 10;
          break;

        case 2:
          defense += 3;
          weaponAttack -= 1;
          speed -= 1;
          hp -= 10;
          break;

        case 3:
          speed += 3;
          weaponAttack -= 1;
          defense -= 1;
          hp -= 10;
          break;

        case 4:
          hp += 30;
          weaponAttack -= 1;
          defense -= 1;
          speed -= 1;
          break;
      }
      System.out.println("Attack: " + weaponAttack);
      System.out.println("Defense: " + defense);
      System.out.println("Speed: " + speed);
      System.out.println("Health: " + hp);
      System.out.println("And what part of nature do you feel closest to?");
      System.out.println("(1) Fire, (2) Water, (3) Earth, (4) Thunder");
      promptChoice = scanner.nextInt();
      switch (promptChoice) {
        case 1: 
          //this.element = new Fire();
          System.out.println("Fire: Burn baby burn!");
          break;

        case 2:
          //this.element = new Water();
          System.out.println("Water: Go with the flow!");
          break;

        case 3:
          //this.element = new Earth();
          System.out.println("Earth: Time to rock!");
          break;

        case 4:
          //this.element = new Thunder();
          System.out.println("Thunder: Boom, they won't know what hit 'em!");
          break;
      }

      System.out.println("Alright! So next... Whoa! what kind of weapon you got there?");
      System.out.println("(1) Sword, (2) Magic Rod, (3) Bow, (4) Ball and Chain");
      promptChoice = scanner.nextInt();
      switch (promptChoice) {
        case 1: 
          //this.weapon = new Sword(weaponAttack);
         System.out.println("Sword: Cuz it's dangerous to go alone!");
          break;

        case 2:
          //this.weapon = new MagicRod(weaponAttack);
          System.out.println("Magic Rod: None shall pass you!");
          break;

        case 3:
          //this.weapon = new Bow(weaponAttack);
          System.out.println("Bow: They'll arch their backs and bow down to your dexterity!");
          break;

        case 4:
          //this.weapon = new BallAndChain(weaponAttack);
          System.out.println("Ball and Chain: You're gonna wreck 'em!");
          break;
      }

      System.out.println("Aaaannnddd finaly: how do you like to explore?");
      System.out.println("(1) Fight first, everything else comes after.\n(2) Only fight if treasure is involved.\n(3) Treasure always comes first; fighting is a last resort.");
      promptChoice = scanner.nextInt();
      //this.strategy = promptChoice;

      //hp = defense = speed = scanner.nextInt();
      
      // TODO: new Character function should have params for weapon, type
      //       and element once those get implemented.
      return new Character(name, hp, defense, speed);
   }
   
}
