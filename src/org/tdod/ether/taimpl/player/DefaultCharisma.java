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
import org.tdod.ether.ta.player.Charisma;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Charisma - This is how attractive and influential you are. Along with Intellect, charisma
 * makes a difference in the price of items at the various shops. A low charisma is sure to make you
 * pay more for an item then the listed price. A high charisma allows you to buy at lower prices.
 *
 * Range is 8? to 21 for reroll.
 */
public class DefaultCharisma extends AbstractStat implements Charisma {

   private static final long serialVersionUID = -8325399017804290814L;

   private static Log _log = LogFactory.getLog(DefaultCharisma.class);

   // Increase purchase cost in percent.
   // final value deviates -1 to +1
   private static final int[] _buyModifier = {
      50,
      50, 50, 50, 50, 50, 50, 50, 40, 40, 40,
      30, 30, 30, 30, 20, 20, 20, 10, 10, 10,
      10, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0
   };

   /**
    * Creates a new DefaultCharisma.
    */
   public DefaultCharisma() {
   }

   /**
    * Creates a new DefaultCharisma at the specified value.
    * @param value the agility.
    */
   public DefaultCharisma(int value) {
      setValue(value);
   }

   /**
    * Gets the buy modifier.  This is a percent value to which the final cost of the item will be
    * modified.
    * @return the buy modifier.
    */
   public int getBuyModifier() {
      return _buyModifier[getModifiedStat()];
   }

   /**
    * Gets the look description based on the value of the agility.
    * @return the look description.
    */
   public String getDescription() {
      if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.CHA_MSG_4_LIMIT)).intValue()) {
         return TaMessageManager.CMSG4.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.CHA_MSG_3_LIMIT)).intValue()) {
         return TaMessageManager.CMSG3.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.CHA_MSG_2_LIMIT)).intValue()) {
         return TaMessageManager.CMSG2.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.CHA_MSG_1_LIMIT)).intValue()) {
         return TaMessageManager.CMSG1.getMessage();
      }

      _log.error("Error stat, " + getModifiedStat() + "," + this.getClass());

      return ERROR_STRING;
   }

}
