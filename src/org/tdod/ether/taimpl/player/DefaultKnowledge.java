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
import org.tdod.ether.ta.player.Knowledge;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Knowledge - This is how wise your character is. This affects the potency of spells you cast on
 * yourself or others.
 * @author rkinney
 */
public class DefaultKnowledge extends AbstractStat implements Knowledge {

   private static final long serialVersionUID = 5630067361599164535L;

   private static Log _log = LogFactory.getLog(DefaultKnowledge.class);

   private static final int[] _friendlySpellBonus = {
      0,
      0,   0,  0,  0,  0,  0,  0,  0,  0,  0,
      5,  10, 15, 20, 25, 30, 35, 40, 45, 50,
      55, 60, 65, 70, 75, 80, 85, 90, 95, 100,
      105, 110, 115, 120, 125, 130, 135, 140, 145, 150,
      155, 160, 165, 170, 175, 180, 185, 190, 195, 200
   };

   /**
    * Creates a new DefaultKnowledge.
    */
   public DefaultKnowledge() {
   }

   /**
    * Creates a new DefaultKnowledge with a specified value.
    * @param value the knowledge value
    */
   public DefaultKnowledge(int value) {
      setValue(value);
   }

   /**
    * Gets the bonus for friendly spells.
    * @return the friendly spell bonus.
    */
   public int getFriendlySpellBonus() {
      return _friendlySpellBonus[getModifiedStat()];
   }

   /**
    * Gets the look description.
    * @return the look description.
    */
   public String getDescription() {
      if (getModifiedStat() <= new Integer(PropertiesManager.getInstance()
            .getProperty(PropertiesManager.KNO_MSG_3_LIMIT)).intValue()) {
         return TaMessageManager.KMSG2.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance()
            .getProperty(PropertiesManager.KNO_MSG_2_LIMIT)).intValue()) {
         return MessageManager.KMSG3.getMessage();
      } else if (getModifiedStat() <= new Integer(PropertiesManager.getInstance()
            .getProperty(PropertiesManager.KNO_MSG_1_LIMIT)).intValue()) {
         return TaMessageManager.KMSG1.getMessage();
      }

      _log.error("Error stat, " + getModifiedStat() + "," + this.getClass());

      return ERROR_STRING;
   }

}
