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

package org.tdod.ether.taimpl.cosmos;

/**
 * You're in the north plaza.
 * There is nobody here.
 * There is nothing on the floor.
 *
 */
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Lair;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.cosmos.enums.DropItemFailCode;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.taimpl.commands.DoMove;
import org.tdod.ether.taimpl.commands.DoSay;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * The default implementation of a room.
 *
 * @author Ron Kinney
 */
public class DefaultRoom implements Room {

   private static final long serialVersionUID = -8589744901530301266L;

   private static Log _log = LogFactory.getLog(DefaultRoom.class);

   private int                              _defaultDescription;
   private int                              _altDescription;
   private int                              _roomFlags;
   private int                              _serviceLevel;
   private ArrayList<Exit>                  _exits = new ArrayList<Exit>();
   private Terrain                          _terrain = Terrain.INVALID;
   private ArrayList<Integer>               _npcs = new ArrayList<Integer>();
   private ArrayList<Lair>                  _lairs = new ArrayList<Lair>();
   private ArrayList<Trigger>               _triggers = new ArrayList<Trigger>();

   private transient ArrayList<Item>        _itemsOnGround;
   private transient ArrayList<Mob>         _mobs;
   private transient ArrayList<Player>      _players;
   private transient ArrayList<String>      _actionPlayerList;

   private transient ArrayList<Item>        _shopItemCache = null;
   private transient ArrayList<Mob>         _npcCache = null;

   private transient int                    _roomNumber;
   private transient boolean                _ignorePermanentDarkness;

   /**
    * Creates a new DefaultRoom.
    */
   public DefaultRoom() {
   }

   /**
    * Initializes this room.
    */
   public void initialize() {
      _itemsOnGround = new ArrayList<Item>();
      _mobs = new ArrayList<Mob>();
      _players = new ArrayList<Player>();
   }

   /**
    * Gets the room number.
    *
    * @return the room number.
    */
   public int getRoomNumber() {
      return _roomNumber;
   }

   /**
    * Sets the room number.
    *
    * @param roomNumber the room number.
    */
   public void setRoomNumber(int roomNumber) {
      _roomNumber = roomNumber;
   }

   /**
    * Gets the default room description.
    *
    * @return the default room description.
    */
   public int getDefaultDescription() {
      return _defaultDescription;
   }

   /**
    * Sets the default room description.
    *
    * @param defaultDescription the default room description.
    */
   public void setDefaultDescription(int defaultDescription) {
      _defaultDescription = defaultDescription;
   }

   /**
    * Gets the alternative room description.
    *
    * @return the alternative room description.
    */
   public int getAltDescription() {
      return _altDescription;
   }

   /**
    * Sets the alternative room description.
    *
    * @param altDescription the alternative room description.
    */
   public void setAltDescription(int altDescription) {
      _altDescription = altDescription;
   }

   /**
    * Sets the rooms short description.
    *
    * @return the rooms short description.
    */
   public String getShortDescription() {
      if (_defaultDescription != _altDescription) {
         return WorldManager.getRoomDescription(_altDescription);
      } else {
         return WorldManager.getRoomDescription(_defaultDescription);
      }
   }

   /**
    * Gets the long description of this room.
    *
    * @return the long description of this room.
    */
   public String getLongDescription() {
      int offset = 1;
      if (_roomNumber < 0) {
         offset = -1;
      }

      if (_defaultDescription != _altDescription) {
         return WorldManager.getRoomDescription(_altDescription + offset);
      } else {
         return WorldManager.getRoomDescription(_defaultDescription + offset);
      }
   }

   /**
    * Adds an exit.
    *
    * @param exit the exit to add.
    */
   public void addExit(Exit exit) {
      _exits.add(exit);
   }

   /**
    * Removes an exit.
    *
    * @param exit the exit to remove.
    */
   public void removeExit(Exit exit) {
      _exits.remove(exit);
   }

