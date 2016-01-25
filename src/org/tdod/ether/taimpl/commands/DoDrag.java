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
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "drag" command.
 *
 * @author Ron Kinney
 */
public class DoDrag extends Command {

   /**
    * Executes the "drag" command.
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
      String[] split = input.split(" ", THREE_PARAMS);

      if (split.length < THREE_PARAMS) {
         return false;
      }

      Player player = (Player) entity;
      Player target = null;
      Room room = player.getRoom();
      ExitDirectionEnum exitDirEnum = ExitDirectionEnum.getCompleteExitKeyword(split[2]);
      Command command = GameUtil.getDirectionCommand(exitDirEnum);

      if (exitDirEnum == null || command == null) {
         return false;
      }

      Exit exit = entity.getRoom().getExit(exitDirEnum);

      // No exit found.
      if (exit == null) {
         entity.print(TaMessageManager.NOEXIT.getMessage());
         return true;
      }

      for (Player tmpPlayer : room.getPlayers()) {
         if (MyStringUtil.contains(tmpPlayer.getName(), split[1])) {
            target = tmpPlayer;
         }
      }

      boolean error = false;
      if (target == null) {
         // Sorry, you don''t see "{0}" nearby.
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), split[1]);
         player.print(message);
         error = true;
      } else if (target.equals(player)) {
         // Sorry, you can't drag yourself.
         player.print(TaMessageManager.NODSLF.getMessage());
         error = true;
      } else if (!target.getStatus().equals(Status.PARALYSED)) {
         // {0} isn''t incapacitated!
         String message = MessageFormat.format(TaMessageManager.CNTDRG.getMessage(), target.getName());
         player.print(message);
         error = true;
      }

      boolean isFollowingGroup = false;
      boolean isInGroup = false;
      if (!error) {
         // Remember old settings.
         isFollowingGroup = target.isFollowingGroup();
         isInGroup = player.getGroupLeader().getGroupList().contains(target);

         // Set new settings.
         target.setDraggedBy(player);
         target.setFollowingGroup(true);
         if (!isInGroup) {
            player.getGroupLeader().getGroupList().add(target);
         }
         player.setDragging(target);

         try {
            command.execute(player, "");
         } finally {
            // Reset old settings.
            target.setFollowingGroup(isFollowingGroup);
            target.setDraggedBy(null);
            if (!isInGroup) {
               player.getGroupLeader().getGroupList().remove(target);
            }
            player.setDragging(null);
         }
      }

      return true;
   }

}
