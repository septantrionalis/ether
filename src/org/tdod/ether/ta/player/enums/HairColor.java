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
 * Hair color.
 * @author rkinney
 */
public enum HairColor {

   /**
    * An invalid hair color.  This is the default enum that is set.
    */
   INVALID(0, "invalid"),

   /**
    * White.
    */
   WHITE(1, "white"),

   /**
    * Silver.
    */
   SILVER(2, "silver"),

   /**
    * Gray.
    */
   GRAY(3, "gray"),

   /**
    * Blonde.
    */
   BLONDE(4, "blonde"),

   /**
    * Copper.
    */
   COPPER(5, "copper"),

   /**
    * Red.
    */
   RED(6, "red"),

   /**
    * Brown.
    */
   BROWN(7, "brown"),

   /**
    * Black.
    */
   BLACK(8, "black"),

   /**
    * Blue.
    */
   BLUE(9, "blue"),

   /**
    * Green.
    */
   GREEN(10, "green"),

   /**
    * Purple.
    */
   PURPLE(11, "purple");

   private int           _index;
   private String        _description;

   /**
    * Creates a new HairColor.
    * @param index the index of the enum.
    * @param description the enum description.
    */
   HairColor(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the index.
    * @return the index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the description.
    * @return the description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets the hair color based on index.
    * @param index the index of the hair color.
    * @return an hair color.
    */
   public static HairColor getHairColor(int index) {
      HairColor[] hairColor = HairColor.values();

      return hairColor[index];
   }

}
