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

package org.tdod.ether.taimpl.cosmos;

import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.ta.cosmos.Lair;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;

/**
 * A default implementation class for a lair.
 *
 * @author Ron Kinney
 *
 */
public class DefaultLair implements Lair {

   private static final int MIN_RUNE_SCROLL_VNUM = 2;
   private static final int MAX_RUNE_SCROLL_VNUM = 9;
   private static final int ITEM_DROP_START_VNUM = 10;

   private int _mob;
   private int _numberOfMobs;
   private int _item;

   /**
    * Creates a DefaultLair.
    *
    * @param mob the mob living in this lair.
    * @param numberOfMobs the total number of mobs living in this lair.
    * @param item the item dropped by this lair.  null is returned if nothing is dropped.
    */
   public DefaultLair(int mob, int numberOfMobs, int item) {
      _mob = mob;
      _numberOfMobs = numberOfMobs;
      _item = item;
   }

   /**
    * Gets the mob living in this lair.
    *
    * @return the mob living in this lair.
    */
   public int getMob() {
      return _mob;
   }

   /**
    * Gets the total number of mobs living in this lair.
    *
    * @return the total number of mobs living in this lair.
    */
   public int getNumberOfMobs() {
      return _numberOfMobs;
   }

   /**
    * Gets the item dropped by this lair.  null is returned if nothing is dropped.
    *
    * @return the item dropped by this lair.  null is returned if nothing is dropped.
    */
   public Item getItem() {
      // From the looks of it, 2-9 are reserved for the Runestaff quest.
      if (_item >= MIN_RUNE_SCROLL_VNUM && _item <= MAX_RUNE_SCROLL_VNUM) {
         return DefaultAppFactory.createRuneScroll(_item);
      } else if (_item >= ITEM_DROP_START_VNUM) {
         return WorldManager.getItem(_item);
      } else {
         return null;
      }
   }
}
