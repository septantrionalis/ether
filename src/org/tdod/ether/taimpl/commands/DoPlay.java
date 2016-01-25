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
import org.tdod.ether.taimpl.commands.handler.HandleBones;
import org.tdod.ether.taimpl.commands.handler.HandleSlots;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.MessageManager;

/**
 * Handles the "play" command.
 *
 * @author Ron Kinney
 */
public class DoPlay extends Command {

   /**
    * The slots command suffix.
    */
   private static final String SLOTS = MessageManager.SLOTS_STRING.getMessage();

   /**
    * The bones command suffix.
    */
   private static final String BONES = MessageManager.BONES_STRING.getMessage();

   /**
    * The bones command suffix.
    */
   private static final String BONES2 = MessageManager.BONES_STRING2.getMessage();

   /**
    * Executes the "play" command.
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

      String parameter = split[1].toLowerCase();

      if (RoomFlags.TAVERN.isSet(room.getRoomFlags())) {
         if (SLOTS.equals(parameter)) {
            return HandleSlots.execute(player, parameter);
         } else if (BONES.equals(parameter)) {
            return HandleBones.execute(player, parameter);
         } else if (BONES2.equals(parameter)) {
            return HandleBones.execute(player, parameter);
         }
      }

      return false;
   }

}
