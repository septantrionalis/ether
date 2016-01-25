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

package org.tdod.ether.taimpl.player;

import java.util.ArrayList;

import org.tdod.ether.ta.player.Spellbook;
import org.tdod.ether.ta.player.enums.SpellbookFailCode;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default spellbook implementation.
 * @author rkinney
 */
public class DefaultSpellbook implements Spellbook {

   private static final long serialVersionUID = -5609465897915344865L;

   private static final int MAX_SPELLS = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_SPELLS));

   private ArrayList<Spell> _spells = new ArrayList<Spell>(MAX_SPELLS);

   /**
    * Creates a new DefaultSpellbook.
    */
   public DefaultSpellbook() {
   }

   /**
    * Purges a spell from the spellbook.
    * @param spell the spell to purge.
    * @return a SpellbookFailCode.
    */
   public SpellbookFailCode purgeSpell(Spell spell) {
      if (!_spells.contains(spell)) {
         return SpellbookFailCode.NOT_FOUND;
      }

      _spells.remove(spell);
      return SpellbookFailCode.NONE;
   }

   /**
    * Scribes a spell into the spellbook.
    * @param spell the spell to scribe.
    * @return a SpellbookFailCode.
    */
   public SpellbookFailCode scribeSpell(Spell spell) {
      if (_spells.contains(spell)) {
         return SpellbookFailCode.DUPLICATE;
      }

      if (_spells.size() >= MAX_SPELLS) {
         return SpellbookFailCode.SPELLBOOK_FULL;
      }

      _spells.add(spell);
      return SpellbookFailCode.NONE;
   }

   /**
    * Gets the list of spells in the spellbook.
    * @return the list of spells in the spellbook.
    */
   public ArrayList<Spell> getSpells() {
      return _spells;
   }

   /**
    * Removes all spells from the book.
    */
   public void clearSpells() {
      _spells.clear();
   }

}
