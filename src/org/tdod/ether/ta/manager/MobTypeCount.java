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

package org.tdod.ether.ta.manager;


/**
 * This class holds the mob type count during repop.  This is mainly
 * used for reporting purposes.
 * @author Ron Kinney
 */
public class MobTypeCount {

   private int _randomMobCount = 0;
   private int _lairMobCount = 0;

   /**
    * Creates a new MobTypeCount.
    */
   public MobTypeCount() {
   }

   /**
    * Creates a new MobTypeCount with defined internal state.
    * @param randomMobCount the random mob count.
    * @param lairMobCount the lair mob count.
    */
   public MobTypeCount(int randomMobCount, int lairMobCount) {
      _randomMobCount = randomMobCount;
      _lairMobCount = lairMobCount;
   }

   /**
    * Gets the random mob count.
    * @return the random mob count.
    */
   public int getRandomMobCount() {
      return _randomMobCount;
   }

   /**
    * Sets the random mob count.
    * @return the random mob count.
    */
   public int getLairMobCount() {
      return _lairMobCount;
   }

   /**
    * Adds the mob type count to this object.
    * @param mobTypeCount the mob type count to add.
    */
   public void addMobTypeCount(MobTypeCount mobTypeCount) {
      _randomMobCount += mobTypeCount.getRandomMobCount();
      _lairMobCount += mobTypeCount.getLairMobCount();
   }

   /**
    * Returns a string representation of this object.
    * @return a string representation of this object.
    */
   public String toString() {
      return "random mobs(" + _randomMobCount + ")," + "lair mobs(" + _lairMobCount + ")";
   }
}
