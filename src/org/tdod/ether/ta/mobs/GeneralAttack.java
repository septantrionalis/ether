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

import org.tdod.ether.ta.mobs.enums.MobWeaponType;
import org.tdod.ether.taimpl.mobs.enums.AttackEffectEnum;

/**
 * The interface defining a general attack.
 * @author Ron Kinney
 */
public interface GeneralAttack {

   /**
    * Gets the total number of attacks.
    * @return the total number of attacks
    */
   int getNumAttacks();

   /**
    * Sets the total number of attacks.
    * @param numAttacks the total number of attacks.
    */
   void setNumAttacks(int numAttacks);

   /**
    * Gets the weapon string.
    * @return the weapon string.
    */
   String getWeapon();

   /**
    * Sets the weapon string.
    * @param weapon the weapon string.
    */
   void setWeapon(String weapon);

   /**
    * Gets the minimum damage.
    * @return the minimum damage.
    */
   int getMinDamage();

   /**
    * Sets the minimum damage.
    * @param minDamage the minimum damage.
    */
   void setMinDamage(int minDamage);

   /**
    * Gets the maximum damage.
    * @return the maximum damage.
    */
   int getMaxDamage();

   /**
    * Sets the maximum damage.
    * @param maxDamage the maximum damage.
    */
   void setMaxDamage(int maxDamage);

   /**
    * Gets the attack effect.
    * @return the attack effect.
    */
   AttackEffectEnum getAttackEffect();

   /**
    * Sets the attack effect.
    * @param attackEffect the attack effect.
    */
   void setAttackEffect(AttackEffectEnum attackEffect);

   /**
    * Gets the minimum attack effect.
    * @return the minimum attack effect.
    */
   int getMinAttackEffect();

   /**
    * Sets the minimum attack effect.
    * @param minAttackEffect the minimum attack effect.
    */
   void setMinAttackEffect(int minAttackEffect);

   /**
    * Gets the maximum attack effect.
    * @return the maximum attack effect.
    */
   int getMaxAttackEffect();

   /**
    * Sets the maximum attack effect.
    * @param maxAttackEffect the maximum attack effect.
    */
   void setMaxAttackEffect(int maxAttackEffect);

   /**
    * Gets the mob weapon type.
    * @return the mob weapon type.
    */
   MobWeaponType getMobWeaponType();

   /**
    * Sets the mob weapon type.
    * @param mobWeaponType the mob weapon type.
    */
   void setMobWeaponType(MobWeaponType mobWeaponType);

}
