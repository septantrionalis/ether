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
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.GameUtil;

/**
 * Disconnects a player based on the pnum.
 * @author rkinney
 */
public class DoDisconnect extends SysopCommand {

   /**
    * Executes the "disconnect" command.
    * @param player The player issuing the command.
    * @param input The player input.
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      try {
         String[] split = input.split(" ", 2);

         if (split.length < 2) {
            displayHelp(player);
            return true;
         }

         int pnum = Integer.valueOf(split[1]);
         Player target;
         try {
            target = WorldManager.getPlayers().get(pnum).getPlayer();
         } catch (Exception e) {
            player.println("PNUM not found.");
            return true;
         }

         GameUtil.disconnectPlayer(target);
      } catch (Exception e) {
         displayHelp(player);
      }
      return true;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: disconnect player#");
   }

}
