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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.commands.handler.HandleItemShop;
import org.tdod.ether.taimpl.commands.handler.HandlePassage;
import org.tdod.ether.taimpl.commands.handler.HandlePromotion;
import org.tdod.ether.taimpl.commands.handler.HandleSpellShop;
import org.tdod.ether.taimpl.commands.handler.HandleTavern;
import org.tdod.ether.taimpl.commands.handler.HandleTemple;
import org.tdod.ether.taimpl.commands.handler.HandleTraining;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.MessageManager;

/**
 * Handles the "buy" command.
 *
 * @author Ron Kinney
 */
public class DoBuy extends Command {

   /**
    * The training string input suffix.  I.E., b training.
    */
   private static final String TRAINING = MessageManager.TRAINING_STRING.getMessage();

   /**
    * The training string input suffix.  I.E., b training.
    */
   private static final String PROMOTION = MessageManager.PROMOTION_STRING.getMessage();

   /**
    * Executes the "buy" command.
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

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }

      if (RoomFlags.EQUIPMENT_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.WEAPON_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.ARMOR_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.MAGIC_SHOP.isSet(room.getRoomFlags())) {
         return HandleItemShop.execute(player, split[1]);
      } else if (RoomFlags.GUILD_HALL.isSet(room.getRoomFlags())) {
         if (split.length == 2 && split[1].toLowerCase().equals(TRAINING)) {
            return HandleTraining.execute(player);
         } else if (split.length == 2 && split[1].toLowerCase().equals(PROMOTION)) {
            return HandlePromotion.execute(player);
         } else {
            return HandleSpellShop.execute(player, split[1]);
         }
      } else if (RoomFlags.TEMPLE.isSet(room.getRoomFlags())) {
         return HandleTemple.execute(player, split[1]);
      } else if (RoomFlags.TAVERN.isSet(room.getRoomFlags())) {
         return HandleTavern.execute(player, split[1]);
      } else if (RoomFlags.INN.isSet(room.getRoomFlags())) {
         return HandleTavern.execute(player, split[1]);
      } else if (RoomFlags.DOCKS.isSet(room.getRoomFlags())) {
         return HandlePassage.execute(player, split[1]);
      }

      return false;
   }
}
