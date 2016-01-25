/****************************************************************************
 * Ether Code base, version 1.0                                             *
 *==========================================================================*
 * Copyright (C) 2011 by Ron Kinney                                         *
 * All rights reserved.                                                     *
 *                                                                          *
 * This program is free software; you can redistribute it and/or modify     *
 * it under the terms of the GNU General Public License as published        *
 * by the Free Software Foundation; either version 2 of the License, or     *
 * (at your option) any later version.                                      *
 *                                                                          *
 * This program is distributed in the hope that it will be useful,          *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of           *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            *
 * GNU General Public License for more details.                             *
 *                                                                          *
 * Redistribution and use in source and binary forms, with or without       *
 * modification, are permitted provided that the following conditions are   *
 * met:                                                                     *
 *                                                                          *
 * * Redistributions of source code must retain this copyright notice,      *
 *   this list of conditions and the following disclaimer.                  *
 * * Redistributions in binary form must reproduce this copyright notice    *
 *   this list of conditions and the following disclaimer in the            *
 *   documentation and/or other materials provided with the distribution.   *
 *                                                                          *
 *==========================================================================*
 * Ron Kinney (ronkinney@gmail.com)                                         *
 * Ether Homepage:   http://tdod.org/ether                                  *
 ****************************************************************************/

package org.tdod.ether.ta.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.ta.cosmos.Area;
import org.tdod.ether.ta.cosmos.Barrier;
import org.tdod.ether.ta.cosmos.CommandTrigger;
import org.tdod.ether.ta.cosmos.Emote;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.Teleporter;
import org.tdod.ether.ta.cosmos.Trap;
import org.tdod.ether.ta.cosmos.Treasure;
import org.tdod.ether.ta.cosmos.World;
import org.tdod.ether.ta.engine.GameEngine;
import org.tdod.ether.ta.engine.GameMechanics;
import org.tdod.ether.ta.engine.RandomItemEngine;
import org.tdod.ether.ta.engine.RandomMobEngine;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.MobWeapon;
import org.tdod.ether.ta.mobs.enums.MobWeaponType;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.taimpl.engine.DefaultRandomItemEngine;
import org.tdod.ether.taimpl.mobs.enums.MobSpellType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;

/**
 * This is a static manager class that gives access to all world and game mechanics, functionality,
 * and variables.  Almost all of the methods in this class are convenience methods to other objects.
 * @author Ron Kinney
 */
public final class WorldManager {

   private static Log _log = LogFactory.getLog(WorldManager.class);

   private static GameEngine                  _gameEngine = null;
   private static GameMechanics               _gameMechanics = null;
   private static World                       _world = null;
   private static ArrayList<PlayerConnection> _playerList = new ArrayList<PlayerConnection>();
   private static HashMap<Item, String>       _itemList = new HashMap<Item, String>();
   private static RandomMobEngine             _randomMobEngine = DefaultAppFactory.createRandomMobEngine();
   private static RandomItemEngine            _randomItemEngine = new DefaultRandomItemEngine();

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private WorldManager() {
   }

   /**
    * Sets the game engine.
    * @param gameEngine the game engine.
    */
   public static void setGameEngine(GameEngine gameEngine) {
      _gameEngine = gameEngine;
   }

   /**
    * Gets the game engine.
    * @return the game engine.
    */
   public static GameEngine getGameEngine() {
      return _gameEngine;
   }

   /**
    * Sets the world.
    * @param world the world.
    */
   public static void setWorld(World world) {
      if (_world != null) {
         _log.error("Attempted to initialize the World twice!");
      }
      _world = world;
   }

   /**
    * Gets the area.
    * @return the area.
    */
   public static Area getArea() {
      if (_world == null || _world.getArea() == null) {
         _log.error("The area is null!");

         return null;
      }

      return _world.getArea();
   }

   /**
    * Gets the room description at the specified index.
    * @param index the room description index.
    * @return the room description at the specified index.
    */
   public static String getRoomDescription(int index) {
      if (_world == null || _world.getRoomDescDatabase() == null) {
         _log.error("The area is null!");

         return null;
      }

      return _world.getRoomDescDatabase().getEntry(index);
   }

   /**
    * Gets the help at the specified key.
    * @param key the help key.
    * @return the help at the specified key.
    */
   public static String getHelp(String key) {
      if (_world == null || _world.getHelpAssistant() == null) {
         _log.error("The world or help database is null!");

         return null;
      }

      return _world.getHelpAssistant().getEntry(key);
   }