   /**
    * Gets the exit to the corresponding room.  null is returned if one is not found.
    *
    * @param toRoomVnum the destination room number.
    *
    * @return the exit to the corresponding room.  null is returned if one is not found.
    */
   public Exit getExit(int toRoomVnum) {
      for (Exit exit : _exits) {
         if (exit.getToRoom() == toRoomVnum) {
            return exit;
         }
      }
      return null;
   }

   /**
    * Gets the exit in the specified direction.  null is returned if the exit does not exit.
    *
    * @param exitDirectionEnum the exit direction.
    *
    * @return the exit in the specified direction.  null is returned if the exit does not exit.
    */
   public Exit getExit(ExitDirectionEnum exitDirectionEnum) {
      for (Exit exit : _exits) {
         if (exit.getExitDirection().equals(exitDirectionEnum)) {
            return exit;
         }
      }
      return null;
   }

   /**
    * Gets the arraylist of exists.
    *
    * @return the arraylist of exists.
    */
   public ArrayList<Exit> getExits() {
      return _exits;
   }

   /**
    * Returns a random exit.
    *
    * @return a random exit.
    */
   public Exit getRandomExit() {
      int randomNumber = Dice.roll(0, _exits.size() - 1);
      Exit exit = _exits.get(randomNumber);
      return exit;
   }

   /**
    * Sets the room flags.
    *
    * @param roomFlags the room flags.
    */
   public void setRoomFlags(int roomFlags) {
      _roomFlags = roomFlags;
   }

   /**
    * Gets the room flags.
    *
    * @return the room flags.
    */
   public int getRoomFlags() {
      return _roomFlags;
   }

   /**
    * Sets the service level.
    * @param serviceLevel the service level.
    */
   public void setServiceLevel(int serviceLevel) {
      _serviceLevel = serviceLevel;
   }

   /**
    * Gets the service level.
    * @return the service level.
    */
   public int getServiceLevel() {
      return _serviceLevel;
   }

   /**
    * Gets the list of items that the shop sells or buys.
    *
    * @return the list of items that the shop sells or buys.
    */
   public ArrayList<Item> getShopItem() {
      synchronized (this) {
         if (_shopItemCache == null) {
            if (RoomFlags.WEAPON_SHOP.isSet(getRoomFlags())) {
               _shopItemCache = WorldManager.getWeaponsForShop(this);
            } else if (RoomFlags.ARMOR_SHOP.isSet(getRoomFlags())) {
               _shopItemCache = WorldManager.getArmorForShop(this);
            } else if (RoomFlags.EQUIPMENT_SHOP.isSet(getRoomFlags())) {
               _shopItemCache = WorldManager.getEquipmentForShop(this);
            } else if (RoomFlags.MAGIC_SHOP.isSet(getRoomFlags())) {
               _shopItemCache = WorldManager.getEquipmentForShop(this);
            }
         }
      }

      return _shopItemCache;
   }

   /**
    * TODO -- I need to refactor these print statements out.
    */
   /**
    * Prints the message to the room.  The message will be displayed to everyone but the
    * from entity.
    *
    * @param fromEntity the entity that the message is coming from.
    * @param message the message.
    * @param ignoreInvisible if a player is invisible, messages are not displayed.  Set this flag to true if the message
    * should be displayed even if the player is invisible.
    */
   public void println(Entity fromEntity, String message, boolean ignoreInvisible) {
      if (!isRoomMessageValid(fromEntity, message, ignoreInvisible)) {
         return;
      }
      displayMessage(fromEntity, message, true);
   }

   /**
    * Prints the message to the room without a carriage return.
    * @param fromEntity the entity that the message is coming from.
    * @param message the message.
    * @param ignoreInvisible if a player is invisible, messages are not displayed.  Set this flag to true if the message
    * should be displayed even if the player is invisible.
    */
   public void print(Entity fromEntity, String message, boolean ignoreInvisible) {
      if (!isRoomMessageValid(fromEntity, message, ignoreInvisible)) {
         return;
      }
      displayMessage(fromEntity, message, false);
   }

