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
 * &WOk, you've asked to join Minex's group.          *
 * &WLimech is asking to join your group.             *
 *
 * &MSorry, Limech is not the leader of a group.      *
 * &MSorry, you don't see "asd" nearby.               *
 * &MSorry, you are already a member of your group.   *
 *
 * Ok, you've disbanded your group!
 * Ok, you've asked to join Hilorex's group.          *
 *
 * Minex has just disbanded the group!
 * Minex is asking to join Hilorex's group.           *
 *
 * &MSorry, you are already a member of your group.   *
 */
public class DoJoin extends Command {

   /**
    * Executes the "join" command.
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

      Player leader = room.getPlayerInRoom(param);

      // No player found.
      if (leader == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), param);
         player.print(message);
         return true;
      }

      // Can't join yourself.
      if (leader.equals(player)) {
         player.print(TaMessageManager.NOJSLF.getMessage());
         return true;
      }

      // You are already in the group.
      if (leader.getGroupList().contains(player)) {
         player.print(TaMessageManager.NOJSLF.getMessage());
         return true;
      }

      // The player is not the group leader.
      if (!leader.getGroupLeader().equals(leader)) {
         String message = MessageFormat.format(TaMessageManager.NOGRLD2.getMessage(), leader.getName());
         player.print(message);
         return true;
      }

      // Disband the old group.
      if (player.getGroupLeader().equals(player)) {
         for (Entity entityInGroup : player.getGroupList()) {
            if (entityInGroup.getEntityType().equals(EntityType.PLAYER)) {
               DoDisband disband = new DoDisband();
               disband.disbandTheGroup(player, false);
               break;
            }
         }
      }

      // Do it...
      player.setGroupLeader(leader);
      player.getGroupList().clear();

      String messageToPlayer = MessageFormat.format(TaMessageManager.ASKGRP.getMessage(), leader.getName());
      String messageToLeader = MessageFormat.format(TaMessageManager.ASKGRP2.getMessage(), player.getName());
      String messageToRoom   = MessageFormat.format(TaMessageManager.OTHAGR1.getMessage(), player.getName(), leader.getName());

      player.print(messageToPlayer);
      leader.print(messageToLeader);
      room.print(player, leader, messageToRoom, false);

      return true;
   }

}
