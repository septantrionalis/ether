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
 * An enumeration of player complexions.
 * @author Ron Kinney
 */
public enum Complexion {

   /**
    * An invalid complexion.
    */
   INVALID(0, "invalid complexion"),

   /**
    * A porcelain complexion.
    */
   PORCELAIN(1, "porcelain complexion"),

   /**
    * A pale complexion.
    */
   PALE(2, "pale complexion"),

   /**
    * A creamy complexion.
    */
   CREAMY(3, "creamy complexion"),

   /**
    * A fair complexion.
    */
   FAIR(4, "fair complexion"),

   /**
    * A rosey complexion.
    */
   ROSEY(5, "rosey complexion"),

   /**
    * A tan complexion.
    */
   TAN(6, "tan complexion"),

   /**
    * A golden complexion.
    */
   GOLDEN(7, "golden complexion"),

   /**
    * A bronzed complexion.
    */
   BRONZED(8, "bronzed complexion"),

   /**
    * A ruddy complexion.
    */
   RUDDY(9, "ruddy complexion"),

   /**
    * An ebony complexion.
    */
   EBONY(10, "ebony complexion");

   private int           _index;
   private String        _description;

   /**
    * Creates a new Complexion.
    * @param index the index of this enum.
    * @param description the description of this complexion.
    */
   Complexion(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the complexion index.
    * @return the complexion index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * The complexion description.
    * @return the complexion description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets a complexion based on the index.
    * @param index the index of the complexion.
    * @return a complexion at the specified index.
    */
   public static Complexion getComplexion(int index) {
      Complexion[] complexion = Complexion.values();

      return complexion[index];
   }

}
