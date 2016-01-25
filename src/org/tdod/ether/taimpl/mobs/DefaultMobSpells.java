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

import org.tdod.ether.ta.mobs.MobSpells;
import org.tdod.ether.taimpl.mobs.enums.MobSpellType;

/**
 * The default implementation class of mob spells.
 * @author minex
 *
 */
public class DefaultMobSpells implements MobSpells {

   private int          _spellSkill;
   private MobSpellType _mobSpellType = MobSpellType.INVALID;
   private int          _minSpell;
   private int          _maxSpell;

   /**
    * Creates a new DefaultMobSpells.
    */
   public DefaultMobSpells() {
   }

   //**********
   // Methods inherited from MobSpells.
   //**********

   /**
    * Gets the mobs spell skill level.
    * @return the mobs spell skill level.
    */
   public int getSpellSkill() {
      return _spellSkill;
   }

   /**
    * Sets the mobs spell skill level.
    * @param spellSkill the mobs spell skill level.
    */
   public void setSpellSkill(int spellSkill) {
      _spellSkill = spellSkill;
   }

   /**
    * Gets the mob spell type.
    * @return the mob spell type.
    */
   public MobSpellType getMobSpellType() {
      return _mobSpellType;
   }

   /**
    * Sets the mob spell type.
    * @param mobSpellType the mob spell type.
    */
   public void setMobSpellType(MobSpellType mobSpellType) {
      _mobSpellType = mobSpellType;
   }

   /**
    * Gets the mininum spell number that the mob can cast.
    * @return the mininum spell number that the mob can cast.
    */
   public int getMinSpell() {
      return _minSpell;
   }

   /**
    * Sets the mininum spell number that the mob can cast.
    * @param minSpell the mininum spell number that the mob can cast.
    */
   public void setMinSpell(int minSpell) {
      _minSpell = minSpell;
   }

   /**
    * Gets the the maxinum spell number that the mob can cast.
    * @return the the maxinum spell number that the mob can cast.
    */
   public int getMaxSpell() {
      return _maxSpell;
   }

   /**
    * Sets the the maxinum spell number that the mob can cast.
    * @param maxSpell the the maxinum spell number that the mob can cast.
    */
   public void setMaxSpell(int maxSpell) {
      _maxSpell = maxSpell;
   }

}
