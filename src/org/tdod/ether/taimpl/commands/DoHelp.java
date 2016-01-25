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
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;

/**
 * &MSorry, that is not an appropriate topic.
 * Theres a space before and after help entries.
 */
public class DoHelp extends Command {

   private static final String HELP_NOT_FOUND = "INVTOP";

   /**
    * Executes the "help" command.
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

      // This is key to filtering help messages.  Any help messages in CAPS cannot be viewed
      // by the player.
      String[] parsed = input.toLowerCase().split(" ", 2);

      if (parsed.length < 2) {
         String help = WorldManager.getHelp("default");
         player.print(help);
         return true;
      }

      String param = parsed[1];
      String help = WorldManager.getHelp(param);
      if (help != null) {
         player.print(help);
         return true;
      } else {
         player.print(WorldManager.getHelp(HELP_NOT_FOUND));
         return true;
      }
   }

   /**
    * Determines if this command can be used when the player is paralysed.
    *
    * @return true if the command can be used while paralyzed.
    */
   public final boolean canUseParalyzed() {
      return true;
   }

}
