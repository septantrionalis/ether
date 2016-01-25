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
import org.tdod.ether.ta.player.Physique;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Physique - This is how strong your character is, how hard he/she hits in battle, and how much
 * can be carried at one time. (see also, encumberance)
 *
 * Range is 14 to 29 for reroll.
 */
public class DefaultPhysique extends AbstractStat implements Physique {

   private static final long serialVersionUID = -4922718611510894526L;

   private static Log _log = LogFactory.getLog(DefaultPhysique.class);

   private static final int[] _physicalDamageBonus = {
      0,
      0, 0, 0, 0, 0, 1, 1, 1, 1, 2,
      2, 2, 2, 2, 3, 3, 3, 3, 3, 3,
      4, 4, 4, 4, 5, 5, 5, 5, 5, 6,
      6, 6, 6, 7, 7, 7, 7, 8, 8, 8,
      8, 9, 9, 9, 9, 10, 10, 10, 10, 11
   };

   private static final int[] _maxEncumbrance = {
      0,
      50,  100, 150, 200, 250, 300, 350, 400, 450, 500,
      550, 600, 650, 700, 750, 800, 850, 900, 950, 1000,
      1050, 1100, 1150, 1200, 1250, 1300, 1350, 1400, 1450, 1500,
      1550, 1600, 1650, 1700, 1750, 1800, 1850, 1900, 1950, 2000,
      2050, 2100, 2150, 2200, 2250, 2300, 2350, 2400, 2450, 2500
   };

   /**
    * Creates a new DefaultPhysique.
    */
   public DefaultPhysique() {
   }

   /**
    * Creates a new DefaultPhysique with a specified value.
    * @param value the physique value.
    */
   public DefaultPhysique(int value) {
      setValue(value);
   }

   /**
    * Gets the damage bonus for physical attacks.
    * @return the damage bonus for physical attacks.
    */
   public int getPhysicalDamageBonus() {
      return _physicalDamageBonus[getModifiedStat()];
   }

   /**
    * Gets the maximum encumbrance.
    * @return the maximum encumbrance.
    */
   public int getMaxEncumbrance() {
      return _maxEncumbrance[getModifiedStat()];
   }

   /**
    * Gets the look description for this stat.
    * @return the look description.
    */
   public String getDescription() {
      if (getModifiedStat() <= new Integer(PropertiesManager.getInstance()
            .getProperty(PropertiesManager.PHY_MSG_3_LIMIT)).intValue()) {
         return TaMessageManager.PMSG3.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance()
            .getProperty(PropertiesManager.PHY_MSG_2_LIMIT)).intValue()) {
         return TaMessageManager.PMSG2.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance()
            .getProperty(PropertiesManager.PHY_MSG_1_LIMIT)).intValue()) {
         return TaMessageManager.PMSG1.getMessage();
      }

      _log.error("Error stat, " + getModifiedStat() + "," + this.getClass());
      return ERROR_STRING;
   }

}