   /**
    * Gets the emote based on the key.  The key is an exact match defined in the
    * data file.
    * @param emote the emote key.
    * @return the emote based on the key.
    */
   public static Emote getEmote(String emote) {
      if (_world == null || _world.getEmoteDatabase() == null) {
         _log.error("The world or emote database null!");

         return null;
      }

      return _world.getEmoteDatabase().getEmote(emote);
   }

   /**
    * Gets the room based on the room number.
    * @param roomNumber the room number.
    * @return the room based on the room number.
    */
   public static Room getRoom(int roomNumber) {
      if (_world == null || _world.getArea() == null) {
         _log.error("The world or area is null!");

         return null;
      }

      return _world.getArea().getEntry(roomNumber);
   }

   /**
    * Gets a weapon based on the vnum.
    * @param weapon the weapon vnum.
    * @return the weapon at the specified vnum.
    */
   public static Weapon getWeapon(int weapon) {
      if (_world == null || _world.getWeaponDatabase() == null) {
         _log.error("The world or weapon database is null!");

         return null;
      }

      return _world.getWeaponDatabase().getWeapon(weapon);
   }

   /**
    * Gets a weapon based on a string.  This string is a partial comparator and
    * the first matching value will be returned, or null if one isn't found.
    * @param weapon the partial or full name of the weapon.
    * @return a weapon based on a string.
    */
   public static Weapon getWeapon(String weapon) {
      if (_world == null || _world.getWeaponDatabase() == null) {
         _log.error("The world or weapon database is null!");

         return null;
      }

      return _world.getWeaponDatabase().getWeapon(weapon);
   }

   /**
    * Gets the complete list of weapons that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of weapons that are sold in a specific shop.
    */
   public static ArrayList<Item> getWeaponsForShop(Room room) {
      if (_world == null || _world.getWeaponDatabase() == null) {
         _log.error("The world or weapon database is null!");

         return null;
      }

      return _world.getWeaponDatabase().getWeaponsForShop(room);
   }

   /**
    * Gets the default weapon.
    * @return the default weapon.  This should never return null.
    */
   public static Weapon getDefaultWeapon() {
      if (_world == null || _world.getWeaponDatabase() == null) {
         _log.error("The world or weapon database is null!");

         return null;
      }

      return _world.getWeaponDatabase().getDefaultWeapon();
   }

   /**
    * Gets an armor at the specified vnum.
    * @param armor the armor vnum.
    * @return an armor at the specified vnum.
    */
   public static Armor getArmor(int armor) {
      if (_world == null || _world.getArmorDatabase() == null) {
         _log.error("The world or armor database is null!");

         return null;
      }

      return _world.getArmorDatabase().getArmor(armor);
   }

   /**
    * Gets an armor based on a string.  This string is a partial comparator and
    * the first matching value will be returned, or null if one isn't found.
    * @param armor the partial or full name of the armor.
    * @return a weapon based on a string.
    */
   public static Armor getArmor(String armor) {
      if (_world == null || _world.getArmorDatabase() == null) {
         _log.error("The world or armor database is null!");

         return null;
      }

      return _world.getArmorDatabase().getArmor(armor);
   }

   /**
    * Gets the complete list of armor that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of armor that are sold in a specific shop.
    */
   public static ArrayList<Item> getArmorForShop(Room room) {
      if (_world == null || _world.getArmorDatabase() == null) {
         _log.error("The world or armor database is null!");

         return null;
      }

      return _world.getArmorDatabase().getArmorForShop(room);
   }

   /**
    * Gets the default armor.
    * @return the default armor.  This should never return null.
    */
   public static Armor getDefaultArmor() {
      if (_world == null || _world.getArmorDatabase() == null) {
         _log.error("The world or armor database is null!");

         return null;
      }

      return _world.getArmorDatabase().getDefaultArmor();
   }

   /**
    * Gets the entire list of mobs.
    * @return the entire list of mobs.
    */
   public static ArrayList<Mob> getMobList() {
      if (_world == null || _world.getMobDatabase() == null) {
         _log.error("The world or mob database is null!");

         return null;
      }

      return _world.getMobDatabase().getMobList();
   }

   /**
    * Gets a list of mobs based on the terrain.
    * @param terrain the terrain for the mobs in question.
    * @return a list of mobs based on the terrain.
    */
   public static ArrayList<Mob> getMobByTerrain(Terrain terrain) {
      if (_world == null || _world.getMobDatabase() == null) {
         _log.error("The world or mob database is null!");

         return null;
      }

      return _world.getMobDatabase().getMobByTerrain(terrain);
   }

