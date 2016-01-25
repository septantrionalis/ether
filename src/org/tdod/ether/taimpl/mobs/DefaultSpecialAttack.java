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

package org.tdod.ether.taimpl.mobs;

import org.tdod.ether.ta.mobs.SpecialAttack;
import org.tdod.ether.util.Dice;

/**
 * The default implementation for a special attack.
 * @author Ron Kinney
 */
public class DefaultSpecialAttack implements SpecialAttack {

   private int _minSpecialDamage;
   private int _maxSpecialDamage;
   private String _specialAttackDescription;

   /**
    * Creates a new DefaultSpecialAttack.
    */
   public DefaultSpecialAttack() {
   }

   /**
    * Checks if the special attack is defined.
    * @return true if the special attack is defined.
    */
   public boolean hasSpecialAttack() {
      if ("".equals(_specialAttackDescription)) {
         return false;
      }
      return true;
   }

   /**
    * Gets the the minimum damage.
    * @return the the minimum damage.
    */
   public int getMinSpecialDamage() {
      return _minSpecialDamage;
   }

   /**
    * Sets the the minimum damage.
    * @param minSpecialDamage the the minimum damage.
    */
   public void setMinSpecialDamage(int minSpecialDamage) {
      _minSpecialDamage = minSpecialDamage;
   }

   /**
    * Gets the the maximum damage.
    * @return the the maximum damage.
    */
   public int getMaxSpecialDamage() {
      return _maxSpecialDamage;
   }

   /**
    * Sets the the maximum damage.
    * @param maxSpecialDamage the the maximum damage.
    */
   public void setMaxSpecialDamage(int maxSpecialDamage) {
      _maxSpecialDamage = maxSpecialDamage;
   }

   /**
    * Gets the special attack description.
    * @return the special attack description.
    */
   public String getSpecialAttackDescription() {
      return _specialAttackDescription;
   }

   /**
    * Sets the special attack description.
    * @param specialAttackDescription the special attack description.
    */
   public void setSpecialAttackDescription(String specialAttackDescription) {
      _specialAttackDescription = specialAttackDescription;
   }

   /**
    * Randomizes the damage.
    * @return a random damage for the special attack.
    */
   public int getRandomizedDamage() {
      return Dice.roll(_minSpecialDamage, _maxSpecialDamage);
   }
}
