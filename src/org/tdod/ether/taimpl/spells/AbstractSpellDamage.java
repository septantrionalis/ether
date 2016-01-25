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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.combat.SpellResultEnum;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.taimpl.spells.enums.ManaEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect2;
import org.tdod.ether.taimpl.spells.enums.PoisonTarget;
import org.tdod.ether.taimpl.spells.enums.SpellTarget;
import org.tdod.ether.taimpl.spells.enums.SpellType;
import org.tdod.ether.util.TaMessageManager;

public abstract class AbstractSpellDamage extends AbstractSpellAttack {

   private static Log _log = LogFactory.getLog(AbstractSpellDamage.class);

   protected void handleSingleTargetSpellAttack(Entity target) {
      SpellResult spellResult = WorldManager.getGameMechanics().calculateOffensiveSpellResult(_entity, target, _spell);

      if (spellResult.getSpellResultEnum().equals(SpellResultEnum.NEGATED)) {
         handleMagicNegated(target);
         return;
      }

      handleAttackSpellSuccess(target, spellResult) ;
      doSpellEffects(target, spellResult) ;
      handleVictimDeath(target);      

   }
   

   protected void handleAreaSpellAttack(ArrayList<Entity> targets, boolean pvp) {      
      SpellResult spellResult = WorldManager.getGameMechanics().handleAreaSpellDamage(_entity, _spell);
      
      handleAttackSpellSuccess(spellResult);
      
      for (Entity target:targets) {
         doSpellEffects(target, spellResult);
         handleVictimDeath(target);      
      }
      
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);

   }
   
   protected void handleNoTargets(String parameters) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");      
   }

   private void doSpellEffects(Entity target, SpellResult spellResult) {
      if (!applyDamage(target, spellResult)) {
         // The mob is in the group.  Remove it.
         if (target.getEntityType().equals(EntityType.MOB) &&
             _entity.getGroupList().contains(target)) {
            _entity.getGroupList().remove(target);
            target.setGroupLeader(target);
         }

         applyPoison(target, spellResult) ; 
         applyManaDrain(target, spellResult) ;
         applyMorale(target, spellResult) ;
         applyWind(target, spellResult) ;        // should do this last since it moves the player out of the room.
      }
      
      applyLifeSteal(target, spellResult) ;
      handleSpecificSpellFunctions(target) ;

      return;
   }

   
   private boolean applyDamage(Entity target, SpellResult spellResult) {
      if (_spell.getSpellType().equals(SpellType.ACOLYTE_ATTACK)  || 
          _spell.getSpellType().equals(SpellType.SORCEROR_ATTACK) ||
          _spell.getSpellType().equals(SpellType.DRUID_ATTACK)    ||
          _spell.getSpellType().equals(SpellType.NECROLYTE_ATTACK)) {
         
         // Only award experience on damage caused while the mob was alive.
         int vitalityBefore = target.getVitality().getCurVitality();
         target.takeDamage(spellResult.getNumberEffect());      
         int totalDamage = vitalityBefore - target.getVitality().getCurVitality();
         if (totalDamage < 0) {
            totalDamage = 0;
         }
         int expGain = (int)(totalDamage * target.getExpPerPointOfDamage(_entity.getLevel())) ;
         if (expGain <= 0) {
            expGain = 1;
         }
         _entity.addExperience(expGain);      
      }
      
      if (target.getVitality().getCurVitality() < 1) {
         return true;
      } else {
         return false;
      }
   }
   
   private void applyWind(Entity target, SpellResult spellResult) {
      if (_spell.getMiscTargetEffect().equals(MiscTargetEffect.WIND)) {
         WorldManager.getGameMechanics().tumbleEntityOutOfRoom(target) ;
      }      
   }
   
   private void applyPoison(Entity target, SpellResult spellResult) {
      if (_spell.getPoisonTarget().equals(PoisonTarget.MOB) ||
          _spell.getPoisonTarget().equals(PoisonTarget.PLAYER) ||
          _spell.getPoisonTarget().equals(PoisonTarget.ROOM_MOB)) {
         target.setStatus(Status.POISONED) ;
         target.setPoisonDamage(_spell.getMaxSpellEffect()) ;
      }      
   }
   
   private void applyManaDrain(Entity target, SpellResult spellResult) {
      if (_spell.getManaEffect().equals(ManaEffect.DRAIN)) {         
         target.getMana().subtractCurMana(spellResult.getNumberEffect());
      }
   }
   
   private void applyLifeSteal(Entity target, SpellResult spellResult) {
      if (_spell.getMiscTargetEffect2().equals(MiscTargetEffect2.LIFE_STEAL)) {         
         _entity.getVitality().addCurVitality(spellResult.getNumberEffect());
      }
      
   }
   
   private void applyMorale(Entity target, SpellResult spellResult) {
      if (_spell.getMiscTargetEffect().equals(MiscTargetEffect.MORAL)) {         
         if (target.getEntityType().equals(EntityType.MOB)) {
            Mob mob = (Mob)target;
            mob.subtractMorale(spellResult.getNumberEffect());
         }
      }    
   }
   
   private void handleVictimDeath(Entity target) {
      if (target.getVitality().getCurVitality() <= 0) {
         _entity.getAttacks().reset();
         if (target.getEntityType().equals(EntityType.PLAYER)) {
            Player targetPlayer = (Player)target;
            WorldManager.getGameMechanics().handlePlayerDeath(targetPlayer, TaMessageManager.YOUDED4.getMessage());
            return;
         } else {
            WorldManager.getGameMechanics().handleMobDeath(_entity, (Mob)target);
            return;
         }
         
      } 
   }
   
   protected void handleAttackSpellSuccess(SpellResult result) {
      String messageToPlayer ; 
      String messageToRoom ;
      
      if (_spell.getSpellTarget().equals(SpellTarget.ROOM_PLAYER) || 
          _spell.getSpellTarget().equals(SpellTarget.ROOM_PLAYER2)) {
         messageToPlayer = MessageFormat.format(TaMessageManager.SPLMDM.getMessage(), 
               result.getNumberEffect());
         messageToRoom = MessageFormat.format(TaMessageManager.SPLMOT.getMessage(), 
               getName(), _spell.getMessage());                  
         _room.print(_entity, messageToRoom, true) ;
      } else {
         messageToPlayer = MessageFormat.format(TaMessageManager.SPLMMN.getMessage(), 
               result.getNumberEffect());
         messageToRoom = MessageFormat.format(TaMessageManager.SPLMMO.getMessage(), 
               getName(), _spell.getMessage());         
         _room.print(_entity, messageToRoom, false) ;
      }

      _entity.print(messageToPlayer) ;
      
      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);

   }

   protected void handleAttackSpellSuccess(Entity target, SpellResult result) {
      String messageToPlayer = "ERROR"; 
      String messageToRoom = "ERROR";
      String messageToVictim = "ERROR";
      
      if (target.getEntityType().equals(EntityType.MOB)) {
         messageToPlayer = MessageFormat.format(TaMessageManager.SPMDAM.getMessage(), 
               target.getName(), result.getNumberEffect());
         messageToRoom = MessageFormat.format(TaMessageManager.USMOT1.getMessage(), 
               getName(), _spell.getMessage(), target.getName());
         messageToVictim = "";
      } else if (target.getEntityType().equals(EntityType.PLAYER)) { 
         messageToPlayer = MessageFormat.format(TaMessageManager.SPLDAM.getMessage(), 
               target.getName(), result.getNumberEffect());
         messageToRoom = MessageFormat.format(TaMessageManager.USEOT1.getMessage(), 
               getName(), _spell.getMessage(), target.getName());
         messageToVictim = MessageFormat.format(TaMessageManager.USEYU1.getMessage(), 
               getName(), _spell.getMessage(), result.getNumberEffect());            
      }
      
      _entity.print(messageToPlayer) ;
      
      _room.print(_entity, target, messageToRoom, false);
      
      target.print(messageToVictim);

      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);
   }   
   
   protected void handleMagicNegated(Entity target) {
      String messageToPlayer ; 
      String messageToRoom ;
      String messageToVictim ;
      
      if (target.getEntityType().equals(EntityType.PLAYER)) {
         messageToPlayer = MessageFormat.format(TaMessageManager.SPLNEF.getMessage(), target.getName());      
         messageToRoom = MessageFormat.format(TaMessageManager.NEFOTH.getMessage(), 
               getName(), _spell.getMessage(), target.getName(), target.getName());
         messageToVictim = MessageFormat.format(TaMessageManager.NEFYOU.getMessage(), 
               getName(), _spell.getMessage());
      } else {
         messageToPlayer = MessageFormat.format(TaMessageManager.SPMNEF.getMessage(), target.getName());
         messageToRoom = MessageFormat.format(TaMessageManager.SNMOTH.getMessage(), 
               getName(), _spell.getMessage(), target.getName(), target.getName());
         messageToVictim = "";
      }
      
      _entity.print(messageToPlayer);
      
      ArrayList<Entity> roomExceptions = new ArrayList<Entity>();
      roomExceptions.add(_entity);
      roomExceptions.add(target);
      _room.print(_entity, target, messageToRoom, false);
      
      target.print(messageToVictim);
      
      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);
   }
   
   protected abstract void handleSpecificSpellFunctions(Entity entity) ;
}
