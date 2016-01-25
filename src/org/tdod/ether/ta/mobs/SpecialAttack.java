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

package org.tdod.ether.ta.mobs;

/**
 * This is an interface that defines a special attack.
 * @author minex
 */
public interface SpecialAttack {

   /**
    * Checks if the special attack is defined.
    * @return true if the special attack is defined.
    */
   boolean hasSpecialAttack();

   /**
    * Gets the the minimum damage.
    * @return the the minimum damage.
    */
   int getMinSpecialDamage();

   /**
    * Sets the the minimum damage.
    * @param minSpecialDamage the the minimum damage.
    */
   void setMinSpecialDamage(int minSpecialDamage);

   /**
    * Gets the the maximum damage.
    * @return the the maximum damage.
    */
   int getMaxSpecialDamage();

   /**
    * Sets the the maximum damage.
    * @param maxSpecialDamage the the maximum damage.
    */
   void setMaxSpecialDamage(int maxSpecialDamage);

   /**
    * Gets the special attack description.
    * @return the special attack description.
    */
   String getSpecialAttackDescription();

   /**
    * Sets the special attack description.
    * @param specialAttackDescription the special attack description.
    */
   void setSpecialAttackDescription(String specialAttackDescription);

   /**
    * Randomizes the damage.
    * @return a random damage for the special attack.
    */
   int getRandomizedDamage();
}
