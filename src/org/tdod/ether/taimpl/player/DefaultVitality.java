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

import java.text.MessageFormat;

import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.taimpl.mobs.enums.Gender;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * The default implementation class for vitality.
 * @author rkinney
 */
public class DefaultVitality implements Vitality {

   private static final long serialVersionUID = -7198215864661985827L;

   private int _curVitality;
   private int _maxVitality;
   private int _drained;

   /**
    * Creates a new DefaultVitality().
    */
   public DefaultVitality() {
   }

   /**
    * Gets the current vitality.
    * @return the current vitality.
    */
   public int getCurVitality() {
      return _curVitality;
   }

   /**
    * Sets the current vitality.
    * @param curVitality the current vitality.
    */
   public void setCurVitality(int curVitality) {
      _curVitality = curVitality;
   }

   /**
    * Subracts the amount from the current vitality.
    * @param value the amount to subtract.
    */
   public void subtractCurVitality(int value) {
      _curVitality -= value;
   }

   /**
    * Adds the specified amount to the current vitality.
    * @param amount the amount to add.
    */
   public void addCurVitality(int amount) {
      _curVitality += amount;
      if (_curVitality > getMaxVitality()) {
         _curVitality = getMaxVitality();
      }
   }

   /**
    * Gets the maximum vitality.
    * @return the maximum vitality.
    */
   public int getMaxVitality() {
      int maxVitality = _maxVitality - _drained;
      if (maxVitality < 1) {
         maxVitality = 1;
      }
      return maxVitality;
   }

   /**
    * Sets the maximum vitality.
    * @param maxVitality the maximum vitality.
    */
   public void setMaxVitality(int maxVitality) {
      _maxVitality = maxVitality;
   }

   /**
    * Gets the percent of vitality left.
    * @return the percent of vitality left.
    */
   public int getVitalityPercent() {
      float percent = ((float) _curVitality / (float) getMaxVitality()) * 100;

      return (int) percent;
   }

   /**
    * Gets the amount that the vitality is drained.
    * @return the amount that the vitality is drained.
    */
   public int getDrained() {
      return _drained;
   }

   /**
    * Sets the amount that the vitality is drained.
    * @param drained the amount that the vitality is drained.
    */
   public void setDrained(int drained) {
      _drained = drained;
   }

   /**
    * Gets the look description based on gender.
    * @param gender the gender.
    * @return a look description.
    */
   public String getDescription(Gender gender) {
      String preFormat = "";

      float percent = getVitalityPercent();

      if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_1_PERCENT)).intValue()) {
         preFormat = TaMessageManager.HMSG5.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_2_PERCENT)).intValue()) {
         preFormat = TaMessageManager.HMSG4.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_3_PERCENT)).intValue()) {
         preFormat = TaMessageManager.HMSG3.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_4_PERCENT)).intValue()) {
         preFormat = TaMessageManager.HMSG2.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_5_PERCENT)).intValue()) {
         preFormat = TaMessageManager.HMSG1.getMessage();
      }

      String description = MessageFormat.format(preFormat, gender.getNoun().toLowerCase());

      return description;
   }

   /**
    * Gets the mob look description.
    * @param name the name of the mob.
    * @return a mob look description.
    */
   public String getMobDescription(String name) {
      String preFormat = "";

      float percent = ((float) _curVitality / (float) getMaxVitality()) * 100;

      if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_1_PERCENT)).intValue()) {
         preFormat = TaMessageManager.MMSG5.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_2_PERCENT)).intValue()) {
         preFormat = TaMessageManager.MMSG4.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_3_PERCENT)).intValue()) {
         preFormat = TaMessageManager.MMSG3.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_4_PERCENT)).intValue()) {
         preFormat = TaMessageManager.MMSG2.getMessage();
      } else if (percent >= new Integer(PropertiesManager.getInstance().getProperty(
            PropertiesManager.HEALTH_MSG_5_PERCENT)).intValue()) {
         preFormat = TaMessageManager.MMSG1.getMessage();
      }

      String description = MessageFormat.format(preFormat, name);

      return description;
   }

}
