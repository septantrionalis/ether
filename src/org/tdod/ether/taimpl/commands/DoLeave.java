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
 * in group :
 * &WOk, you are no longer a member of Minex's group.
 * &WLimech has just left Minex's group.
 *
 * in queue :
 * &WOk, you are no longer waiting to join Minex's group.
 * &WMinex has just stopped waiting to join Limech's group.
 */
public class DoLeave extends Command {

   /**
    * Executes the "leave" command.
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
      if (split.length > 1) {
         return false;
      }

      Room room = player.getRoom();

      Entity leader = player.getGroupLeader();
      if (leader.equals(player)) {
         // TODO need to verify this functionality -- what happens if the leader leaves the group.
         player.print(TaMessageManager.YOULDR.getMessage());
         return true;
      }

      // TODO need to verify that these are the messages that are sent.
      String messageToPlayer;
      String messageToRoom;

      // Determine if the player is part of the leaders group first.
      Entity entityLeaving = null;
      for (Entity entityInGroup : leader.getGroupList()) {
         if (entityInGroup.equals(player)) {
            entityLeaving = entityInGroup;
            break;
         }
      }

      // The player is in the leaders group.
      if (entityLeaving != null) {
         messageToPlayer = MessageFormat.format(TaMessageManager.LEVGRP.getMessage(), leader.getName());
         messageToRoom = MessageFormat.format(TaMessageManager.OTHLVG.getMessage(), entityLeaving.getName(), leader.getName());

         entityLeaving.print(messageToPlayer);
         room.print(entityLeaving, messageToRoom, false);

         leader.getGroupList().remove(entityLeaving);
         entityLeaving.setGroupLeader(player);
         return true;
      }

      // The player is not in the leaders group, so the player must have asked but has not been added yet.
      messageToPlayer = MessageFormat.format(TaMessageManager.LEVGRP3.getMessage(), leader.getName());
      messageToRoom = MessageFormat.format(TaMessageManager.OTHSWG.getMessage(), player.getName(), leader.getName());

      player.print(messageToPlayer);
      room.print(player, messageToRoom, false);

      player.setGroupLeader(player);

      return true;
   }
}