   /**
    * Prints the message to the room.  The message will be displayed to everyone but the originating
    * player and target.
    *
    * @param player the player that the message is coming from.
    * @param target the targeted player.
    * @param message the message.
    * @param ignoreInvisible if a player is invisible, messages are not displayed.  Set this flag to true if the message
    * should be displayed even if the player is invisible.
    */
   public void println(Entity player, Entity target, String message, boolean ignoreInvisible) {
      if (!isRoomMessageValid(player, message, ignoreInvisible)) {
         return;
      }

      for (Player roomPlayer : _players) {
         if (!player.equals(roomPlayer) && !target.equals(roomPlayer)) {
            roomPlayer.println(message);
         }
      }
   }

   /**
    * Prints the message to the room without a carriage return.  The message will be displayed to everyone but the originating
    * player and target.
    *
    * @param player the player that the message is coming from.
    * @param target the targeted player.
    * @param message the message.
    * @param ignoreInvisible if a player is invisible, messages are not displayed.  Set this flag to true if the message
    * should be displayed even if the player is invisible.
    */
   public void print(Entity player, Entity target, String message, boolean ignoreInvisible) {
      if (!isRoomMessageValid(player, message, ignoreInvisible)) {
         return;
      }

      for (Player roomPlayer : _players) {
         if (!player.equals(roomPlayer) && !target.equals(roomPlayer)) {
            roomPlayer.print(message);
         }
      }
   }

   /**
    * Prints a message to everyone but members of a group.
    *
    * @param player the player where the message is originating from.
    * @param messageToRoom the message.
    */
   public void printlnToNonGroup(Entity player, String messageToRoom) {
      Entity leader = player.getGroupLeader();

      if (!player.isInvisible()) {
         for (Player playerInRoom : getPlayers()) {
            if (!leader.getGroupList().contains(playerInRoom) && playerInRoom != leader) {
               playerInRoom.println(messageToRoom);
            }
         }
      }

   }

   /**
    * Prints a message to everyone but members of a group.
    *
    * @param player the player where the message is originating from.
    * @param messageToRoom the message.
    */
   public void printToNonGroup(Entity player, String messageToRoom) {
      Entity leader = player.getGroupLeader();

      if (!player.isInvisible()) {
         for (Player playerInRoom : getPlayers()) {
            if (!leader.getGroupList().contains(playerInRoom) && playerInRoom != leader) {
               playerInRoom.print(messageToRoom);
            }
         }
      }
   }

   /**
    * Adds the player to the room.
    *
    * @param player the player to add.
    */
   public synchronized void addPlayer(Player player) {
      _players.add(player);
   }

   /**
    * Removes a player from the room.
    *
    * @param player the player to remove.
    */
   public synchronized void removePlayer(Player player) {
      boolean removed = _players.remove(player);
      if (!removed) {
         _log.warn("Attempted to remove " + player.getName()
            + " from room " + _roomNumber + " but player was not in the room.");
      }
   }

   /**
    * Checks if the room is populated with other players.
    *
    * @param entity the entity making the query.
    *
    * @return true if the room is populated with other players.
    */
   public boolean isRoomPopulatedWithOtherPlayers(Entity entity) {
      for (Player toPlayer : _players) {
         if (entity == null || !entity.equals(toPlayer)) {
            return true;
         }
      }

      return false;
   }

   /**
    * Checks if the room is populated with other mobs or players.
    *
    * @param entity The entity making the query.
    *
    * @return true if the room is populated with other players or mobs
    */
   public boolean isRoomPopulatedWithOtherEntity(Entity entity) {
      for (Player toPlayer : _players) {
         if (entity.equals(toPlayer)) {
            return true;
         }
      }

      for (Mob mob : getMobs()) {
         if (entity.equals(mob)) {
            return true;
         }
      }

      return false;
   }

   /**
    * Checks if the room is populated with other non-invisible players.
    *
    * @param entity The entity making the query.
    *
    * @return true if the room is populated with other non-invisible players.
    */
   public boolean isRoomPopulatedWithOtherNonInvisiblePlayers(Entity entity) {
      for (Player toPlayer : _players) {
         if (!entity.equals(toPlayer)) {
            if (!toPlayer.isInvisible()) {
               return true;
            }
         }
      }

      return false;
   }

