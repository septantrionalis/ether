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
import org.tdod.ether.ta.cosmos.Lair;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.engine.RandomMobEngine;
import org.tdod.ether.ta.manager.MobTypeCount;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.Dice;

/**
 * The default implementation class for the random mob engine.
 *
 * @author Ron Kinney
 */
public class DefaultRandomMobEngine implements RandomMobEngine {

   private static Log _log = LogFactory.getLog(DefaultRandomMobEngine.class);

   /**
    * Creates a new DefaultRandomMobEngine.
    */
   public DefaultRandomMobEngine() {
   }

   /**
    * Populates the world with random mobs based on internal rules.
    */
   public void populateRandomMobs() {
      _log.debug("Populating random mobs...");
      Iterator<Terrain> iterator = RoomMapByTerrain.getMap().keySet().iterator();
      while (iterator.hasNext()) {
         MobTypeCount terrainMobTypeCount = new MobTypeCount();
         Terrain terrain = iterator.next();
         ArrayList<Room> rooms = RoomMapByTerrain.getMap().get(terrain);

         for (Room room : rooms) {
            MobTypeCount roomMobTypeCount = getRoomMobCount(room);
            terrainMobTypeCount.addMobTypeCount(roomMobTypeCount);
         }

         int mobsAdded = populateTerrain(terrain, rooms, terrainMobTypeCount);

         if (mobsAdded > 0) {
            _log.info(terrain + "(rooms=" + rooms.size() + ")=" + terrainMobTypeCount.toString()
                  + ".  Added " + mobsAdded + " random mobs.");
         }
      }
   }

   /**
    * Populates mobs based on terrain.
    *
    * @param terrain the terrain.
    * @param rooms the list of rooms.
    * @param terrainMobTypeCount the total number of mobs in the given terrain.
    * @return the total number of mobs added.
    */
   private int populateTerrain(Terrain terrain, ArrayList<Room> rooms, MobTypeCount terrainMobTypeCount) {
      int mobsToAdd = 0;
      float percent = ((float) terrainMobTypeCount.getRandomMobCount() / (float) rooms.size());
      if (percent * 100 < terrain.getMobThreshold()) {
         float percentThreshold = terrain.getMobThreshold() / 100F;
         mobsToAdd = (int) ((percentThreshold - percent) * (float) rooms.size());
         int maxRoomIndex = rooms.size() - 1;
         for (int count = 0; count < mobsToAdd; count++) {
            int roomIndex = Dice.roll(0,  maxRoomIndex);
            Room room = rooms.get(roomIndex);
            Mob mob = getRandomMob(terrain).clone(room);
            if (mob != null) {
               room.placeMob(mob);
            } else {
               _log.error("Terrain " + terrain + " needs mobs, but none are defined.");
            }
         }
      }

      return mobsToAdd;
   }

   /**
    * Gets a single random mob based on terrain.
    *
    * @param terrain the terrain.
    * @return a random mob.
    */
   private Mob getRandomMob(Terrain terrain) {
      ArrayList<Mob> mobs = WorldManager.getMobByTerrain(terrain);
      int maxMobIndex = mobs.size() - 1;
      int index = Dice.roll(0, maxMobIndex);

      return mobs.get(index);
   }

   /**
    * Gets the mob count of a single room.
    *
    * @param room the room in question.
    * @return a MobTypeCount.
    */
   private MobTypeCount getRoomMobCount(Room room) {
      int randomMobCount = 0;
      int lairMobCount = 0;

      ArrayList<Integer> lairMobVnumList = new ArrayList<Integer>();
      for (Lair lair : room.getLairs()) {
         lairMobVnumList.add(Integer.valueOf(lair.getMob()));
      }

      for (Mob mob : room.getMobs()) {
         if (lairMobVnumList.contains(Integer.valueOf(mob.getVnum()))) {
            lairMobCount++;
         } else {
            randomMobCount++;
         }
      }

      return new MobTypeCount(randomMobCount, lairMobCount);
   }

}
