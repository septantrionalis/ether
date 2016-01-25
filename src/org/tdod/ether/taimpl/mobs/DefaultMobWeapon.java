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

import org.tdod.ether.ta.mobs.MobWeapon;
import org.tdod.ether.ta.mobs.enums.MobWeaponType;

/**
 * The default implementation class for a mobs weapon.
 * @author Ron Kinney
 */
public class DefaultMobWeapon implements MobWeapon {

   private String _name;
   private MobWeaponType _mobWeaponType = null;
   private int _minDamage;
   private int _maxDamage;
   private int _v1;

   /**
    * Creates a new DefaultMobWeapon.
    */
   public DefaultMobWeapon() {
   }

   //**********
   // Methods inherited from EquipmentDatabase.
   //**********

   /**
    * Gets the weapon name.
    * @return the weapon name.
    */
   public String getName() {
      return _name;
   }

   /**
    * Sets the weapon name.
    * @param name the weapon name.
    */
   public void setName(String name) {
      _name = name;
   }

   /**
    * Gets the mob weapon type.
    * @return the mob weapon type.
    */
   public MobWeaponType getMobWeaponType() {
      return _mobWeaponType;
   }

   /**
    * Sets the mob weapon type.
    * @param mobWeaponType the mob weapon type.
    */
   public void setMobWeaponType(MobWeaponType mobWeaponType) {
      _mobWeaponType = mobWeaponType;
   }

   /**
    * Gets the minimum damage for the weapon.
    * @return the minimum damage for the weapon.
    */
   public int getMinDamage() {
      return _minDamage;
   }

   /**
    * Sets the minimum damage for the weapon.
    * @param minDamage the minimum damage for the weapon.
    */
   public void setMinDamage(int minDamage) {
      _minDamage = minDamage;
   }

   /**
    * Gets the maximum damage for the weapon.
    * @return the maximum damage for the weapon.
    */
   public int getMaxDamage() {
      return _maxDamage;
   }

   /**
    * Sets the maximum damage for the weapon.
    * @param maxDamage the maximum damage for the weapon.
    */
   public void setMaxDamage(int maxDamage) {
      _maxDamage = maxDamage;
   }

   /**
    * Gets the value.
    * @return the value.
    */
   public int getV1() {
      return _v1;
   }

   /**
    * Sets the value.
    * @param v1 the value.
    */
   public void setV1(int v1) {
      _v1 = v1;
   }


}
