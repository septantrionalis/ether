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

package org.tdod.ether.ta.cosmos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.enums.DropItemFailCode;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.mobs.enums.Terrain;

/**
 * An interface defining the API for a single room.
 *
 * @author Ron Kinney
 */
public interface Room extends Serializable {

   /**
    * Initializes this room.
    */
   void initialize();

   /**
    * Gets the room number.
    *
    * @return the room number.
    */
   int getRoomNumber();

   /**
    * Sets the room number.
    *
    * @param roomNumber the room number.
    */
   void setRoomNumber(int roomNumber);

   /**
    * Gets the default room description.
    *
    * @return the default room description.
    */
   int getDefaultDescription();

   /**
    * Sets the default room description.
    *
    * @param defaultDescription the default room description.
    */
   void setDefaultDescription(int defaultDescription);

   /**
    * Gets the alternative room description.
    *
    * @return the alternative room description.
    */
   int getAltDescription();

   /**
    * Sets the alternative room description.
    *
    * @param altDescription the alternative room description.
    */
   void setAltDescription(int altDescription);

   /**
    * Sets the rooms short description.
    *
    * @return the rooms short description.
    */
   String getShortDescription();

   /**
    * Gets the long description of this room.
    *
    * @return the long description of this room.
    */
   String getLongDescription();

   /**
    * Adds an exit.
    *
    * @param exit the exit to add.
    */
   void addExit(Exit exit);

   /**
    * Removes an exit.
    *
    * @param exit the exit to remove.
    */
   void removeExit(Exit exit);

   /**
    * Gets the exit to the corresponding room.  null is returned if one is not found.
    *
    * @param toRoomVnum the destination room number.
    *
    * @return the exit to the corresponding room.  null is returned if one is not found.
    */
   Exit getExit(int toRoomVnum);

   /**
    * Gets the exit in the specified direction.  null is returned if the exit does not exit.
    *
    * @param exitDirectionEnum the exit direction.
    *
    * @return the exit in the specified direction.  null is returned if the exit does not exit.
    */
   Exit getExit(ExitDirectionEnum exitDirectionEnum);

   /**
    * Gets the arraylist of exists.
    *
    * @return the arraylist of exists.
    */
   ArrayList<Exit> getExits();

   /**
    * Returns a random exit.
    *
    * @return a random exit.
    */
   Exit getRandomExit();

   /**
    * Sets the room flags.
    *
    * @param roomFlags the room flags.
    */
   void setRoomFlags(int roomFlags);

   /**
    * Gets the room flags.
    *
    * @return the room flags.
    */
   int getRoomFlags();

   /**
    * Sets the service level.
    * @param serviceLevel the service level.
    */
   void setServiceLevel(int serviceLevel);

   /**
    * Gets the service level.
    * @return the service level.
    */
   int getServiceLevel();

   /**
    * Gets the list of items that the shop sells or buys.
    *
    * @return the list of items that the shop sells or buys.
    */
   ArrayList<Item> getShopItem();

   /**
    * Prints the message to the room.  The message will be displayed to everyone but the
    * from entity.
    *
    * @param fromEntity the entity that the message is coming from.
    * @param message the message.
    * @param ignoreInvisible if a player is invisible, messages are not displayed.  Set this flag to true if the message
    * should be displayed even if the player is invisible.
    */
   void println(Entity fromEntity, String message, boolean ignoreInvisible);

   /**
    * Prints the message to the room without a carriage return.
    * @param fromEntity the entity that the message is coming from.
    * @param message the message.
    * @param ignoreInvisible if a player is invisible, messages are not displayed.  Set this flag to true if the message
    * should be displayed even if the player is invisible.
    */
   void print(Entity fromEntity, String message, boolean ignoreInvisible);

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
   void println(Entity player, Entity target, String message, boolean ignoreInvisible);

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
   void print(Entity player, Entity target, String message, boolean ignoreInvisible);

   /**
    * Prints a message to everyone but members of a group.
    *
    * @param player the player where the message is originating from.
    * @param messageToRoom the message.
    */
   void printlnToNonGroup(Entity player, String messageToRoom);

   /**
    * Prints a message to everyone but members of a group.
    *
    * @param player the player where the message is originating from.
    * @param messageToRoom the message.
    */
   void printToNonGroup(Entity player, String messageToRoom);

   /**
    * Adds the player to the room.
    *
    * @param player the player to add.
    */
   void addPlayer(Player player);

   /**
    * Removes a player from the room.
    *
    * @param player the player to remove.
    */
   void removePlayer(Player player);

