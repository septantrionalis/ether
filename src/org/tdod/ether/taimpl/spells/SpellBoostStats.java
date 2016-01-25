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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Stat;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.taimpl.spells.enums.StatModifier;
import org.tdod.ether.util.TaMessageManager;

/**
 * PLAYER(guess): &bYou cast an enhancement spell on Hilorex!
 * TARGET(guess): &RMinex just cast an enhancement spell on you!
 * ROOM(guess):   &bMinex just cast an enhancement spell on Hilorex!
 */
public class SpellBoostStats extends AbstractUtilitySpell {
   
   public SpellBoostStats() {
   }

   public SpellBoostStats(Entity entity, Room room, Spell spell) {
      _entity = entity;
      _room = room;
      _spell = spell;
   }
   
   protected boolean handleSpellFunction(Entity target) {
      // Determine that stats to modify.
      Stat stat1 = null;
      Stat stat2 = null;
      Stat stat3 = null;
      if (_spell.getStatBonus().equals(StatModifier.PHYSIQUE)) {
         stat1 = target.getStats().getPhysique();
      } else if (_spell.getStatBonus().equals(StatModifier.STAMINA)) {
         stat1 = target.getStats().getStamina();
      } else if (_spell.getStatBonus().equals(StatModifier.AGILITY)) {
         stat1 = target.getStats().getAgility();
      } else if (_spell.getStatBonus().equals(StatModifier.BODY)) {
         stat1 = target.getStats().getPhysique();
         stat2 = target.getStats().getStamina();
         stat3 = target.getStats().getAgility();
      } else if (_spell.getStatBonus().equals(StatModifier.MENTAL)) {
         stat1 = target.getStats().getIntellect();
         stat2 = target.getStats().getKnowledge();
      }

      // Check if timers already exist.  Fail if so...
      if (stat1 != null && stat1.getBoostTimer() > 0) {
         return false;
      }
      if (stat2 != null && stat2.getBoostTimer() > 0) {
         return false;
      }
      if (stat3 != null && stat3.getBoostTimer() > 0) {
         return false;
      }
      
      // Handle to timers and effect.
      int effectTime = _spellResult.getEffectTime();
      if (stat1 != null) {
         stat1.setBoostTimer(effectTime) ;
         stat1.setBoost(_spellResult.getNumberEffect());         
      }
      if (stat2 != null) {
         stat2.setBoostTimer(effectTime) ;
         SpellResult spellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);
         stat2.setBoost(spellResult.getNumberEffect());         
      }
      if (stat3 != null) {
         stat3.setBoostTimer(effectTime) ;
         SpellResult spellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);
         stat3.setBoost(spellResult.getNumberEffect());         
      }
      
      return true;
   }

   
   protected boolean handleSpellFunctionOld(Entity target) {
      if (_spell.getStatBonus().equals(StatModifier.PHYSIQUE)) {
         target.getStats().getPhysique().setBoostTimer(_spellResult.getEffectTime()) ;
         target.getStats().getPhysique().setBoost(_spellResult.getNumberEffect());
      } else if (_spell.getStatBonus().equals(StatModifier.STAMINA)) {
         target.getStats().getStamina().setBoostTimer(_spellResult.getEffectTime()) ;
         target.getStats().getStamina().setBoost(_spellResult.getNumberEffect());         
      } else if (_spell.getStatBonus().equals(StatModifier.AGILITY)) {
         target.getStats().getAgility().setBoostTimer(_spellResult.getEffectTime()) ;
         target.getStats().getAgility().setBoost(_spellResult.getNumberEffect());
      } else if (_spell.getStatBonus().equals(StatModifier.BODY)) {
         target.getStats().getPhysique().setBoostTimer(_spellResult.getEffectTime()) ;
         target.getStats().getPhysique().setBoost(_spellResult.getNumberEffect());
         target.getStats().getStamina().setBoost(_spellResult.getNumberEffect());         
         target.getStats().getAgility().setBoost(_spellResult.getNumberEffect());         
      } else if (_spell.getStatBonus().equals(StatModifier.MENTAL)) {
         target.getStats().getIntellect().setBoostTimer(_spellResult.getEffectTime()) ;
         target.getStats().getIntellect().setBoost(_spellResult.getNumberEffect());
         target.getStats().getKnowledge().setBoost(_spellResult.getNumberEffect());
      }
      return true;
   }
   
   protected void handleSingleTargetMessage(Entity target) {
      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLPRO.getMessage(), 
            _spell.getMessage(), target.getName());
      String messageToVictim = MessageFormat.format(TaMessageManager.PROYOU.getMessage(), 
            _entity.getName(), _spell.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.PROOTH.getMessage(), 
            _entity.getName(), _spell.getMessage(), target.getName());

      displayBeneficialMessages(messageToPlayer, messageToRoom, messageToVictim, target);      
   }
   
   protected void handleAreaMessage() {
      _entity.println("You cast an area spell.") ;
      _room.println(_entity, _entity.getName() + " casts an area spell!.", false);
   }

}
