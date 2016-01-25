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

package org.tdod.ether.ta.cosmos.enums;

/**
 * An enumeration of fail codes when an entity is moving.
 *
 * @author Ron Kinney
 */
public enum MoveFailCode {

   /**
    * A cucessful move.
    */
   NONE,

   /**
    * No exit in that direction.
    */
   NO_EXIT,

   /**
    * The door is locked.
    */
   EXIT_LOCKED,

   /**
    * The room is full.
    */
   ROOM_FULL,

   /**
    * A barrier.
    */
   BARRIER,

   /**
    * The barrier was unlocked.
    */
   UNLOCK_BARRIER,

   /**
    * A rogue picked the barrier.
    */
   PICKED_BARRIER,

   /**
    * Mobs cannot enter this room.
    */
   NO_MOBS_ALLOWED;

}
