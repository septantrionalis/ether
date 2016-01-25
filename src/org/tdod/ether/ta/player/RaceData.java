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

import org.tdod.ether.ta.player.enums.RaceEnum;

/**
 * An interface for data pertaining to race.
 * @author rkinney
 */
public interface RaceData {

   /**
    * Gets the vitality bonus for this race.
    * @return the vitality bonus for this race.
    */
   int getVitality();

   /**
    * Sets the vitality bonus for this race.
    * @param vitality the vitality bonus for this race.
    */
   void setVitality(int vitality);

   /**
    * Gets the race.
    * @return the race.
    */
   RaceEnum getRace();

   /**
    * Sets the race.
    * @param race the race.
    */
   void setRace(RaceEnum race);

   /**
    * Gets the starting stat modifiers.
    * @return the starting stat modifiers.
    */
   BaseStats getStatModifiers();

   /**
    * Gets the minimum starting gold bonus.
    * @return the minimum starting gold bonus.
    */
   int getMinStartingGold();

   /**
    * Sets the minimum starting gold bonus.
    * @param minGold the minimum starting gold bonus.
    */
   void setMinStartingGold(int minGold);

   /**
    * Gets the maximum starting gold bonus.
    * @return the maximum starting gold bonus.
    */
   int getMaxStartingGold();

   /**
    * Sets the maximum starting gold bonus.
    * @param maxGold the maximum starting gold bonus.
    */
   void setMaxStartingGold(int maxGold);

}
