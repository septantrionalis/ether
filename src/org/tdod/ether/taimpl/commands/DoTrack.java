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
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Level 1
 * success/failure = same as hunt.
 *
 * Rest = 10 seconds
 *
 * &MSorry, warriors aren't very good at tracking.
 * &MThis area is too well traveled to pick up a trail.
 * &MSorry, you can't track yourself.
 * &MYou can't find any sign of Ron Kinney.
 * ->rest
 *
 * &YRon Kinney's trail leads down.
 * &YRon Kinney's trail leads up.
 * &YRon Kinney's trail leads south.
 *
 * Same room:
 * &MThat would be rather foolish.
 *
 * Minex is very thoroughly examining the area.
 */
public class DoTrack extends Command {

   /**
    * Track minimum rest period.
    */
   private static final int TRACK_REST_MIN =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TRACK_REST_MIN));

   /**
    * Track maximum rest period.
    */
   private static final int TRACK_REST_MAX =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TRACK_REST_MAX));

   /**
    * Executes the "track" command.
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

      // Sorry, {0} aren't very good at tracking.
      if (!player.getPlayerClass().equals(PlayerClass.HUNTER)) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.CNTTRK.getMessage(),
               player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // You are still physically exhausted from your previous activities!
      if (player.isResting()) {
         player.print(TaMessageManager.ATTEXH.getMessage());
         return true;
      }

      // This area is too well traveled to pick up a trail.
      if (GameUtil.isTown(player.getRoom())) {
         player.print(TaMessageManager.NOTRKH.getMessage());
         return true;
      }

      Player target = GameUtil.getPlayer(split[1]);

      // Sorry, you can't track yourself.
      if (player.equals(target)) {
         player.print(TaMessageManager.NTRSLF.getMessage());
         return true;
      }

      // That would be rather foolish.
      if (target != null && player.getRoom().equals(target.getRoom())) {
         player.print(TaMessageManager.FOLISH.getMessage());
         return true;
      }

      int restPeriod = Dice.roll(TRACK_REST_MIN, TRACK_REST_MAX);
      player.setRestTicker(restPeriod);

      // Minex is very thoroughly examining the area.
      String messageToRoom = MessageFormat.format(TaMessageManager.TRKING.getMessage(), player.getName());
      player.getRoom().print(player, messageToRoom, false);

      // You can't find any sign of asdf.
      if (target == null) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NOTRAL.getMessage(), split[1]);
         player.print(messageToPlayer);
         return true;
      }

      ExitDirectionEnum exitDirection = target.getTrackingMap().get(player.getRoom());
      if (exitDirection == null) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.NOTRAL.getMessage(), target.getName());
         player.print(messageToPlayer);
         return true;
      }

      String exitDirectionString;
      if (exitDirection.equals(ExitDirectionEnum.DOWN)) {
         exitDirectionString = "down";
      } else if (exitDirection.equals(ExitDirectionEnum.UP)) {
         exitDirectionString = "up";
      } else {
         exitDirectionString = exitDirection.getLongDescription();
      }
      String messageToPlayer = MessageFormat.format(
            TaMessageManager.HEWENT.getMessage(), target.getName(), exitDirectionString);
      player.print(messageToPlayer);

      return true;
   }
}
