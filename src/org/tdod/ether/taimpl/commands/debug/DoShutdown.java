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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.util.GameUtil;

/**
 * Shuts the game down.
 *
 * @author Ron Kinney
 */
public class DoShutdown extends SysopCommand {

   private static Log _log = LogFactory.getLog(DoShutdown.class);

   private static ShutdownTimer _timer;

   /**
    * Executes the "shutdown" command.
    *
    * @param player The player issuing the command.
    * @param input The player input.
    *
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ");

      if (split.length != 2) {
         displayHelp(player);
         return true;
      }

      if (split[1].toLowerCase().equals("cancel")) {
         if (_timer != null) {
            _timer.cancel();
            ArrayList<PlayerConnection> players = WorldManager.getPlayers();
            for (PlayerConnection playerConn : players) {
               playerConn.getPlayer().println("*****Server shutdown cancelled!");
            }
         }

         return true;
      }

      int delay = Integer.valueOf(split[1]);

      ArrayList<PlayerConnection> players = WorldManager.getPlayers();
      for (PlayerConnection playerConn : players) {
         playerConn.getPlayer().println(
               "*****Server shutdown in " + delay + " seconds!!!");
      }

      synchronized (this) {
         if (_timer != null) {
            _timer.cancel();
         }

         _timer = new ShutdownTimer();
         _timer.setDelay(delay);
         new Thread(_timer).start();
      }

      return true;
   }

   /**
    * The shutdown timer.  This allows the sysop to delay shutdown for a certain number of seconds.
    *
    * @author Ron Kinney
    */
   private static class ShutdownTimer implements Runnable {

      private int     _secondDelay;
      private boolean _cancel = false;

      /**
       * Creates a new ShutdownTimer.
       */
      public ShutdownTimer() {
      }

      /**
       * Sets the server shutdown delay.
       *
       * @param secondDelay the number of seconds to delay shutdown.
       */
      public void setDelay(int secondDelay) {
         _secondDelay = secondDelay;
      }

      /**
       * Cancels the server shutdown.
       */
      public void cancel() {
         _cancel = true;
         _log.info("Canceling shutdown timer " + _secondDelay);
      }

      /**
       * Shuts the server down, if the process has not been been cancelled.
       */
      public void run() {
         _log.info("Shutting server down in " + _secondDelay + " seconds!");

         try {
            Thread.sleep(_secondDelay * GameUtil.TIME_DIVISION);
         } catch (Exception e) {
            _log.fatal(e);
         }

         if (!_cancel) {
            GameUtil.savePlayers();
            WorldManager.getGameEngine().shutdown();
         } else {
            _log.info("Shutdown timer " + _secondDelay + " expired...");
         }

      }

   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: shutdown [delay in seconds]|shutdown cancel");
   }

}
