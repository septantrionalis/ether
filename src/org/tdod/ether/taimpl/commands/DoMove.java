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
import org.tdod.ether.ta.cosmos.Trigger;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.enums.TriggerType;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * The move command.
 * @author rkinney
 */
public class DoMove extends Command {

   /**
    * The V2 value indicating a tapestry can be moved.
    */
   public static final int V2_MOVE_TAPESTRY  = 10;

   /**
    * Executes the "move" command.
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
               && t.getV2() == V2_MOVE_TAPESTRY) {
            trigger = t;
            break;
         }
      }

      // The trigger was not found.
      if (trigger == null) {
         return false;
      }

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }

      // Get the command trigger.
      CommandTrigger commandTrigger = GameUtil.getCommandTrigger(player, "move " + split[1].toLowerCase());
      if (commandTrigger == null) {
         return false;
      }

      // Can't issue the move command with mobs in the room.
      if (!player.getRoom().getMobs().isEmpty()) {
         player.print(TaMessageManager.CDTNOW.getMessage());
         return true;
      }

      // Can't perform the move command if the the player is resting.
      if (player.isResting()) {
         player.print(TaMessageManager.TOOFST.getMessage());
         return true;
      }

      // Success
      if (!player.getRoom().getActionPlayerList().contains(player.getName())) {
         player.getRoom().getActionPlayerList().add(player.getName());
      }

      player.getRoom().setAltDescription(trigger.getV5());

      player.println(commandTrigger.getPlayerMessage());
      String messageToRoom = MessageFormat.format(commandTrigger.getRoomMessage(), player.getName());
      player.getRoom().println(player, messageToRoom, false);

      return true;
   }

}