   /**
    * Gets a mob at the specified vnum.
    * @param mobVnum the vnum.
    * @return a mob at the specified vnum.
    */
   public static Mob getMob(int mobVnum) {
      if (_world == null || _world.getMobDatabase() == null) {
         _log.error("The world or mob database is null!");

         return null;
      }

      return _world.getMobDatabase().getMob(mobVnum);
   }

   /**
    * Gets a spell based on the key.  The key is exact and is defined in the
    * data file.
    * @param spell the spell key.
    * @return the spell based on the key.
    */
   public static Spell getSpell(String spell) {
      if (_world == null || _world.getSpellDatabase() == null) {
         _log.error("The world or spell database is null!");

         return null;
      }

      return _world.getSpellDatabase().getSpell(spell);
   }

   /**
    * Gets the entire list of spells based on class.
    * @param playerClass the player class.
    * @return the entire list of spells based on class.
    */
   public static ArrayList<Spell> getSpells(PlayerClass playerClass) {
      if (_world == null || _world.getSpellDatabase() == null) {
         _log.error("The world or spell database is null!");

         return null;
      }

      return _world.getSpellDatabase().getSpells(playerClass);
   }

   /**
    * Gets the attack spell for a mob based on the specified criteria.
    * @param mobSpellType the mob spell type.
    * @param index the index of the spell.
    * @return the attack spell for a mob.
    */
   public static Spell getAttackSpellFor(MobSpellType mobSpellType, int index) {
      if (_world == null || _world.getSpellDatabase() == null) {
         _log.error("The world or spell database is null!");

         return null;
      }

      return _world.getSpellDatabase().getAttackSpellFor(mobSpellType, index);
   }

   /**
    * Gets an equipment based on the vnum.
    * @param equipment the equipment vnum.
    * @return the equipment based on the vnum.
    */
   public static Equipment getEquipment(int equipment) {
      if (_world == null || _world.getEquipmentDatabase() == null) {
         _log.error("The world or equipment database is null!");

         return null;
      }

      return _world.getEquipmentDatabase().getEquipment(equipment);
   }

   /**
    * Gets an equipment based on a string.  The string can be a partial match and the first
    * matching instance will be return.  A null is returned if one is not found.
    * @param equipment the partial or full equipment name.
    * @return an equipment based on a string or null if one was not found.
    */
   public static Equipment getEquipment(String equipment) {
      if (_world == null || _world.getEquipmentDatabase() == null) {
         _log.error("The world or equipment database is null!");

         return null;
      }

      return _world.getEquipmentDatabase().getEquipment(equipment);
   }

   /**
    * Gets the complete list of equipment that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of equipment that are sold in a specific shop.
    */
   public static ArrayList<Item> getEquipmentForShop(Room room) {
      if (_world == null || _world.getEquipmentDatabase() == null) {
         _log.error("The world or equipment database is null!");

         return null;
      }

      return _world.getEquipmentDatabase().getEquipmentForShop(room);
   }

   /**
    * Gets the list of mob weapons based on weapon type.
    * @param mobWeaponType the mob weapon type.
    * @return the list of mob weapons based on weapon type.
    */
   public static ArrayList<MobWeapon> getMobWeapons(MobWeaponType mobWeaponType) {
      if (_world == null || _world.getMobWeaponDatabase() == null) {
         _log.error("The world or weapon database is null!");

         return null;
      }

      return _world.getMobWeaponDatabase().getWeapons(mobWeaponType);
   }

   /**
    * Gets an npc based on the specified vnum.
    * @param index the npc vnum.
    * @return an npc.
    */
   public static Mob getNpc(int index) {
      if (_world == null || _world.getNpcDatabase() == null) {
         _log.error("The world or npc database is null!");

         return null;
      }

      return _world.getNpcDatabase().getNpc(index);
   }

   /**
    * Sets the list of online players.
    * @param players the list containined the players online.
    */
   public static void setPlayers(ArrayList<PlayerConnection> players) {
      _playerList = players;
   }

   /**
    * Gets the list of online players.
    * @return the list of online players.
    */
   public static ArrayList<PlayerConnection> getPlayers() {
      return _playerList;
   }

   /**
    * Gets all of the items that have been cloned in the game.
    * @return all of the items that have been cloned in the game.
    */
   public static HashMap<Item, String> getItemsInExistance() {
      return _itemList;
   }

