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

import java.text.MessageFormat;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentType;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * 0) &MSorry, thrown weapons are not permitted here.                      *
 * 1) &MSorry, you are not yet experienced enough to use thrown weapons.   *
 * 2) &MSorry, but you don't seem to have one.                             *
 * 3) &MSorry, that is not a throwing weapon!                              *
 * 4) &MSorry, archers may not wield that weapon.                          *
 * 5) &MYou are too inexperienced to use that item.                        *
 * 6) &MSorry, "asd" isn't in range.                                       *
 *
 * Ranged weapons disappear if the floor is full.
 * Need to be level 2 to use thrown weapons
 * Throw range = 3
 *
 * Missed = Sorry, you'll have to rest a while before you can move.
 * &MYour attack missed!
 * &MThe minotaur was just nearly missed by a dart from the north!
 * Hit = The minotaur was just hit by a dart from the north!
 * &MYour attack hit the minotaur for 6 damage!
 * &MThe minotaur was just hit by a dart from the north!
 * 13, 14, 15
 * &MThe minotaur was just hit by an axe from the north!
 * &YThe minotaur falls to the ground lifeless!
 *
 * &WYou cannot leave in the heat of battle!
 * -> only occurs if there's a mob in the room.
 * throw axe mino
 * Your attack hit the minotaur for 22 damage!
 * ne
 * &MSorry, you'll have to rest a while before you can move.
 * throw axe mino
 * &MYou are still physically exhausted from your previous activities!
 */
