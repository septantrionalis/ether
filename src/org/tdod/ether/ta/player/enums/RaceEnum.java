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

package org.tdod.ether.ta.player.enums;

/**
 * An enumeration of player races.
 * @author rkinney
 */
public enum RaceEnum {

   /**
    * An invalid race.
    */
   INVALID(0, "Invalid"),

   /**
    * Elven.
    */
   ELVEN(1, "Elven"),

   /**
    * Dwarvish.
    */
   DWARVEN(2, "Dwarven"),

   /**
    * Gnomish.
    */
   GNOMISH(3, "Gnomish"),

   /**
    * Human.
    */
   HUMAN(4, "Human"),

   /**
    * Goblin.
    */
   GOBLIN(5, "Goblin"),

   /**
    * Half-Ogre.
    */
   HALF_OGRE(6, "Half-Ogre");

   private int           _index;
   private String        _name;

   /**
    * The constructor.
    * @param index the index of the race.
    * @param name the name of the race.
    */
   RaceEnum(int index, String name) {
      _index = index;
      _name = name;
   }

   /**
    * Gets the index of the race.
    * @return the index of the race.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the name of the race.
    * @return the name of the race.
    */
   public String getName() {
      return _name;
   }

   /**
    * Gets a race based on index.
    * @param index the index.
    * @return a race.
    */
   public static RaceEnum getRace(int index) {
      RaceEnum[] race = RaceEnum.values();

      return race[index];
   }

}
