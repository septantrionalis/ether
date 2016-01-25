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
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.TaMessageManager;

/**
 * eat at
 * &MSorry, but you don't seem to have one.
 * &CYou feel much better after eating a meal.
 * &WLimech just ate a ration of food.
 * &MYou can't eat that!
 */
public class DoEat extends Command {

   /**
    * Executes the "eat" command.
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
      Room room = entity.getRoom();

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }
      String param = split[1].toLowerCase();

      Item item = player.getItem(param);

      // Item does not exist, or is not a piece of equipment.
      if (item == null || !(item instanceof Equipment)) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      Equipment eq = (Equipment) item;

      // Can only drink of item type water.
      if (!eq.getEquipmentSubType().equals(EquipmentSubType.FOOD)) {
         player.print(TaMessageManager.NOTEAT.getMessage());
         return true;
      }

      // Success...
      player.addHungerTicker((int) (eq.getMaxEquipmentEffect() * Dice.generateNumberVariance()));

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUATE.getMessage(), eq.getName());
      player.print(messageToPlayer);
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHATE.getMessage(), player.getName(),
            eq.getLongDescription());
      room.print(player, messageToRoom, false);

      player.removeItemFromInventory(eq);
      eq.destroy();
      eq = null;
      return true;
   }

}
