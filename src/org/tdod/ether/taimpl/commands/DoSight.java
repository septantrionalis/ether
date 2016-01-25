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
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * 1) &MSorry, warriors aren't very good at sighting targets.  *
 * 2) &MIt is too crowded here to sight targets.               *
 * 3) &MSorry, there's no exit in that direction.              *
 *
 * Can't sight into town.                                      *
 * -> &MSorry, there's no exit in that direction.              *
 *
 * sight wer                                                   *
 * -> does nothing.                                            *
 *
 * You can't see northeast.                                    *
 * -> locked doors                                             *
 *
 * &WMinex is looking to the northeast.
 * &WMinex is looking to the up.
 */
public class DoSight extends Command {

   /**
    * Maximum sight depth.
    */
   private static final int DEPTH = Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.SIGHT_DEPTH));

   /**
    * Executes the "sight" command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      String[] split = input.split(" ");
      if (split.length != 2) {
         return false;
      }

      if (!entity.getPlayerClass().equals(PlayerClass.ARCHER)) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.CNTSIT.getMessage(),
               entity.getPlayerClass().getName().toLowerCase() + "s");
         entity.print(messageToPlayer);
         return true;
      }

      if (GameUtil.isTown(entity.getRoom())) {
         entity.print(TaMessageManager.NOSITH.getMessage());
         return true;
      }

      ExitDirectionEnum exitDirection = ExitDirectionEnum.getCompleteExitKeyword(split[1]);

      if (exitDirection != null) {
         Exit exit = entity.getRoom().getExit(exitDirection);
         String messageToRoom = MessageFormat.format(TaMessageManager.INSOTH2.getMessage(),
               entity.getName(), exitDirection.toString().toLowerCase());
         entity.getRoom().print(entity, messageToRoom, false);

         // No exit found.
         if (exit == null) {
            entity.print(TaMessageManager.NOEXIT.getMessage());
            return true;
         }

         // Can't sight through a closed barrier.
         if (!exit.isPassable()) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.CNTSEE.getMessage(),
                  exitDirection.toString().toLowerCase());
            entity.print(messageToPlayer);
            return true;
         }

         Room room = WorldManager.getRoom(exit.getToRoom());

         // Can't sight into towns.
         if (GameUtil.isTown(room)) {
            entity.print(TaMessageManager.NOEXIT.getMessage());
            return true;
         }

         int depth = 0;
         while (exit != null) {
            // A Telearena quirk.  The player can sight into dark rooms if he is carrying a light.
            depth++;
            if (entity.hasLight() || !RoomFlags.DARK.isSet(entity.getRoom().getRoomFlags())) {
               DoDefault.displayRoomDescription(entity, room, true);
            } else {
               DoDefault.displayRoomDescription(entity, room, false);
            }
            if (depth >= DEPTH) {
               return true;
            }
            exit = room.getExit(exitDirection);

            if (exit != null) {
               room = WorldManager.getRoom(exit.getToRoom());
            }
         }
      }

      return true;
   }
}
