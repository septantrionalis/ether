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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.CommandTrigger;
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "push" command.
 *
 * @author Ron Kinney
 */
public class DoPush extends Command {

   private static Log _log = LogFactory.getLog(DoPush.class);

   /**
    * V2 value indicating that the push teleports the player.
    */
   private static final int V2_PUSH_STONE_3  = 3;

   /**
    * Teleports the entity.
    */
   private static final int V3_TELEPORT      = 15;

   /**
    * Lifts permanent darkness.
    */
   private static final int V3_LIFT_DARKNESS = 17;

   /**
    * V2 value indicating that the push opens a passage.
    */
   private static final int V2_PUSH_STONE_4  = 4;

   /**
    * Open passage.
    */
   private static final int V3_OPEN_PASSAGE  = 12;

   /**
    * Executes the "push" command.
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
      Trigger trigger = null;
      for (Trigger t : player.getRoom().getTriggers()) {
         if (t.getTriggerType().equals(TriggerType.PUZZLE)) {
            trigger = t;
            break;
         }
      }

      if (trigger == null) {
         return false;
      }

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }

      if (player.isResting()) {
         player.print(TaMessageManager.TOOFST.getMessage());
         return true;
      }

      Room targetRoom = WorldManager.getRoom(trigger.getV4());

      // Get the command trigger.
      CommandTrigger commandTrigger = GameUtil.getCommandTrigger(player, "push " + split[1].toLowerCase());
      if (commandTrigger == null) {
         return false;
      }

      if (trigger.getV2() == V2_PUSH_STONE_3) {
         return handlePushStone3(commandTrigger, player, trigger, targetRoom);
      } else if (trigger.getV2() == V2_PUSH_STONE_4) {
         return handlePushStone4(commandTrigger, player, trigger, targetRoom);
      }

      return false;
   }

   /**
    * The push stone command that handles a v2 value of 3.
    * @param commandTrigger the command trigger.
    * @param player The player issuing the push command.
    * @param trigger The trigger.
    * @param targetRoom The target room.
    *
    * @return true if this is a successful command.
    */
   private boolean handlePushStone3(CommandTrigger commandTrigger, Player player, Trigger trigger, Room targetRoom) {
      player.println(commandTrigger.getPlayerMessage());
      // String messageToRoom = MessageFormat.format(MessageManager.CTHM2.getMessage(), player.getName());
      // player.getRoom().println(player, messageToRoom, false);

      if (trigger.getV3() == V3_TELEPORT) {
         String messageToSourceRoom = MessageFormat.format(TaMessageManager.EXT11.getMessage(), player.getName());
         player.getRoom().print(player, messageToSourceRoom, false);

         String messageToTargetRoom = MessageFormat.format(TaMessageManager.ENT11.getMessage(), player.getName());
         targetRoom.print(player, messageToTargetRoom, false);

         player.teleportToRoom(targetRoom.getRoomNumber());
      } else if (trigger.getV3() == V3_LIFT_DARKNESS) {
         int startVnum = trigger.getV4();
         int endVnum = trigger.getV5();
         for (int roomNumber = startVnum; roomNumber <= endVnum; roomNumber++) {
            WorldManager.getRoom(roomNumber).setIgnorePermanentDarkness(true);
         }
      } else {
         _log.error("Unhandled push command: " + trigger.getV3());
      }

      return true;
   }

   /**
    * The push stone command that handles a v2 value of 4.
    * @param commandTrigger the command trigger.
    * @param player The player issuing the push command.
    * @param trigger The trigger.
    * @param targetRoom The target room.
    *
    * @return true if this is a successful command.
    */
   private boolean handlePushStone4(CommandTrigger commandTrigger, Player player, Trigger trigger, Room targetRoom) {
      if (trigger.getV3() == V3_OPEN_PASSAGE) {
         // Unlock all doors.
         for (Exit exit : targetRoom.getExits()) {
            if (exit.getDoor() != null) {
               exit.getDoor().setIsUnlocked(true);
            }
         }
      }

      trigger.setTriggered(true);
      targetRoom.setAltDescription(trigger.getV5());

      // Send messages
      player.println(commandTrigger.getPlayerMessage());

      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      return true;
   }
}
