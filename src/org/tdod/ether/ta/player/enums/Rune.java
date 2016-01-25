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
 * An enumeration of runes.
 * @author rkinney
 */
public enum Rune {

   /**
    * None.
    */
   NONE(0, "None"),

   /**
    * White.
    */
   WHITE(1, "White"),

   /**
    * Yellow.
    */
   YELLOW(2, "Yellow"),

   /**
    * Green.
    */
   GREEN(3, "Green"),

   /**
    * Blue.
    */
   BLUE(4, "Blue"),

   /**
    * Violet.
    */
   VIOLET(5, "Violet"),

   /**
    * Red.
    */
   RED(6, "Red"),

   /**
    * Orange.
    */
   ORANGE(7, "Orange"),

   /**
    * Gold.
    */
   GOLD(8, "Gold");

   private int           _index;
   private String        _name;

   /**
    * The constructor.
    * @param index the index of the rune.
    * @param name the name of the rune.
    */
   Rune(int index, String name) {
      _index = index;
      _name = name;
   }

   /**
    * Gets the index.
    * @return the index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the name.
    * @return the name.
    */
   public String getName() {
      return _name;
   }

   /**
    * Gets the rune based on index.
    * @param index the index of the rune.
    * @return a rune.
    */
   public static Rune getRune(int index) {
      Rune[] rune = Rune.values();

      return rune[index];
   }

}
