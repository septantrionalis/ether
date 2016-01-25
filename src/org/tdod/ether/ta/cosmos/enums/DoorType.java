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

package org.tdod.ether.ta.cosmos.enums;


/**
 * Represents a door type.
 *
 * @author Ron Kinney
 */
public enum DoorType {

   /**
    * The key to unlock the door is an item.
    */
   ITEM_KEY(0),

   /**
    * Door is locked for players with the specified rune.
    */
   HAS_RUNE(-1),

   /**
    * A minimum specified rune is required for passage.
    */
   MINIMUM_RUNE(-2),

   /**
    * The destination room is a private room.
    */
   PRIVATE_ROOM(-3),

   /**
    * Only promoted characters can pass.
    */
   PROMOTE_DOOR(-4),

   /**
    * Unlocking the door requires the player to solve a puzzle.
    */
   PUZZLE(-8),

   /**
    * An undefined door type.
    */
   NULL(-100);

   private int _index;

   /**
    * Creates a new door type.
    *
    * @param index The enumeration index.
    */
   DoorType(int index) {
      _index = index;
   }

   /**
    * Gets the enumeration index.
    *
    * @return the enumeration index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the door type at the specified index.
    *
    * @param index the index of the door type.
    *
    * @return the door type at the specified index.
    */
   public static DoorType getDoorType(int index) {
      DoorType[] types = DoorType.values();
      for (DoorType type : types) {
         if (index == type.getIndex()) {
            return type;
         }
      }

      return null;
   }

}
