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

package org.tdod.ether.taimpl.combat;

import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.combat.SpellResultEnum;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;

/**
 * An implementation class that defines a the result of a spell.
 *
 * @author Ron Kinney
 */
public class DefaultSpellResult implements SpellResult {

   protected static final int EFFECTS_TIME =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.PLAYER_EFFECT_TIME)).intValue();

   private SpellResultEnum   _spellResultEnum = SpellResultEnum.SUCCESS;
   private int               _numberEffect;
   private int               _effectTime;

   /**
    * Creates a new DefaultSpellResult.
    */
   public DefaultSpellResult() {
      _effectTime = ((int) (EFFECTS_TIME * Dice.generateNumberVariance()));
   }

   /**
    * Gets the spell result enumeration.
    *
    * @return the spell result enumeration.
    */
   public final SpellResultEnum getSpellResultEnum() {
      return _spellResultEnum;
   }

   /**
    * Sets the spell result enumeration.
    *
    * @param spellResultEnum the spell result enumeration.
    */
   public final void setSpellResultEnum(SpellResultEnum spellResultEnum) {
      _spellResultEnum = spellResultEnum;
   }

   /**
    * Gets the effect value.
    *
    * @return the effect value.
    */
   public final int getNumberEffect() {
      return _numberEffect;
   }

   /**
    * Sets the number effect.
    *
    * @param numberEffect The number effect.
    */
   public final void setNumberEffect(int numberEffect) {
      _numberEffect = numberEffect;
   }

   /**
    * Gets the effect duration.
    *
    * @return The effect duration.
    */
   public final int getEffectTime() {
      return _effectTime;
   }
}
