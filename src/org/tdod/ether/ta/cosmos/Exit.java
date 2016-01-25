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

import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;

/**
 * An interface that defines an exit.
 *
 * @author Ron Kinney
 */
public interface Exit {

   /**
    * Gets the destination room vnum.
    *
    * @return the destination room vnum.
    */
   int getToRoom();

   /**
    * Sets the destination room vnum.
    *
    * @param toRoom the destination room vnum.
    */
   void setToRoom(int toRoom);

   /**
    * Gets the direction of this exit.
    *
    * @return the direction of this exit.
    */
   ExitDirectionEnum getExitDirection();

   /**
    * Sets the direction of this exit.
    *
    * @param exitDirection the direction of this exit.
    */
   void setExitDirection(ExitDirectionEnum exitDirection);

   /**
    * Gets the door object.
    *
    * @return the door object.
    */
   Door getDoor();

   /**
    * Sets the door object.
    *
    * @param door the door object.
    */
   void setDoor(Door door);

   /**
    * Determines if this exit can be passed through.  I.E., not null, or unlocked.
    *
    * @return true if this exit can be passed through.
    */
   boolean isPassable();

}
