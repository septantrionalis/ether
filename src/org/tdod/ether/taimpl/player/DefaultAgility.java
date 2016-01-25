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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.player.Agility;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Agility - This is how agile your character is. This determines how fast your character may run,
 * how many attacks you get, how often you dodge an attack and how long you may run before
 * tripping.
 */
public class DefaultAgility extends AbstractStat implements Agility {

   private static final long serialVersionUID = 1741878076780035656L;

   private static Log _log = LogFactory.getLog(DefaultAgility.class);

   private static final int[] _attackNumberBonus = {
      0,                               // 0
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,    // 1 - 10
      0, 0, 0, 0, 1, 1, 1, 1, 1, 1,    // 11 - 20
      1, 1, 1, 1, 1, 1, 1, 1, 1, 2,    // 21 - 30
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2,    // 31 - 40
      2, 2, 2, 2, 2, 2, 2, 2, 2, 2     // 41 - 50
   };

   private static final int[] _dodgeBonus = {
      0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      1, 1, 1, 1, 1, 1, 2, 2, 2, 2,
      3, 3, 3, 3, 4, 4, 4, 4, 5, 5,
      5, 5, 6, 6, 6, 6, 7, 7, 7, 7,
      8, 8, 8, 8, 9, 9, 9, 9, 9, 9
   };

   private static final int[] _tripModifier = {
      0,
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
      11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
      21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
      31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
      41, 42, 43, 44, 45, 46, 47, 48, 49, 50
   };

   /**
    * Creates a new DefaultAgility.
    */
   public DefaultAgility() {
   }

   /**
    * Creates a new DefaultAgility at the specified value.
    * @param value the agility.
    */
   public DefaultAgility(int value) {
      setValue(value);
   }

   /**
    * Gets the attack number bonus.
    * @return the attack number bonus.
    */
   public int getAttackNumberBonus() {
      return _attackNumberBonus[getModifiedStat()];
   }

   /**
    * Gets the dodge bonus.
    * @return the dodge bonus.
    */
   public int getDodgeBonus() {
      return _dodgeBonus[getModifiedStat()];
   }

   /**
    * Gets the trip modifier.
    * @return the trip modifier.
    */
   public int getTripModifier() {
      return _tripModifier[getModifiedStat()];
   }

   /**
    * Gets the look description based on the value of the agility.
    * @return the look description.
    */
   public String getDescription() {
      if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.AGI_MSG_3_LIMIT)).intValue()) {
         return TaMessageManager.AMSG2.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.AGI_MSG_2_LIMIT)).intValue()) {
         return MessageManager.AMSG3.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.AGI_MSG_1_LIMIT)).intValue()) {
         return TaMessageManager.AMSG1.getMessage();
      }

      _log.error("Error stat, " + getModifiedStat() + "," + this.getClass());

      return ERROR_STRING;
   }
}
