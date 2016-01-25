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

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.util.GameUtil;

/**
 * Displays connection information for all players in the game.
 *
 * @author Ron Kinney
 */
public class DoUsers extends SysopCommand {

   /**
    * Executes the "users" command.
    *
    * @param player The player issuing the command.
    * @param input The player input.
    *
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      if (input.split(" ").length > 1) {
         return false;
      }

      ArrayList<PlayerConnection> players = WorldManager.getPlayers();

      player.println("PNUM  Port   State    Idle    NAME@HostIP");
      player.println("-----------------------------------------------");

      for (PlayerConnection playerOnline : players) {

         int pnum = players.indexOf(playerOnline);
         int port = playerOnline.getShell().getConnection().getConnectionData().getPort();
         PlayerStateEnum state = playerOnline.getPlayer().getState();
         String name = getName(playerOnline);
         String ip = playerOnline.getShell().getConnection().getConnectionData().getHostName();
         long idle = getIdle(playerOnline);

         StringBuilder sb = new StringBuilder();
         Formatter formatter = new Formatter(sb, Locale.US);
         formatter.format("%03d   %-4s  %-7s  %-6d  %s@%s", pnum, port, state, idle, name, ip);
         player.println(sb.toString());
      }

      return true;
   }

   /**
    * Gets the name of the player.  This method is needed because there can be connection information for a player that
    * has not entered a name yet.  In this case, "unknown" will be returned.
    *
    * @param playerConn The player connection object.
    *
    * @return the players name, or unknown if one has not been defined yet.
    */
   private String getName(PlayerConnection playerConn) {
      String name = playerConn.getPlayer().getName();
      if (name == null) {
         return "Unknown";
      }

      return name;
   }

   /**
    * Calculates the amount of time a player has been idle.
    *
    * @param playerConn The player connection object.
    *
    * @return the idle time of a player.
    */
   private long getIdle(PlayerConnection playerConn) {
      long lastActivity = playerConn.getShell().getConnection().getConnectionData().getLastActivity();
      long currentTime = System.currentTimeMillis();
      long secondsIdle = (currentTime - lastActivity) / GameUtil.TIME_DIVISION;

      return secondsIdle;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: users");
   }

}
