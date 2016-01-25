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

import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.RaceData;
import org.tdod.ether.ta.player.enums.RaceEnum;

/**
 * The implemenation class for race data.
 * @author rkinney
 */
public class DefaultRaceData implements RaceData {

   private int       _vitality;
   private RaceEnum  _race = RaceEnum.INVALID;
   private BaseStats _statModifiers = new DefaultBaseStats();
   private int       _minStartingGold;
   private int       _maxStartingGold;

   /**
    * Creates a new DefaultRaceData.
    */
   public DefaultRaceData() {
   }

   /**
    * Gets the vitality bonus for this race.
    * @return the vitality bonus for this race.
    */
   public int getVitality() {
      return _vitality;
   }

   /**
    * Sets the vitality bonus for this race.
    * @param vitality the vitality bonus for this race.
    */
   public void setVitality(int vitality) {
      _vitality = vitality;
   }

   /**
    * Gets the race.
    * @return the race.
    */
   public RaceEnum getRace() {
      return _race;
   }

   /**
    * Sets the race.
    * @param race the race.
    */
   public void setRace(RaceEnum race) {
      _race = race;
   }

   /**
    * Gets the starting stat modifiers.
    * @return the starting stat modifiers.
    */
   public BaseStats getStatModifiers() {
      return _statModifiers;
   }

   /**
    * Gets the minimum starting gold bonus.
    * @return the minimum starting gold bonus.
    */
   public int getMinStartingGold() {
      return _minStartingGold;
   }

   /**
    * Sets the minimum starting gold bonus.
    * @param minGold the minimum starting gold bonus.
    */
   public void setMinStartingGold(int minGold) {
      _minStartingGold = minGold;
   }

   /**
    * Gets the maximum starting gold bonus.
    * @return the maximum starting gold bonus.
    */
   public int getMaxStartingGold() {
      return _maxStartingGold;
   }

   /**
    * Sets the maximum starting gold bonus.
    * @param maxGold the maximum starting gold bonus.
    */
   public void setMaxStartingGold(int maxGold) {
      _maxStartingGold = maxGold;
   }

}
