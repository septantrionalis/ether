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

package org.tdod.ether.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.cosmos.CommandTrigger;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.taimpl.commands.AbstractMovementCommand;
import org.tdod.ether.taimpl.commands.DoDown;
import org.tdod.ether.taimpl.commands.DoEast;
import org.tdod.ether.taimpl.commands.DoNorth;
import org.tdod.ether.taimpl.commands.DoNorthEast;
import org.tdod.ether.taimpl.commands.DoNorthWest;
import org.tdod.ether.taimpl.commands.DoSouth;
import org.tdod.ether.taimpl.commands.DoSouthEast;
import org.tdod.ether.taimpl.commands.DoSouthWest;
import org.tdod.ether.taimpl.commands.DoUp;
import org.tdod.ether.taimpl.commands.DoWest;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.taimpl.player.state.PlayerStateContext;
import org.tdod.ether.taimpl.player.state.PlayingState;

/**
 * Reusable game utilities go here.
 * @author rkinney
 */
public final class GameUtil {

   /**
    * The dividend used when converting milliseconds to seconds.
    */
   public static final int TIME_DIVISION = 1000;

   private static Log _log = LogFactory.getLog(GameUtil.class);

   private static GameUtil   _instance;
   private static final Date _serverUptime = new Date(System.currentTimeMillis());

