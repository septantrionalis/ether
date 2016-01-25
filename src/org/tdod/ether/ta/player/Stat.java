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

package org.tdod.ether.ta.player;

import java.io.Serializable;

/**
 * The interface defining a stat.
 * @author rkinney
 */
public interface Stat extends Serializable {

   /**
    * Gets the stat value.
    * @return the stat value.
    */
   int getValue();

   /**
    * Sets the stat value.
    * @param value the stat value.
    */
   void setValue(int value);

   /**
    * Gets the modified stat.
    * @return the modified stat.
    */
   int getModifiedStat();

   /**
    * Gets the stat look description.
    * @return the stat look description.
    */
   String getDescription();

   /**
    * Adds a stat to this stat.
    * @param stat the stat to add to this stat.
    */
   void add(Stat stat);

   /**
    * Gets the boost timer.
    * @return the boost timer.
    */
   int getBoostTimer();

   /**
    * Sets the boost timer.
    * @param boostTimer the boost timer.
    */
   void setBoostTimer(int boostTimer);

   /**
    * Decreases all stat timers.
    * @return true if any stat timers expired.
    */
   boolean decreaseTimers();

   /**
    * Gets the drain timer.
    * @return the drain timer.
    */
   int getDrainTimer();

   /**
    * Sets the drain timer.
    * @param drainTimer the drain timer.
    */
   void setDrainTimer(int drainTimer);

   /**
    * Gets the boost value.
    * @return the boost value.
    */
   int getBoost();

   /**
    * Sets the boost value.
    * @param boost the boost value.
    */
   void setBoost(int boost);

   /**
    * Gets the drain value.
    * @return the drain value.
    */
   int getDrain();

   /**
    * Sets the drain value.
    * @param drain the drain value.
    */
   void setDrain(int drain);

   /**
    * Resets all boost and drain effects and timers.
    */
   void resetEnchants();

   /**
    * Removes all draining effects from the stat.
    */
   void removeDrain();
}
