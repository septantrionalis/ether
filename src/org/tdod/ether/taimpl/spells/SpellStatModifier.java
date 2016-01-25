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

package org.tdod.ether.taimpl.spells;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.spells.AbstractSpellCommand;
import org.tdod.ether.taimpl.spells.enums.StatModifier;

/**
 * PLAYER(guess): &bYou cast a draining spell on Hilorex!
 * TARGET(guess): &RMinex just cast a draining spell on you!
 * ROOM(guess):   &bMinex just cast a draining spell on Hilorex!
 */
public class SpellStatModifier extends AbstractSpellCommand {

   private static Log _log = LogFactory.getLog(SpellStatModifier.class);

   protected void handleSingleTarget(Entity target) {
      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }

      if (!_spell.getStatBonus().equals(StatModifier.NONE)) {
         SpellBoostStats spellBoostStats = new SpellBoostStats(_entity, _room, _spell) ;
         spellBoostStats.handleSingleTarget(target) ;
      }

      if (_spell.getArmorModifier() != 0) {
         SpellMagicDefense boostSelf = new SpellMagicDefense(_entity, _room, _spell) ;
         boostSelf.handleSingleTarget(target);
      }

      if (!_spell.getStatPenalty().equals(StatModifier.NONE)) {
         SpellDrainStats spellDrainStats = new SpellDrainStats(_entity, _room, _spell);
         spellDrainStats.handleSingleTarget(target) ;
      }
   }

   protected void handleAreaTarget(ArrayList<Entity> targets, boolean effectsPlayers) {
      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }

      if (_spell.getArmorModifier() != 0) {
         SpellMagicDefense boostSelf = new SpellMagicDefense(_entity, _room, _spell) ;
         boostSelf.handleAreaTarget(targets, effectsPlayers);
      }
   }

   protected void handleNoTargets(String parameters) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");      
   }

}
