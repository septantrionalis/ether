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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.enums.TriggerResult;
import org.tdod.ether.util.TaMessageManager;

/**
 * Teleports a player to a room.
 * @author rkinney
 */
public class DoTeleport extends SysopCommand {

   /**
    * Executes the sysop command.
    * @param player the player issuing the command.
    * @param input the player input.
    * @return true if the command was successful.
    */
   public boolean executeSysopCommand(Player player, String input) {
      try {
         String[] split = input.split(" ");

         Entity target = WorldManager.getPlayers().get(Integer.valueOf(split[1])).getPlayer();
         if (target == null) {
            player.println("Player not found.");
            return true;
         }
         int roomVnum = Integer.valueOf(split[2]);
         Room toRoom = WorldManager.getRoom(roomVnum);
         if (toRoom == null) {
            player.println("Room not found.");
            return true;
         }

         Room fromRoom = target.getRoom();

         target.print(TaMessageManager.OOP1.getMessage());
         String messageFromRoom = MessageFormat.format(TaMessageManager.EXT11.getMessage(), target.getName());
         fromRoom.print(target, messageFromRoom, false);
         String messageToRoom = MessageFormat.format(TaMessageManager.ENT11.getMessage(), target.getName());
         toRoom.print(target, messageToRoom, false);

         TriggerResult result = target.teleportToRoom(roomVnum);
         if (result.equals(TriggerResult.DEATH)) {
            WorldManager.getGameMechanics().handlePlayerDeath((Player) target, TaMessageManager.YOUDED1.getMessage());
         }

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
      player.println("Syntax: teleport pl# room# #(0-2)");
   }

}