   private static final int ATTACK_REST_WAIT_TIME
      = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.ATTACK_REST_WAIT_TIME)).intValue();

   public static final String ENTER_HELP_KEYWORD = "ENTRTA";
   public static final String EXIT_HELP_KEYWORD = "EXITTA";

   /**
    * This is the list of supported directions that the player can throw or look.
    */
   private static final ArrayList<ExitDirectionEnum> EXIT_DIRECTIONS = new ArrayList<ExitDirectionEnum>();

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private GameUtil() {
      initialize();
   }

   /**
    * Initializes this class.
    */
   private void initialize() {
   }

   public static ArrayList<ExitDirectionEnum> getValidExitDirectionsForLook()    {
      synchronized (EXIT_DIRECTIONS) {
         if (EXIT_DIRECTIONS.isEmpty()) {
            EXIT_DIRECTIONS.add(ExitDirectionEnum.NORTH);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.NORTHEAST);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.EAST);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.SOUTHEAST);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.SOUTH);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.SOUTHWEST);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.WEST);
            EXIT_DIRECTIONS.add(ExitDirectionEnum.NORTHWEST);
         }
      }
      
      return EXIT_DIRECTIONS;
   }

   
   /**
    * Gets the single instance of this class.
    * @return the single instance of this class.
    */
   public static GameUtil getInstance() {
      synchronized (GameUtil.class) {
         if (_instance == null) {
            _instance = new GameUtil();
         }
      }
      return _instance;
   }

   /**
    * Gets the opposite direction of the specified exit.
    * @param exitDirection the exit in question.
    * @return the opposite direction of the specified exit.
    */
   public static ExitDirectionEnum getOppositeExit(ExitDirectionEnum exitDirection) {
      switch (exitDirection) {
      case NORTH: return ExitDirectionEnum.SOUTH;
      case EAST: return ExitDirectionEnum.WEST;
      case SOUTH: return ExitDirectionEnum.NORTH;
      case WEST: return ExitDirectionEnum.EAST;
      case NORTHEAST: return ExitDirectionEnum.SOUTHWEST;
      case NORTHWEST: return ExitDirectionEnum.SOUTHEAST;
      case SOUTHEAST: return ExitDirectionEnum.NORTHWEST;
      case SOUTHWEST: return ExitDirectionEnum.NORTHEAST;
      case UP: return ExitDirectionEnum.DOWN;
      case DOWN: return ExitDirectionEnum.UP;
      default: return ExitDirectionEnum.UNKNOWN;
      }
   }

   /**
    * Gets the appropriate movement command based on the exit direction.
    * @param direction the direction in question.
    * @return the related movement command.
    */
   public static AbstractMovementCommand getDirectionCommand(ExitDirectionEnum direction) {
      if (direction == null) {
         return null;
      }
      AbstractMovementCommand command = null;
      switch (direction) {
      case NORTH :
         command = new DoNorth();
         break;
      case SOUTH :
         command = new DoSouth();
         break;
      case EAST :
         command = new DoEast();
         break;
      case WEST :
         command = new DoWest();
         break;
      case NORTHEAST :
         command = new DoNorthEast();
         break;
      case NORTHWEST :
         command = new DoNorthWest();
         break;
      case SOUTHEAST :
         command = new DoSouthEast();
         break;
      case SOUTHWEST :
         command = new DoSouthWest();
         break;
      case UP :
         command = new DoUp();
         break;
      case DOWN :
         command = new DoDown();
         break;
      default:
         break;
      }
      return command;
   }

   /**
    * Determines if the leader is in a group.
    * @param leader the leader.
    * @return true of the leader is in a group.
    */
   public static boolean isLeaderInGroup(Entity leader) {
      for (Entity follower : leader.getGroupList()) {
         if (follower.getEntityType().equals(EntityType.PLAYER)) {
            return true;
         }
      }

      return false;
   }

   /**
    * Gets all players in the group.  Every other entity will be filtered out.
    * @param player the player.
    * @return a list of players in the group.
    */
   public static ArrayList<Player> getPlayersInGroup(Player player) {
      ArrayList<Player> list = new ArrayList<Player>();
      for (Entity entity : player.getGroupLeader().getGroupList()) {
         if (entity.getEntityType().equals(EntityType.PLAYER)
               && entity.getRoom().equals(player.getRoom())) {
            list.add((Player) entity);
         }
      }
      return list;
   }

   /**
    * Takes a string and formats it so that words are not cut off at the 80 column mark.
    * If a word cuts into the 80th column, then it is wrapped to the next line instead.
    * @param str The string to format.
    * @return A formatted string.
    */
   public static String formatStringTo80Columns(String str) {
      ArrayList<String> list = new ArrayList<String>();
      String[] split = str.split(" ");
      StringBuffer buffer = new StringBuffer();
      for (int count = 0; count < split.length; count++) {
         String word = split[count];
         if ((buffer.length() + word.length() + 1) > 80) {
            list.add(buffer.toString());
            buffer = new StringBuffer(word);
         } else {
            if (buffer.length() == 0) {
               buffer.append(word);
            } else {
               buffer.append(" " + word);
            }
         }
      }
      list.add(buffer.toString());

      StringBuffer finalBuffer = new StringBuffer();
      for (String str1 : list) {
         finalBuffer.append(str1 + "\n");
      }

      return finalBuffer.toString();
   }

   /**
    * The player enters the world for the first time.
    * @param stateContext the PlayerStateContext.
    */
   public static void enterWorld(PlayerStateContext stateContext) {
      stateContext.getPlayerConnection().getShell().setHideInput(false);
      stateContext.getPlayerConnection().getPlayer().save();

      Player player = stateContext.getPlayerConnection().getPlayer();
      player.print("\n" + WorldManager.getHelp(ENTER_HELP_KEYWORD));

      player.getRoom().addPlayer(player);

      String message = MessageFormat.format(TaMessageManager.XARENT.getMessage(), player.getName());
      player.setState(PlayerStateEnum.PLAYING);
      player.getRoom().print(player, message, false);

      PlayingState state = new PlayingState();
      stateContext.setState(state);

      TriggerResult triggerResult = player.teleportToRoom(player.getRoom().getRoomNumber());

      if (triggerResult.equals(TriggerResult.DEATH)) {
         WorldManager.getGameMechanics().handlePlayerDeath(player, TaMessageManager.YOUDED1.getMessage());
      }
   }

   /**
    * Determines if the room is a town.
    * @param room the room in question.
    * @return true if the room is a town.
    */
   public static boolean isTown(Room room) {
      if (room.getTerrain().equals(Terrain.TOWN)) {
         return true;
      }

      return false;
   }

   /**
    * Checks if the room is an arena.
    * @param room the room in quesiton.
    * @return true if the room is an arena.
    */
   public static boolean isArena(Room room) {
      if (RoomFlags.ARENA.isSet(room.getRoomFlags())) {
         return true;
      }

      return false;
   }

   /**
    * Determines the target, whether its a player, mob, or npc, based on parameters passed in by the player.
    * @param player the player who issued the command.
    * @param room the room the player is in.
    * @param param the parameter the player type.
    * @return the target, or null if one was not found.
    */
   public static Entity getTarget(Player player, Room room, String param) {
      // The joys of TA... attack any players first.
      Player targetPlayer = getPlayer(room, param);
      if (targetPlayer != null) {
         return targetPlayer;
      }

      // No player found... try searching for a mob.
      Mob targetMob = getMob(room, param);
      if (targetMob != null) {
         return targetMob;
      }

      // NPC's
      if (room.getNpcs() != null) {
         for (Mob mob : room.getNpcs()) {
            if (MyStringUtil.contains(mob.getName(), param)) {
               return mob;
            }
         }
      }

      return null;
   }

   /**
    * Gets a mob within the room based on the parameter the player typed.
    * @param room the room the player typed the command in.
    * @param param the parameter the player typed.
    * @return the mob associated with the parameter, or null if one was not found.
    */
   public static Mob getMob(Room room, String param) {
      for (Mob mob : room.getMobs()) {
         if (MyStringUtil.contains(mob.getName(), param)) {
            return mob;
         }
      }

      return null;
   }

   /**
    * Gets a mob within the room based on the parameter the player typed.
    * @param room the room the player type the command in.
    * @param param the parameter the player typed.
    * @return the player associated with the parameter, or null if one was not found.
    */
   public static Player getPlayer(Room room, String param) {
      for (Player tmpPlayer : room.getPlayers()) {
         if (MyStringUtil.contains(tmpPlayer.getName(), param)) {
            return tmpPlayer;
         }
      }

      return null;
   }

   /**
    * Gets a player within the entire world based on the parameter the player typed.
    * @param param the parameter the player typed.
    * @return the player associated with the parameter, or null if one was not found.
    */
   public static Player getPlayer(String param) {
      for (PlayerConnection targetConnection : WorldManager.getPlayers()) {
         if (MyStringUtil.contains(targetConnection.getPlayer().getName(), param)) {
            return targetConnection.getPlayer();
         }
      }

      return null;
   }

   /**
    * Used to encrypt a String.
    * @param x The String to compute the hash with.
    * @return a hash stored as an array of bytes.
    */
   public static byte[] computeHash(String x) {
      if (x == null) {
         return null;
      }
      try {
         java.security.MessageDigest d = null;
         d = java.security.MessageDigest.getInstance("SHA-1");
         d.reset();
         d.update(x.getBytes());
         return  d.digest();
      } catch (Exception e) {
         _log.fatal(e);
      }

      return null;
   }

   /**
    * Converts a hash to a String.
    * @param b the hash.
    * @return a String.
    */
   public static String byteArrayToHexString(byte[] b) {
      if (b == null) {
         return null;
      }
      StringBuffer sb = new StringBuffer(b.length * 2);
      for (int i = 0; i < b.length; i++) {
        int v = b[i] & 0xff;
        if (v < 16) {
          sb.append('0');
        }
        sb.append(Integer.toHexString(v));
      }
      return sb.toString().toUpperCase();
   }

   /**
    * Awards the player experience based on the entity's vitality.
    * @param player the player gaining the experience.
    * @param target the target entity.
    * @param vitalityBefore the vitality of the entity before the attack.
    */
   public static void giveExperience(Player player, Entity target, int vitalityBefore) {
      // Only award experience on damage caused while the mob was alive.
      int totalDamage = vitalityBefore - target.getVitality().getCurVitality();
      if (totalDamage < 0) {
         totalDamage = 0;
      }
      int expGain = (int) (totalDamage * target.getExpPerPointOfDamage(player.getLevel()));
      if (expGain <= 0) {
         expGain = 1;
      }
      player.addExperience(expGain);

   }

   /**
    * Randomizes the wait time when the player uses all of his/her attacks.
    * @return a random wait time.
    */
   public static int randomizeRestWaitTime() {
      return ATTACK_REST_WAIT_TIME - (Dice.roll(0, 2) - 1);  // 11, 12, 13
   }

   /**
    * Checks the targets vitality and handles its death.
    * @param attacker the attacker.
    * @param target the target.
    */
   public static synchronized void checkAndHandleKill(Entity attacker, Entity target) {
      if (target.getVitality().getCurVitality() <= 0) {
         attacker.setRestTicker(GameUtil.randomizeRestWaitTime());
         attacker.getAttacks().reset();
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            Player targetPlayer = (Player) target;
            WorldManager.getGameMechanics().handlePlayerDeath(targetPlayer, TaMessageManager.YOUDED4.getMessage());
         } else {
            WorldManager.getGameMechanics().handleMobDeath(attacker, (Mob) target);
         }
      }
   }

   /**
    * Checks if the entity has been drained in any way.
    * @param entity the entity in question.
    * @return true if the entity is drained.
    */
   public static boolean isEntityDrained(Entity entity) {
      if (entity.getStats().isDrained() || entity.getVitality().getDrained() > 0) {
         return true;
      }

      return false;
   }

   /**
    * Checks if the entity is resting.
    * @param entity the entity in question.
    * @return true if the entity is resting.
    */
   public static boolean isResting(Entity entity) {
      if (entity.getEntityType().equals(EntityType.PLAYER)) {
         if (entity.isResting() || entity.isMentallyExhausted()) {
            return true;
         }

         return false;
      }

      return false;
   }

   /**
    * Determines if an item is the runestaff.
    * @param item the item in question.
    * @return true if the item is the runestaff.
    */
   public static boolean isRunestaff(Item item) {
      if (!item.getItemType().equals(ItemType.EQUIPMENT)) {
         return false;
      }
      Equipment eq = (Equipment) item;
      if (eq.getEquipmentType().equals(EquipmentType.MAJOR_MAGIC_ITEM) && eq.getV12() == 3) {
         return true;
      }

      return false;
   }

   /**
    * Determines if an item is the rod of power.
    * @param item the item in question.
    * @return true if the item is the rod of power.
    */
   public static boolean isRodOfPower(Item item) {
      if (!item.getItemType().equals(ItemType.EQUIPMENT)) {
         return false;
      }
      Equipment eq = (Equipment) item;
      if (eq.getEquipmentSubType().equals(EquipmentSubType.ROD_OF_POWER)) {
         return true;
      }

      return false;
   }

   /**
    * Checks if the item is a manastone.
    * @param item the item in question.
    * @return true if the item is a manastone.
    */
   public static boolean isManastone(Item item) {
      if (!item.getItemType().equals(ItemType.EQUIPMENT)) {
         return false;
      }
      Equipment eq = (Equipment) item;
      if (eq.getEquipmentSubType().equals(EquipmentSubType.MAJOR_MANA_BOOST)) {
         return true;
      }

      return false;      
   }

   public static boolean isHeartstone(Item item) {
      if (!item.getItemType().equals(ItemType.EQUIPMENT)) {
         return false;
      }
      Equipment eq = (Equipment) item;
      if (eq.getEquipmentSubType().equals(EquipmentSubType.RECALL)) {
         return true;
      }

      return false;      
   }

   /**
    * Saves all players in the game.
    */
   public static void savePlayers() {
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         playerConn.getPlayer().save();
      }

   }

   /**
    * Resets the world.
    */
   public static void resetWorld() {
      WorldManager.initializeLairs();

      _log.info("Reseting barriers and triggers.");
      Collection<Room> rooms = WorldManager.getArea().getRoomMap().values();
      Iterator<Room> itr = rooms.iterator();
      while (itr.hasNext()) {
         Room room = itr.next();

         room.setAltDescription(room.getDefaultDescription());

         for (Exit exit : room.getExits()) {
            if (exit.getDoor() != null) {
               exit.getDoor().setIsUnlocked(false);
            }
         }

         for (Trigger trigger : room.getTriggers()) {
            trigger.setTriggered(false);
         }

         if (room.getActionPlayerList() != null) {
            room.getActionPlayerList().clear();
         }
         room.setIgnorePermanentDarkness(false);
      }

   }

   /**
    * Gets the complete list of items that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of items that are sold in a specific shop.
    */
   public static ArrayList<Item> getItemsForShop(Room room, ArrayList<Item> resultList, ArrayList<Item> masterList) {
      for (Item item:masterList) {
         if (item.getTown().getIndex() != 0 && 
             item.getTown().getIndex() <= (room.getTerrain().getIndex() - 10)) {
            resultList.add(item);
         }
      }
      Collections.sort(resultList);
      return resultList;

   }

   /**
    * Gets the command trigger for the room based on input.
    * @param player the player
    * @param input the input
    * @return a CommandTrigger, or null if one is not found.
    */
   public static CommandTrigger getCommandTrigger(Player player, String input) {
      CommandTrigger commandTrigger = null;

      ArrayList<Trigger> triggers = player.getRoom().getTriggers();
      if (player.getRoom().getTriggers() == null) {
         return commandTrigger;         
      }

      for (Trigger t:triggers) {
         CommandTrigger ct = WorldManager.getCommandTrigger(t.getV2());
         if (ct != null && ct.getCommand().toLowerCase().equals(input)) {
            commandTrigger = ct;
            break;
         }
      }

      return commandTrigger;
   }

   public static void disconnectPlayer(Player player) {
      player.save();
      player.print(WorldManager.getHelp(GameUtil.EXIT_HELP_KEYWORD));
      player.setDisconnected(true);

      int roomNumber = player.getRoom().getRoomNumber();
      Room room = WorldManager.getRoom(roomNumber);
      // room.removePlayer(stateContext.getPlayerConnection()) ;

      String message = MessageFormat.format(TaMessageManager.LEVGAM.getMessage(), player.getName());
      room.print(player, message, false);

      PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnecting, null);      
   }

   public static Date getServerUptime() {
      return _serverUptime;
   }
}