public class DoThrow extends AbstractAttackCommand {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoThrow.class);

   /**
    * Level that the player can use the throw command.
    */
   private static final int THROW_LEVEL =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.THROW_LEVEL));

   /**
    * The maximum range of the throw command.
    */
   private static final int THROW_DEPTH =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.THROW_DEPTH));

   /**
    * Executes the "throw" command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return false;
      }

      Player player = (Player) entity;

      String[] split = input.split(" ");
      if (split.length < THREE_PARAMS) {
         return false;
      }

      // Sorry, thrown weapons are not permitted here.
      if (GameUtil.isTown(player.getRoom())) {
         player.print(TaMessageManager.NOTHHR.getMessage());
         return true;
      }

      // Can't attack if the player is resting.
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // Sorry, you are not yet experienced enough to use thrown weapons.
      if (player.getLevel() < THROW_LEVEL) {
         player.print(TaMessageManager.CTHYET.getMessage());
         return true;
      }

      // Attempt to find the item from the inventory.  Iterate through weapon containers.
      Item itemToThrow = null;
      Equipment container = null;
      ArrayList<Item> inventory = player.getInventory();
      for (Item item : inventory) {
         if (item.getItemType().equals(ItemType.EQUIPMENT)) {
            Equipment eq = (Equipment) item;
            if (eq.getEquipmentType().equals(EquipmentType.THROWN_WEAPON_CONTAINER)
                  && eq.getCharges() > 0) {
               container = eq;
               item = (Equipment) WorldManager.getItem(eq.getMinEquipmentEffect());
            }
         }
         if (MyStringUtil.contains(item.getName(), split[1])) {
            if (container != null) {
               String origin = "Spawned from a " + container.getName() + ") from " + player.getName();
               itemToThrow = item.clone(origin);
            } else {
               itemToThrow = item;
            }
            break;
         }
         container = null;
      }

      // Sorry, but you don't seem to have one.
      if (itemToThrow == null) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      // Sorry, that is not a throwing weapon!
      if (!itemToThrow.getItemType().equals(ItemType.EQUIPMENT)) {
         player.print(TaMessageManager.CTHTHT.getMessage());
         return true;
      }

      Equipment eqToThrow = (Equipment) itemToThrow;
      if (!eqToThrow.getEquipmentType().equals(EquipmentType.SUPPLY_WEAPON)) {
         player.print(TaMessageManager.CTHTHT.getMessage());
         return true;
      }

      // Sorry, archers may not wield that weapon.
      if (!eqToThrow.canUse(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NTTWEP.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // You are too inexperienced to use that item.
      if (player.getLevel() < eqToThrow.getLevel()) {
         player.print(TaMessageManager.TOOINX.getMessage());
         return true;
      }

      if (findTarget(player, container, eqToThrow, split[2], THROW_DEPTH)) {
         return true;
      }

      // Sorry, "asd" isn't in range.
      String messageToPlayer = MessageFormat.format(TaMessageManager.ARNNCE.getMessage(), split[2]);
      entity.print(messageToPlayer);
      return true;
   }

   /**
    * Calculates the MeleeResult.
    *
    * @param player The player who issued the throw command.
    * @param target The target.
    * @param weapon The item being thrown.
    *
    * @return a MeleeResult.
    */
   protected final MeleeResult calculateMeleeResult(Player player, Entity target, Item weapon) {
      if (!weapon.getItemType().equals(ItemType.EQUIPMENT)) {
         _log.error("Attempted to throw a non-equipment type of item " + weapon.getName());
         return null;
      }
      return WorldManager.getGameMechanics().doPlayerThrowAttack(player, target, (Equipment) weapon);
   }

   /**
    * Displays the thrown message to all recipients.
    *
    * @param player The player who issued the throw command.
    * @param target The target.
    * @param result A MeleeResult
    * @param exitDirection The direction of the throw command.
    * @param weapon The weapon being thrown.
    */
   protected final void displayMessage(Player player, Entity target, MeleeResult result,
         ExitDirectionEnum exitDirection, Item weapon) {
      if (!weapon.getItemType().equals(ItemType.EQUIPMENT)) {
         _log.error("Can only throw item of type equipment.  Got " + weapon.getItemType() + " for " + weapon.getName());
         return;
      }

      Equipment eq = (Equipment) weapon;
      String messageToPlayer = "ERROR";
      String messageToTargetRoom = "ERROR";
      String messageToVictim = "ERROR";
      Room targetRoom = target.getRoom();
      switch (result.getMeleeResultEnum()) {
      case MISS:
         messageToPlayer = TaMessageManager.ATTFUM.getMessage();
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToTargetRoom = MessageFormat.format(TaMessageManager.TFMOTH2.getMessage(),
                  target.getName(), eq.getLongDescription(), GameUtil.getOppositeExit(exitDirection).getLongDescription());
            messageToVictim = MessageFormat.format(TaMessageManager.TFMYOU1.getMessage(),
                  player.getName(), eq.getLongDescription());
         } else {
            messageToTargetRoom = MessageFormat.format(TaMessageManager.TFMMOT2.getMessage(),
                  target.getName(), eq.getLongDescription(), GameUtil.getOppositeExit(exitDirection).getLongDescription());
            messageToVictim = "";
         }
         break;
      case HIT:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTHIT.getMessage(),
                  target.getName(), result.getDamage());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.THROTH2.getMessage(),
                  target.getName(), eq.getLongDescription(), GameUtil.getOppositeExit(exitDirection).getLongDescription());
            messageToVictim = MessageFormat.format(TaMessageManager.THRYOU1.getMessage(),
                  player.getName(), eq.getLongDescription(), result.getDamage());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTHTM.getMessage(),
                  target.getName(), result.getDamage());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.THRMOT2.getMessage(),
                  target.getName(), eq.getLongDescription(), GameUtil.getOppositeExit(exitDirection).getLongDescription());
            messageToVictim = "";
         }
         break;
      case DODGE:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTDOG.getMessage(),
                  target.getName());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.TDGOTH1.getMessage(),
                  target.getName(), eq.getLongDescription(), player.getName());
            messageToVictim = MessageFormat.format(TaMessageManager.TDGYOU1.getMessage(),
                  eq.getLongDescription(), player.getName());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.MATDOG.getMessage(),
                  target.getName());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.TDGMOT.getMessage(),
                  target.getName(), eq.getLongDescription(), player.getName());
            messageToVictim = "";
         }
         break;
      case GLANCE:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTGLN.getMessage(),
                  target.getName());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.GLNOTH.getMessage(),
                  player.getName(), eq.getName(), target.getName());
            messageToVictim = MessageFormat.format(TaMessageManager.GLNYOU.getMessage(),
                  player.getName(), eq.getName());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTGNM.getMessage(),
                  target.getName());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.TGNMOT.getMessage(),
                  target.getName(), eq.getLongDescription(), player.getName());
            messageToVictim = "";
         }
         break;
      default :
         _log.error("Got unhandled melee result of " + result.getMeleeResultEnum());
         break;
      }

      String messageToOriginalRoom = MessageFormat.format(TaMessageManager.THROTH12.getMessage(), player.getName(),
            eq.getLongDescription(), exitDirection.getLongDescription());
      player.getRoom().print(player, messageToOriginalRoom, false);

      player.print(messageToPlayer);
      targetRoom.print(player, target, messageToTargetRoom, false);
      target.print(messageToVictim);
   }

}
