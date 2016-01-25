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

package org.tdod.ether.ta;

import java.util.ArrayList;
import java.util.HashMap;

import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Attacks;
import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.Mana;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Spellbook;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.taimpl.mobs.enums.Gender;

/**
 * The base class for all "living things" in the game -- players, mobs, npcs, etc.
 *
 * @author Ron Kinney
 */
public interface Entity {

   /**
    * Gets the name of the entity.
    * @return the name of the entity.
    */
   String getName();

   /**
    * Sets the name of the entity.
    * @param name the name of the entity.
    */
   void setName(String name);

   /**
    * Gets the vitality.
    * @return the vitality.
    */
   Vitality getVitality();

   /**
    * Gets the amount of experience given per point of damage.
    * @param playerLevel the attacking players level.
    * @return the amount of experience given per point of damage.
    */
   float getExpPerPointOfDamage(int playerLevel);

   /**
    * Takes the given amount of damage.
    * @param amount the amount of damage taken.
    * @return true if the entity's health goes below 0.
    */
   boolean takeDamage(int amount);

   /**
    * Sends a message to the entity followed by a carriage return.
    * @param str the message to send.
    */
   void println(String str);

   /**
    * Sends a message to the entity without the carriage return.
    * @param str the message to send.
    */
   void print(String str);

   /**
    * Gets the entity's level.
    * @return the entity's level.
    */
   int getLevel();

   /**
    * Sets the entity's level.
    * @param level the entity's level.
    */
   void setLevel(int level);

   /**
    * Gets the entity's combat skill.
    * @return the entity's combat skill.
    */
   int getCombatSkill();

   /**
    * Gets the entity type.
    * @return the entity type.
    */
   EntityType getEntityType();

   /**
    * Gets the armor rating.
    * @return the armor rating.
    */
   int getArmorRating();

   /**
    * Gets the room the entity currently resides in.
    * @return the room the entity currently resides in.
    */
   Room getRoom();

   /**
    * A convenience method that sets the room the entity will reside in.
    * @param room the room the entity will reside in.
    */
   void setRoom(Room room);

   /**
    * Sets the room that the entity will reside in based on the room number.
    * @param room the room number.
    */
   void setRoom(int room);

   /**
    * Moves the entity to this room.  No messages are displayed.
    * @param toRoomNumber the room number to move the entity to.
    * @return a TriggerResult, since teleporting an entity can still trigger a trigger.
    */
   TriggerResult teleportToRoom(int toRoomNumber);

   /**
    * Teleports this player to the specified room, ignoring all triggers.
    * @param toRoomNumber the room to teleport to.
    */
   void teleportToRoomIgnoreTriggers(int toRoomNumber);

   /**
    * Move the entity to this room.
    * @param toRoomNumber the destination room number.
    * @param ignoreBarrier true if any barriers can be ignored.
    * @return a move fail code.
    */
   MoveFailCode moveToRoom(int toRoomNumber, boolean ignoreBarrier);

   /**
    * Gets the status.
    * @return the status.
    */
   Status getStatus();

   /**
    * Sets the status.
    * @param status the status.
    */
   void setStatus(Status status);

   /**
    * Gets the poison damage per tick.
    * @return the poison damage per tick.
    */
   int getPoisonDamage();

   /**
    * Sets the poison damage per tick.
    * @param poisonDamage the poison damage per tick.
    */
   void setPoisonDamage(int poisonDamage);

   /**
    * Gets the mana.
    * @return the mana.
    */
   Mana getMana();

   /**
    * Gets the regeneration ticker.  If this value is above 0, the entity has higher than
    * normal vitality regeneration.
    * @return the regeneration ticker.
    */
   int getRegenerationTicker();

   /**
    * Sets the regeneration ticker.  If this value is above 0, the entity has higher than
    * normal vitality regeneration.
    * @param value the regeneration ticker.
    */
   void setRegenerationTicker(int value);

   /**
    * Reduces the regeneration ticker.  If the ticker is above 0, the entity has higher than
    * normal vitality regeneration.
    * @param value the amount to reduce the ticker.
    */
   void reduceRegenerationTicker(int value);

   /**
    * Gets the hunger ticker.  If this value is below zero, the entity will starve.
    * @return the hunger ticker.
    */
   int getHungerTicker();

   /**
    * Increases the hunger ticker by the specified amount.
    * @param value the amount to increase the hunger ticker.
    */
   void addHungerTicker(int value);

   /**
    * Sets the hunger ticker by the specified amount.
    * @param value the amount to set the hunger ticker.
    */
   void setHungerTicker(int value);

   /**
    * Gets the thirst ticker.  If this value is below zero, the entity will be thirsty.
    * @return the thirst ticker.
    */
   int getThirstTicker();

   /**
    * Increases the thirst ticker by the specified amount.
    * @param value the amount to increase the thirst ticker.
    */
   void addThirstTicker(int value);

   /**
    * Sets the thirst ticker by the specified amount.
    * @param value the amount to set the thirst ticker.
    */
   void setThirstTicker(int value);
   
