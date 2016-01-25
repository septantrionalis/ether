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
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * &MSorry, you are not the leader of a group.           *
 * &MSorry, you don't see "as" nearby.                   *
 * &MSorry, Limech is not a member of your group.        *
 * &MSorry, you may not remove yourself from your group. *
 *
 * &WLimech is no longer a member of your group.
 * &WYou are no longer a member of Minex's group.
 * &W%s has just been kicked out of %s's group.
 */
public class DoRemove extends Command {

   /**
    * Executes the "remove" command.
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

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }
      String param = split[1].toLowerCase();
      Room room = player.getRoom();

      Player playerToRemove = room.getPlayerInRoom(param);

      // The player is not a group leader.
      if (!player.getGroupLeader().equals(player)) {
         player.print(TaMessageManager.NOGRLD.getMessage());
         return true;
      }

      // No player found.
      if (playerToRemove == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), param);
         player.print(message);
         return true;
      }

      // The player is already group leader.
      if (player.equals(playerToRemove)) {
         player.print(TaMessageManager.NRGSLF.getMessage());
         return true;
      }

      // The player is not in the group.
      if (!player.getGroupLeader().getGroupList().contains(playerToRemove)) {
         String message = MessageFormat.format(TaMessageManager.WRNGRP.getMessage(), playerToRemove.getName());
         player.print(message);
         return true;
      }

      // Remove the player.
      playerToRemove.setGroupLeader(playerToRemove);
      player.getGroupLeader().getGroupList().remove(playerToRemove);

      // Send the messages;
      String messageToPlayer = MessageFormat.format(TaMessageManager.REMGRP.getMessage(), playerToRemove.getName());
      String messageToTarget = MessageFormat.format(TaMessageManager.LEVGRP.getMessage(), player.getName());
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHRMG.getMessage(),
            playerToRemove.getName(), player.getName());

      player.print(messageToPlayer);
      playerToRemove.print(messageToTarget);
      room.print(player, playerToRemove, messageToRoom, false);

      return true;
   }

}
