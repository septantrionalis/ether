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

import java.util.HashMap;
import java.util.Set;

import org.tdod.ether.ta.cosmos.Area;
import org.tdod.ether.ta.cosmos.Room;

/**
 * The implmentation class of an area.
 *
 * @author Ron Kinney
 */
public class DefaultArea implements Area {

   private static final long serialVersionUID = -7331024264869116703L;

   private HashMap<Integer, Room> _rooms = new HashMap<Integer, Room>();

   /**
    * Creates a new DefaultArea.
    */
   public DefaultArea() {
   }

   /**
    * Gets a room.
    *
    * @param roomNum The room number in question.
    *
    * @return a room associated with the room number.
    */
   public Room getEntry(int roomNum) {
      return _rooms.get(Integer.valueOf(roomNum));
   }

   /**
    * Gets the entire room map.
    *
    * @return the entire room map.
    */
   public HashMap<Integer, Room> getRoomMap() {
      return _rooms;
   }

   /**
    * Initializes the area.
    */
   public void initialize() {
      Set<Integer> roomVnums = _rooms.keySet();
      for (Integer roomVnum : roomVnums) {
         Room room = _rooms.get(roomVnum);
         room.initialize();
         room.setRoomNumber(roomVnum);
      }
   }

}
