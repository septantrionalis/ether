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

/**
 * An interface defining the API for a teleporter.
 *
 * @author Ron Kinney
 */
public interface Teleporter {

   /**
    * Gets the teleporter vnum.
    *
    * @return the teleporter vnum.
    */
   int getVnum();

   /**
    * Sets  the teleporter vnum.
    *
    * @param vnum the teleporter vnum.
    */
   void setVnum(int vnum);

   /**
    * Gets the message that will be displayed to the player.
    *
    * @return the message that will be displayed to the player.
    */
   String getVictimMessage();

   /**
    * Sets the message that will be displayed to the player.
    *
    * @param victimMessage the message that will be displayed to the player.
    */
   void setVictimMessage(String victimMessage);

   /**
    * Gets the message that will be displayed to the destination room.
    *
    * @return the message that will be displayed to the destination room.
    */
   String getToRoomMessage();

   /**
    * Sets the message that will be displayed to the destination room.
    *
    * @param toRoomMessage the message that will be displayed to the destination room.
    */
   void setToRoomMessage(String toRoomMessage);

   /**
    * Gets the message that will be displayed to the original room.
    *
    * @return the message that will be displayed to the original room.
    */
   String getFromRoomMessage();

   /**
    * Sets the message that will be displayed to the original room.
    *
    * @param fromRoomMessage the message that will be displayed to the original room.
    */
   void setFromRoomMessage(String fromRoomMessage);

}
