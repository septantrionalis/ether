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
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "order" command.
 *
 * @author Ron Kinney
 */
public class DoOrder extends Command {

   /**
    * Executes the "order" command.
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

      Room room = entity.getRoom();
      Player player = (Player) entity;

      String[] split = input.split(" ", FOUR_PARAMS);
      if (split.length < FOUR_PARAMS) {
         if (!split[2].equals(MessageManager.STOP_STRING.getMessage())
               && !split[2].equals(MessageManager.ST_STRING.getMessage())) {
            return false;
         }
      }

      Mob mob = GameUtil.getMob(room, split[1]);

      String command = split[2].toLowerCase();
      if (command.equals(MessageManager.FOLLOW_STRING.getMessage())
            || command.equals(MessageManager.FO_STRING.getMessage())) {
         Player target = GameUtil.getPlayer(room, split[THREE_PARAMS]);
         return handleFollow(player, mob, target);
      } else if (command.equals(MessageManager.TRACK_STRING.getMessage())
            || command.equals(MessageManager.TR_STRING.getMessage())) {
         return handleTrack(player, mob, split[THREE_PARAMS]);
      } else if (command.equals(MessageManager.STOP_STRING.getMessage())
            || command.equals(MessageManager.ST_STRING.getMessage())) {
         return handleStop(player, mob);
      }

      return false;
   }

   /**
    * ORDER BL FOLLOW RON                                                                 *
    * if  "what" cannot be found:                                                         *
    * &MSorry, but you don't seem to have one to command.                                 *
    *                                                                                     *
    * if "who" cannot be found:                                                           *
    * &MThe black orchid looks around in confusion.                                       *
    *                                                                                     *
    * &MSorry, you can't command your followers to follow you.                            *
    *                                                                                     *
    * success:                                                                            *
    * EVERYONE BUT "PLAYER": &WMinex just gave an order to the black orchid.              *
    * PLAYER:&WThe white orchid stops following you and begins following Minex.           *
    * ROOM:&WThe white orchid stops following Ron Kinney and begins following Minex.      *
    * TARGET:&WThe black orchid stops following Minex and begins following you.           *
    *                                                                                     *
    * if in group :                                                                       *
    * place the mob in the targets group list.                                            *
    * It shouldn't show up when the player types "gr" but will once its disbanded.        *
    *
    * @param player The player issuing the order command.
    * @param mob The target mob.
    * @param target The target player.
    *
    * @return true if this is a sucessful command.
    */
   private boolean handleFollow(Player player, Mob mob, Player target) {
      Room room = player.getRoom();

      String messageToPlayer;
      // Sorry, but you don't seem to have one to command.
      if (mob == null
            || !player.getGroupList().contains(mob)) {
         player.print(TaMessageManager.NOMCMD.getMessage());
         return true;
      }

      // {0} just gave an order to the {1}.
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHCMD.getMessage(), player.getName(), mob.getName());
      room.print(player, messageToRoom, false);

      // The {0} looks around in confusion.
      if (target == null) {
         messageToPlayer = MessageFormat.format(TaMessageManager.NCFOLL.getMessage(), mob.getName());
         player.print(messageToPlayer);
         return true;
      }

      // Sorry, you can't command your followers to follow you.
      if (player.equals(target)) {
         player.print(TaMessageManager.NCFSLF.getMessage());
         return true;
      }

      // Success
      target.getGroupList().add(mob);
      player.getGroupList().remove(mob);
      mob.setGroupLeader(target);

      messageToPlayer = MessageFormat.format(TaMessageManager.CMDFOL.getMessage(), mob.getName(), target.getName());
      player.print(messageToPlayer);

      messageToRoom =
         MessageFormat.format(TaMessageManager.CMFOLL.getMessage(), mob.getName(), player.getName(), target.getName());
      room.print(player, target, messageToRoom, false);