   /**
    * Returns a player according to the string passed in or null if a player was not found.
    *
    * @param player the player in question, in string format.
    *
    * @return a player according to the string passed in or null if a player was not found.
    */
   public Player getPlayerInRoom(String player) {
      for (Player roomPlayer : _players) {
         if (MyStringUtil.contains(roomPlayer.getName(), player)) {
            return roomPlayer;
         }
      }

      return null;
   }

   /**
    * Gets the list of players.
    *
    * @return the list of players.
    */
   public ArrayList<Player> getPlayers() {
      return _players;
   }

   /**
    * Gets the room terrain.
    *
    * @return the room terrain.
    */
   public Terrain getTerrain() {
      return _terrain;
   }

   /**
    * Sets the room terrain.
    *
    * @param terrain the room terrain.
    */
   public void setTerrain(Terrain terrain) {
      _terrain = terrain;
   }

   /**
    * Gets the list of lairs.
    *
    * @return the list of lairs.
    */
   public ArrayList<Lair> getLairs() {
      return _lairs;
   }

   /**
    * Gets the room triggers.
    *
    * @return the room triggers.
    */
   public ArrayList<Trigger> getTriggers() {
      return _triggers;
   }

   /**
    * Sets the room triggers.
    *
    * @param triggers the room trigger.
    */
   public void setTriggers(ArrayList<Trigger> triggers) {
      _triggers = triggers;
   }

   /**
    * Handles the room triggers.
    * @param entity the entity that triggered the triggers.
    * @return a TriggerResult.
    */
   public TriggerResult handleTriggers(Entity entity) {
      TriggerResult finalTriggerResult = TriggerResult.NOTHING;
      for (Trigger trigger : getTriggers()) {
         TriggerResult triggerResult = trigger.handleTrigger(entity);
         finalTriggerResult = finalTriggerResult.getFinalTriggerResult(triggerResult);
      }

      return finalTriggerResult;
   }

   /**
    * This method will populate a lair and give the mob any items defined in the data files.
    * It will not respawn the lair mob if any other mobs are in the room.  In this case, the lair
    * mob WILL respawn if a player is in the room.
    */
   public void initializeLair() {
      // Clear the room of any mobs first before placing the lair mob.
      boolean clearRoom = true;

      for (Lair lair : _lairs) {
         if (lair.getNumberOfMobs() > 0) {
            if (clearRoom) {
               clear();
               clearRoom = false;
            }

            placeLairMob(lair);

            // The lair has an item. Give it to the last mob in the room.
            if (lair.getItem() != null) {
               Mob lastMob = _mobs.get(_mobs.size() - 1);
               Item item = lair.getItem().clone("initializeLair for " + lastMob.getName());
               InventoryFailCode code = lastMob.placeItemInInventory(item, true);
               if (code != InventoryFailCode.NONE) {
                  item.destroy();
                  item = null;
               }
            }

         }
      }
   }

   /**
    * This method is fairly similar to initialize lair.  However, it will only repopulate the mob and
    * not the item the mob is carrying.  Mobs will not respawn if a player is in the room.
    */
   public void populateLair() {
      for (Lair lair : _lairs) {
         if (lair.getNumberOfMobs() > 0) {
            if (_mobs.isEmpty() && _players.isEmpty()) {
               placeLairMob(lair);
            }
         }
      }
   }

   /**
    * Removes an item from the room ground.
    *
    * @param item the item to remove from the room ground.
    */
   public synchronized void removeItemFromRoom(Item item) {
      if (!_itemsOnGround.remove(item)) {
         _log.error("Attempted to remove " + item.getName() + " from room " + _roomNumber + " but the item was not there.");
      }
   }

