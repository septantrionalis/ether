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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.commands.handler.HandleBones;
import org.tdod.ether.taimpl.commands.handler.HandleSlots;
import org.tdod.ether.util.GameUtil;

/**
 * Displays basic game system information.
 *
 * @author Ron Kinney
 */
public class DoMemory extends SysopCommand {

   /**
    * Executes the "memory" command.
    *
    * @param player The player issuing the command.
    * @param input The player input.
    *
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ");
      if (split.length != 1) {
         return false;
      }

      player.println("&CSlots Global Winnings: &W" + HandleSlots.getGlobalWinnings());
      player.println("&CBones Global Winnings: &W" + HandleBones.getGlobalWinnings());

      Runtime runtime = Runtime.getRuntime();
      long usedMemory = runtime.totalMemory() - runtime.freeMemory();

      player.println("&CUsed Memory: &W" + usedMemory);
      player.println("&CTotal Rooms: &W" + WorldManager.getArea().getRoomMap().size());
      player.println("&CTotal Mobs: &W" + WorldManager.getMobsInExistance().size());
      player.println("&CTotal Items: &W" + WorldManager.getItemsInExistance().size());

      displayUptime(player);
      return true;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: memory");
   }

   /**
    * Displays the server uptime.
    * @param player the player issuing the command.
    */
   private void displayUptime(Player player) {
      Calendar cal1 = new GregorianCalendar();
      Calendar cal2 = new GregorianCalendar();
      cal1.setTime(GameUtil.getServerUptime());
      cal2.setTime(new Date(System.currentTimeMillis()));
      long milis1 = cal1.getTimeInMillis();
      long milis2 = cal2.getTimeInMillis();
      long diff = milis2 - milis1;
      long seconds = (diff / 1000) % 60;
      long minutes = (diff / (60 * 1000)) % 60;
      long diffHours = (diff / (60 * 60 * 1000)) % 24;
      long diffDays = diff / (24 * 60 * 60 * 1000);

      player.println("Uptime: "
            + diffDays + " days, "
            + diffHours + " hours, "
            + minutes + " minutes, "
            + seconds + " seconds");
   }
}
