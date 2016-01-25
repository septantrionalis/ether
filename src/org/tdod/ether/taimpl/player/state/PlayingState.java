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

package org.tdod.ether.taimpl.player.state;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.commands.CommandManager;
import org.tdod.ether.ta.cosmos.Emote;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

public class PlayingState implements PlayerState {

   public void execute(PlayerStateContext stateContext, String input) {
      Player player = stateContext.getPlayerConnection().getPlayer();

      doCommand(player, input) ;
   }

   public void doCommand(Entity entity, String input) {
      String trimmed = input.trim().toLowerCase();
      String commandString = getCommandString(trimmed);

      // Emotes first
      Emote emote = WorldManager.getEmote(commandString);
      if (emote != null) {
         handleEmote(entity, emote, trimmed.toLowerCase());
         return;
      }

      // check if the initial command exists "c motu" for example.
      Command command = CommandManager.getInstance().getCommand(commandString);
      if (command == null) {
         // second attempt. see if the entire string exits. "ls i" for example.
         command = CommandManager.getInstance().getCommand(trimmed.toLowerCase());
      }

      Room room = entity.getRoom();
      boolean success = false;
      if (command != null) {
         // A command exists. Execute it.
         if (entity.getStatus().equals(Status.PARALYSED) && !command.canUseParalyzed()) {
            entity.print(TaMessageManager.PARLYZ.getMessage());
            return;
         }
         
         success = command.execute(entity, trimmed.toLowerCase());
      }

      if (!success) {
         if (room.isRoomPopulatedWithOtherPlayers(entity)) {
            // Someone is in the room, so send the input as a message.
            String messageToRoom = "From " + entity.getName() + ": " + input;
            room.println(entity, messageToRoom, true);
            entity.print(TaMessageManager.MSGSNT.getMessage());
         } else {
            // No one in the room and no such command.
            entity.print(TaMessageManager.BYSELF2.getMessage());
         }
      }
   }
   
   /**
    * Parses out the initial character of the input string. For example, this
    * will return "cast" from "cast motu".
    * 
    * @param input
    *           The input string.
    * @return the initial string.
    */
   private String getCommandString(String input) {
      String[] params = input.split(" ");

      return params[0];
   }

   private void handleEmote(Entity entity, Emote emote, String input) {
      if (entity.getStatus().equals(Status.PARALYSED)) {
         entity.print(TaMessageManager.PARLYZ.getMessage());
         return;
      }
      
      String[] params = input.split(" ");
      Room room = entity.getRoom();

      if (params.length == 1 || emote.getToTarget() == null) {
         // A target was not specified or emote does not need a target
         // The emote requires a target.
         if (emote.getToRoom() == null) {
            entity.print(TaMessageManager.ARNAWH.getMessage());
            return;
         }

         // Print appropriate messages.
         entity.println(emote.getToPlayer());
         String messageToRoom = MessageFormat.format(emote.getToRoom(), entity.getName());
         room.println(entity, messageToRoom, true);
      } else {
         String target = params[1];

         // Find players
         ArrayList<Player> playerList = room.getPlayers();
         Player targetPlayer = null;
         String name = null;
         for (Player tmpPlayer : playerList) {
            if (MyStringUtil.contains(tmpPlayer.getName(), target)) {
               targetPlayer = tmpPlayer;
               name = targetPlayer.getName();
               break;
            }
         }

         // Try to find a mob.
         if (name == null) {
            for (Mob mob : room.getMobs()) {
               if (MyStringUtil.contains(mob.getName(), target)) {
                  name = mob.getPrefix().toLowerCase() + " " + mob.getName();
                  break;
               }
            }
         }

         // Try to find an npc.
         if (name == null) {
            for (Mob npc : room.getNpcs()) {
               if (MyStringUtil.contains(npc.getName(), target)) {
                  name = npc.getPrefix().toLowerCase() + " " + npc.getName();
                  break;
               }
            }
         }

         // Could not find anyone by that name.
         if (name == null) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.ARNNHR
                  .getMessage(), target);
            entity.print(messageToPlayer);
            return;
         }

         // Message to the player;
         entity.println(emote.getToPlayer());

         // Message to everyone else.
         String messageToOthers = MessageFormat.format(emote.getToTarget(), entity.getName(), name);
         ArrayList<Entity> exceptions = new ArrayList<Entity>();
         exceptions.add(entity);
         if (targetPlayer != null) {
            // Message to the target
            String messageToTarget = MessageFormat.format(emote.getToTarget(), entity.getName(), "you");
            targetPlayer.println(messageToTarget);

            room.println(entity, targetPlayer, messageToOthers, true);
         } else {
            room.println(entity, messageToOthers, true);
         }

      }
   }
}
