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
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles a default command.
 *
 * @author Ron Kinney
 */
public class DoDefault extends Command {

   /**
    * Executes the a default command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      displayRoomDescription(entity);

      return true;
   }

   /**
    * Displays the room description to the entity.
    *
    * @param entity The entity to display the room description to.
    */
   public static void displayRoomDescription(Entity entity) {
      Room room = entity.getRoom();
      displayRoomDescription(entity, room, false);
   }

   /**
    * Displays the room description to an entity, taking darkness into consideration.
    *
    * @param entity The entity to display the room description to.
    * @param room The room to display.
    * @param ignoreDark True if darkness is ignored.
    */
   public static void displayRoomDescription(Entity entity, Room room, boolean ignoreDark) {
      String description;

      if (room.isIlluminated() || ignoreDark) {
         description = room.getDefaultRoomString(entity);
      } else {
         description = TaMessageManager.TOODRK.getMessage();
      }

      entity.print(description);
   }
}
