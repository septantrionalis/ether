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
 * Stuns a mob.
 *
 * Rest = 7 seconds
 * MS = 20 seconds, 28 seconds
 *
 * &MSorry, warriors aren't very good at mesmerizing monsters.       *
 *
 * &MYour attempt to mesmerize the cave bear has failed.             *
 * &YYou have successfully mesmerized the cave bear!                 *
 *
 * Minex is attempting to mesmerize the cave bear.                   *
 *
 * Mesmerize in town:
 * &MThere aren't any monsters here suitable for mesmerizing.        *
 * -> no room message
 * -> no rest
 *
 * Mesmerize player:
 * &MThere aren't any monsters here suitable for mesmerizing.        *
 * -> no room message
 * -> still rest
 */
public class DoMesmerize extends Command {

   /**
    * Minimum rest period.
    */
   private static final int MESMERIZE_REST_MIN =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_REST_MIN));

   /**
    * Maximum rest period.
    */
   private static final int MESMERIZE_REST_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_REST_MAX));

   /**
    * Base skill chance.
    */
   private static final int MESMERIZE_SKILL_CHANCE =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_SKILL_CHANCE));

   /**
    * Success chance per level.
    */
   private static final int MESMERIZE_SKILL_PER_LEVEL =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_SKILL_PER_LEVEL));

   /**
    * Minimum success chance.
    */
   private static final int MESMERIZE_CHANCE_MIN =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_CHANCE_MIN));

   /**
    * Maximum success chance.
    */
   private static final int MESMERIZE_CHANCE_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_CHANCE_MAX));

   /**
    * Amount of turns that the mesmerized mob will remain mesmerized.
    */
   private static final int MESMERIZE_TURN_SKIP =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MESMERIZE_TURN_SKIP));

   /**
    * Executes the "mesmerize" command.
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

      // Sorry, warriors aren't very good at mesmerizing monsters.
      if (!player.getPlayerClass().equals(PlayerClass.HUNTER)) {
         String messageToPlayer =
            MessageFormat.format(TaMessageManager.CNTMSM.getMessage(), player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // You are still physically exhausted from your previous activities!
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // There aren't any monsters here suitable for mesmerizing.
      if (GameUtil.isTown(player.getRoom())) {
         player.print(TaMessageManager.NOMSMH.getMessage());
         return true;
      }
      Entity target = GameUtil.getTarget(player, player.getRoom(), split[1]);
      setRestPeriod(player);
      if (target == null || target.getEntityType().equals(EntityType.PLAYER)) {
         player.print(TaMessageManager.NOMSMH.getMessage());
         return true;
      }

      Mob mob = (Mob) target;

      // Minex is attempting to mesmerize the cave bear.
      String messageToRoom = MessageFormat.format(TaMessageManager.MSMING.getMessage(), player.getName(), mob.getName());
      player.getRoom().print(player, messageToRoom, false);

      // Your attempt to mesmerize the cave bear has failed.
      if (!success(player, mob)) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.DNTMSM.getMessage(), mob.getName());
         player.print(messageToPlayer);
         return true;
      }

      String messageToPlayer = MessageFormat.format(TaMessageManager.MSMMON.getMessage(), mob.getName());
      player.print(messageToPlayer);

      mob.setMesmerizedTicker(MESMERIZE_TURN_SKIP);

      return true;
   }

   /**
    * Determines if the mesmerize command was succesful on an entity.
    *
    * @param player The player attempting to mesmerize.
    * @param entity The target entity.
    *
    * @return true if the skill was successful.
    */
   private boolean success(Player player, Entity entity) {
      int levelMod = (player.getLevel() - entity.getLevel()) * MESMERIZE_SKILL_PER_LEVEL;
      int chance = MESMERIZE_SKILL_CHANCE + levelMod;
      if (chance < MESMERIZE_CHANCE_MIN) {
         chance = MESMERIZE_CHANCE_MIN;
      } else if (chance > MESMERIZE_CHANCE_MAX) {
         chance = MESMERIZE_CHANCE_MAX;
      }
      int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);
      if (roll < chance) {
         return true;
      }
      return false;
   }

   /**
    * The rest period for using the mesmerize command.
    *
    * @param player The player using the command.
    */
   private void setRestPeriod(Player player) {
      int restPeriod = Dice.roll(MESMERIZE_REST_MIN, MESMERIZE_REST_MAX);
      player.setRestTicker(restPeriod);
   }
}
