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

package org.tdod.ether.taimpl.commands.sysop;

import java.text.MessageFormat;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * Summons a mob into a specific room.
 * @author rkinney
 */
public class DoSummon extends SysopCommand {

   /**
    * Executes the sysop command.
    * @param player the player issuing the command.
    * @param input the player input.
    * @return true if the command was successful.
    */
   public boolean executeSysopCommand(Player player, String input) {
      try {
         String[] split = input.split(" ", 2);
         String[] params = split[1].split(" ");
         int mobNumber = new Integer(params[0]).intValue();
         int roomNumber = new Integer(params[1]).intValue();

         Mob mob = WorldManager.getMob(mobNumber);
         if (mob == null) {
            player.println("Mob does not exist.");
            return true;
         }

         Room room = WorldManager.getRoom(roomNumber);
         if (room == null) {
            player.println("Room does not exist.");
            return true;
         }
         Mob clonedMob = mob.clone(room);

         MoveFailCode code = room.placeMob(clonedMob);

         if (code.equals(MoveFailCode.ROOM_FULL)) {
            player.println("Room Full.");
            clonedMob.destroy();
            clonedMob = null;
            return true;
         }

         String messageToRoom = MessageFormat.format(TaMessageManager.MONSUM.getMessage(), mob.getPrefix(), mob.getName());
         player.getRoom().print(null, messageToRoom, false);

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
      player.println("Syntax: summon # room# - Summon mob# to room#");
   }

}
