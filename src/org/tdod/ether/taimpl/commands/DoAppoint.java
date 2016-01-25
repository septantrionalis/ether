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
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * PLAYER: &MOk, you've appointed Limech as the new leader of your group!
 * TARGET: &WMinex has just appointed you as the new leader of the group.
 * ROOM:   &WLimech has just appointed Minex as the new group leader.
 *
 * &MSorry, you are not the leader of a group.                                *
 * &MSorry, you don't see "asd" nearby.                                       *
 * &MSorry, you are already the leader of your group.                         *
 * &MSorry, Glenda the Good Witch is not a member of your group.              *
 */
public class DoAppoint extends Command {

   /**
    * Executes the "appoint" command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }
      String param = split[1].toLowerCase();

      Room room = entity.getRoom();
      Player playerToAppoint = room.getPlayerInRoom(param);

      // The player is not a group leader.
      if (!entity.getGroupLeader().equals(entity)) {
         entity.print(TaMessageManager.NOGRLD.getMessage());
         return true;
      }

      // No player found.
      if (playerToAppoint == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), param);
         entity.print(message);
         return true;
      }

      // The player is already group leader.
      if (entity.equals(playerToAppoint)) {
         entity.print(TaMessageManager.NOAPSL.getMessage());
         return true;
      }

      // The appointed player is not in the group.
      if (!entity.getGroupLeader().getGroupList().contains(playerToAppoint)) {
         String message = MessageFormat.format(TaMessageManager.WRNGRP.getMessage(), playerToAppoint.getName());
         entity.print(message);
         return true;
      }

      // Appoint the new leader.
      playerToAppoint.getGroupList().clear();
      playerToAppoint.getGroupList().add(entity);
      playerToAppoint.setGroupLeader(playerToAppoint);
      entity.setGroupLeader(playerToAppoint);
      for (Entity entityInGroup : entity.getGroupList()) {
         playerToAppoint.getGroupList().add(entityInGroup);
         entityInGroup.setGroupLeader(playerToAppoint);
      }
      playerToAppoint.getGroupList().remove(playerToAppoint);
      entity.getGroupList().clear();

      // Display the messages.
      String messageToPlayer = MessageFormat.format(TaMessageManager.APTLDR.getMessage(), playerToAppoint.getName());
      String messageToTarget = MessageFormat.format(TaMessageManager.APTYOU.getMessage(), entity.getName());
      String messageToRoom =
         MessageFormat.format(TaMessageManager.OTHAPT.getMessage(), entity.getName(), playerToAppoint.getName());

      entity.print(messageToPlayer);
      playerToAppoint.print(messageToTarget);
      room.print(entity, playerToAppoint, messageToRoom, false);

      return true;
   }
}
