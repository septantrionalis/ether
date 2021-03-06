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
import org.tdod.ether.ta.player.PlayerConnection;

/**
 * The global chat command.
 * @author rkinney
 */
public class DoGlobalChat extends Command {

   private static final int TIME_THRESHOLD = 2500;
   private static final int COUNT_THRESHOLD = 5;

   /**
    * Executes the "global chat" command.
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

      String[] output = input.split(" ", 2);

      if (output.length < 2) {
         return false;
      }

      long lastChatTime = System.currentTimeMillis() - player.getLastGlobalChat();

      // The player chatted awhile ago, reset the count.
      if (lastChatTime > TIME_THRESHOLD) {
         player.setGlobalChatCount(0);
      }

      // Spam.
      if (lastChatTime <= TIME_THRESHOLD && player.getGlobalChatCount() >= COUNT_THRESHOLD) {
         player.println("Put a lid on it!!");
         return true;
      }

      // Set chat variables.
      player.setGlobalChatCount(player.getGlobalChatCount() + 1);
      player.setLastGlobalChat(System.currentTimeMillis());

      // Send the message out.
      for (PlayerConnection playerConnection : WorldManager.getPlayers()) {
         String messageToRoom = "From " + player.getName() + " (Global): " + output[1];
         playerConnection.getPlayer().println(messageToRoom);
      }

      return true;
   }

}
