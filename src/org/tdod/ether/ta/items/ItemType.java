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

package org.tdod.ether.ta.items;

/**
 * An enumeration of item types.
 * @author Ron Kinney
 */
public enum ItemType {

   /**
    * The item is of type equipment.
    */
   EQUIPMENT("equipment"),

   /**
    * The item is of type weapon.
    */
   WEAPON("weapon"),

   /**
    * The item is of type armor.
    */
   ARMOR("armor"),

   /**
    * The item is of type rune scroll.
    */
   RUNE_SCROLL("rune scroll");

   private String _description;

   /**
    * Creates a new ItemType.
    * @param description the item description.
    */
   ItemType(String description) {
      _description = description;
   }

   /**
    * Gets the item description.
    * @return the item description.
    */
   public String getDescription() {
      return _description;
   }
}
