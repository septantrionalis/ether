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

package org.tdod.ether.ta.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;

/**
 * A sysop command.
 *
 * @author Ron Kinney
 */
public abstract class SysopCommand extends Command {

   private static Log _log = LogFactory.getLog(SysopCommand.class);

   /**
    * Executes a sysop command.
    *
    * @param entity The entity issuing the sysop command.
    * @param input The input string.
    *
    * @return true if the command was valid.
    */
   public final boolean execute(Entity entity, String input) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return false;
      }
      Player player = (Player) entity;
      if (!player.isSysop()) {
         return false;
      }

      String lowerCaseInput = input.toLowerCase();

      Room room = player.getRoom();
      _log.info(player.getName()
            + "[Room #" + room.getRoomNumber() + ":" + room.getShortDescription() + "] issued a sysop command: "
            + lowerCaseInput);

      try {
         return executeSysopCommand(player, lowerCaseInput);
      } catch (Exception e) {
         displayHelp(player);
         return true;
      }
   }

   /**
    * Sysop commands can be used while paralyzed.
    *
    * @return true.
    */
   public boolean canUseParalyzed() {
      return true;
   }

   /**
    * Execute the sysop command.
    *
    * @param player The player issuing the sysop command.
    * @param input The input string.
    *
    * @return true if the command was valid.
    */
   public abstract boolean executeSysopCommand(Player player, String input);

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public abstract void displayHelp(Player player);
}
