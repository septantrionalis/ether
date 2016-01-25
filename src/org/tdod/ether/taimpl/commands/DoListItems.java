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
import java.util.Formatter;
import java.util.Locale;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "list items" command.
 *
 * @author Ron Kinney
 */
public class DoListItems extends Command {

   /**
    * Executes the "list items" command.
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

      if (input.split(" ").length > 2) {
         return false;
      }
      Room room = player.getRoom();

      if (RoomFlags.EQUIPMENT_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.WEAPON_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.ARMOR_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.MAGIC_SHOP.isSet(room.getRoomFlags())) {
         ArrayList<Item> itemList = room.getShopItem();
         player.println("&W+======================+=======+");
         player.println("&W| &CItem&W                 | &CPrice&W |");
         player.println("&W+----------------------+-------+");

         for (Item item : itemList) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb, Locale.US);
            formatter.format("&W| &C%-20s&W | &C%5d&W |", item.getName(), item.getCost());
            player.println(sb.toString());
         }

         player.println("&W+----------------------+-------+");

         String messageToRoom = MessageFormat.format(TaMessageManager.SGNOTH.getMessage(),
               player.getName());
         room.print(player, messageToRoom, false);
         return true;
      } else {
         return false;
      }
   }

}
