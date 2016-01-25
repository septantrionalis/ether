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
 * Hair length.
 * @author rkinney
 */
public enum HairLength {

   /**
    * An invalid hair length.  This is the default enum that is set.
    */
   INVALID(0, "invalid hair length"),

   /**
    * Short.
    */
   SHORT(1, "short hair"),

   /**
    * Shoulder.
    */
   SHOULDER_LENGTH(2, "shoulder length hair"),

   /**
    * Waist.
    */
   WAIST_LENGTH(3, "waist length hair"),

   /**
    * Knee.
    */
   KNEE_LENGTH(4, "knee length hair"),

   /**
    * Ankle.
    */
   ANKLE_LENGTH(5, "ankle length hair");

   private int           _index;
   private String        _description;

   /**
    * Creates a new HairColor.
    * @param index the index of the enum.
    * @param description the enum description.
    */
   HairLength(int index, String description) {
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
    * Gets the hair length based on index.
    * @param index the index of the hair length.
    * @return a hair length.
    */
   public static HairLength getHairLength(int index) {
      HairLength[] hairLength = HairLength.values();

      return hairLength[index];
   }

}
