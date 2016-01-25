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
import org.tdod.ether.ta.cosmos.Exit;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;
import org.tdod.ether.util.MessageManager;

/**
 * Handles the "northeast" command.
 *
 * @author Ron Kinney
 */
public class DoNorthEast extends AbstractMovementCommand {

   /**
    * Moves the player in the appropriate direction.
    *
    * @param entity The entity performing the move command.
    * @param ignoreBarrier True if the entity can ignore any barriers.
    * @param inGroup True if the entity is in a group.
    *
    * @return true if the move was successful.
    */
   public final boolean doMove(Entity entity, boolean ignoreBarrier, boolean inGroup) {
      Exit exit = entity.getRoom().getExit(ExitDirectionEnum.NORTHEAST);

      doMovePlayer(entity, exit, ExitDirectionEnum.NORTHEAST, ignoreBarrier, inGroup);

      return true;
   }

   /**
    * Returns the leave message for the room if the player is in a group.
    *
    * @param entity The entity that is moving.
    *
    * @return the leave message for the room if the player is in a group.
    */
   protected final String getGroupLeaveMessage(Entity entity) {
      return MessageFormat.format("&Y" + MessageManager.GROUP_ROOM_MOVE1.getMessage(),
            entity.getName(), ExitDirectionEnum.NORTHEAST.getLongDescription());
   }

   /**
    * Returns the enter message for the room if the player is in a group.
    *
    * @param entity The entity that is moving.
    *
    * @return the enter message for the room if the player is in a group.
    */
   protected final String getGroupEnterMessage(Entity entity) {
      return MessageFormat.format("&Y" + MessageManager.GROUP_ROOM_ARRIVED1.getMessage(),
            entity.getName(), ExitDirectionEnum.SOUTHWEST.getLongDescription());
   }

   /**
    * Returns the leave message for the room.
    *
    * @param entity The entity that is moving.
    *
    * @return the leave message for the room.
    */
   protected final String getLeaveMessage(Entity entity) {
      return entity.getMessageHandler().getLeaveMessage(ExitDirectionEnum.NORTHEAST);
   }

   /**
    * Returns the enter message for the room.
    *
    * @param entity The entity that is moving.
    *
    * @return the enter message for the room.
    */
   public final String getEnterMessage(Entity entity) {
      return entity.getMessageHandler().getEnterMessage(ExitDirectionEnum.SOUTHWEST);
   }

}