   /**
    * Checks if the room is populated with other players.
    *
    * @param entity the entity making the query.
    *
    * @return true if the room is populated with other players.
    */
   boolean isRoomPopulatedWithOtherPlayers(Entity entity);

   /**
    * Checks if the room is populated with other mobs or players.
    *
    * @param entity The entity making the query.
    *
    * @return true if the room is populated with other players or mobs
    */
   boolean isRoomPopulatedWithOtherEntity(Entity entity);

   /**
    * Checks if the room is populated with other non-invisible players.
    *
    * @param entity The entity making the query.
    *
    * @return true if the room is populated with other non-invisible players.
    */
   boolean isRoomPopulatedWithOtherNonInvisiblePlayers(Entity entity);

   /**
    * Returns a player according to the string passed in or null if a player was not found.
    *
    * @param player the player in question, in string format.
    *
    * @return a player according to the string passed in or null if a player was not found.
    */
   Player getPlayerInRoom(String player);

   /**
    * Gets the list of players.
    *
    * @return the list of players.
    */
   ArrayList<Player> getPlayers();

   /**
    * Gets the room terrain.
    *
    * @return the room terrain.
    */
   Terrain getTerrain();

   /**
    * Sets the room terrain.
    *
    * @param terrain the room terrain.
    */
   void setTerrain(Terrain terrain);

   /**
    * Removes an item from the room ground.
    *
    * @param item the item to remove from the room ground.
    */
   void removeItemFromRoom(Item item);

   /**
    * Drop an item in the room.
    *
    * @param item the item to drop.
    *
    * @return a DropItemFailCode.
    */
   DropItemFailCode dropItemInRoom(Item item);

   /**
    * Gets the list of items on the ground.
    *
    * @return the list of items on the ground.
    */
   ArrayList<Item> getItemsOnGround();

   /**
    * Returns the room information.
    *
    * @param entity the entity that the information will be displayed to.
    *
    * @return the room information.
    */
   String getDefaultRoomString(Entity entity);

   /**
    * Builds the debug string.
    *
    * @return a debug string representation of the room.
    */
   String toDebugString();

   /**
    * Places a mob in the room.
    *
    * @param mob the mob to place in the room.
    *
    * @return A move fail code.
    */
   MoveFailCode placeMob(Mob mob);

   /**
    * Gets the complete list of mobs in the room.
    *
    * @return the complete list of mobs in the room.
    */
   ArrayList<Mob> getMobs();

   /**
    * Gets a list of hostile mobs in the room.  I.E., those not in the group, etc.
    *
    * @return a list of hostile mobs in the room.  I.E., those not in the group, etc.
    */
   ArrayList<Mob> getHostileMobs();

   /**
    * Gets the list of NPCs in the room.
    *
    * @return the list of NPCs in the room.
    */
   ArrayList<Mob> getNpcs();

   /**
    * Gets the list of NPCs as an array of vnums.
    * @return the list of NPCs as an array of vnums.
    */
   ArrayList<Integer> getNpcVnums();

   /**
    * Removes the mob from the room.
    *
    * @param mob the mob to remove from the room.
    */
   void removeMob(Mob mob);

   /**
    * Checks if the room is illuminated.
    *
    * @return true if the room is not dark.
    */
   boolean isIlluminated();

   /**
    * Gets the list of lairs.
    *
    * @return the list of lairs.
    */
   ArrayList<Lair> getLairs();

   /**
    * Initializes the lair.  This call completely resets the lair repoping both items and mobs.
    */
   void initializeLair();

   /**
    * Populates the lair with mobs only.
    */
   void populateLair();

   /**
    * Gets the room triggers.
    *
    * @return the room triggers.
    */
   ArrayList<Trigger> getTriggers();

   /**
    * Sets the room triggers.
    *
    * @param triggers the room trigger.
    */
   void setTriggers(ArrayList<Trigger> triggers);

   /**
    * Handles the room triggers.
    * @param entity the entity that triggered the triggers.
    * @return a TriggerResult.
    */
   TriggerResult handleTriggers(Entity entity);

   /**
    * Clears the room of mobs.
    */
   void clear();

   /**
    * Sets the flag on whether or not permanent darkness should be ignored.
    * @param ignorePermanentDarkness the permanent darkness flag.
    */
   void setIgnorePermanentDarkness(boolean ignorePermanentDarkness);

   /**
    * Gets the permanent darkness flag.
    * @return the permanent darkness flag.
    */
   boolean isIgnorePermantentDarkness();

   /**
    * Gets the list of players who performed any pre-actions for the room.
    * @return the list of players who performed any pre-actions for the room.
    */
   List<String> getActionPlayerList();
}
