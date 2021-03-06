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
import org.tdod.ether.ta.player.enums.Rune;

/**
 * This door prevents a player from entering if he has a specific rune.
 *
 * @author Ron Kinney
 */
public class HasRuneDoor extends AbstractDoor {

   /**
    * Gets the move fail code when a player attempts to pass through this door.
    *
    * @param player the player attempting to pass through this door.
    *
    * @return the move fail code.
    */
   public MoveFailCode getMoveFailCode(Player player) {
      if (getV4() != (player.getRune().getIndex())) {
         return MoveFailCode.BARRIER;
      }

      return MoveFailCode.NONE;
   }

   /**
    * Gets the door type.
    *
    * @return the door type.
    */
   public DoorType getDoorType() {
      return DoorType.HAS_RUNE;
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
      buffer.append(", Rune: " + Rune.getRune(getV4()));
      buffer.append("\n");

      return buffer.toString();
   }
}
