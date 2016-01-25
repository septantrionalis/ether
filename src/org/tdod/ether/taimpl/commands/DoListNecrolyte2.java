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

package org.tdod.ether.taimpl.commands;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;

/**
 * Handles the "list necrolyte2" command.
 *
 * @author Ron Kinney
 */
public class DoListNecrolyte2 extends AbstractDoSpellList {

   /**
    * Executes the spell list command.
    *
    * @param entity The entity issuing the list command.
    * @param input The entity input.
    */
   protected final void doSpellList(Entity entity, String input) {
      setSpellClass(PlayerClass.NECROLYTE);

      ArrayList<Spell> spellList = WorldManager.getSpells(getSpellClass());

      printNecrolyteHeader(entity);

      int count = 0;
      int split = spellList.size() / 2;
      for (Spell spell : spellList) {
         count += 1;
         if (count <= split) {
            continue;
         }
         StringBuilder sb = new StringBuilder();
         Formatter formatter = new Formatter(sb, Locale.US);
         formatter.format("&W| &R%-20s&W | &R%5d&W | &R%5d&W |", spell.getName(), spell.getMana(), spell.getCost());
         entity.println(sb.toString());

      }

      return;
   }

}
