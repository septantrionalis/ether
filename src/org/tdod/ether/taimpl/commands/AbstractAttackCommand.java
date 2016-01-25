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

package org.tdod.ether.taimpl.commands;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.enums.DropItemFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;

/**
 * This is an abstract class that handles generic non-spell attacking functions -- such as ranged and melee attacks.
 *
 * @author rkinney
 */
public abstract class AbstractAttackCommand extends Command {

   /**
    * Wait time when attacks are used up.
    */
   private static final int ATTACK_COMBAT_WAIT_TIME
      = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.ATTACK_COMBAT_WAIT_TIME)).intValue();

   /**
    * Attacks an entity.
    *
    * @param player The player that is attacking.
    * @param target The target entity
    * @param exitDirectionEnum The direction that the attack came from.  This is used for ranged attacks.  Set null for melee.
    * @param weapon The attacking weapon.
    */
   protected final synchronized void attackEntity(Player player,
         Entity target, ExitDirectionEnum exitDirectionEnum, Item weapon) {
      // The mob is in the group.  Remove it.
      if (target.getEntityType().equals(EntityType.MOB) &&
          player.getGroupList().contains(target)) {
         player.getGroupList().remove(target);
         target.setGroupLeader(target);
      }
      
      // Reset combat ticker only if the room contains mob.
      if (!player.getRoom().getHostileMobs().isEmpty()) {
         player.setCombatTicker(ATTACK_COMBAT_WAIT_TIME);
      }

      int vitalityBefore = target.getVitality().getCurVitality();
      MeleeResult result = calculateMeleeResult(player, target, weapon);
      displayMessage(player, target, result, exitDirectionEnum, weapon);
      target.takeDamage(result.getDamage());

      GameUtil.giveExperience(player, target, vitalityBefore);

      // Adjust number of attacks appropriately
      player.getAttacks().attack();
      if (player.getAttacks().getAttacksLeft() == 0) {
         player.setRestTicker(GameUtil.randomizeRestWaitTime());
      }

      // Dead!
      GameUtil.checkAndHandleKill(player, target);
      return;
   }

   /**
    * Traverses a certain number of rooms in the direction specified when throwing an item.
    *
    * @param player The player executing the command.
    * @param room The current room.
    * @param exitDirectionEnum The direction to traverse.
    * @param eqToThrow The item to throw.
    * @param param The parameter that the player passed in for the command.
    * @param maxDepth The maximum depth to traverse.
    *
    * @return true if the method hits a dead end in the rooms.
    */
   protected final boolean traverseDirection(Player player,
         Room room, ExitDirectionEnum exitDirectionEnum, Equipment eqToThrow, String param, int maxDepth) {
      int depth = 0;
      do {
         depth++;
         if (depth > maxDepth) {
            return false;
         }
         Entity target = GameUtil.getTarget(player, room, param);
         if (target == null || target.equals(player)) {
            Exit exit = room.getExit(exitDirectionEnum);
            if (exit == null) {
               return false;
            } else {
               room = WorldManager.getRoom(exit.getToRoom());
            }
         } else {
            player.removeItemFromInventory(eqToThrow);
            attackEntity(player, target, exitDirectionEnum, eqToThrow);
            DropItemFailCode code = room.dropItemInRoom(eqToThrow);
            if (!code.equals(DropItemFailCode.NONE)) {
               eqToThrow.destroy();
               eqToThrow = null;
            }
            return true;
         }
      } while(true);

   }

   /**
    * Finds a target in the direction specifed.
    *
    * @param player The player executing the command.
    * @param container The equipment container.
    * @param ammo The ammo.
    * @param param The paramter the player passed in with the command.
    * @param range The number of rooms to traverse.
    *
    * @return true if a target was found, false otherwise.
    */
   protected final boolean findTarget(Player player, Equipment container, Equipment ammo, String param, int range) {
      for (ExitDirectionEnum exitDirection : GameUtil.getValidExitDirectionsForLook()) {
         Exit exit = player.getRoom().getExit(exitDirection);
         Room room;
         if (exit != null) {
            if (exit.isPassable()) {
               room = WorldManager.getRoom(exit.getToRoom());
               if (traverseDirection(player, room, exitDirection, ammo, param, range)) {
                  // Melee found.
                  if (container != null) {
                     container.setCharges(container.getCharges() - 1);
                  }
                  return true;
               }
            }
         }
      }

      return false;
   }

   /**
    * Determines the result of a single attack.
    *
    * @param player The player that is attacking.
    * @param target The victim.
    * @param weapon The attacking weapon.
    *
    * @return A MeleeResult.
    */
   protected abstract MeleeResult calculateMeleeResult(Player player, Entity target, Item weapon);

   /**
    * Displays all attacking messages to the appropriate rooms.
    *
    * @param player The player that is attacking.
    * @param target The victim.
    * @param result The MeleeResult.
    * @param exitDirection The direction that the attack came from.  This is used for ranged attacks.  Set null for melee.
    * @param weapon The attacking weapon.
    */
   protected abstract void displayMessage(Player player,
         Entity target, MeleeResult result, ExitDirectionEnum exitDirection, Item weapon);

}