   /**
    * Gets all of the mobs that have been cloned in the game.  Currently, this method iterates through
    * every room in an attempt to find all of the mobs that have been created.  I originally wanted to
    * have a separate hashmap containing all defined mobs, but this created a maintenance nightmare
    * that could have caused a memory leak if killed mobs were not cleaned up.
    * @return all of the mobs that have been cloned in the game.
    */
   public static ArrayList<Mob> getMobsInExistance() {
      ArrayList<Mob> mobList = new ArrayList<Mob>();

      // Scour every room for mobs.
      Collection<Room> c = _world.getArea().getRoomMap().values();
      Iterator<Room> itr = c.iterator();
      while (itr.hasNext()) {
         ArrayList<Mob> mobs = itr.next().getMobs();
         for (Mob mob : mobs) {
            mobList.add(mob);
         }
      }

      return mobList;
   }

   /**
    * Sets the game mechanics.
    * @param gameMechanics the game mechanics.
    */
   public static void setGameMechanics(GameMechanics gameMechanics) {
      _gameMechanics = gameMechanics;
   }

   /**
    * Gets the game mechanics.
    * @return the game mechanics.
    */
   public static GameMechanics getGameMechanics() {
      if (_gameMechanics == null) {
         _log.error("GameMechanics is null!");
      }

      return _gameMechanics;
   }

   /**
    * Gets an item based on the vnum.
    * @param vnum the item vnum.
    * @return an item based on the vnum.
    */
   public static Item getItem(int vnum) {
      Item eq = getEquipment(vnum);
      if (eq != null) {
         return eq;
      }

      eq = getArmor(vnum);
      if (eq != null) {
         return eq;
      }

      eq = getWeapon(vnum);
      return eq;
   }

   /**
    * Gets an item based on a full or partial string name.  The first match is returned
    * or null if one is not found.
    * @param item the full or partial name of the item.
    * @return an item based on a full or partial string name.
    */
   public static Item getItem(String item) {
      Item eq = getEquipment(item);
      if (eq != null) {
         return eq;
      }

      eq = getArmor(item);
      if (eq != null) {
         return eq;
      }

      eq = getWeapon(item);
      return eq;
   }

   /**
    * Gets a barrier based on its index.
    * @param number the barrier index.
    * @return a barrier.
    */
   public static Barrier getBarrier(int number) {
      if (_world == null || _world.getBarrierDatabase() == null) {
         _log.error("The world or barrier database is null!");

         return null;
      }

      return _world.getBarrierDatabase().getBarrier(number);
   }

   /**
    * Gets a trap based on its index.
    * @param number the trap index.
    * @return a trap.
    */
   public static Trap getTrap(int number) {
      if (_world == null || _world.getTrapDatabase() == null) {
         _log.error("The world or trap database is null!");

         return null;
      }

      return _world.getTrapDatabase().getTrap(number);
   }

   /**
    * Gets a teleporter based on its index.
    * @param number the teleporter index.
    * @return a teleporter.
    */
   public static Teleporter getTeleporter(int number) {
      if (_world == null || _world.getTeleporterDatabase() == null) {
         _log.error("The world or teleporter database is null!");

         return null;
      }

      return _world.getTeleporterDatabase().getTeleporter(number);
   }

   /**
    * Gets the command trigger based on the index.
    * @param index the command trigger index.
    * @return a command trigger.
    */
   public static CommandTrigger getCommandTrigger(int index) {
      if (_world == null || _world.getCommandTriggerDatabase() == null) {
         _log.error("The world or command trigger database is null!");

         return null;
      }

      return _world.getCommandTriggerDatabase().getCommandTrigger(index);
   }

   /**
    * Gets a treasure based on its index.
    * @param number the treasure index.
    * @return a treasure.
    */
   public static Treasure getTreasure(int number) {
      if (_world == null || _world.getTreasureDatabase() == null) {
         _log.error("The world or treasure database is null!");

         return null;
      }

      return _world.getTreasureDatabase().getTreasure(number);
   }

   /**
    * Initializes the lairs.  This method will not only repop mobs, but the items
    * they hold too.
    */
   public static void initializeLairs() {
      _log.info("Initializing lairs");
      Collection<Room> c = _world.getArea().getRoomMap().values();
      Iterator<Room> itr = c.iterator();
      while (itr.hasNext()) {
         Room room = itr.next();
         room.initializeLair();
      }
   }

   /**
    * Populates the world with random mobs.  This method generates mobs based
    * on an internal algorithm.
    */
   public static void populateRandomMobs() {
      _randomMobEngine.populateRandomMobs();
   }

   /**
    * Populates the world with random items.  This method generates items based
    * on an internal algorithm.
    */
   public static void populateRandomItems() {
      _randomItemEngine.populateRandomItems();
   }
}
