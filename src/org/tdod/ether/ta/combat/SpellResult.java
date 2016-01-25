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

package org.tdod.ether.ta.combat;


/**
 * An interface that defines a the result of a spell.
 *
 * @author Ron Kinney
 */
public interface SpellResult {

   /**
    * Gets the spell result enumeration.
    *
    * @return the spell result enumeration.
    */
   SpellResultEnum getSpellResultEnum();

   /**
    * Sets the spell result enumeration.
    *
    * @param spellResultEnum the spell result enumeration.
    */
   void setSpellResultEnum(SpellResultEnum spellResultEnum);

   /**
    * Gets the effect value.
    *
    * @return the effect value.
    */
   int getNumberEffect();

   /**
    * Sets the number effect.
    *
    * @param numberEffect The number effect.
    */
   void setNumberEffect(int numberEffect);

   /**
    * Gets the effect duration.
    *
    * @return The effect duration.
    */
   int getEffectTime();
}
