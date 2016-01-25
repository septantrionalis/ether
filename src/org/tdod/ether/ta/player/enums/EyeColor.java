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
 * Eye color.
 * @author rkinney
 */
public enum EyeColor {

   /**
    * An invalid eye color.  This is the default enum that is set.
    */
   INVALID(0, "invalid eyes"),

   /**
    * Yellow.
    */
   YELLOW(1, "yellow eyes"),

   /**
    * Green.
    */
   GREEN(2, "green eyes"),

   /**
    * Blue.
    */
   BLUE(3, "blue eyes"),

   /**
    * Violet.
    */
   VIOLET(4, "violet eyes"),

   /**
    * Red.
    */
   RED(5, "red eyes"),

   /**
    * Brown.
    */
   BROWN(6, "brown eyes"),

   /**
    * Hazel.
    */
   HAZEL(7, "hazel eyes"),

   /**
    * Gray.
    */
   GRAY(8, "gray eyes"),

   /**
    * Black.
    */
   BLACK(9, "black eyes");

   private int           _index;
   private String        _description;

   /**
    * Creates a new EyeColor.
    * @param index the index of the enum.
    * @param description the enum description.
    */
   EyeColor(int index, String description) {
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
    * Gets the eye color based on index.
    * @param index the index of the eye color.
    * @return an eye color.
    */
   public static EyeColor getEyeColor(int index) {
      EyeColor[] eyeColor = EyeColor.values();

      return eyeColor[index];
   }

}
