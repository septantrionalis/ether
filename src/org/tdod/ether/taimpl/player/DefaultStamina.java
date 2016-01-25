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

import org.tdod.ether.ta.player.Stamina;

/**
 * Stamina - This determines how many hit points your character starts with, how many you get
 * when you gain a higher level, how often you get thirsty or hungry, how many spaces you can
 * run before you have to rest and how many hit points you regenerate every round.
 */
public class DefaultStamina extends AbstractStat implements Stamina {

   private static final long serialVersionUID = 3761911611485664809L;

   private static final int[] _hpBonus = {
      0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
      1, 1, 1, 1, 2, 2, 2, 2, 2, 3,
      3, 3, 3, 3, 4, 4, 4, 4, 4, 5,
      5, 5, 5, 6, 6, 6, 6, 7, 7, 7,
      7, 8, 8, 8, 8, 9, 9, 9, 9, 10
   };

   // chance to regen hp per 5 seconds.
   private static final int[] _hpRegeneration = {
       3,
       4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
      14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
      24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
      34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
      44, 45, 46, 47, 48, 49, 50, 51, 52, 53
   };

   // Additional values taken when sustenance is reduced.
   private static final int[] _sustenanceModifier = {
      10,
      9, 9, 9, 8, 8, 8, 7, 7, 7, 6,
      6, 6, 5, 5, 5, 4, 4, 4, 3, 3,
      3, 2, 2, 2, 1, 1, 1, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
   };

   /**
    * Creates a new DefaultStamina.
    */
   public DefaultStamina() {
   }

   /**
    * Creates a new DefaultStamina with a specified value.
    * @param value the value of the stamina.
    */
   public DefaultStamina(int value) {
      setValue(value);
   }

   /**
    * Gets the hit point bonus per level.
    * @return the hit point bonus per level.
    */
   public int getHpBonus() {
      return _hpBonus[getModifiedStat()];
   }

   /**
    * Gets the hp regeneation.
    * @return the hp regeneration.
    */
   public int gethpRegeneration() {
      return _hpRegeneration[getModifiedStat()];
   }

   /**
    * Determines how fast the player needs to eat and drink.
    * @return the sustenance modifier.
    */
   public int getSustenanceModifier() {
      return _sustenanceModifier[getModifiedStat()];
   }

   /**
    * Gets the look description of this stat.  Stamina is not currently displayed.
    * @return the look description of this stat.  Stamina is not currently displayed.
    */
   public String getDescription() {
      return "TBD";
   }

}
