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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * You are carrying 8 gold crowns.
 * You are carrying 8 gold crowns, and a staff.
 * You are carrying 8 gold crowns, a staff, and a staff.
 */
public class DoInventory extends Command {

   /**
    * Executes the "inventory" command.
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

      if (input.split(" ").length > 1) {
         return false;
      }

      ArrayList<Item> inventory = player.getInventory();

      StringBuffer buffer = new StringBuffer();
      String goldString = MessageFormat.format(TaMessageManager.INVDSP.getMessage(), player.getGold());
      buffer.append(goldString);
      if (inventory.size() == 0) {
         buffer.append(".");
      } else {
         int size = 0;
         for (Item item : inventory) {
            size++;
            if (size == inventory.size()) {
               buffer.append(", and " + item.getLongDescriptionWithCharges());
            } else {
               buffer.append(", " + item.getLongDescriptionWithCharges());
            }
         }

         buffer.append(".");
      }

      player.println(buffer.toString());
      String messageToRoom = MessageFormat.format(TaMessageManager.TAKINV.getMessage(),
            player.getName(), player.getGender().getPronoun().toLowerCase(), player.getGender().getPronoun().toLowerCase());
      player.getRoom().print(player, messageToRoom, false);

      return true;
   }

}
