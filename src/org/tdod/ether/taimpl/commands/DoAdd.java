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
 * &MSorry, you are not the leader of a group.       *
 * &MSorry, You may not add yourself to your group.  *
 *
 * &MSorry, Limech has not asked to join your group. *
 * &MMinex would like you to join his group.         *
 * &WMinex is asking Limech to join his group.       *
 *
 * &WLimech is now a member of your group.           *
 * &WYou are now a member of Minex's group.          *
 * &WLimech has been accepted into Minex's group.    *
 */
public class DoAdd extends Command {

   /**
    * Executes the "add" command.
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

      String messageToPlayer;
      String messageToTarget;
      String messageToRoom;

      Player playerToAdd = room.getPlayerInRoom(param);

      // No player found.
      if (playerToAdd == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), param);
         entity.print(message);
         return true;
      }

      // The player is not a group leader.
      if (!entity.getGroupLeader().equals(entity)) {
         entity.print(TaMessageManager.NOGRLD.getMessage());
         return true;
      }

      // You can't add yourself to a group.
      if (entity.equals(playerToAdd)) {
         entity.print(TaMessageManager.NAGSLF.getMessage());
         return true;
      }

      // The player is already in the group.
      if (entity.getGroupList().contains(playerToAdd)) {
         String message = MessageFormat.format(TaMessageManager.ALRGRP.getMessage(), playerToAdd.getName());
         entity.print(message);
         return true;
      }

      // Player hasn't asked to join the group yet...
      if (!playerToAdd.getGroupLeader().equals(entity)) {
         messageToPlayer = MessageFormat.format(TaMessageManager.NASGRP.getMessage(), playerToAdd.getName());
         messageToTarget = MessageFormat.format(TaMessageManager.NASGRP2.getMessage(),
               entity.getName(), entity.getGender().getPronoun().toLowerCase());
         messageToRoom   = MessageFormat.format(TaMessageManager.OTHAGR2.getMessage(),
               entity.getName(), playerToAdd.getName(), entity.getGender().getPronoun().toLowerCase());

         entity.print(messageToPlayer);
         playerToAdd.print(messageToTarget);
         room.print(entity, playerToAdd, messageToRoom, false);
         return true;
      }

      // Do it...
      entity.setGroupLeader(entity);
      entity.getGroupList().add(playerToAdd);

      messageToPlayer = MessageFormat.format(TaMessageManager.JOIGRP.getMessage(), playerToAdd.getName());
      messageToTarget = MessageFormat.format(TaMessageManager.ACCGRP.getMessage(), entity.getName());
      messageToRoom   = MessageFormat.format(TaMessageManager.OTHACG.getMessage(), playerToAdd.getName(), entity.getName());

      entity.print(messageToPlayer);
      playerToAdd.print(messageToTarget);
      room.print(entity, playerToAdd, messageToRoom, false);

      return true;
   }
}
