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
 * &WYou already have a light source.                 *
 *
 * > light water                                      *
 * &MYou can't light that!                            *
 *
 * > light asd                                        *
 * &MSorry, but you don't seem to have one.           *
 *
 * > drop to
 * &WYour torch just burned out.
 * ->torch disappears
 *
 * &WIt's too dark to see.
 * > light to
 * &WYour torch now provides you with light.
 * You're in a cave.
 * There is nobody here.
 * There is nothing on the floor.
 *
 * You're in a cave.
 * There is nobody here.
 * There is nothing on the floor.
 * > drop to
 * &WYour torch just burned out.
 * &WIt's too dark to see.
 */
public class DoLight extends Command {

   /**
    * Executes the "light" command.
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

      Room room = player.getRoom();

      String param = split[1].toLowerCase();
      Item item = player.getItem(param);

      // Item does not exist, or is not a piece of equipment.
      if (item == null || !(item instanceof Equipment)) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      Equipment eq = (Equipment) item;

      // Check if the player already has a light source.
      // TODO does this message get displayed if the player has a soulstone.
      for (Item itemInInventory : player.getInventory()) {
         if (itemInInventory.isActivated()) {
            player.print(TaMessageManager.ALRLIT.getMessage());
            return true;
         }
      }

      // Can only light item subtype of light.
      if (!eq.getEquipmentSubType().equals(EquipmentSubType.LIGHT)) {
         player.print(TaMessageManager.NOTLIT.getMessage());
         return true;
      }

      // Do it.
      eq.setEffectTimer((int) (eq.getMaxEquipmentEffect() * Dice.generateNumberVariance()));
      eq.setActivated(true);

      String messageToPlayer = MessageFormat.format(TaMessageManager.YOULIT.getMessage(), eq.getName());
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHLIT.getMessage(), player.getName(), eq.getName());

      player.print(messageToPlayer);
      room.printToNonGroup(player, messageToRoom);

      DoDefault.displayRoomDescription(player);

      return true;
   }

}
