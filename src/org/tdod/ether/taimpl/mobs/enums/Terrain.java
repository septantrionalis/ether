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

package org.tdod.ether.taimpl.mobs.enums;

import org.tdod.ether.util.PropertiesManager;

public enum Terrain {

   DUNGEON1(0,"Dungeon1", 
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DUNGEON1_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DUNGEON1_ITEM_THRESHOLD))),
   DUNGEON2(1,"Dungeon2",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DUNGEON2_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DUNGEON2_ITEM_THRESHOLD))),
   DUNGEON3(2,"Dungeon3",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DUNGEON3_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DUNGEON3_ITEM_THRESHOLD))),
   MOUNTAINS(3,"Mountains",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MOUNTAINS_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MOUNTAINS_ITEM_THRESHOLD))),
   FOREST(4,"Forest",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FOREST_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FOREST_ITEM_THRESHOLD))),
   SWAMP(5,"Swamp",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.SWAMP_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.SWAMP_ITEM_THRESHOLD))),   
   DESERT(6,"Desert",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DESERT_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.DESERT_ITEM_THRESHOLD))),
   JUNGLE(7,"Jungle",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.JUNGLE_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.JUNGLE_ITEM_THRESHOLD))),
   FIRE1(8,"Fire1",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FIRE1_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FIRE1_ITEM_THRESHOLD))),
   FIRE2(9,"Fire2",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FIRE2_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.FIRE2_ITEM_THRESHOLD))),
   SPECIAL(10,"Special",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.SPECIAL_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.SPECIAL_ITEM_THRESHOLD))),
   TOWN(11, "Town",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN1_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN1_ITEM_THRESHOLD))),
   /* TOWN2(12, "Town 2",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN2_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN2_ITEM_THRESHOLD))),
   TOWN3(13, "Town 3",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN3_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN3_ITEM_THRESHOLD))),
   TOWN4(14, "Town 4",
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN4_MOB_THRESHOLD)),
            Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.TOWN4_ITEM_THRESHOLD))),*/
   UNKNOWN(99, "Unknown"),
   INVALID(-1,"Invalid");

   private int    _index;
   private String _description;
   private int    _mobThreshold;
   private int    _itemThreshold;
   
   Terrain(int index, String description) {
      this(index, description, 0, 0);
   }
   
   Terrain(int index, String description, int mobThreshold, int itemThreshold) {
      _index = index;
      _description = description;
      _mobThreshold = mobThreshold;
      _itemThreshold = itemThreshold;
   }
   
   public int getIndex() {
      return _index;
   }
   
   public String getDescription() {
      return _description;
   }

   public int getMobThreshold() {
      return _mobThreshold;
   }

   public int getItemThreshold() {
      return _itemThreshold;      
   }

   public static Terrain getTerrain(int index) {
      for (Terrain terrain:Terrain.values()) {
         if (terrain.getIndex() == index) {
            return terrain;
         }
      }
      
      return Terrain.INVALID;
   }
   
}
