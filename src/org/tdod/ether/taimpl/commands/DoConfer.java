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
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.util.TaMessageManager;

/**
 * PLAYER: &Y-- Message sent only to group --
 * GROUP:  &YFrom Minex (to group): ready?
 */
public class DoConfer extends Command {

   /**
    * Executes the "confer" command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      String[] split = input.split(" ", TWO_PARAMS);
      if (split.length < 2) {
         return false;
      }

      Room room = entity.getRoom();
      String param = split[1].toLowerCase();

      entity.print(TaMessageManager.GRPSNT.getMessage());

      String message = MessageFormat.format(TaMessageManager.GTALKU.getMessage(), entity.getName(), param);
      if (!entity.equals(entity.getGroupLeader()) && room.equals(entity.getGroupLeader().getRoom())) {
         entity.getGroupLeader().print(message);
      }
      for (Entity entityInGroup : entity.getGroupLeader().getGroupList()) {
         if (!entity.equals(entityInGroup) && room.equals(entityInGroup.getRoom())) {
            entityInGroup.print(message);
         }
      }

      return true;
   }

}
