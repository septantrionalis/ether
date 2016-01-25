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

import org.tdod.ether.taimpl.mobs.enums.SpecialAbilityEnum;

/**
 * Special Ability interface.
 * @author Ron Kinney
 */
public interface SpecialAbility {

   /**
    * Gets the special ability description.
    * @return the special ability description.
    */
   String getSpecialAbilityDescription();

   /**
    * Sets the special ability description.
    * @param specialAbilityDescription the special ability description.
    */
   void setSpecialAbilityDescription(String specialAbilityDescription);

   /**
    * Gets the special ability type.
    * @return the special ability type.
    */
   SpecialAbilityEnum getSpecialAbility();

   /**
    * Sets the special ability type.
    * @param specialAbility the special ability type.
    */
   void setSpecialAbility(SpecialAbilityEnum specialAbility);

}
