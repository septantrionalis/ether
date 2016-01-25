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

import java.text.MessageFormat;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.spells.AbstractUtilitySpell;
import org.tdod.ether.taimpl.spells.enums.SpellTarget;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * The abstract spell command class.
 * @author rkinney
 */
public abstract class AbstractSpellCommand {

   private static Log _log = LogFactory.getLog(AbstractUtilitySpell.class);

   protected Entity           _entity;
   protected Room             _room;
   protected Spell            _spell;

   /**
    * Executes the spell command.
    * @param entity the entity issuing the command.
    * @param spell the spell.
    * @param targetStr any parameters passed in by the entity.
    */
   public void execute(Entity entity, Spell spell, String targetStr) {
      _entity = entity;
      _room = entity.getRoom();
      _spell = spell;

      // Mentally Exhausted
      if (_entity.isMentallyExhausted()) {
         _entity.print(TaMessageManager.SPLEXH.getMessage());
         return;
      }

      // Played does not have the spell
      if (!_entity.getSpellbook().getSpells().contains(spell)) {
         _entity.print(TaMessageManager.NOSPEL.getMessage());
         return;
      }

      // Not enough mana.
      if (_entity.getMana().getCurMana() < spell.getMana()) {
         _entity.print(TaMessageManager.LOWSPL.getMessage());
         return;
      }

      // Determine spell type -- mob area, player area, single target, etc
      if (spell.getSpellTarget().equals(SpellTarget.SPECIFIED)) {
         findSingleTarget(targetStr);
         return;
      }

      if (spell.getSpellTarget().equals(SpellTarget.ROOM_MOB)
          || spell.getSpellTarget().equals(SpellTarget.ROOM_MOB2)) {
         ArrayList<Mob> roomTargets = _room.getMobs();

         // create a temporary list to modify.
         ArrayList<Entity> targets = new ArrayList<Entity>();
         for (Entity target : roomTargets) {
            targets.add(target);
         }
         findAreaTargets(targets, targetStr, false);
         return;
      }

      if (spell.getSpellTarget().equals(SpellTarget.ROOM_PLAYER)
          || spell.getSpellTarget().equals(SpellTarget.ROOM_PLAYER2)) {
         ArrayList<Player> roomTargets = _room.getPlayers();

         // create a temporary list to modify.
         ArrayList<Entity> targets = new ArrayList<Entity>();
         for (Entity target : roomTargets) {
            if (!target.equals(_entity)) {
               targets.add(target);
            }
         }
         findAreaTargets(targets, targetStr, true);
         return;
      }

      if (spell.getSpellTarget().equals(SpellTarget.SUMMON)) {
         handleNoTargets(targetStr);
         return;
      }

      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.");
      _entity.println("Something went wrong!  Please contact an admin.");

   }

