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

package org.tdod.ether.taimpl.cosmos.enums;

/**
 * An enumeration of towns.
 * @author rkinney
 */
public enum Town {

   /**
    * Not a town.
    */
   NONE(0, "None"),

   /**
    * Town #1.
    */
   TOWN1(1, "Town 1"),

   /**
    * Town #2.
    */
   TOWN2(2, "Town 2"),

   /**
    * Town #3.
    */
   TOWN3(3, "Town 3"),

   /**
    * Town #4.
    */
   TOWN4(4, "Town 4"),

   /**
    * An invalid town.
    */
   INVALID(-1, "Invalid");

   private int           _index;
   private String        _description;

   /**
    * Creates a new town enumeration.
    * @param index the index of the town.
    * @param description a description of the town.
    */
   Town(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the index of the enum.
    * @return the index of the enum.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the description of the enum.
    * @return the description of the enum.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets the town enum based on the index.
    * @param index the index of the town.
    * @return a town.
    */
   public static Town getTown(int index) {
      Town[] town = Town.values();

      return town[index];
   }

}
