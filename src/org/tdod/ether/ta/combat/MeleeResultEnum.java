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

package org.tdod.ether.ta.combat;

/**
 * An enumeration of melee results.
 *
 * @author Ron Kinney
 */
public enum MeleeResultEnum {

   /**
    * The attack missed.
    */
   MISS(0, "miss"),

   /**
    * The attack hit.
    */
   HIT(1, "hit"),

   /**
    * A critical hit.
    */
   CRIT(2, "critical"),

   /**
    * The target dodged the attack.
    */
   DODGE(3, "dodge"),

   /**
    * The attack glanced off of the targets armor.
    */
   GLANCE(4, "glance");

   private int           _index;
   private String        _description;

   /**
    * Creates a new MeleeResultEnum.
    *
    * @param index The index of the enumeration.
    * @param description The description of the melee result.
    */
   MeleeResultEnum(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Returns the index of the enumeration.
    *
    * @return the index of the enumeration.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the melee result description.
    *
    * @return the melee result description.
    */
   public String getDescription() {
      return _description;
   }
}