   /**
    * The spell failed.
    */
   protected void handleFailedSpell() {
      _entity.print(TaMessageManager.SFAILD.getMessage());

      String messageToRoom = MessageFormat.format(TaMessageManager.SFDYOU.getMessage(), getName());
      _room.print(_entity, messageToRoom, false);

      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);
   }

   /**
    * The spell was unsuccessful.
    */
   protected void handleUnsuccessfulSpell() {
      _entity.print(TaMessageManager.SPLFLD.getMessage());

      String messageToRoom = MessageFormat.format(TaMessageManager.SFDYOU.getMessage(), getName());
      _room.print(_entity, messageToRoom, false);

      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);
   }

   /**
    * The spell was a success.
    */
   protected void handleSpellSuccess() {
      _entity.addExperience(WorldManager.getGameMechanics().getExpForUtilitySpells(_entity, _spell));
      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);
   }

   /**
    * Finds a target, not including the caster.
    * @param targetStr the target string.
    * @return the target.
    */
   protected Entity findOtherTarget(String targetStr) {
      Entity target = null;
      // The joys of TA... attack any players first.
      for (Player tmpPlayer : _room.getPlayers()) {
         if (MyStringUtil.contains(tmpPlayer.getName(), targetStr)) {
            if (tmpPlayer.equals(_entity)) {
               handleUnsuccessfulSpell();
               return null;
            } else {
               return tmpPlayer;
            }
         }
      }

      // Find a mob if target is not a player.
      if (target == null) {
         for (Mob mob : _room.getMobs()) {
            if (MyStringUtil.contains(mob.getName(), targetStr)) {
               return mob;
            }
         }
      }

      return null;
   }

   /**
    * Searches for the specified target, including the caster.
    * @param targetStr the target string.
    * @return the target.
    */
   protected Entity findTargetIncludingSelf(String targetStr) {
      Entity target = null;
      // The joys of TA... attack any players first.
      for (Player tmpPlayer : _room.getPlayers()) {
         if (MyStringUtil.contains(tmpPlayer.getName(), targetStr)) {
            return tmpPlayer;

         }
      }

      // Find a mob if target is not a player.
      if (target == null) {
         for (Mob mob : _room.getMobs()) {
            if (MyStringUtil.contains(mob.getName(), targetStr)) {
               return mob;
            }
         }
      }

      return null;
   }

   /**
    * Displays beneficial messages.
    * @param messageToPlayer the message to the player.
    * @param messageToRoom the message to the room.
    * @param messageToTarget the message to the target.
    * @param target the target of the spell.
    */
   protected void displayBeneficialMessages(String messageToPlayer, String messageToRoom,
         String messageToTarget, Entity target) {
      displayMessages(messageToPlayer, messageToRoom, messageToTarget, target);
   }

   /**
    * Displays harmful messages.
    * @param messageToPlayer the message to the player.
    * @param messageToRoom the message to the room.
    * @param messageToTarget the message to the target.
    * @param target the target of the spell.
    */
   protected void displayHarmfulMessages(String messageToPlayer, String messageToRoom, String messageToTarget, Entity target) {
      displayMessages(messageToPlayer, messageToRoom, messageToTarget, target); // guess
   }

   /**
    * Displays beneficial area effect messages.
    * @param messageToPlayer the message to the player.
    * @param messageToRoom the message to the room.
    */
   protected void displayBeneficialAreaEffectMessages(String messageToPlayer, String messageToRoom) {
      displayMessages(messageToPlayer, messageToRoom, null, null); // guess
   }

   /**
    * Displays a message.
    * @param messageToPlayer the message to the player.
    * @param messageToRoom the message to the room.
    * @param messageToTarget the message to the target.
    * @param target the target of the spell.
    */
   private void displayMessages(String messageToPlayer, String messageToRoom, String messageToTarget, Entity target) {
      _entity.print(messageToPlayer);
      if (target != null) {
         _room.print(_entity, target, messageToRoom, false);
         if (!_entity.equals(target)) {
            target.print(messageToTarget);
         }
      } else {
         _room.print(_entity, messageToRoom, false);
      }

   }

   /**
    * Handles a single target based on a string.
    * @param targetStr the target string.
    */
   private void findSingleTarget(String targetStr) {
      // Spell requires a target.
      if (targetStr == null) {
         _entity.print(TaMessageManager.SNDTRG.getMessage());
         return;
      }

      Entity target = findTargetIncludingSelf(targetStr);

      // No mob found.
      if (target == null) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), targetStr);
         _entity.print(messageToPlayer);
         return;
      }

      handleSingleTarget(target);
   }

   /**
    * Handles an array of targets.
    * @param targets the array of targets.
    * @param parameters any parameters passed in by the entity.
    * @param player the targets are a player.
    */
   private void findAreaTargets(ArrayList<Entity> targets, String parameters, boolean player) {
      if (parameters != null) {
         _entity.print(TaMessageManager.SDNTRG.getMessage());
         return;
      }

      handleAreaTarget(targets, player);
   }

   protected String getName() {
      if (_entity.getEntityType().equals(EntityType.MOB)) {
         return ((Mob) _entity).getPrefix() + " " + _entity.getName();
      } else {
         return _entity.getName();
      }


   }
   
   /**
    * Handles a single target.
    * @param target the target.
    */
   protected abstract void handleSingleTarget(Entity target);

   /**
    * An area effect spell.
    * @param targets the list of targets.
    * @param player true if the targets are.
    */
   protected abstract void handleAreaTarget(ArrayList<Entity> targets, boolean player);

   /**
    * The spell had no targets.
    * @param parameters any parameters passed in by the entity.
    */
   protected abstract void handleNoTargets(String parameters);
}
