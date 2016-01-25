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

package org.tdod.ether.ta.mobs.enums;

/**
 * An enumeration of mob weapon types.
 * @author Ron Kinney
 */
public enum MobWeaponType {

   /**
    * A small one handed weapon.
    */
   SMALL_ONE_HANDED(0, "Small One-Handed"),

   /**
    * A medium one handed weapon.
    */
   MEDIUM_ONE_HANDED(1, "Medium One-Handed"),

   /**
    * A large one handed weapon.
    */
   LARGE_ONE_HANDED(2, "Large One-Handed"),

   /**
    * A large two handed weapon.
    */
   LARGE_TWO_HANDED(3, "Large Two-Handed");

   private int           _index;
   private String        _description;

   /**
    * Defines a new MobWeaponType.
    * @param index the weapon type index.
    * @param description the weapon type description.
    */
   MobWeaponType(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the weapon type index.
    * @return the weapon type index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the weapon type description.
    * @return the weapon type description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets the MobWeaponType enum from the index.
    * @param index the index of the MobWeaponType.
    * @return the MobWeaponType enum from the index.
    */
   public static MobWeaponType getMobWeaponType(int index) {
      MobWeaponType[] mobWeaponType = MobWeaponType.values();

      return mobWeaponType[index];
   }

}
