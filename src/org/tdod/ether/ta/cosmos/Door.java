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

package org.tdod.ether.ta.cosmos;

import org.tdod.ether.ta.cosmos.enums.DoorType;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.player.Player;

/**
 * An interface that defines a door.
 *
 * @author Ron Kinney
 */
public interface Door {

   /**
    * Gets the door type.
    *
    * @return the door type.
    */
   DoorType getDoorType();

   /**
    * Gets the value at index 0.
    *
    * @return the value at index 0.
    */
   int getV0();

   /**
    * Sets the value for index 0.
    *
    * @param v0 the value for index 0.
    */
   void setV0(int v0);

   /**
    * Gets the value at index 3.
    *
    * @return the value at index 3.
    */
   int getV3();

   /**
    * Sets the value for index 3.
    *
    * @param v3 the value for index 3.
    */
   void setV3(int v3);

   /**
    * Gets the value at index 4.
    *
    * @return the value at index 4.
    */
   int getV4();

   /**
    * Sets the value for index 4.
    *
    * @param v4 the value for index 4.
    */
   void setV4(int v4);

   /**
    * Gets the value at index 5.
    *
    * @return the value at index 5.
    */
   int getV5();

   /**
    * Sets the value for index 5.
    *
    * @param v5 the value for index 5.
    */
   void setV5(int v5);

   /**
    * Gets the value at index 6.
    *
    * @return the value at index 6.
    */
   int getV6();

   /**
    * Sets the value for index 6.
    *
    * @param v6 the value for index 6;
    */
   void setV6(int v6);

   // Door state that isn't saved in a file.

   /**
    * Checks if this door is unlocked.
    *
    * @return true if this door is unlocked.
    */
   boolean isUnlocked();

   /**
    * Sets the locked state of this door.
    *
    * @param isUnlocked The locked state of this door.
    */
   void setIsUnlocked(boolean isUnlocked);

   /**
    * Determines the move fail code if a player attempts to go through this door.
    *
    * @param player The player attempting to go through this door.
    *
    * @return The move fail code.
    */
   MoveFailCode getMoveFailCode(Player player);

   /**
    * Returns the debug string.
    *
    * @return The debug string.
    */
   String toDebugString();

}
