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

package org.tdod.ether.ta.player;

import java.io.Serializable;

/**
 * Interface for mana.
 * @author rkinney
 */
public interface Mana extends Serializable {

   /**
    * Gets the current mana.
    * @return the current mana.
    */
   int getCurMana();

   /**
    * Sets the current mana.
    * @param curMana the current mana.
    */
   void setCurMana(int curMana);

   /**
    * Gets the maximum mana.
    * @return the maximum mana.
    */
   int getMaxMana();

   /**
    * Sets the maximum mana.
    * @param maxMana the maximum mana.
    */
   void setMaxMana(int maxMana);

   /**
    * Subtracts the amount of mana from the current pool.
    * @param value the amount of mana to subtract.
    */
   void subtractCurMana(int value);

   /**
    * Adds the amount of mana to the current pool.
    * @param amount the amount of mana to add.
    */
   void addCurMana(int amount);

   /**
    * Restores the current mana.
    */
   void restoreMana();

}
