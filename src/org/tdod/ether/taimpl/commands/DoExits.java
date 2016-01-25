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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.TaMessageManager;

/**
 * Exits: se.
 * Exits: n,ne,e,s,w,nw.
 *
 * @author Ron Kinney
 */
public class DoExits extends Command {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoExits.class);

   /**
    * Executes the "exits" command.
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

      Room room = player.getRoom();

      String[] split = input.split(" ");

      if (split.length > 1) {
         return false;
      }

      // TODO is the dark check before or after the param check?
      if (!room.isIlluminated()) {
         player.print(TaMessageManager.TOODRK.getMessage());
         return true;
      }

      ExitDirectionEnum[] exitOrder = {
            ExitDirectionEnum.NORTH, ExitDirectionEnum.NORTHEAST, ExitDirectionEnum.EAST, ExitDirectionEnum.SOUTHEAST,
            ExitDirectionEnum.SOUTH, ExitDirectionEnum.SOUTHWEST, ExitDirectionEnum.WEST, ExitDirectionEnum.NORTHWEST,
            ExitDirectionEnum.UP, ExitDirectionEnum.DOWN
      };

      StringBuffer buffer = new StringBuffer();
      buffer.append("Exits: ");
      for (ExitDirectionEnum exit : exitOrder) {
         if (room.getExit(exit) != null) {
            buffer.append(exit.getShortDescription() + ",");
         }
      }

      if (buffer.length() == 0) {
         _log.error("Well thats odd.  Room " + room.getRoomNumber() + " does not have any exits!");
         return false;
      }

      buffer.replace(buffer.length() - 1, buffer.length(), ".");
      player.println(buffer.toString());

      return true;
   }

}
