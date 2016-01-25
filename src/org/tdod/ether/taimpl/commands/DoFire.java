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
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentType;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * fire sling
 * 0)&MSorry, that is not an appropriate command.                          *
 * 1)&MSorry, missle weapons are not permitted here.                       *
 * 2)&MSorry, you are not yet experienced enough to use missle weapons.    *
 * 3)&MSorry, but you don't seem to have one.                              *
 * 4)&MSorry, that is not a missle weapon!                                 *
 * 5)&MSorry, you haven't got a stone!                                     *
 * 5)&MSorry, you haven't got an arrow!                                    *
 * 6)&MSorry, "asd" isn't in range.
 *
 * &MYour attack hit the minotaur for 11 damage!
 * &MThe minotaur was just hit by a stone from the north!
 *
 * &MYour attack missed!
 * &MThe minotaur was just nearly missed by a stone from the north!
 *
 * &MMinex just fired an arrow to the south!
 *
 * Level 2 to use the fire command.
 *
 * pouch of stones(7)
 * a quiver(20)
 * There is a stone lying on the floor.
 * There is an arrow lying on the floor.
 * @author minex
 */
public class DoFire extends AbstractAttackCommand {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoFire.class);

   /**
    * Level that the player can use the fire command.
    */
   private static final int FIRE_LEVEL =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FIRE_LEVEL));

   /**
    * Executes the "fire" command.
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

      // Sorry, missle weapons are not permitted here.
      if (GameUtil.isTown(player.getRoom())) {
         player.print(TaMessageManager.NOFRHR.getMessage());
         return true;
      }

      // Can't attack if the player is resting.
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // Sorry, you are not yet experienced enough to use missle weapons.
      if (player.getLevel() < FIRE_LEVEL) {
         player.print(TaMessageManager.CFRYET.getMessage());
         return true;
      }

      Weapon weapon = player.getWeapon();

      // Sorry, but you don't seem to have one.
      if (!MyStringUtil.contains(weapon.getName(), split[1])) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      // Sorry, that is not a missle weapon!
      if (weapon.getRange() < 1) {
         player.print(TaMessageManager.CFRTHT.getMessage());
         return true;
      }

      Equipment ammoVnum = (Equipment) WorldManager.getItem(weapon.getAmmoVnum());
      Item ammo = null;
      Equipment container = null;
      ArrayList<Item> inventory = player.getInventory();
      for (Item item : inventory) {
         if (item.getItemType().equals(ItemType.EQUIPMENT)) {
            Equipment eq = (Equipment) item;
            if (eq.getEquipmentType().equals(EquipmentType.AMMO_CONTAINER)
                  && eq.getCharges() > 0) {
               container = eq;
               item = (Equipment) WorldManager.getItem(eq.getMinEquipmentEffect());
            }
         }
         if (ammoVnum.getVnum() == item.getVnum()) {
            if (container != null) {
               String origin = "Spawned from a " + container.getName() + " from " + player.getName();
               ammo = item.clone(origin);
            } else {
               ammo = item;
            }
            break;
         }
         container = null;
      }


      // Sorry, you haven't got a stone!
      if (ammo == null) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NOTPRJ.getMessage(), ammoVnum.getLongDescription());
         player.println(messageToPlayer);
         return true;
      }

      if (findTarget(player, container, (Equipment) ammo, split[2], weapon.getRange())) {
         return true;
      }

      // Sorry, "asd" isn't in range.
      String messageToPlayer = MessageFormat.format(TaMessageManager.ARNNCE.getMessage(), split[2]);
      entity.print(messageToPlayer);
      return true;
   }

   /**
    * Calculates the melee result for a fired weapon.
    *
    * @param player The player issuing the fire command.
    * @param target The target.
    * @param weapon The weapon being fired.
    *
    * @return A melee result.
    */
   protected final MeleeResult calculateMeleeResult(Player player, Entity target, Item weapon) {
      if (!weapon.getItemType().equals(ItemType.EQUIPMENT)) {
         _log.error("Attempted to fire a non-equipment type of item " + weapon.getName());
         return null;
      }
      return WorldManager.getGameMechanics().doPlayerThrowAttack(player, target, (Equipment) weapon);
   }

   /**
    * Displays the messages to the room, player, and target.
    *
    * @param player The player issuing the fire command.
    * @param target The target.
    * @param result The melee result
    * @param exitDirection The direction of the fire command.
    * @param weapon The weapon being fired.
    */
   protected final void displayMessage(Player player,
         Entity target, MeleeResult result, ExitDirectionEnum exitDirection, Item weapon) {
      if (!weapon.getItemType().equals(ItemType.EQUIPMENT)) {
         _log.error("Can only fire item of type equipment.  Got " + weapon.getItemType() + " for " + weapon.getName());
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
            messageToVictim = MessageFormat.format(TaMessageManager.FFMYOU1.getMessage(),
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
            messageToVictim = MessageFormat.format(TaMessageManager.FIRYOU1.getMessage(),
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
            messageToTargetRoom = MessageFormat.format(TaMessageManager.FDGOTH1.getMessage(),
                  target.getName(), eq.getLongDescription(), player.getName());
            messageToVictim = MessageFormat.format(TaMessageManager.FDGYOU1.getMessage(),
                  eq.getLongDescription(), player.getName());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.MATDOG.getMessage(),
                  target.getName());
            messageToTargetRoom = MessageFormat.format(TaMessageManager.FDGMOT.getMessage(),
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

      String messageToOriginalRoom = MessageFormat.format(TaMessageManager.FIROTH12.getMessage(),
            player.getName(), eq.getLongDescription(), exitDirection.getLongDescription());
      player.getRoom().print(player, messageToOriginalRoom, false);

      player.print(messageToPlayer);
      targetRoom.print(player, target, messageToTargetRoom, false);
      target.print(messageToVictim);
   }
}
