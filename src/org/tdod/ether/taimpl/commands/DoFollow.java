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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.TaMessageManager;

/**
 * &MYou are the leader of this group.
 * &WYou are following your group.
 */
public class DoFollow extends Command {

   /**
    * Executes the "follow" command.
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
      if (split.length > 1) {
         return false;
      }

      Entity leader = player.getGroupLeader();
      if (leader.equals(player)) {
         player.print(TaMessageManager.YOULDR.getMessage());
         return true;
      }

      player.print(TaMessageManager.GRPFOL.getMessage());
      player.setFollowingGroup(true);
      return true;
   }
}
