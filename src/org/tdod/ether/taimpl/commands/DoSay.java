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
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;

/**
 * Handles the "say" command.
 *
 * @author Ron Kinney
 */
public class DoSay extends Command {

   private static Log _log = LogFactory.getLog(DoSay.class);

   /**
    * Vnum in the data file that specifies the komi riddle.
    */
   private static final int SAY_KOMI_VALUE    = 2;

   /**
    * Vnum in the data file that specifies the arok riddle.
    */
   private static final int SAY_AROK_VALUE    = 5;

   /**
    * Vnum in the data file that specifies the cinders riddle.
    */
   public static final int SAY_CINDERS_ETHER  = 8;

   /**
    * Vnum in the data file that specifies the ether riddle.
    */
   private static final int SAY_ETHER         = 9;

   /**
    * Vnum in the data file that specifies the dwarflord riddle.
    */
   private static final int SAY_DWARFLORD     = 11;
   /**
    * Executes the "say" command.
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

      if (!trigger.getTriggerType().equals(TriggerType.PUZZLE)) {
         return false;
      }

      // Get the command trigger.
      CommandTrigger commandTrigger = GameUtil.getCommandTrigger(player, "say " + split[1].toLowerCase());
      if (commandTrigger == null) {
         return false;
      }

      if (trigger.getV2() == SAY_KOMI_VALUE) {
         return handleKomi(commandTrigger, player, trigger, split[1].toLowerCase());
      } else if (trigger.getV2() == SAY_AROK_VALUE) {
         return handleArok(commandTrigger, player, trigger, split[1].toLowerCase());
      } else if (trigger.getV2() == SAY_CINDERS_ETHER) {
         return handleCindersEther(commandTrigger, player, split[1].toLowerCase());
      } else if (trigger.getV2() == SAY_DWARFLORD) {
         return handleDwarflord(commandTrigger, player, trigger, split[1].toLowerCase());
      } else {
         return false;
      }
   }

   /**
    * Handles the komi puzzle.
    * @param commandTrigger the command trigger.
    * @param player The player that is solving the riddle.
    * @param trigger The trigger.
    * @param param Parameters passed in by the player.
    *
    * @return true if the riddle was solved.
    */
   private boolean handleKomi(CommandTrigger commandTrigger, Player player, Trigger trigger, String param) {
      player.getRoom().getExit(ExitDirectionEnum.SOUTH).getDoor().setIsUnlocked(true);
      player.getRoom().getExit(ExitDirectionEnum.EAST).getDoor().setIsUnlocked(true);
      player.getRoom().setAltDescription(trigger.getV5());

      player.println(commandTrigger.getPlayerMessage());

      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      trigger.setTriggered(true);
      return true;
   }

   /**
    * Handles the arok puzzle.
    * @param commandTrigger the command trigger.
    * @param player The player that is solving the riddle.
    * @param trigger The trigger.
    * @param param Parameters passed in by the player.
    *
    * @return true if the riddle was solved.
    */
   private boolean handleArok(CommandTrigger commandTrigger, Player player, Trigger trigger, String param) {
      player.getRoom().getExit(ExitDirectionEnum.NORTH).getDoor().setIsUnlocked(true);
      player.getRoom().setAltDescription(trigger.getV5());

      player.println(commandTrigger.getPlayerMessage());

      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      trigger.setTriggered(true);
      return true;
   }

   /**
    * This method determines if the riddle is for "cinders" or "ether".
    * @param commandTrigger the command trigger.
    * @param player the player attempting to solve the riddle.
    * @param param the player input.
    * @return false if the command was not valid.
    */
   private boolean handleCindersEther(CommandTrigger commandTrigger, Player player, String param) {
      if (param.equals(MessageManager.CINDERS_STRING.getMessage())) {
         return handleCinders(commandTrigger, player, param);
      } else if (param.equals(MessageManager.ETHER_STRING.getMessage())) {
         return handleEther(commandTrigger, player, param);
      }

      return false;
   }

   /**
    * Solves the cinders riddle.
    * @param commandTrigger the command trigger.
    * @param player the player.
    * @param param the player input.
    * @return false if the command was not valid.
    */
   private boolean handleCinders(CommandTrigger commandTrigger, Player player, String param) {
      if (!player.getRoom().getActionPlayerList().contains(player.getName())) {
         player.getRoom().getActionPlayerList().add(player.getName());
      }
      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      return true;
   }

   /**
    * Solves the ether riddle.
    * @param commandTrigger the command trigger.
    * @param player the player.
    * @param param the player input.
    * @return false if the command was not valid.
    */
   private boolean handleEther(CommandTrigger commandTrigger, Player player, String param) {
      if (!player.getRoom().getActionPlayerList().contains(player.getName())) {
         return false;
      }
      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      Trigger trigger = null;
      for (Trigger t : player.getRoom().getTriggers()) {
         if (t.getTriggerType().equals(TriggerType.PUZZLE)
               && t.getV2() == SAY_ETHER) {
            trigger = t;
            break;
         }
      }

      if (trigger == null) {
         _log.error("Handle ether called from room " + player.getRoom().getRoomNumber());
         return true;
      }

      player.teleportToRoom(trigger.getV4());

      return true;
   }

   /**
    * Solve the dwarflord riddle.
    * @param commandTrigger the command trigger.
    * @param player the player attempting to solve the riddle.
    * @param trigger the trigger.
    * @param param the player input.
    * @return false if the command was not valid.
    */
   private boolean handleDwarflord(CommandTrigger commandTrigger, Player player, Trigger trigger, String param) {
      player.getRoom().getExit(ExitDirectionEnum.NORTH).getDoor().setIsUnlocked(true);
      player.getRoom().setAltDescription(trigger.getV5());

      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      return true;
   }
}
