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

package org.tdod.ether.taimpl.commands.debug;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.player.state.PlayingState;
import org.tdod.ether.util.MyStringUtil;

/**
 * This is an experimental command.  It forces a mob to do a command specified
 * by the sysop.  The sysop can essentially force a mob to do just about anything
 * that a player can do.  Most of this functionality has not been tested and
 * individual command mechanics can be unpredictable.
 *
 * Note: some commands check to see if a mob is executing the command return
 * immediately.
 *
 * @author Ron Kinney
 */
public class DoForce extends SysopCommand {

   /**
    * Executes the "force" command.
    *
    * @param player The player issuing the command.
    * @param input The player input.
    *
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ", THREE_PARAMS);
      if (split.length < THREE_PARAMS) {
         displayHelp(player);
         return true;
      }

      String mobName = split[1];
      String commandString = split[2].trim().toLowerCase();

      Mob mob = null;
      for (Mob mobInWorld : WorldManager.getMobsInExistance()) {
         if (MyStringUtil.contains(mobInWorld.getName(), mobName)) {
            mob = mobInWorld;
            break;
         }
      }

      if (mob == null) {
         player.println("Mob not found.");
         return true;
      }

      new PlayingState().doCommand(mob, commandString);

      return true;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: force mob command");
   }

}
