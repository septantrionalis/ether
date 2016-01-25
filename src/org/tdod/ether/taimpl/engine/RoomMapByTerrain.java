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

package org.tdod.ether.taimpl.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.taimpl.mobs.enums.Terrain;

/**
 * This helper class holds the list of rooms separated by terrain.
 * @author rkinney
 */
public final class RoomMapByTerrain {

   private static Log _log = LogFactory.getLog(RoomMapByTerrain.class);

   private static HashMap<Terrain, ArrayList<Room>> _roomMapByTerrain = new HashMap<Terrain, ArrayList<Room>>();

   /**
    * A private constructor to enforce the singleton design pattern.
    */
   private RoomMapByTerrain() {
   }

   /**
    * Gets the room map.
    * @return the room map.
    */
   public static HashMap<Terrain, ArrayList<Room>> getMap() {
      initializeMobMapByTerrain();
      return _roomMapByTerrain;
   }

   /**
    * Initializes the mob by terrain hashmap.
    */
   private static void initializeMobMapByTerrain() {
      synchronized (_roomMapByTerrain) {
         if (_roomMapByTerrain.isEmpty()) {
            _log.info("_mobMapByTerrain is empty.  Populating...");
            Collection<Room> rooms = WorldManager.getArea().getRoomMap().values();
            Iterator<Room> itr = rooms.iterator();
            while (itr.hasNext()) {
               Room room = itr.next();

               ArrayList<Room> roomsByTerrain = _roomMapByTerrain.get(room.getTerrain());
               if (roomsByTerrain == null) {
                  roomsByTerrain = new ArrayList<Room>();
                  _roomMapByTerrain.put(room.getTerrain(), roomsByTerrain);
               }
               roomsByTerrain.add(room);
            }
            printMobMapByTerrain();
         }
      }
   }

   /**
    * Prints the mob map.
    */
   private static void printMobMapByTerrain() {
      StringBuffer buffer = new StringBuffer();
      Iterator<Terrain> iterator = _roomMapByTerrain.keySet().iterator();
      while (iterator.hasNext()) {
         Terrain terrain = iterator.next();
         ArrayList<Room> rooms = _roomMapByTerrain.get(terrain);
         buffer.append(terrain + "=" + rooms.size() + ",");
      }
      _log.info("Number of rooms in each terrain:" + buffer);
   }

}
