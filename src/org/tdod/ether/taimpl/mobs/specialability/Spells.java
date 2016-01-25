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

package org.tdod.ether.taimpl.mobs.specialability;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.specialability.SpecialAbilityCommand;
import org.tdod.ether.ta.player.Spellbook;
import org.tdod.ether.ta.spells.AbstractSpellCommand;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.ta.spells.SpellCommandManager;
import org.tdod.ether.taimpl.spells.enums.SpellTarget;
import org.tdod.ether.util.Dice;

public class Spells extends SpecialAbilityCommand {

   public void execute(Mob mob, Room room) {
      Entity victim = mob.findRandomHostileEntity(room);
      if (victim == null) {
         return;
      }

      Spell spell = getSpell(mob.getSpellbook()) ;
      AbstractSpellCommand spellCommand = SpellCommandManager.getInstance().getSpellCommand(spell.getSpellType());
      
      if (spell.getSpellTarget().equals(SpellTarget.ROOM_MOB) ||
            spell.getSpellTarget().equals(SpellTarget.ROOM_MOB2) ||
            spell.getSpellTarget().equals(SpellTarget.ROOM_PLAYER) ||
            spell.getSpellTarget().equals(SpellTarget.ROOM_PLAYER2)) {
         spellCommand.execute(mob, spell, null) ;
      } else {
         spellCommand.execute(mob, spell, victim.getName()) ;         
      }

   }
   
   private Spell getSpell(Spellbook book) {
      int spellIndex = Dice.roll(0, book.getSpells().size() - 1);
      return book.getSpells().get(spellIndex);
   }
   
}