      String messageToTarget = MessageFormat.format(TaMessageManager.CMFYOU.getMessage(), mob.getName(), player.getName());
      target.print(messageToTarget);
      return true;
   }

   /**
    * ORDER WH TRACK MINEX                                              *
    * if  "what" cannot be found:                                       *
    * &MSorry, but you don't seem to have one to command.               *
    *                                                                   *
    * if "who" cannot be found:                                         *
    * &MThe white orchid can't find any sign of asd.                    *
    *                                                                   *
    * PLAYER:&WThe white orchid has picked up Minex's trail!            *
    * ROOM:&WRon Kinney just gave an order to the white orchid.         *
    * ROOM:&WThe white orchid is very thoroughly examining the area.    *
    * &YThe white orchid has just gone to the west.                     *
    *                                                                   *
    * &MSorry, orcs aren't very good at tracking.                       *
    * ->rabbits and orchids appear to be good at tracking.              *
    * &MSorry, you can't command your followers to track you.           *
    *
    * @param player The player issuing the order command.
    * @param mob The target mob.
    * @param targetString The target player in String format.
    *
    * @return true if this is a sucessful command.
    */
   private boolean handleTrack(Player player, Mob mob, String targetString) {
      Room room = player.getRoom();

      String messageToPlayer;

      // Sorry, but you don't seem to have one to command.
      if (mob == null || !player.getGroupList().contains(mob)) {
         player.print(TaMessageManager.NOMCMD.getMessage());
         return true;
      }

      Player target = GameUtil.getPlayer(targetString);

      // {0} just gave an order to the {1}.
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHCMD.getMessage(), player.getName(), mob.getName());
      room.print(player, messageToRoom, false);

      // The white orchid can't find any sign of asd.
      if (target == null || target.getTrackingMap().get(room) == null) {
         messageToPlayer = MessageFormat.format(TaMessageManager.NCTRAL.getMessage(), mob.getName(), targetString);
         player.print("&M" + messageToPlayer);
         return true;
      }

      // Sorry, you can't command your followers to track you.
      if (player.equals(target)) {
         player.print(TaMessageManager.NCTSLF.getMessage());
         return true;
      }

      // Sorry, {0} aren''t very good at tracking.
      if (!mob.canTrack()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTTRK.getMessage(), mob.getPlural());
         player.print(messageToPlayer);
         return true;
      }

      // Success
      mob.setChasing(target);

      messageToPlayer = MessageFormat.format(TaMessageManager.CMDTRK.getMessage(), mob.getName(), target.getName());
      player.print(messageToPlayer);

      messageToRoom = MessageFormat.format(TaMessageManager.CTRING.getMessage(), mob.getName());
      room.print(player, messageToRoom, false);

      return true;
   }

   /**
    * order black stop
    * if tracking :
    * PLAYER:&WThe black orchid stops tracking Minex.
    * else {
    * PLAYER:<nothing>
    * }
    * ROOM:&WRon Kinney just gave an order to the white orchid.
    *
    * @param player The player issuing the order command.
    * @param mob The target mob.
    *
    * @return true if this is a sucessful command.
    */
   private boolean handleStop(Player player, Mob mob) {
      Room room = player.getRoom();

      // Sorry, but you don't seem to have one to command.
      if (mob == null || !player.getGroupList().contains(mob)) {
         player.print(TaMessageManager.NOMCMD.getMessage());
         return true;
      }

      // {0} just gave an order to the {1}.
      String messageToRoom = MessageFormat.format(TaMessageManager.OTHCMD.getMessage(), player.getName(), mob.getName());
      room.print(player, messageToRoom, false);

      if (mob.getChasing() != null) {
         String messageToPlayer =
            MessageFormat.format(TaMessageManager.CMDSTP.getMessage(), mob.getName(), mob.getChasing().getName());
         mob.setChasing(null);
         player.print(messageToPlayer);
      }

      return true;
   }

}
