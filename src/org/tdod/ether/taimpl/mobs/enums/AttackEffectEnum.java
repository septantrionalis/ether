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

package org.tdod.ether.taimpl.mobs.enums;


/**
 * An enumeration of attack effects.
 * @author Ron Kinney
 */
public enum AttackEffectEnum {

   /**
    * No attack effect.
    */
   NONE(0, "None"),

   /**
    * Poison.
    */
   POISON(1, "Poison"),

   /**
    * An invalid attack effect.
    */
   NONE2(2, "Invalid"),

   /**
    * An invalid attack effect.
    */
   NONE3(3, "Invalid"),

   /**
    * The attack drains mana.
    */
   MANA_DRAIN(4, "Mana Drain"),

   /**
    * The attack drains max vitality.
    */
   VITALITY_DRAIN(5, "Vitality Drain"),

   /**
    * Paralysis.
    */
   PARALYSIS(6, "Paralysis"),

   /**
    * An invalid attack effect.
    */
   INVALID(-1, "Invalid");

   private int           _index;
   private String        _description;

   /**
    * Creates a new AttackEffectEnum.
    * @param index The attack enum index.
    * @param description The description of the attack effect.
    */
   AttackEffectEnum(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the enum index.
    * @return the enum index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the attack effect description.
    * @return the attack effect description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets the attack effect enum based on the index.  This method does no index checking, so if
    * the index is out of bounds, an out of bounds exception will be thrown.
    * @param index the enum index.
    * @return an AttackEffectEnum.
    */
   public static AttackEffectEnum getAttackEffectEnum(int index) {
      AttackEffectEnum[] attackEffectEnum = AttackEffectEnum.values();

      return attackEffectEnum[index];
   }

}
