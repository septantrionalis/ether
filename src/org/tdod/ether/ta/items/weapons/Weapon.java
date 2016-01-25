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

package org.tdod.ether.ta.items.weapons;

import org.tdod.ether.ta.items.Item;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.taimpl.items.weapons.enums.SpecialWeaponFunction;

/**
 * The interface for a weapon.
 * @author Ron Kinney
 */
public interface Weapon extends Item {

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
    * Gets the magic attack message.
    * @return the magic attack message.
    */
   String getMagicAttackMessage();

   /**
    * Sets the magic attack message.
    * @param magicAttackMessage the magic attack message.
    */
   void setMagicAttackMessage(String magicAttackMessage);

   /**
    * Gets the weapon type.
    * @return the weapon type.
    */
   int getType();

   /**
    * Sets the weapon type.
    * @param type the weapon type.
    */
   void setType(int type);

   /**
    * Gets the armor rating for the weapon.
    * @return the armor rating for the weapon.
    */
   int getArmorRating();

   /**
    * Sets the armor rating for the weapon.
    * @param armorRating the armor rating for the weapon.
    */
   void setArmorRating(int armorRating);

   /**
    * Gets the special weapon function.
    * @return the special weapon function.
    */
   SpecialWeaponFunction getSpecFunction();

   /**
    * Sets the special weapon function.
    * @param specFunction the special weapon function.
    */
   void setSpecFunction(SpecialWeaponFunction specFunction);

   /**
    * Sets the town that sells this weapon.
    * @param town the town that sells this weapon.
    */
   void setTown(Town town);

   /**
    * Sets the weapon range.
    * @param range the weapon range.
    */
   void setRange(int range);

   /**
    * Gets the weapon range.
    * @return the weapon range.
    */
   int getRange();

   /**
    * Sets the weapon ammo vnum.
    * @param ammoVnum the ammo vnum.
    */
   void setAmmoVnum(int ammoVnum);

   /**
    * Gets the weapon ammo vnum.
    * @return the weapon ammo vnum.
    */
   int getAmmoVnum();

   /**
    * Sets the value at index 9.
    * @param v9 the value at index 9.
    */
   void setV9(int v9);

   /**
    * Gets the value at index 9.
    * @return the value at index 9.
    */
   int getV9();

   /**
    * Sets the value at index 10.
    * @param v10 the value at index 10.
    */
   void setV10(int v10);

   /**
    * Gets the value at index 10.
    * @return the value at index 10.
    */
   int getV10();

   /**
    * Sets the value at index 11.
    * @param v11 the value at index 11.
    */
   void setV11(int v11);

   /**
    * Gets the value at index 11.
    * @return the value at index 11.
    */
   int getV11();

   /**
    * Sets the value at index 12.
    * @param v12 the value at index 12.
    */
   void setV12(int v12);

   /**
    * Gets the value at index 12.
    * @return the value at index 12.
    */
   int getV12();

   /**
    * Sets the value at index 13.
    * @param v13 the value at index 13.
    */
   void setV13(int v13);

   /**
    * Gets the value at index 13.
    * @return the value at index 13.
    */
   int getV13();

}
