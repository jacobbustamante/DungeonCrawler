/*
 * Character.java
 * Character class file for DungeonCrawler.
 *
 * CSC 481 - Fall 2014 - Final Project - Dr. Hassal
 * 
 * Jacob Bustamante, Nathan Farnum
 */

 /* TODO: 1) add weapon, type, and element.
  *          possibly as objects of those classes?
  *       2) Do everything else..
  *       
 */
public class Character {
   private final String name;
   private int hp;
   private int defense;
   private int speed;
   
   public Character(String name, int hp, int defense, int speed) {
      this.name = name;
      this.hp = hp;
      this.defense = defense;
      this.speed = speed;
   }
   
}
