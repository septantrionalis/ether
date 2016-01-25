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

/**
 * A public interface for a mobs weapon.
 * @author Ron Kinney
 */
public interface MobWeapon {

   /**
    * Gets the weapon name.
    * @return the weapon name.
    */
   String getName();

   /**
    * Sets the weapon name.
    * @param name the weapon name.
    */
   void setName(String name);

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

   /**
    * Gets the minimum damage for the weapon.
    * @return the minimum damage for the weapon.
    */
   int getMinDamage();

   /**
    * Sets the minimum damage for the weapon.
    * @param minDamage the minimum damage for the weapon.
    */
   void setMinDamage(int minDamage);

   /**
    * Gets the maximum damage for the weapon.
    * @return the maximum damage for the weapon.
    */
   int getMaxDamage();

   /**
    * Sets the maximum damage for the weapon.
    * @param maxDamage the maximum damage for the weapon.
    */
   void setMaxDamage(int maxDamage);

   /**
    * Gets the value.
    * @return the value.
    */
   int getV1();

   /**
    * Sets the value.
    * @param v1 the value.
    */
   void setV1(int v1);

}
