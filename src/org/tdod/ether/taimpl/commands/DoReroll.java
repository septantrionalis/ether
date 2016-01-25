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

import java.text.MessageFormat;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "reroll" command.
 *
 * @author Ron Kinney
 */
public class DoReroll extends Command {

   /**
    * Minimum level that the player can perform the reroll command.
    */
   private static final int MIN_REROLL_LEVEL =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.MIN_REROLL_LEVEL)).intValue();

   /**
    * The room that the player is teleported to when the reroll command is issued.
    */
   private static final int REROLL_ROOM_NUMBER =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.STARTING_ROOM)).intValue();

   /**
    * Executes the "reroll" command.
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

      if (input.split(" ").length > 1) {
         return false;
      }

      if (player.getLevel() > MIN_REROLL_LEVEL) {
         return false;
      }

      player.rerollStats();

      DoStatus command = new DoStatus();
      command.execute(player, input);

      if (player.getRoom().getRoomNumber() != REROLL_ROOM_NUMBER) {
         String messageToRoom = MessageFormat.format(TaMessageManager.EXT12.getMessage(), player.getName());
         player.getRoom().print(player, messageToRoom, false);
      }

      player.teleportToRoom(REROLL_ROOM_NUMBER);

      return true;
   }

}
