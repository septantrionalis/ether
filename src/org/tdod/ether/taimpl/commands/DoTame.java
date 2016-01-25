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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Places a mob into the hunters group.
 *
 * 7 second rest time.
 *
 * &MSorry, warriors aren't very good at taming monsters.                              *
 * Town:
 * &MThere aren't any monsters here suitable for taming.    (N) (No message to room)   *
 *
 * Tame player or not found :
 * &MThere aren't any monsters here suitable for taming.    (R) (No message to room)   *
 *
 * &MYou have already tamed the cave bear.                  (R) (Message to room)      *
 *
 * &MYour attempt to tame the lizard man has failed.        (R)                        *
 * &YYou have successfully tamed the cave bear!             (R)                        *
 * Minex is attempting to tame the cave bear.                                          *
 *
 * Mob gets put in the group.
 */
public class DoTame extends Command {

   /**
    * Minimum rest value.
    */
   private static final int TAME_REST_MIN =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TAME_REST_MIN));

   /**
    * Maximum rest value.
    */
   private static final int TAME_REST_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TAME_REST_MAX));

   /**
    * Base success chance.
    */
   private static final int TAME_SKILL_CHANCE =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TAME_SKILL_CHANCE));

   /**
    * Improved success chance per level.
    */
   private static final int TAME_SKILL_PER_LEVEL =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TAME_SKILL_PER_LEVEL));

   /**
    * Minimum tame chance.
    */
   private static final int TAME_CHANCE_MIN =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TAME_CHANCE_MIN));

   /**
    * Maximum rest value.
    */
   private static final int TAME_CHANCE_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TAME_CHANCE_MAX));

   /**
    * Executes the "tame" command.
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

      String[] split = input.split(" ", 2);

      if (split.length < 2) {
         return false;
      }

      // Sorry, warriors aren't very good at taming monsters.
      if (!player.getPlayerClass().equals(PlayerClass.HUNTER)) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.CNTTAM.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // You are still physically exhausted from your previous activities!
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // There aren't any monsters here suitable for taming.
      if (GameUtil.isTown(player.getRoom())) {
         player.print(TaMessageManager.NOTAMH.getMessage());
         return true;
      }
      Entity target = GameUtil.getTarget(player, player.getRoom(), split[1]);
      setRestPeriod(player);
      if (target == null || target.getEntityType().equals(EntityType.PLAYER)) {
         player.print(TaMessageManager.NOTAMH.getMessage());
         return true;
      }

      Mob mob = (Mob) target;

      // Minex is attempting to tame the cave bear.
      String messageToRoom = MessageFormat.format(TaMessageManager.TAMING.getMessage(), player.getName(), mob.getName());
      player.getRoom().print(player, messageToRoom, false);

      // You have already tamed the cave bear.
      if (mob.getTamedBy() != null && mob.getTamedBy().equals(player)) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.ALRTAM.getMessage(), mob.getName());
         player.print(messageToPlayer);
         return true;
      }

      // Your attempt to tame the lizard man has failed.
      if (!success(player, mob)) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.DNTTAM.getMessage(), mob.getName());
         player.print(messageToPlayer);
         return true;
      }

      String messageToPlayer = MessageFormat.format(TaMessageManager.TAMMON.getMessage(), mob.getName());
      player.print(messageToPlayer);

      mob.setTamedBy(player);

      return true;
   }

   /**
    * Sets the rest period for the tame command.
    *
    * @param player The player issuing the command.
    */
   private void setRestPeriod(Player player) {
      int restPeriod = Dice.roll(TAME_REST_MIN, TAME_REST_MAX);
      player.setRestTicker(restPeriod);
   }

   /**
    * Determins if the tame command was successful.
    *
    * @param player The player issuing the tame command.
    * @param entity The target entity.
    *
    * @return true if the tame command was successful.
    */
   private boolean success(Player player, Entity entity) {
      int levelMod = (player.getLevel() - entity.getLevel()) * TAME_SKILL_PER_LEVEL;
      int chance = TAME_SKILL_CHANCE + levelMod;
      if (chance < TAME_CHANCE_MIN) {
         chance = TAME_CHANCE_MIN;
      } else if (chance > TAME_CHANCE_MAX) {
         chance = TAME_CHANCE_MAX;
      }
      int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
      if (roll < chance) {
         return true;
      }

      return false;
   }

}