   /**
    * Drop an item in the room.
    *
    * @param item the item to drop.
    *
    * @return a DropItemFailCode.
    */
   public synchronized DropItemFailCode dropItemInRoom(Item item) {
      if (item.getItemType().equals(ItemType.RUNE_SCROLL)) {
         item.destroy();
         return DropItemFailCode.NONE;
      }

      if (GameUtil.isTown(this)) {
         return DropItemFailCode.TOWN;
      }

      int maxItemsInRoom = new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.MAX_ITEMS_IN_ROOM)).intValue();
      if (_itemsOnGround.size() >= maxItemsInRoom) {
         return DropItemFailCode.NO_SPACE_IN_ROOM;
      }

      _itemsOnGround.add(item);
      return DropItemFailCode.NONE;
   }

   /**
    * Gets the list of items on the ground.
    *
    * @return the list of items on the ground.
    */
   public ArrayList<Item> getItemsOnGround() {
      return _itemsOnGround;
   }

   /**
    * Builds the debug string.
    *
    * @return a debug string representation of the room.
    */
   public String toDebugString() {
      StringBuffer buffer = new StringBuffer();

      buffer.append("&CRoom Number:&W " + _roomNumber + "\n");
      buffer.append("&CDefault Room Description:&W " + _defaultDescription + "\n");
      buffer.append("&CAlt Room Description:&W " + _altDescription + "\n");
      buffer.append("&CShort Description:&W " + getShortDescription() + "\n");
      buffer.append("&CLong Description:\n&W " + getLongDescription() + "\n");
      buffer.append("&CTerrain:&W " + _terrain.getDescription() + "\n");
      buffer.append("&CRoom Flags:&W" + RoomFlags.getFlagsString(_roomFlags) + "\n");
      buffer.append("&CService Level:&W" + _serviceLevel + "\n");

      for (Lair lair : _lairs) {
         String lairMob;
         try {
            lairMob = WorldManager.getMob(lair.getMob()).getName();
         } catch (Exception e) {
            lairMob = "ERROR";
         }
         buffer.append("&CLair:&W " + lairMob + "(" + lair.getNumberOfMobs() + "), ");
         if (lair.getItem() != null) {
            buffer.append("Item=" + lair.getItem().getName());
         } else {
            buffer.append("Item=none");
         }

         buffer.append("\n");
      }

      for (Trigger trigger : _triggers) {
         buffer.append("&CTrigger:&W " + trigger.toString());
         buffer.append("\n");
      }

      buffer.append("&CItems:&W [");
      for (Item item : _itemsOnGround) {
         buffer.append(item.getName() + ",");
      }
      buffer.append("]\n");

      buffer.append("&CMobiles:&W [");
      for (Mob mobs : getMobs()) {
         buffer.append(mobs.getName() + ",");
      }
      buffer.append("]\n");

      buffer.append("&CPlayers:&W [");
      for (Player p : _players) {
         buffer.append(p.getName() + ",");
      }
      buffer.append("]\n");

      if (_actionPlayerList != null) {
         buffer.append("&CAction Player List:&W [");
         for (String name : _actionPlayerList) {
            buffer.append(name + ",");
         }
         buffer.append("]\n");
      }

      buffer.append("&CExits:&W\n");
      for (Exit exit : _exits) {
         try {
            Room room = WorldManager.getRoom(exit.getToRoom());
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("  %-9s: %d [%s]", exit.getExitDirection().getLongDescription(),
                  exit.getToRoom(), room.getShortDescription());
            buffer.append(sb.toString() + "\n");

            if (exit.getDoor() != null) {
               buffer.append(exit.getDoor().toDebugString());
            }

         } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("%s", "**Under Construction**");
            buffer.append("             ");
            buffer.append(sb.toString() + "\n");
         }

      }

      return buffer.toString();
   }

   /**
    * Returns the room information.
    *
    * @param entity the entity that the information will be displayed to.
    *
    * @return the room information.
    */
   public String getDefaultRoomString(Entity entity) {
      StringBuffer buffer = new StringBuffer();

      buffer.append("&Y" + getShortDescription() + "\n");
      buffer.append("&G" + buildMobsInRoomString(getNpcs()));
      buffer.append("&R" + buildMobsInRoomString(getMobs()));
      buffer.append("&M" + buildPlayersInRoomString(entity));

      if (!isRoomPopulatedWithOtherNonInvisiblePlayers(entity)
            && !isRoomPopulatedWithNpcs()
            && _mobs.size() == 0) {
         buffer.append(TaMessageManager.BYSELF.getMessage());
      }
      buffer.append(buildItemsInRoomString());

      return buffer.toString();
   }

   /**
    * Places a mob in the room.
    *
    * @param mob the mob to place in the room.
    *
    * @return A move fail code.
    */
   public synchronized MoveFailCode placeMob(Mob mob) {
      int maxMobsInRoom = Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_MONSTERS_PER_ROOM));
      if (_mobs.size() >= maxMobsInRoom) {
         return MoveFailCode.ROOM_FULL;
      }

      _mobs.add(mob);

      return MoveFailCode.NONE;
   }

   /**
    * Gets the complete list of mobs in the room.
    *
    * @return the complete list of mobs in the room.
    */
   public ArrayList<Mob> getMobs() {
      return _mobs;
   }

   /**
    * Gets a list of hostile mobs in the room.  I.E., those not in the group, etc.
    *
    * @return a list of hostile mobs in the room.  I.E., those not in the group, etc.
    */
   public ArrayList<Mob> getHostileMobs() {
      ArrayList<Mob> hostileMobs = new ArrayList<Mob>();
      for (Mob mob : _mobs) {
         if (!mob.isFriendly()) {
            hostileMobs.add(mob);
         }
      }
      return hostileMobs;
   }

   /**
    * Gets the list of NPCs in the room.
    *
    * @return the list of NPCs in the room.
    */
   public ArrayList<Mob> getNpcs() {
      if (_npcs == null) {
         return null;
      }
      synchronized (_npcs) {
         if (_npcCache == null) {
            _npcCache = new ArrayList<Mob>();
            for (Integer mobVnum : _npcs) {
               _npcCache.add(WorldManager.getNpc(mobVnum));
            }
         }
      }

      return _npcCache;
   }

   /**
    * Gets the list of NPCs as an array of vnums.
    * @return the list of NPCs as an array of vnums.
    */
   public ArrayList<Integer> getNpcVnums() {
      return _npcs;
   }

   /**
    * Removes the mob from the room.
    *
    * @param mob the mob to remove from the room.
    */
   public void removeMob(Mob mob) {
      if (!_mobs.remove(mob)) {
         _log.error("Attempted to remove " + mob.getName() + " from room "
               + _roomNumber + " but the mob was not there.");
      }
   }

   /**
    * Checks if the room is illuminated.
    *
    * @return true if the room is not dark.
    */
   public boolean isIlluminated() {
      // Room has the permanent dark flag and it has not been removed.
      if (RoomFlags.PERM_DARK.isSet(getRoomFlags()) && !isIgnorePermantentDarkness()) {
         return false;
      }

      // Room has the darkness flag or perm dark flag.  At this point, any torch or glowstone will lift the darkness.
      if (RoomFlags.DARK.isSet(getRoomFlags()) || RoomFlags.PERM_DARK.isSet(getRoomFlags())) {
         for (Player player : getPlayers()) {
            if (player.hasLight()) {
               return true;
            }
         }

         return false;
      }

      return true;
   }

   /**
    * Formats the item string so that it looks like :
    *
    * There is nothing on the floor.
    * There is a torch lying on the floor.
    * There is a torch, and a torch lying on the floor.
    * There is a torch, a torch, and a torch lying on the floor.

    * @return a formatted string of items.
    */
   private String buildItemsInRoomString() {
      String itemString = MyStringUtil.buildItemListToString(_itemsOnGround);
      if (itemString == null) {
         return TaMessageManager.NOTING.getMessage();
      } else {
         StringBuffer buffer = new StringBuffer();
         buffer.append(TaMessageManager.SOMTNG.getMessage());
         buffer.append(" " + itemString);
         buffer.append(TaMessageManager.ONFLOR.getMessage());
         return buffer.toString();
      }
   }

   /**
    * Formats the player string so that it looks like :
    *
    * There is nobody here.
    * A is here.
    * A and B are here.
    * A, B and C are here.
    * A, B, C and D are here.
    *
    * @param entity the player that the string will be displayed to.
    *
    * @return a formatted string of players.
    */
   private String buildPlayersInRoomString(Entity entity) {
      StringBuffer buffer = new StringBuffer();
      ArrayList<Player> playerList = new ArrayList<Player>();
      for (Player toPlayer : _players) {
         if (entity == null || !entity.equals(toPlayer)) {
            if (!toPlayer.isInvisible()) {
               playerList.add(toPlayer);
            }
         }
      }

      if (playerList.size() == 1) {
         String name = playerList.get(0).getName();
         String players = MessageFormat.format(TaMessageManager.ONEOTH.getMessage(), name);
         buffer.append(players);
      } else if (playerList.size() > 1) {
         int count = 0;
         StringBuffer tempBuffer = new StringBuffer();
         String finalPlayer = "";
         for (Player player : playerList) {
            if (count == playerList.size() - 1) {
               // tempBuffer.append(" and " + player.getName());
               finalPlayer = player.getName();
            } else if (count == playerList.size() - 2) {
               tempBuffer.append(player.getName());
            } else {
               tempBuffer.append(player.getName() + ", ");
            }
            count++;
         }
         String players = MessageFormat.format(TaMessageManager.TWOOTH.getMessage(), tempBuffer.toString(), finalPlayer);
         buffer.append(players);
      }

      return buffer.toString();
   }

   /**
    * There are two hobgoblins here.
    * There is a lizard woman, and a huge rat here.
    * There is an orc here.
    * There's probably an easier way to construct this String, but fuck if I can figure it out.
    *
    * @param mobs the list of mobs to create this string from.
    *
    * @return a formatted mob string.
    */
   private String buildMobsInRoomString(ArrayList<Mob> mobs) {
      StringBuffer buffer = new StringBuffer();

      // No mobs found.
      if (mobs == null || mobs.size() == 0) {
         return "";
      }

      HashMap<String, Integer> map = new HashMap<String, Integer>();
      HashMap<String, Mob> mobMap = new HashMap<String, Mob>();

      // Create two maps.  One mapping to their count, and one mapping to the Mob object.
      // The mob object is needed because mob.getPlural or mob.getName are used, depending on the count.
      for (Mob mob : mobs) {
         mobMap.put(mob.getName(), mob);
         Integer i = map.get(mob.getName());
         if (i == null) {
            map.put(mob.getName(), Integer.valueOf(1));
         } else {
            i = Integer.valueOf(i.intValue() + 1);
            map.put(mob.getName(), i);
         }

      }

      // Create the String.
      ArrayList<String> strings = new ArrayList<String>();
      Set<String> keys = mobMap.keySet();
      int index = 0;
      for (String key : keys) {
         Integer count = map.get(key);
         Mob mob = mobMap.get(key);
         StringBuffer tmpBuffer = new StringBuffer();

         if (index == 0) {
            // The first listed mob.
            if (count == 1) {
               // There's only one mob... use the singular.
               tmpBuffer.append("is ");
            } else {
               // There are multiple mobs... use the plural.
               tmpBuffer.append("are ");
            }
         } else if (index == map.size() - 1) {
            // The last listed mob.
            tmpBuffer.append(", and ");
         } else {
            // Any middle listed mobs.
            tmpBuffer.append(", ");
         }
         // Display the correct string based on the number of similar mobs.
         int mobNumber = 1;
         if (count.intValue() == mobNumber++) {
            tmpBuffer.append(mob.getPrefix().toLowerCase() + " " + mob.getName());
         } else if (count.intValue() == mobNumber++) {
            tmpBuffer.append("two " + mob.getPlural());
         } else if (count.intValue() == mobNumber++) {
            tmpBuffer.append("three " + mob.getPlural());
         } else if (count.intValue() == mobNumber++) {
            tmpBuffer.append("four " + mob.getPlural());
         } else if (count.intValue() == mobNumber++) {
            tmpBuffer.append("five " + mob.getPlural());
         }
         strings.add(tmpBuffer.toString());
         index++;
      }

      // Create the beginning and end of the string.
      buffer.append("There ");
      for (String str : strings) {
         buffer.append(str);
      }
      buffer.append(" here.\n");

      return buffer.toString();
   }

   /**
    * Checks if the room is populated with NPCs.
    *
    * @return true if the room is populated with NPCs.
    */
   private boolean isRoomPopulatedWithNpcs() {
      if (getNpcs() == null) {
         return false;
      } else if (getNpcs().size() == 0) {
         return false;
      }

      return true;
   }

   /**
    * Clears the room of mobs.
    */
   public void clear() {
      ArrayList<Mob> purgedMobs = new ArrayList<Mob>();
      for (Mob mob : getMobs()) {
         purgedMobs.add(mob);
      }

      for (Mob mob : purgedMobs) {
         mob.getGroupLeader().getGroupList().remove(mob);
         mob.getRoom().removeMob(mob);

         ArrayList<Item> purgedItems = new ArrayList<Item>();
         for (Item item : mob.getInventory()) {
            purgedItems.add(item);
         }
         for (Item item : purgedItems) {
            mob.removeItemFromInventory(item);
            item.destroy();
            item = null;
         }

         mob.destroy();
         mob = null;
      }

   }

   /**
    * Sets the flag on whether or not permanent darkness should be ignored.
    * @param ignorePermanentDarkness the permanent darkness flag.
    */
   public void setIgnorePermanentDarkness(boolean ignorePermanentDarkness) {
      _ignorePermanentDarkness = ignorePermanentDarkness;
   }

   /**
    * Gets the permanent darkness flag.
    * @return the permanent darkness flag.
    */
   public boolean isIgnorePermantentDarkness() {
      return _ignorePermanentDarkness;
   }

   /**
    * Gets the list of players who performed any pre-actions for the room.
    * @return the list of players who performed any pre-actions for the room.
    */
   public List<String> getActionPlayerList() {
      synchronized (this) {
         for (Trigger t : getTriggers()) {
            if (t.getTriggerType().equals(TriggerType.PUZZLE)
                  && (t.getV2() == DoSay.SAY_CINDERS_ETHER || t.getV2() == DoMove.V2_MOVE_TAPESTRY)
                  && _actionPlayerList == null) {
               _actionPlayerList = new ArrayList<String>();
               break;
            }
         }
      }

      return _actionPlayerList;
   }

   /**
    * Places the mob belonging to this lair into this room.
    * @param lair the lair.
    */
   private void placeLairMob(Lair lair) {
      Mob lairMob = WorldManager.getMob(lair.getMob());
      Mob leader = null;
      for (int index = 0; index < lair.getNumberOfMobs(); index++) {
         Mob mob = lairMob.clone(this);
         MoveFailCode code = placeMob(mob);

         if (code.equals(MoveFailCode.NONE)) {
            if (leader == null) {
               leader = mob;
               leader.setGroupLeader(leader);
            } else {
               mob.setGroupLeader(leader);
               leader.getGroupList().add(mob);
            }
         }
      }
   }

   /**
    * Checks if the message can be displayed to the entity.
    * @param fromEntity the entity receiving the message.
    * @param message the message.
    * @param ignoreInvisible the ignore invisible flag.
    * @return true if the message can be displayed.
    */
   private boolean isRoomMessageValid(Entity fromEntity, String message, boolean ignoreInvisible) {
      if (message == null) {
         return false;
      }
      if (fromEntity != null && fromEntity.isInvisible() && !ignoreInvisible) {
         return false;
      }
      return true;
   }

   /**
    * Displays a message to all players in the room.
    * @param fromEntity the entity from where the message originated.
    * @param message the message.
    * @param println use println instead of print.
    */
   private void displayMessage(Entity fromEntity, String message, boolean println) {
      for (Player toPlayer : _players) {
         if (fromEntity == null || !fromEntity.equals(toPlayer)) {
            if (println) {
               toPlayer.println(message);
            } else {
               toPlayer.print(message);
            }
         }
      }
   }

}
