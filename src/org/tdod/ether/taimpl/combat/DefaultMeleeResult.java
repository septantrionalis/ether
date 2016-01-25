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

package org.tdod.ether.taimpl.combat;

import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.combat.MeleeResultEnum;

/**
 * This is an implementation class that represents the result of a melee attack.
 *
 * @author Ron Kinney
 */
public class DefaultMeleeResult implements MeleeResult {

   private MeleeResultEnum _meleeResultEnum = MeleeResultEnum.MISS;
   private int             _damage = 0;
   private String          _weapon;

   /**
    * Creats a new DefaultMeleeResult.
    */
   public DefaultMeleeResult() {
   }

   /**
    * Gets the MeleeResultEnum.
    *
    * @return the MeleeResultEnum.
    */
   public final MeleeResultEnum getMeleeResultEnum() {
      return _meleeResultEnum;
   }

   /**
    * Sets the MeleeResultEnum.
    *
    * @param meleeResultEnum The MeleeResultEnum
    */
   public final void setMeleeResultEnum(MeleeResultEnum meleeResultEnum) {
      _meleeResultEnum = meleeResultEnum;
   }

   /**
    * Gets the damage inflicted on the target.
    *
    * @return the damage inflicted on the target.
    */
   public final int getDamage() {
      return _damage;
   }

   /**
    * Sets the inflicted damage.
    *
    * @param damage the inflicted damage.
    */
   public final void setDamage(int damage) {
      _damage = damage;
   }

   /**
    * Gets the weapon string.
    *
    * @return the weapon string.
    */
   public final String getWeapon() {
      return _weapon;
   }

   /**
    * Sets the weapon string.
    *
    * @param weapon the weapon string.
    */
   public final void setWeapon(String weapon) {
      _weapon = weapon;
   }

}
