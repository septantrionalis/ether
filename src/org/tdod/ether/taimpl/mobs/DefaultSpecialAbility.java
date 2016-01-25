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

import org.tdod.ether.ta.mobs.SpecialAbility;
import org.tdod.ether.taimpl.mobs.enums.SpecialAbilityEnum;

/**
 * The default implementation class for a special ability.
 * @author Ron Kinney
 */
public class DefaultSpecialAbility implements SpecialAbility {

   private SpecialAbilityEnum _specialAbility = SpecialAbilityEnum.INVALID;
   private String _specialAbilityDescription;

   //**********
   // Methods inherited from SpecialAbility.
   //**********

   /**
    * Gets the special ability description.
    * @return the special ability description.
    */
   public String getSpecialAbilityDescription() {
      return _specialAbilityDescription;
   }

   /**
    * Sets the special ability description.
    * @param specialAbilityDescription the special ability description.
    */
   public void setSpecialAbilityDescription(String specialAbilityDescription) {
      _specialAbilityDescription = specialAbilityDescription;
   }

   /**
    * Gets the special ability type.
    * @return the special ability type.
    */
   public SpecialAbilityEnum getSpecialAbility() {
      return _specialAbility;
   }

   /**
    * Sets the special ability type.
    * @param specialAbility the special ability type.
    */
   public void setSpecialAbility(SpecialAbilityEnum specialAbility) {
      _specialAbility = specialAbility;
   }

}
