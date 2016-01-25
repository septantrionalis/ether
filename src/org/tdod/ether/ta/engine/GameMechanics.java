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

package org.tdod.ether.ta.engine;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.ta.spells.Spell;

/**
 * This is an interface that defines general game mechanics.  Many mechanics in Telearena are unknown
 * and hidden away in the original source code.  This is an attempt to move out all of this
 * functionality into one class so that it can be easily tweaked.
 *
 * @author Ron Kinney
 */
public interface GameMechanics {

   /**
    * Determines if an entity trips while moving.
    *
    * @param entity the entity
    * @return true if the entity tripped.
    */
   boolean entityTripped(Entity entity);

   /**
    * Determines if an entity picked a locked door.
    *
    * @param entity the entity
    * @param consumes set to 1 if the door can be picked by a rogue.
    * @return true if the door is picked.
    */
   boolean pickedLock(Entity entity, int consumes);

   /**
    * Does whatever is needed when the player pleases the gods.  In this case, a random stat will be increased
    * by one point up to the maximum specified in the properties file.
    *
    * @param entity the entity who please the gods.
    */
   void handlePleasedGods(Entity entity);

   /**
    * Handles donations to the temple and returns true if the gods are pleased.
    *
    * @param entity the entity who donated.
    * @param amount the amount donated.
    * @return true if the gods are pleased.
    */
   boolean donate(Entity entity, int amount);

   /**
    * Determines the amount of experience gained for casting utility and heal spells.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell
    * @return the amount of exp gained.
    */
   int getExpForUtilitySpells(Entity entity, Spell spell);

   /**
    * Tumbles an entity out of the room.
    *
    * @param entity the entity.
    */
   void tumbleEntityOutOfRoom(Entity entity);

   /**
    * Handles the death of a mob.
    *
    * @param entity the entity who killed the mob.
    * @param mob the mob who died.
    */
   void handleMobDeath(Entity entity, Mob mob);

   /**
    * Determines if a spell is successful.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell being cast.
    * @return true if the spell is successful.
    */
   boolean isSpellSuccess(Entity entity, Spell spell);

   /**
    * Determines if a spell is successful.
    *
    * @param entity the entity who cast the spell.
    * @param mob the mob being charmed.
    * @param spell the spell being cast.
    * @return true if the spell is successful.
    */
   boolean isCharmSpellSuccess(Entity entity, Mob mob, Spell spell);

   /**
    * Calculates the spell result of an area utility spell.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell.
    * @return a SpellResult
    */
   SpellResult calculateAreaUtilitySpellResult(Entity entity, Spell spell);

   /**
    * Calculates the spell result of an area attack spell.
    *
    * @param entity the entity who cast the spell.
    * @param spell the spell.
    * @return a SpellResult
    */
   SpellResult handleAreaSpellDamage(Entity entity, Spell spell);

   /**
    * Calculates the spell result of a utility spell.
    *
    * @param entity the entity who cast the spell.
    * @param victim the target.
    * @param spell the spell.
    * @return a SpellResult
    */
   SpellResult calculateUtilitySpellResult(Entity entity, Entity victim, Spell spell);

   /**
    * Calculates the spell result of an offensive spell.
    *
    * @param entity the entity who cast the spell.
    * @param victim the target.
    * @param spell the spell.
    * @return a SpellResult
    */
   SpellResult calculateOffensiveSpellResult(Entity entity, Entity victim, Spell spell);

   /**
    * This method calculates the mental rest delay of a spell.
    *
    * @param entity the entity casting the spell.
    * @param spell the spell.
    */
   void handleMentalRestDelay(Entity entity, Spell spell);

   /**
    * Handles a players death.
    *
    * @param player the player who died.
    * @param playerDeathMsg the death message sent to the player.
    */
   void handlePlayerDeath(Player player, String playerDeathMsg);

   /**
    * Calculates the mob health.  This method adds a bit of randomness to the game.
    *
    * @param mobCombatSkill the combat skill of the mob.
    * @param mobLevel the mob level.
    * @param mobVariance the mob variance.
    * @return the mobs vitality.
    */
   Vitality calculateMobHealth(int mobCombatSkill, int mobLevel, float mobVariance);

   /**
    * Calculates the amount of experience gained per point of damage.
    *
    * @param playerLevel the players level,.
    * @param mobLevel the mobs level.
    * @param mobVariance the mob variance.
    * @return the experience points awarded per point of damage.
    */
   float getExpPerPointOfDamage(int playerLevel, int mobLevel, float mobVariance);

   /**
    * Determines the melee result of a mob attacking an entity.
    *
    * @param attacker the attacking mobs.
    * @param victim the victim.
    * @return a MeleeResult.
    */
   MeleeResult doMobAttackEntity(Mob attacker, Entity victim);

   /**
    * Determines the melee result of a thrown attack.
    *
    * @param player the attacker.
    * @param target the target.
    * @param thrownEq the equipment being thrown.
    * @return a MeleeResult.
    */
   MeleeResult doPlayerThrowAttack(Player player, Entity target, Equipment thrownEq);

   /**
    * Determines the melee result of a player melee attack.
    *
    * @param player the attacking player.
    * @param target the target.
    * @return a MeleeResult.
    */
   MeleeResult doPlayerMeleeAttack(Player player, Entity target);

   /**
    * Calculates the amount of gold a mob drops.
    *
    * @param mob the mob
    * @return the amount of gold dropped.
    */
   int calculateMobGoldDrop(Mob mob);

}
