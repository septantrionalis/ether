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

package org.tdod.ether.taimpl.player;

import org.tdod.ether.ta.player.Stat;
import org.tdod.ether.util.PropertiesManager;

/**
 * The abstract class for stats.
 * @author rkinney
 */
public abstract class AbstractStat implements Stat {

   private static final long serialVersionUID = 5951328704243391905L;

   /**
    * The minimum possible stat value.
    */
   public static final int MIN_STAT = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MIN_STAT)).intValue();

   /**
    * The maximum possible stat value.
    */
   public static final int MAX_STAT = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_STAT)).intValue();

   protected static final String ERROR_STRING = "ERROR";

   private int _value;
   private int _boostTimer = 0;
   private int _drainTimer = 0;
   private int _boost = 0;
   private int _drain = 0;

   /**
    * Gets the statistic value.
    * @return the statistic value.
    */
   public int getValue() {
      return _value;
   }

   /**
    * Sets the statistic value.
    * @param value the statistic value.
    */
   public void setValue(int value) {
      _value = value;
   }

   /**
    * Gets the modified stat.
    * @return the modified stat.
    */
   public int getModifiedStat() {
      int modifiedStat = getValue() + _boost - _drain;
      if (modifiedStat < MIN_STAT) {
         modifiedStat = MIN_STAT;
      } else if (modifiedStat > MAX_STAT) {
         modifiedStat = MAX_STAT;
      }
      return modifiedStat;
   }

   /**
    * Gets the string representation of this stat.
    * @return the string representation of this stat.
    */
   public String toString() {
      return "" + getModifiedStat();
   }

   /**
    * Adds the stat value to this stat value.
    * @param stat the stat to add to this stat.
    */
   public void add(Stat stat) {
      _value += stat.getValue();
   }

   /**
    * Gets the boost timer.
    * @return the boost timer.
    */
   public int getBoostTimer() {
      return _boostTimer;
   }

   /**
    * Sets the boost timer.
    * @param boostTimer the boost timer.
    */
   public void setBoostTimer(int boostTimer) {
      _boostTimer = boostTimer;
   }

   /**
    * Sets the drain timer.
    * @return the drain timer.
    */
   public int getDrainTimer() {
      return _drainTimer;
   }

   /**
    * Sets the drain timer.
    * @param drainTimer the drain timer.
    */
   public void setDrainTimer(int drainTimer) {
      _drainTimer = drainTimer;
   }

   /**
    * Gets the amount this stat is boosted by.
    * @return the amount this stat is boosted by.
    */
   public int getBoost() {
      return _boost;
   }

   /**
    * Sets the boost value for this stat.
    * @param boost the value to boost this stat by.
    */
   public void setBoost(int boost) {
      _boost = boost;
   }

   /**
    * Gets the amount this stat is drained by.
    * @return the amount this stat is drained by.
    */
   public int getDrain() {
      return _drain;
   }

   /**
    * Sets the drain value for this stat.
    * @param drain the value to drain this stat by.
    */
   public void setDrain(int drain) {
      _drain = drain;
   }

   /**
    * Decreases all timers for this stat.
    * @return true if any timers were expired.
    */
   public boolean decreaseTimers() {
      boolean effectExpired = false;
      if (_drainTimer > 0) {
         _drainTimer--;
         if (_drainTimer <= 0) {
            _drain = 0;
            effectExpired = true;
         }
      }

      if (_boostTimer > 0) {
         _boostTimer--;
         if (_boostTimer <= 0) {
            _boost = 0;
            effectExpired = true;
         }
      }

      return effectExpired;
   }

   /**
    * Resets all boost and drain timers and values.
    */
   public void resetEnchants() {
      _drainTimer = 0;
      _boostTimer = 0;
      _drain = 0;
      _boost = 0;
   }

   /**
    * Removes all draining effects.
    */
   public void removeDrain() {
      _drainTimer = 0;
      _drain = 0;
   }

}
