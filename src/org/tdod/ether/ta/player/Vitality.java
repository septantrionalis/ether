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

import org.tdod.ether.taimpl.mobs.enums.Gender;

/**
 * Interface for vitality.
 * @author rkinney
 */
public interface Vitality extends Serializable {

   /**
    * Gets the current vitality.
    * @return the current vitality.
    */
   int getCurVitality();

   /**
    * Sets the current vitality.
    * @param curVitality the current vitality.
    */
   void setCurVitality(int curVitality);

   /**
    * Subracts the amount from the current vitality.
    * @param value the amount to subtract.
    */
   void subtractCurVitality(int value);

   /**
    * Gets the maximum vitality.
    * @return the maximum vitality.
    */
   int getMaxVitality();

   /**
    * Sets the maximum vitality.
    * @param maxVitality the maximum vitality.
    */
   void setMaxVitality(int maxVitality);

   /**
    * Gets the look description based on gender.
    * @param gender the gender.
    * @return a look description.
    */
   String getDescription(Gender gender);

   /**
    * Gets the mob look description.
    * @param name the name of the mob.
    * @return a mob look description.
    */
   String getMobDescription(String name);

   /**
    * Adds the specified amount to the current vitality.
    * @param amount the amount to add.
    */
   void addCurVitality(int amount);

   /**
    * Gets the percent of vitality left.
    * @return the percent of vitality left.
    */
   int getVitalityPercent();

   /**
    * Gets the amount that the vitality is drained.
    * @return the amount that the vitality is drained.
    */
   int getDrained();

   /**
    * Sets the amount that the vitality is drained.
    * @param drained the amount that the vitality is drained.
    */
   void setDrained(int drained);

}
