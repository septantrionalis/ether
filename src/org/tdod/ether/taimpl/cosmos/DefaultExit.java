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

package org.tdod.ether.taimpl.cosmos;

import org.tdod.ether.ta.cosmos.Door;
import org.tdod.ether.ta.cosmos.Exit;

/**
 * A default implementation class for an exit.
 *
 * @author Ron Kinney
 */
public class DefaultExit implements Exit {

   private int               _toRoom = -1;
   private ExitDirectionEnum _exitDirection = ExitDirectionEnum.UNKNOWN;
   private Door              _door;

   /**
    * Creates a new DefaultExit.
    */
   public DefaultExit() {
   }

   /**
    * Creates a default exit with the following attributes set.
    *
    * @param exitDirectionEnum The direction of the exit.
    * @param toRoom The destination room vnum.
    */
   public DefaultExit(ExitDirectionEnum exitDirectionEnum, int toRoom) {
      _exitDirection = exitDirectionEnum;
      _toRoom = toRoom;
   }

   /**
    * Gets the destination room vnum.
    *
    * @return the destination room vnum.
    */
   public int getToRoom() {
      return _toRoom;
   }

   /**
    * Sets the destination room vnum.
    *
    * @param toRoom the destination room vnum.
    */
   public void setToRoom(int toRoom) {
      _toRoom = toRoom;
   }

   /**
    * Gets the direction of this exit.
    *
    * @return the direction of this exit.
    */
   public ExitDirectionEnum getExitDirection() {
      return _exitDirection;
   }

   /**
    * Sets the direction of this exit.
    *
    * @param exitDirection the direction of this exit.
    */
   public void setExitDirection(ExitDirectionEnum exitDirection) {
      _exitDirection = exitDirection;
   }

   /**
    * Gets the door object.
    *
    * @return the door object.
    */
   public Door getDoor() {
      return _door;
   }

   /**
    * Sets the door object.
    *
    * @param door the door object.
    */
   public void setDoor(Door door) {
      _door = door;
   }

   /**
    * Determines if this exit can be passed through.  I.E., not null, or unlocked.
    *
    * @return true if this exit can be passed through.
    */
   public boolean isPassable() {
      if (_door == null || (_door != null && _door.isUnlocked())) {
         return true;
      }

      return false;
   }
}
