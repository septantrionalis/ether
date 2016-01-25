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

package org.tdod.ether.taimpl.cosmos.doors;

import org.tdod.ether.ta.cosmos.enums.DoorType;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.player.Player;

/**
 * This door requires the player to solve a puzzle for passage.
 * Room 1192, 1222, 1284, 1452, 1456
 *
 * @author Ron Kinney
 */
public class PuzzleDoor extends AbstractDoor {

   /**
    * Gets the move fail code when a player attempts to pass through this door.
    *
    * @param player the player attempting to pass through this door.
    *
    * @return the move fail code.
    */
   public MoveFailCode getMoveFailCode(Player player) {
      if (isUnlocked()) {
         return MoveFailCode.NONE;
      }

      if (getV3() == 0) {
         return MoveFailCode.NO_EXIT;
      }

      return MoveFailCode.BARRIER;
   }

   /**
    * Gets the door type.
    *
    * @return the door type.
    */
   public DoorType getDoorType() {
      return DoorType.PUZZLE;
   }

   /**
    * Returns the debug string.
    *
    * @return The debug string.
    */
   public String toDebugString() {
      StringBuffer buffer = new StringBuffer();

      buffer.append("             ");
      buffer.append("Door Type: " + getDoorType());
      buffer.append("\n");

      return buffer.toString();
   }

}