   /**
    * Decreases both the hunger and thirst ticker and accordingly updates the entities status
    * if any of these tickers are reduced below 0.
    * @param value the amount to decrease the hunger and thirst ticker.
    */
   void subtractSustenance(int value);

   /**
    * Increase both the hunger and thirst ticker by the specified amount.
    * @param value the amount to increase both the thirst and hunger ticker.
    */
   void addSustenance(int value);

   /**
    * Gets the base stats.
    * @return the base stats.
    */
   BaseStats getStats();

   /**
    * Decreases the effect timers.
    * @return true if the effect timer hits 0.
    */
   boolean decreaseEffectTimers();

   /**
    * Sets the magic protection.
    * @param magicProtection the magic protection.
    */
   void setMagicProtection(int magicProtection);

   /**
    * Gets the magic protection.
    * @return the magic protection.
    */
   int getMagicProtection();

   /**
    * Sets the magic protection timer.
    * @param magicProtectionTimer the magic protection timer value.
    */
   void setMagicProtectionTimer(int magicProtectionTimer);

   /**
    * Sets the invisibility timer.
    * @param invisibilityTimer the invisibility timer value.
    */
   void setInvisibiltyTimer(int invisibilityTimer);

   /**
    * Checks if this entity is invisible.
    * @return true if this entity is invisible.
    */
   boolean isInvisible();

   /**
    * Gets the resting string.  This is currently used for the DoOrder command.
    * @return the resting string.
    */
   String getRestingString();

   /**
    * Gets the group leader.  If the entity is not in a group, this method returns
    * the entity itself.  Hopefully, this method never returns null.
    * @return the group leader.
    */
   Entity getGroupLeader();

   /**
    * Sets the group leader.  If the entity is not in a group, set this to the
    * entity itself.  This value should not be null.
    * @param leader the group leader.
    */
   void setGroupLeader(Entity leader);

   /**
    * Gets the list of entities in the group if this entity is the leader of a group.
    * An empty list is returned otherwise.
    * @return the list of entities in the group if this entity is the leader of a group.
    */
   ArrayList<Entity> getGroupList();

   /**
    * Sets the group list.
    * @param groupList the group list.
    */
   void setGroupList(ArrayList<Entity> groupList);

   /**
    * Checks if this entity is following a group.
    * @return true if this entity is following a group.
    */
   boolean isFollowingGroup();

   /**
    * Sets the is following flag.
    * @param isFollowingGroup true if this entity is in a group.
    */
   void setFollowingGroup(boolean isFollowingGroup);

   /**
    * Checks if this entity is in the middle of attacking.
    * @return true if this entity is in the middle of attacking.
    */
   boolean isAttacking();

   /**
    * Checks if this entity is resting.
    * @return true if this entity is resting.
    */
   boolean isResting();

   /**
    * Checks if this entity is mentally exhausted.
    * @return true if this entity is mentally exhausted.
    */
   boolean isMentallyExhausted();

   /**
    * The amount of gold being carried.
    * @return the amount of gold being carried.
    */
   int getGold();

   /**
    * Subtracts the amount of gold from the entity.  If the entity's gold goes
    * below 0, the gold amount will be set to 0 and the overflow will be returned.
    * @param amount the amount of gold to subtract.
    * @return the overflow amount.
    */
   int subtractGold(int amount);

   /**
    * This method adds to the amount of gold that the player has. Note that this
    * method is constrained by the MAX_INVENTORY_GOLD properties value. This
    * method returns the amount of over flow, or 0 if the set did not go over
    * the MAX_INVENTORY_GOLD value.
    *
    * @param amount The amount of gold to give the player.
    * @return the amount of over flow, or 0 if the set did not go over the
    *         MAX_INVENTORY_GOLD value.
    */
   int addGold(int amount);

   /**
    * This method sets the amount of gold that the player has. Note that this
    * method is constrained by the MAX_INVENTORY_GOLD properties value. This
    * method returns the amount of over flow, or 0 if the set did not go over
    * the MAX_INVENTORY_GOLD value.
    *
    * @param gold The amount of gold the player will have.
    * @return the amount of over flow, or 0 if the set did not go over the
    *         MAX_INVENTORY_GOLD value.
    */
   int setGold(int gold);

   /**
    * Checks if this entity has a lit torch.
    * @return true if a lit torch is being carried.
    */
   boolean hasLitTorch();

   /**
    * Checks if this entity has light.
    * @return true if this entity has light.
    */
   boolean hasLight();

   /**
    * This removes an item of the same vnum from the inventory.
    *
    * @param vnum The vnum of the item to remove.
    * @return true if the item was removed, false otherwise.
    */
   boolean removeItemFromInventory(int vnum);

   /**
    * This removes the exact item from the inventory. That is, the object has to
    * be of the same instance.
    *
    * @param item The item to remove.
    * @return true if the item was removed, false otherwise.
    */
   boolean removeItemFromInventory(Item item);

