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

import java.text.MessageFormat;
import java.util.ArrayList;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Stat;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.taimpl.spells.enums.StatModifier;
import org.tdod.ether.util.TaMessageManager;

public class SpellDrainStats extends AbstractSpellAttack {

   public SpellDrainStats() {
   }

   public SpellDrainStats(Entity entity, Room room, Spell spell) {
      _entity = entity;
      _room = room;
      _spell = spell;
   }

   protected void handleSingleTargetSpellAttack(Entity target) {
      // Determine that stats to modify.
      Stat stat1 = null;
      Stat stat2 = null;
      Stat stat3 = null;
      if (_spell.getStatPenalty().equals(StatModifier.PHYSIQUE)) {
         stat1 = target.getStats().getPhysique();
      } else if (_spell.getStatPenalty().equals(StatModifier.STAMINA)) {
         stat1 = target.getStats().getStamina();
      } else if (_spell.getStatPenalty().equals(StatModifier.AGILITY)) {
         stat1 = target.getStats().getAgility();
      } else if (_spell.getStatPenalty().equals(StatModifier.BODY)) {
         stat1 = target.getStats().getPhysique();
         stat2 = target.getStats().getStamina();
         stat3 = target.getStats().getAgility();
      } else if (_spell.getStatPenalty().equals(StatModifier.MENTAL)) {
         stat1 = target.getStats().getIntellect();
         stat2 = target.getStats().getKnowledge();
      }

      // Check if timers already exist.  Fail if so...
      if (stat1 != null && stat1.getDrainTimer() > 0) {
         handleFailedSpell();
         return;
      }
      if (stat2 != null && stat2.getDrainTimer() > 0) {
         handleFailedSpell();
         return;
      }
      if (stat3 != null && stat3.getDrainTimer() > 0) {
         handleFailedSpell();
         return;
      }

      // Handle to timers and effect.
      SpellResult primarySpellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);
      // int effectTime = primarySpellResult.getEffectTime();
      int effectTime = 1;
      if (stat1 != null) {
         stat1.setDrainTimer(effectTime) ;
         stat1.setDrain(primarySpellResult.getNumberEffect());         
      }
      if (stat2 != null) {
         stat2.setDrainTimer(effectTime) ;
         SpellResult spellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);
         stat2.setDrain(spellResult.getNumberEffect());         
      }
      if (stat3 != null) {
         stat3.setDrainTimer(effectTime) ;
         SpellResult spellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);
         stat3.setDrain(spellResult.getNumberEffect());         
      }

      displayMessages(target);

      return;
   }

   private void displayMessages(Entity target) {
      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLPRO.getMessage(), 
            _spell.getMessage(), target.getName());
      String messageToVictim = MessageFormat.format(TaMessageManager.PROYOU.getMessage(), 
            _entity.getName(), _spell.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.PROOTH.getMessage(), 
            _entity.getName(), _spell.getMessage(), target.getName());

      displayHarmfulMessages(messageToPlayer, messageToRoom, messageToVictim, target);
   }
   
   protected void handleSingleTargetSpellAttackOld(Entity target) {
      SpellResult spellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);

      if (_spell.getStatPenalty().equals(StatModifier.PHYSIQUE)) {
         target.getStats().getPhysique().setDrainTimer(spellResult.getEffectTime()) ;
         target.getStats().getPhysique().setDrain(spellResult.getNumberEffect());
      } else if (_spell.getStatPenalty().equals(StatModifier.STAMINA)) {
         target.getStats().getStamina().setDrainTimer(spellResult.getEffectTime()) ;
         target.getStats().getStamina().setDrain(spellResult.getNumberEffect());         
      } else if (_spell.getStatPenalty().equals(StatModifier.AGILITY)) {
         target.getStats().getAgility().setDrainTimer(spellResult.getEffectTime()) ;
         target.getStats().getAgility().setDrain(spellResult.getNumberEffect());
      } else if (_spell.getStatPenalty().equals(StatModifier.BODY)) {
         target.getStats().getPhysique().setDrainTimer(spellResult.getEffectTime()) ;
         target.getStats().getPhysique().setDrain(spellResult.getNumberEffect());
         target.getStats().getStamina().setDrain(spellResult.getNumberEffect());         
         target.getStats().getAgility().setDrain(spellResult.getNumberEffect());         
      } else if (_spell.getStatPenalty().equals(StatModifier.MENTAL)) {
         target.getStats().getIntellect().setDrainTimer(spellResult.getEffectTime()) ;
         target.getStats().getIntellect().setDrain(spellResult.getNumberEffect());
         target.getStats().getKnowledge().setDrain(spellResult.getNumberEffect());
      }
      
      displayMessages(target);
            
      return;

   }
   
   protected void handleAreaSpellAttack(ArrayList<Entity> targets, boolean pvp) {
      _entity.println("You cast an area spell.") ;
      _room.println(_entity, _entity.getName() + " casts an area spell!.", false);
   }

   
}
