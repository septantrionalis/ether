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
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * level 1
 * FOUND     : 1111              4
 * NOT FOUND : 1111111111111111  16
 * 20%
 *
 * level 10
 * FOUND     : 111111111111      12
 * NOT FOUND : 11111111          8
 * 60%
 *
 * Start of 20% + 4% per level.
 *
 * Rest times = 25 to 30
 * stats/level do not affect rest rate
 *
 * &MSorry, warriors aren't very good at hunting.           *
 * &MThe only animals around here are rats.                 *
 *
 * &MYou can't carry anything else.
 * &YYour hunt is fruitful and you gain a ration of food.
 * ->a ration of food
 *
 * &MYou can't seem to find any food.
 *
 * Minex is searching for something to eat.
 */
public class DoHunt extends Command {

   /**
    * Minimum value for determing if the hunt command succeeds.
    */
   private static final int HUNTER_SKILL_START =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.HUNTER_SKILL_START));

   /**
    * Maximum value for determing if the hunt command succeeds.
    */
   private static final int HUNTER_SKILL_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.HUNTER_SKILL_MAX));

   /**
    * Skill point per level.
    */
   private static final int HUNTER_SKILL_PER_LEVEL =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.HUNTER_SKILL_PER_LEVEL));

   /**
    * Minimum rest period.
    */
   private static final int HUNT_REST_MIN =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.HUNT_REST_MIN));

   /**
    * Maximum rest period.
    */
   private static final int HUNT_REST_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.HUNT_REST_MAX));

   /**
    * The item to conjure on a successful hunt.
    */
   private static final int HUNT_CONJURE_VNUM =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.HUNT_CONJURE_VNUM));

   /**
    * Executes the "hunt" command.
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

      if (split.length > 1) {
         return false;
      }

      // Sorry, warriors aren't very good at hunting.
      if (!player.getPlayerClass().equals(PlayerClass.HUNTER)) {
         String messageToPlayer =
            MessageFormat.format(TaMessageManager.CNTHNT.getMessage(), player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // You are still physically exhausted from your previous activities!
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // The only animals around here are rats.
      if (GameUtil.isTown(player.getRoom())) {
         player.print(TaMessageManager.NOHNTH.getMessage());
         return true;
      }

      int restPeriod = Dice.roll(HUNT_REST_MIN, HUNT_REST_MAX);
      player.setRestTicker(restPeriod);

      String messageToRoom = MessageFormat.format(TaMessageManager.HNTING.getMessage(), player.getName());
      player.getRoom().printToNonGroup(player, messageToRoom);

      if (!success(player)) {
         player.print(TaMessageManager.DNTFND.getMessage());
         return true;
      }

      Equipment clonedRation = WorldManager.getEquipment(HUNT_CONJURE_VNUM);
      InventoryFailCode code = player.placeItemInInventory(clonedRation.clone("Hunted by " + player.getName()), false);

      // You can't carry anything else.
      if (!code.equals(InventoryFailCode.NONE)) {
         clonedRation.destroy();
         player.print(TaMessageManager.INVFUL.getMessage());
         return true;
      }

      // Your hunt is fruitful and you gain a ration of food.
      player.print(TaMessageManager.FNDRAT.getMessage());

      return true;
   }

   /**
    * Determines if the hunt was successful.
    *
    * @param player The player issuing the hunt command.
    *
    * @return true if the hunt succeeded.
    */
   private boolean success(Player player) {
      int chance = HUNTER_SKILL_START + (player.getLevel() * HUNTER_SKILL_PER_LEVEL);
      if (chance > HUNTER_SKILL_MAX) {
         chance = HUNTER_SKILL_MAX;
      }
      int roll = Dice.roll(Dice.MIN_PERCENTAGE, Dice.MAX_PERCENTAGE);

      if (roll < chance) {
         return true;
      }

      return false;
   }
}
