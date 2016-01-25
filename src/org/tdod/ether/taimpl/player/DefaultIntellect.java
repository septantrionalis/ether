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
import org.tdod.ether.ta.player.Intellect;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Intellect - This is how smart your character is. Intellect improves your chance to-hit in
 * physical combat and also increases the potency of your offensive spells. For rogues, this
 * attribute also add to the chance of successfully picking a lock, avoiding a trap or robbing
 * another player.
 */
public class DefaultIntellect extends AbstractStat implements Intellect {

   private static final long serialVersionUID = 4000727213970910776L;

   private static Log _log = LogFactory.getLog(DefaultIntellect.class);

   // hit chance = base + diff + mobArmor - intBonus
   // diff = mobLevel-charLevel;
   // base = 12 (high is harder, lower is easier)
   // d20 >= hit chance
   private static final int[] _meleeHitBonus = {
      -1,
      -1, -1, -1, -1,  0,  0,  0,  0,  0, 1,
      1, 1, 1, 1, 2, 2, 2, 2, 2, 3,
      3, 3, 3, 3, 4, 4, 4, 4, 4, 5,
      5, 5, 5, 5, 5, 6, 6, 6, 6, 6,
      7, 7, 7, 7, 7, 8, 8, 8, 8, 8
   };

   private static final int[] _offensiveSpellPotency = {
      0,
      0,   0,  0,  0,  0,  0,  0,  0,  0,  0,
      5,  10, 15, 20, 25, 30, 35, 40, 45, 50,
      55, 60, 65, 70, 75, 80, 85, 90, 95, 100,
      105, 110, 115, 120, 125, 130, 135, 140, 145, 150,
      155, 160, 165, 170, 175, 180, 185, 190, 195, 200
   };

   // It seems as if 19 is the max int bonus for rogues.
   // success = base + diff + intBonus
   // base = 8 (high is harder, lower is easier)
   // diff = trapLevel-charLevel
   // d2 >= success
   private static final int[] _rogueSkillBonus = {
      1,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 2, 2, 3, 3, 4, 4, 5, 5,
      5, 5, 5, 5, 5, 6, 6, 6, 6, 6,
      7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
      8, 8, 8, 8, 8, 8, 8, 8, 8, 8
   };

   /**
    * Creates a new DefaultIntellect.
    */
   public DefaultIntellect() {
   }

   /**
    * Creates a new DefaultIntellect at the specified value.
    * @param value the players intellect.
    */
   public DefaultIntellect(int value) {
      setValue(value);
   }

   /**
    * Gets the melee hit bonus.
    * @return the melee hit bonus.
    */
   public int getMeleeHitBonus() {
      return _meleeHitBonus[getModifiedStat()];
   }

   /**
    * Gets the offensive spell modifier.
    * @return the offensive spell modifier.
    */
   public int getOffensiveSpellPotency() {
      return _offensiveSpellPotency[getModifiedStat()];
   }

   /**
    * Gets bonuses for rogues skills.
    * @return bonuses for rogues skills.
    */
   public int getRogueSkillBonus() {
      return _rogueSkillBonus[getModifiedStat()];
   }

   /**
    * Gets the skill look description.
    * @return the skill look description.
    */
   public String getDescription() {
      if (getModifiedStat() <= new Integer(
            PropertiesManager.getInstance().getProperty(PropertiesManager.INT_MSG_3_LIMIT)).intValue()) {
         return TaMessageManager.IMSG2.getMessage();
      } else if (getModifiedStat() <= new Integer(
            PropertiesManager.getInstance().getProperty(PropertiesManager.INT_MSG_2_LIMIT)).intValue()) {
         return MessageManager.IMSG3.getMessage();
      } else if (getModifiedStat() <= new Integer(
            PropertiesManager.getInstance().getProperty(PropertiesManager.INT_MSG_1_LIMIT)).intValue()) {
         return TaMessageManager.IMSG1.getMessage();
      }

      _log.error("Error stat, " + getModifiedStat() + "," + this.getClass());

      return ERROR_STRING;

   }


}
