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
import java.util.ArrayList;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * &WOk, you've disbanded your group!           *
 *
 * &WMinex has just disbanded the group!        *
 *
 * &MSorry, you are not the leader of a group.  *
 */
public class DoDisband extends Command {

   /**
    * Executes the "disband" command.
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

      String[] split = input.split(" ", 2);
      if (split.length > 1) {
         return false;
      }

      Player player = (Player) entity;

      if (!player.getGroupLeader().equals(player)) {
         player.print(TaMessageManager.NOGRLD.getMessage());
         return true;
      }

      disbandTheGroup(player, true);

      return true;
   }

   /**
    * Disbands the group.
    *
    * @param leader The leader of the group.
    * @param displayMessages Display messages.
    */
   public final void disbandTheGroup(Player leader, boolean displayMessages) {
      leader.setGroupLeader(leader);

      if (displayMessages) {
         leader.print(TaMessageManager.DISGRP.getMessage());
      }

      String messageToGroup = MessageFormat.format(TaMessageManager.GRPDIS.getMessage(), leader.getName());

      // Leader keeps any mobs.  Create a temporary holding list of players to remove.
      ArrayList<Entity> removedPlayers = new ArrayList<Entity>();
      for (Entity entity : leader.getGroupList()) {
         if (entity.getEntityType().equals(EntityType.PLAYER)) {
            removedPlayers.add(entity);
         }
      }

      // Remove the players and send a message.
      for (Entity player : removedPlayers) {
         if (displayMessages) {
            player.print(messageToGroup);
         }
         player.setGroupLeader(player);
         leader.getGroupList().remove(player);
      }
   }

}
