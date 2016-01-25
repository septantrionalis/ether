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

import org.tdod.ether.util.Dice;

/**
 * An enumeration of slots.
 * @author rkinney
 */
public enum Slots {

   /**
    * An apple.
    */
   APPLE("Apple"),

   /**
    * An orange.
    */
   ORANGE("Orange"),

   /**
    * A lemon.
    */
   LEMON("Lemon"),

   /**
    * A cherry.
    */
   CHERRY("Cherry"),

   /**
    * A crown.
    */
   CROWN("Crown");

   private String        _description;

   /**
    * Creates a new Slots enum.
    * @param description a description of the slot.
    */
   Slots(String description) {
      _description = description;
   }

   /**
    * Gets the description.
    * @return the description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Returns a random slot.
    * @return a random slot.
    */
   public static Slots getRandomSlot() {
      Slots[] slots = Slots.values();

      int index = Dice.roll(0, slots.length - 1);

      return slots[index];
   }

}
