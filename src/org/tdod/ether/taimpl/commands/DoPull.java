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
 * Handles the "pull" command.
 *
 * @author Ron Kinney
 */
public class DoPull extends Command {

   /**
    * The V2 value indicating a lever that can be pulled.
    */
   private static final int V2_PULL_VALUE                = 1;

   /**
    * The V3 value indicating that the lever disables a trap.
    */
   private static final int V3_DISABLE_TRAP              = 9;

   /**
    * The V3 value indicuating that the lever will enable/disable teleporters.
    * Design note -- I'm not sure if this is specific to teleporters or all triggers in general.  As
    * of now, I just limited it to toggling teleporters.
    */
   private static final int V3_TOGGLE_TELEPORTERS        = 11;

   /**
    * A hidden lever that opens a passage.
    */
   private static final int V3_HIDDEN_LEVER_OPEN_PASSAGE = 12;

   /**
    * The V3 value indicating that the lever opens all passages in the specified room.
    */
   private static final int V3_OPEN_PASSAGE              = 14;

   /**
    * Executes the "pull" command.
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

      // Find the appropriate trigger.
      Trigger trigger = null;
      for (Trigger t : player.getRoom().getTriggers()) {
         if (t.getTriggerType().equals(TriggerType.PUZZLE)
               && t.getV2() == V2_PULL_VALUE) {
            trigger = t;
            break;
         }
      }

      // Not found.
      if (trigger == null) {
         return false;
      }

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }

      // The correct command is 'pull lever'
      CommandTrigger commandTrigger = GameUtil.getCommandTrigger(player, "pull " + split[1].toLowerCase());
      if (commandTrigger == null) {
         return false;
      }

      // Can't pull the lever is the player is resting.
      if (player.isResting()) {
         player.print(TaMessageManager.TOOFST.getMessage());
         return true;
      }

      // Do the appropriate action.
      if (trigger.getV3() == V3_DISABLE_TRAP) {
         return handleDisableTrap(commandTrigger, player, trigger);
      } else if (trigger.getV3() == V3_OPEN_PASSAGE) {
         return handleOpenPassage(commandTrigger, player, trigger);
      } else if (trigger.getV3() == V3_HIDDEN_LEVER_OPEN_PASSAGE) {
         return handleHiddenLeverOpenPassage(commandTrigger, player, trigger);
      } else if (trigger.getV3() == V3_TOGGLE_TELEPORTERS) {
         return handleToggleTriggers(commandTrigger, player, trigger);
      }

      // The action was not found.
      return false;
   }

   /**
    * Disables a trap.
    * @param commandTrigger the command trigger.
    * @param player The player pulling the lever.
    * @param trigger The trigger.
    *
    * @return true if this is a successful command.
    */
   private boolean handleDisableTrap(CommandTrigger commandTrigger, Player player, Trigger trigger) {
      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      Room targetRoom = WorldManager.getRoom(trigger.getV4());

      for (Trigger t : targetRoom.getTriggers()) {
         if (t.getTriggerType().equals(TriggerType.TRAP)) {
            t.setTriggered(true);
         }
      }

      return true;
   }

   /**
    * Opens all passages in the specified room.
    * @param commandTrigger the command trigger.
    * @param player The player pulling the lever.
    * @param trigger The trigger.
    *
    * @return true if this is a successful command.
    */
   private boolean handleOpenPassage(CommandTrigger commandTrigger, Player player, Trigger trigger) {
      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      Room targetRoom = WorldManager.getRoom(trigger.getV4());

      // Unlock all doors.
      for (Exit exit : targetRoom.getExits()) {
         if (exit.getDoor() != null) {
            exit.getDoor().setIsUnlocked(true);
         }
      }
      targetRoom.setAltDescription(trigger.getV5());

      return true;
   }

   /**
    * A hidden lever that opens a passage.
    * @param commandTrigger the command trigger.
    * @param player the player performing the action.
    * @param trigger the trigger.
    * @return true if the lever could be pulled, false otherwise.
    */
   private boolean handleHiddenLeverOpenPassage(CommandTrigger commandTrigger, Player player, Trigger trigger) {
      if (!player.getRoom().getActionPlayerList().contains(player.getName())) {
         return false;
      }

      handleOpenPassage(commandTrigger, player, trigger);

      return true;
   }

   /**
    * A hidden lever enables/disables teleporters.
    * @param commandTrigger the command trigger.
    * @param player the player performing the action.
    * @param trigger the trigger.
    * @return true if the lever could be pulled, false otherwise.
    */
   private boolean handleToggleTriggers(CommandTrigger commandTrigger, Player player, Trigger trigger) {
      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);
      Room targetRoom = WorldManager.getRoom(trigger.getV4());
      for (Trigger targetTrigger : targetRoom.getTriggers()) {
         if (targetTrigger.getV7() == 1) {
            targetTrigger.setV7(0);
         } else {
            targetTrigger.setV7(1);
         }
      }
      return true;
   }
}