   /**
    * Places an item into the entities inventory.
    * @param item the item to place.
    * @param ignoreWeight set to true if the item should ignore weight constraints.
    * @return an InventoryFailCode.
    */
   InventoryFailCode placeItemInInventory(Item item, boolean ignoreWeight);

   /**
    * Gets the entire inventory list.
    * @return the inventory list.
    */
   ArrayList<Item> getInventory();

   /**
    * Gets the player class.
    * @return the player class.
    */
   PlayerClass getPlayerClass();

   /**
    * Gets the gender.
    * @return the gender.
    */
   Gender getGender();

   /**
    * Sets the gender.
    * @param gender the gender.
    */
   void setGender(Gender gender);

   /**
    * Gets the tracking map.  This is a map of rooms the entity went through along
    * with the direction.  This is used for tracking.
    * @return the tracking map.
    */
   HashMap<Room, ExitDirectionEnum> getTrackingMap();

   /**
    * Checks if this entity is similar to the specified entity.
    * @param entity the entity to compare.
    * @return true if this entity is similar to the specified entity.
    */
   boolean isSimilar(Entity entity);

   /**
    * Gets the spell book.
    * @return the spell book.
    */
   Spellbook getSpellbook();

   /**
    * Sets the spell book.
    * @param spellbook the spell book.
    */
   void setSpellbook(Spellbook spellbook);

   /**
    * Decreases the mental exhaustion ticker.
    */
   void decreaseMentalExhaustionTicker();

   /**
    * Sets the mental exhaustion ticker.  If this value is above 0, then the entity is
    * mentally exhausted.
    * @param value the mental exhaustion ticker.
    */
   void setMentalExhaustionTicker(int value);

   /**
    * Adds the amount of experience.
    * @param experience the amount of experience to add.
    */
   void addExperience(long experience);

   /**
    * Gets the attacks.
    * @return the attacks.
    */
   Attacks getAttacks();

   /**
    * Decreases the rest ticker.
    */
   void decreaseRestTicker();

   /**
    * Decrease the combat ticker.
    */
   void decreaseCombatTicker();

   /**
    * Sets the rest ticker.  If this number is greater than 0, then the
    * entity is resting.
    * @param value the rest ticker value.
    */
   void setRestTicker(int value);

   /**
    * Sets the combat ticker.  If this number is greater than 0, then the entity
    * has attacked recently and is considered in combat.
    * @param value the combat ticker.
    */
   void setCombatTicker(int value);

   /**
    * Gets the spell hit difficulty for this entity.
    * @return the spell hit difficulty for this entity.
    */
   int getSpellHitDifficulty();

   /**
    * Gets the message handler.
    * @return the message handler.
    */
   EntityMessageHandler getMessageHandler();

   /**
    * Sets the message handler.
    * @param messageHandler the message handler.
    */
   void setMessageHandler(EntityMessageHandler messageHandler);

   /**
    * Sets the paralysis ticker.  If this value is greater than 0, than the entity
    * is considered to be paralyzed.
    * @param value the paralysis ticker value.
    */
   void setParalysisTicker(int value);

   /**
    * Decreases the paralysis ticker.
    * @param amount the amount to decrease the ticker.
    * @return true if the entity is no longer paralyzed.
    */
   boolean decreaseParalysisTicker(int amount);

   /**
    * Gets the player that this entity is dragging.  Null if none.
    * @return the player that this entity is dragging.  Null if none.
    */
   Player getDragging();

   /**
    * Sets the dragging player.
    * @param dragging the dragging player.
    */
   void setDragging(Player dragging);

   /**
    * Gets the player that this entity is being dragged by.
    * @return the player that this entity is being dragged by.
    */
   Player getDraggedBy();

   /**
    * Sets the player that this entity is being dragged by.
    * @param draggedBy the player that this entity is being dragged by.
    */
   void setDraggedBy(Player draggedBy);

   /**
    * Gets the last time this entity has moved.
    * @return the last time this entity has moved.
    */
   long getLastMove();

   /**
    * Sets the last time this entity has moved.
    * @param lastMove the last time this entity has moved.
    */
   void setLastMove(long lastMove);

   /**
    * This checks if the entity is currently tripping.
    * @return true if this entity is currently tripping.
    */
   boolean isTripping();

   /**
    * This sets the entity into a tripping state.
    * @param isTripping the tripping flag.
    */
   void setIsTripping(boolean isTripping);

   /**
    * This is a debug command.  This checks if the entity can trip
    * by moving too fast.
    * @return true if this entity can ignore the tripping mechanism when moving too fast.
    */
   boolean getIgnoreTrip();

   /**
    * This is a debug command.  This checks if the entity can trip
    * by moving too fast.
    * @param ignoreTrip the ignore trip flag.
    */
   void setIgnoreTrip(boolean ignoreTrip);

   /**
    * Gets the debug string for this entity.
    * @return the debug string for this entity.
    */
   String getDebugString();
}
