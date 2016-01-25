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

package org.tdod.ether.ta.spells;

import java.util.ArrayList;

import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.mobs.enums.MobSpellType;
import org.tdod.ether.util.InvalidFileException;

/**
 * Contains all spell data.
 * @author rkinney
 */
public interface SpellDatabase {

   /**
    * Gets the list of spells for a particular class.
    * @param playerClass the class in question.
    * @return the list of spells for a particular class.
    */
   ArrayList<Spell> getSpells(PlayerClass playerClass);

   /**
    * Gets a spell based on a string.
    * @param spell the spell in string format.
    * @return a spell.
    */
   Spell getSpell(String spell);

   /**
    * Gets a the mob attack spell for a particular mob spell type.
    * @param mobSpellType the mob spell type.
    * @param index the index of the spell.
    * @return a spell.
    */
   Spell getAttackSpellFor(MobSpellType mobSpellType, int index);

   /**
    * Initializes the data.
    * @throws InvalidFileException thrown if the spell file is invalid.
    */
   void initialize() throws InvalidFileException;

}
