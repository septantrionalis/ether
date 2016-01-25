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

import org.tdod.ether.ta.cosmos.Teleporter;

/**
 * A default implementation for teleporters.
 *
 * @author Ron Kinney
 */
public class DefaultTeleporter implements Teleporter {

   private int      _vnum;
   private String   _victimMessage;
   private String   _toRoomMessage;
   private String   _fromRoomMessage;

   /**
    * Creates a new DefaultTeleporter.
    */
   public DefaultTeleporter() {
   }

   /**
    * Gets the teleporter vnum.
    *
    * @return the teleporter vnum.
    */
   public int getVnum() {
      return _vnum;
   }

   /**
    * Sets  the teleporter vnum.
    *
    * @param vnum the teleporter vnum.
    */
   public void setVnum(int vnum) {
      _vnum = vnum;
   }

   /**
    * Gets the message that will be displayed to the player.
    *
    * @return the message that will be displayed to the player.
    */
   public String getVictimMessage() {
      return _victimMessage;
   }

   /**
    * Sets the message that will be displayed to the player.
    *
    * @param victimMessage the message that will be displayed to the player.
    */
   public void setVictimMessage(String victimMessage) {
      _victimMessage = victimMessage;
   }

   /**
    * Gets the message that will be displayed to the destination room.
    *
    * @return the message that will be displayed to the destination room.
    */
   public String getToRoomMessage() {
      return _toRoomMessage;
   }

   /**
    * Sets the message that will be displayed to the destination room.
    *
    * @param toRoomMessage the message that will be displayed to the destination room.
    */
   public void setToRoomMessage(String toRoomMessage) {
      _toRoomMessage = toRoomMessage;
   }

   /**
    * Gets the message that will be displayed to the original room.
    *
    * @return the message that will be displayed to the original room.
    */
   public String getFromRoomMessage() {
      return _fromRoomMessage;
   }

   /**
    * Sets the message that will be displayed to the original room.
    *
    * @param fromRoomMessage the message that will be displayed to the original room.
    */
   public void setFromRoomMessage(String fromRoomMessage) {
      _fromRoomMessage = fromRoomMessage;
   }

}
