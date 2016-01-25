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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * &MSorry, missle weapons are not permitted here.
 * &MSorry, hostility is not permitted here.
 * &MSorry, you can't attack yourself.
 * &MSorry, you don't see "j" nearby.
 *
 * &MYour attack hit the huge rat for 13 damage!
 * &MMinex just attacked the hobgoblin with a warhammer!
 *
 * &MThe giant bat dodged your attack!
 * &MThe imp barely dodged Minex's warhammer!
 *
 * &MYour attack missed!
 * &MMinex's poorly executed attack misses the imp!
 *
 * &YThe huge rat falls to the ground lifeless!
 * &CYou found 2 gold crowns while searching the imp's corpse.
 *
 * &YThe minotaur falls to the ground lifeless!
 * &CYou found 10 gold crowns while searching the area.
 * &MWhile searching the area, you notice a copper key, but you can't carry it.
 */
public class DoAttack extends AbstractAttackCommand {

   /**
    * Logging variable.
    */
   private static Log _log = LogFactory.getLog(DoAttack.class);

   /**
    * Executes the "attack" command.
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

      Room room = entity.getRoom();
      Player player = (Player) entity;

      String[] split = input.split(" ");
      if (split.length < 2) {
         return false;
      }
      String param = split[1].toLowerCase();

      // Can't fight in a safe room.
      if (RoomFlags.SAFE.isSet(room.getRoomFlags())) {
         player.print(TaMessageManager.NOFTHR.getMessage());
         return true;
      }

      // Can't attack if the player is resting.
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // That is not a melee weapon!
      if (player.getWeapon().getRange() > 0) {
         player.print(TaMessageManager.NOTMLE.getMessage());
         return true;
      }

      Entity target = GameUtil.getTarget(player, room, param);

      // Nothing found...
      if (target == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), param);
         player.print(message);
         return true;
      }

      // Can't attack self
      if (target.equals(player)) {
         player.print(TaMessageManager.NOASLF.getMessage());
         return true;
      }

      // Done
      attackEntity(player, target, null, player.getWeapon());
      return true;
   }

   /**
    * Calculates the melee attack result.
    *
    * @param player The player making the attack.
    * @param target The target entity.
    * @param weapon The weapon that the player is attacking with.
    *
    * @return A MeleeResult.
    */
   protected final MeleeResult calculateMeleeResult(Player player, Entity target, Item weapon) {
      return WorldManager.getGameMechanics().doPlayerMeleeAttack(player, target);
   }

   /**
    * Displays the attacking message to all recipients.
    *
    * @param player The player making the attack.
    * @param target The target entity.
    * @param result The MeleeResult.
    * @param exitDirection Not applicable for this method, but listed in the parameter to satisfy AbstractAttackCommand.
    * @param item The weapon that the player is attacking with.
    */
   protected final void displayMessage(Player player,
         Entity target, MeleeResult result, ExitDirectionEnum exitDirection, Item item) {
      if (!item.getItemType().equals(ItemType.WEAPON)) {
         _log.error("Can only attack with item of type weapon.  Got " + item.getItemType() + " for " + item.getName());
         return;
      }

      String messageToPlayer = "ERROR";
      String messageToRoom = "ERROR";
      String messageToVictim = "ERROR";
      String weapon = item.getLongDescription();
      switch (result.getMeleeResultEnum()) {
      case MISS:
         messageToPlayer = TaMessageManager.ATTFUM.getMessage();
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToRoom = MessageFormat.format(TaMessageManager.FUMOTH.getMessage(),
                  player.getName(), target.getName());
            messageToVictim = MessageFormat.format(TaMessageManager.FUMYOU.getMessage(),
                  player.getName());
         } else {
            messageToRoom = MessageFormat.format(TaMessageManager.FMMOTH.getMessage(),
                  player.getName(), target.getName());
            messageToVictim = "";
         }
         break;
      case HIT:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTHIT.getMessage(),
                  target.getName(), result.getDamage());
            messageToRoom = MessageFormat.format(TaMessageManager.HITOTH.getMessage(),
                  player.getName(), target.getName(), weapon);
            messageToVictim = MessageFormat.format(TaMessageManager.HITYOU.getMessage(),
                  player.getName(), weapon, result.getDamage());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTHTM.getMessage(),
                  target.getName(), result.getDamage());
            messageToRoom = MessageFormat.format(TaMessageManager.HTMOTH.getMessage(),
                  player.getName(), target.getName(), weapon);
            messageToVictim = "";
         }
         break;
      case CRIT:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTHIT2.getMessage(),
                  target.getName(), result.getDamage());
            messageToRoom = MessageFormat.format(TaMessageManager.HITOTH.getMessage(),
                  player.getName(), target.getName(), weapon);
            messageToVictim = MessageFormat.format(TaMessageManager.HITYOU.getMessage(),
                  player.getName(), weapon, result.getDamage());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTHTM2.getMessage(),
                  target.getName(), result.getDamage());
            messageToRoom = MessageFormat.format(TaMessageManager.HTMOTH.getMessage(),
                  player.getName(), target.getName(), weapon);
            messageToVictim = "";
         }
         break;
      case DODGE:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTDOG.getMessage(),
                  target.getName());
            messageToRoom = MessageFormat.format(TaMessageManager.DOGOTH.getMessage(),
                  target.getName(), player.getName(), player.getWeapon().getName());
            messageToVictim = MessageFormat.format(TaMessageManager.DOGYOU.getMessage(),
                  player.getName());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.MATDOG.getMessage(),
                  target.getName());
            messageToRoom = MessageFormat.format(TaMessageManager.DGMOTH.getMessage(),
                  target.getName(), player.getName(), player.getWeapon().getName());
            messageToVictim = "";
         }
         break;
      case GLANCE:
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTGLN.getMessage(),
                  target.getName());
            messageToRoom = MessageFormat.format(TaMessageManager.GLNOTH.getMessage(),
                  player.getName(), player.getWeapon().getName(), target.getName());
            messageToVictim = MessageFormat.format(TaMessageManager.GLNYOU.getMessage(),
                  player.getName(), player.getWeapon().getName());
         } else {
            messageToPlayer = MessageFormat.format(TaMessageManager.ATTGNM.getMessage(),
                  target.getName());
            messageToRoom = MessageFormat.format(TaMessageManager.GNMOTH.getMessage(),
                  player.getName(), player.getWeapon().getName(), target.getName());
            messageToVictim = "";
         }
         break;
      default :
         _log.error("Got unhandled melee result of " + result.getMeleeResultEnum());
         break;
      }

      player.print(messageToPlayer);
      player.getRoom().print(player, target, messageToRoom, false);
      target.print(messageToVictim);
   }

}
