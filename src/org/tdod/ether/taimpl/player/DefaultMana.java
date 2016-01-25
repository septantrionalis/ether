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

import org.tdod.ether.ta.player.Mana;

/**
 * Implementation class for mana.
 * @author rkinney
 */
public class DefaultMana implements Mana {

   private static final long serialVersionUID = 8450129239235394025L;

   private int _curMana;
   private int _maxMana;

   /**
    * Creates a new DefaultMana.
    */
   public DefaultMana() {
   }

   /**
    * Gets the current mana.
    * @return the current mana.
    */
   public int getCurMana() {
      return _curMana;
   }

   /**
    * Sets the current mana.
    * @param curMana the current mana.
    */
   public void setCurMana(int curMana) {
      _curMana = curMana;
   }

   /**
    * Gets the maximum mana.
    * @return the maximum mana.
    */
   public int getMaxMana() {
      return _maxMana;
   }

   /**
    * Sets the maximum mana.
    * @param maxMana the maximum mana.
    */
   public void setMaxMana(int maxMana) {
      _maxMana = maxMana;
   }

   /**
    * Subtracts the amount of mana from the current pool.
    * @param value the amount of mana to subtract.
    */
   public void subtractCurMana(int value) {
      _curMana -= value;
      if (_curMana < 0) {
         _curMana = 0;
      }
   }

   /**
    * Adds the amount of mana to the current pool.
    * @param amount the amount of mana to add.
    */
   public void addCurMana(int amount) {
      _curMana += amount;
      if (_curMana > _maxMana) {
         _curMana = _maxMana;
      }
   }

   /**
    * Restores the current mana.
    */
   public void restoreMana() {
      _curMana = _maxMana;
   }

}
