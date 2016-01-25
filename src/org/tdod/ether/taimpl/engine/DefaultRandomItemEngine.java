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
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.engine.RandomItemEngine;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;

/**
 * This class defines the interface for the engine that generates random items through out the world.
 * @author Ron Kinney
 */
public class DefaultRandomItemEngine implements RandomItemEngine {

   private static Log _log = LogFactory.getLog(DefaultRandomItemEngine.class);

   private static final String WORLD_DROP_STRING = "WORLD_DROP_STRING";

   private static final String WORLD_ITEM_DROPS
      = PropertiesManager.getInstance().getProperty(PropertiesManager.WORLD_ITEM_DROPS);

   /**
    * Creates a new DefaultRandomItemEngine.
    */
   public DefaultRandomItemEngine() {
   }

   /**
    * Populates the world with random items based on internal rules.
    */
   public void populateRandomItems() {
      _log.debug("Populating random items...");
      Iterator<Terrain> iterator = RoomMapByTerrain.getMap().keySet().iterator();
      while (iterator.hasNext()) {
         int itemCount = 0;
         Terrain terrain = iterator.next();
         ArrayList<Room> rooms = RoomMapByTerrain.getMap().get(terrain);

         for (Room room : rooms) {
            itemCount += getRoomItemCount(room);
         }

         int itemsDropped = populateTerrain(terrain, rooms, itemCount);

         if (itemsDropped > 0) {
            _log.info(terrain + "(rooms=" + rooms.size() + ")=" + itemCount
                  + ".  Dropped " + itemsDropped + " random items.");
         }
      }
   }

   /**
    * Gets the total items on the ground that was spawned randomly.
    * @param room the room.
    * @return the total items on the ground that was spawned randomly.
    */
   private int getRoomItemCount(Room room) {
      int count = 0;
      for (Item item : room.getItemsOnGround()) {
         String source = WorldManager.getItemsInExistance().get(item);
         if (WORLD_DROP_STRING.equals(source)) {
            count++;
         }
      }
      return count;
   }

   /**
    * Populates mobs based on terrain.
    *
    * @param terrain the terrain.
    * @param rooms the list of rooms.
    * @param terrainItemCount the total number of mobs in the given terrain.
    * @return the total number of mobs added.
    */
   private int populateTerrain(Terrain terrain, ArrayList<Room> rooms, int terrainItemCount) {
      int itemsToAdd = 0;
      float percent = ((float) terrainItemCount / (float) rooms.size());
      if (percent * 100 < terrain.getItemThreshold()) {
         float percentThreshold = terrain.getItemThreshold() / 100F;
         itemsToAdd = (int) ((percentThreshold - percent) * (float) rooms.size());
         int maxRoomIndex = rooms.size() - 1;
         for (int count = 0; count < itemsToAdd; count++) {
            int roomIndex = Dice.roll(0,  maxRoomIndex);
            Room room = rooms.get(roomIndex);
            Item item = getRandomItem();
            if (item != null) {
               room.dropItemInRoom(item.clone(WORLD_DROP_STRING));
            }
         }
      }

      return itemsToAdd;
   }

   /**
    * Gets a random item defined in the properties file.
    * @return a random item.
    */
   private Item getRandomItem() {
      try {
         String[] split = WORLD_ITEM_DROPS.split(" ");
         int roll = Dice.roll(0, split.length - 1);
         int vnum = Integer.valueOf(split[roll]);
         return WorldManager.getItem(vnum);
      } catch (Exception e) {
         _log.error("Error parsing the world item drop list: " + WORLD_ITEM_DROPS);
      }
      return null;
   }
}
