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
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * &MSorry, you are not a member of any group.     *
 * &MAt your level you may need that item.         *
 *
 * %s just shared %s gold with the group, your share was %u gold.          *
 * %s just shared gold with the group, but you couldn't carry your share.  *
 * You shared %s gold with the group, each share was %u gold.
 * %s just shared gold with %s group.
 *
 * Sorry, %s can't carry that much more gold.
 */
public class DoShare extends Command {

   /**
    * Level required to issue the share command.
    */
   private static final int LEVEL_TO_DROP_ITEMS = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.LEVEL_TO_DROP_ITEMS)).intValue();

   /**
    * Executes the "share" command.
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
      Room room = player.getRoom();

      Entity leader = player.getGroupLeader();

      // Must be in a group to share.
      if (!GameUtil.isLeaderInGroup(leader)) {
         player.print(TaMessageManager.NOTGRP.getMessage());
         return true;
      }

      // Don't allow low level characters to share gold.
      if (player.getLevel() < LEVEL_TO_DROP_ITEMS) {
         player.print(TaMessageManager.CNTGVG.getMessage());
         return true;
      }

      // Calculate the share amount.
      float playerCount = 1; // Start with one because of the leader.
      for (Entity follower : leader.getGroupList()) {
         if (follower.getEntityType().equals(EntityType.PLAYER)) {
            if (follower.getRoom().equals(room)) { // TODO, not sure if TA checks this.
               playerCount++;
            }
         }
      }

      int amountPerPlayer = 0;
      float shareAmount = 0;
      try {
         shareAmount = new Float(split[1]);
         amountPerPlayer = (int) (shareAmount / playerCount);
      } catch (Exception e) {
         // TODO how does TA handle non-integer values. "share a", for example.
         return false;
      }

      String messageToTarget;
      String messageToPlayer;
      String messageToRoom;

      // Determine if the player has enough gold.
      if (shareAmount > player.getGold()) {
         player.print(TaMessageManager.BNKNHA.getMessage());
         return true;
      }

      // Share the gold...
      for (Entity follower : leader.getGroupList()) {
         if (follower.getEntityType().equals(EntityType.PLAYER)) {
            if (follower.getRoom().equals(room)) { // TODO, not sure if TA checks this.
               int overflow = follower.addGold(amountPerPlayer);
               player.subtractGold(amountPerPlayer - overflow);
               if (overflow > 0) {
                  messageToTarget = MessageFormat.format(TaMessageManager.JSTSHN.getMessage(), player.getName());
               } else {
                  messageToTarget = MessageFormat.format(TaMessageManager.JSTSHG.getMessage(),
                        player.getName(), shareAmount, amountPerPlayer);
               }
               follower.print(messageToTarget);
            }
         }
      }

      // Print the messages
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUSHG.getMessage(),
            shareAmount, amountPerPlayer);
      player.print(messageToPlayer);
      if (!player.isInvisible()) {
         messageToRoom = MessageFormat.format(TaMessageManager.OTHSHR.getMessage(),
               player.getName(), player.getGender().getPronoun().toLowerCase());
         room.printToNonGroup(player, messageToRoom);
      }

      return true;
   }

}
