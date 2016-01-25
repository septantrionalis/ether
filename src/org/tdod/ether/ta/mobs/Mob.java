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

package org.tdod.ether.ta.mobs;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.Vitality;
import org.tdod.ether.taimpl.mobs.enums.SubType;
import org.tdod.ether.taimpl.mobs.enums.Terrain;

/**
 * This is an interface for a mob.
 * @author Ron Kinney
 */
public interface Mob extends Cloneable, Entity {

   /**
    * The mob vnum.
    * @return the mob vnum.
    */
   int getVnum();

   /**
    * Sets the mob vnum.
    * @param vnum the mob vnum.
    */
   void setVnum(int vnum);

   /**
    * Gets the mob prefix description.
    * @return the mob prefix description.
    */
   String getPrefix();

   /**
    * Sets the mob prefix description.
    * @param prefix the mob prefix description.
    */
   void setPrefix(String prefix);

   /**
    * Gets the plural form of the mobs name.
    * @return the plural form of the mobs name.
    */
   String getPlural();

   /**
    * Sets the plural form of the mobs name.
    * @param plural the plural form of the mobs name.
    */
   void setPlural(String plural);

   /**
    * Sets the mob terrain.
    * @return the mob terrain.
    */
   Terrain getTerrain();

   /**
    * Sets the mob terrain.
    * @param terrain the mob terrain.
    */
   void setTerrain(Terrain terrain);

   /**
    * Gets the mob description.
    * @return the mob description.
    */
   String getDescription();

   /**
    * Sets the mob description.
    * @param description the mob description.
    */
   void setDescription(String description);

   /**
    * Gets the mob's morale.
    * @return the mob's morale.
    */
   int getMorale();

   /**
    * Sets the mobs morale.
    * @param morale the mobs morale.
    */
   void setMorale(int morale);

   /**
    * Subtracts the amount from the mobs morale.
    * @param value the amount to subtract from the mobs morale.
    */
   void subtractMorale(int value);

   /**
    * Adds the amount to the mobs morale.
    * @param value the amount to add.
    */
   void addMorale(int value);

   /**
    * Gets the mobs treasure drop.
    * @return the mobs treasure drop.
    */
   int getTreasure();

   /**
    * Sets the mobs treasure drop.
    * @param treasure the mobs treasure drop.
    */
   void setTreasure(int treasure);

   /**
    * Sets the mobs armor rating.
    * @param armor the mobs armor rating.
    */
   void setArmorRating(int armor);

   /**
    * Gets the mobs hit dice.
    * @return the mobs hit dice.
    */
   int getHitDice();

   /**
    * Sets the mobs hit dice.
    * @param hitDice the mobs hit dice.
    */
   void setHitDice(int hitDice);

   /**
    * Gets the mobs regeneration.
    * @return the mobs regeneration.
    */
   int getRegeneration();

   /**
    * Sets the mobs regeneration.
    * @param regeneration the mobs regeneration.
    */
   void setRegeneration(int regeneration);

   /**
    * Sets the combat skill.
    * @param combatSkill the combat skill.
    */
   void setCombatSkill(int combatSkill);

   /**
    * Gets the general attack.
    * @return the general attack.
    */
   GeneralAttack getGeneralAttack();

   /**
    * Sets the general attack.
    * @param generalAttack the general attack.
    */
   void setGeneralAttack(GeneralAttack generalAttack);

   /**
    * Gets the special attack percentage.
    * @return the special attack percentage.
    */
   int getSpecialAttackPercent();

   /**
    * Sets the special attack percentage.
    * @param specialAttackPercent the special attack percentage.
    */
   void setSpecialAttackPercent(int specialAttackPercent);

   /**
    * Gets the special attack.
    * @return the special attack.
    */
   SpecialAttack getSpecialAttack();

   /**
    * Sets the special attack.
    * @param specialAttack the special attack.
    */
   void setSpecialAttack(SpecialAttack specialAttack);

   /**
    * Gets the special ability.
    * @return the special ability.
    */
   SpecialAbility getSpecialAbility();

   /**
    * Sets the special ability.
    * @param specialAbility the special ability.
    */
   void setSpecialAbility(SpecialAbility specialAbility);

   /**
    * Gets the mobs spells.
    * @return the mobs spells.
    */
   MobSpells getMobSpells();

   /**
    * Sets the mobs spells.
    * @param mobSpells the mobs spells.
    */
   void setMobSpells(MobSpells mobSpells);

   /**
    * Gets the mobs sub-type.
    * @return the mobs sub-type.
    */
   SubType getSubType();

   /**
    * Sets the mobs sub-type.
    * @param subType the mobs sub-type.
    */
   void setSubType(SubType subType);

   /**
    * Sets the vitality.
    * @param vitality the vitality.
    */
   void setVitality(Vitality vitality);

   /**
    * Gets the mobs weapon.
    * @return the mobs weapon.
    */
   MobWeapon getMobWeapon();

   /**
    * Sets the mobs weapon.
    * @param mobWeapon the mobs weapon.
    */
   void setMobWeapon(MobWeapon mobWeapon);

   /**
    * Destroys this mob.  This method needs to be called to cleanup
    * any resources taken by the mob.
    */
   void destroy();

   /**
    * Clones this mob.  This method is called whenever a new mob is needed to
    * roam the world.  This takes resources, so whenever the mobs is not needed,
    * the engine needs to call destroy.
    * @param room the room that the mob will start in.
    * @return the cloned mob.
    */
   Mob clone(Room room);

   /**
    * Gets the look description.
    * @return the look description of the mob.
    */
   String getLookDescription();

   /**
    * Sets the activity ticker.  The mob will rest when this ticker is greater than 0 and
    * will do something when it reaches 0.
    * @param value the activity ticker.
    * @see isResting, decreaseActivityTicker
    */
   void setActivityTicker(int value);

   /**
    * Decreases the mobs activity ticker.
    * @see isResting, setActivityTicker
    */
   void decreaseActivityTicker();

   /**
    * Makes the mob do something if possible.  The mob will pass if it is mesmerized,
    * has health below 0, is fleeing, etc.
    */
   void doSomething();

   /**
    * Determines if this is a lair mob.
    * @return true if this is a lair mob.
    */
   boolean isLairMob();

   /**
    * Checks if this mob is friendly.  I.E., won't attack a player.
    * @return true if this is a friendly mob.
    */
   boolean isFriendly();

   /**
    * Sets the friendly flag.
    * @param isFriendly the friendly flag.
    */
   void setIsFriendly(boolean isFriendly);

   /**
    * Gets the mesmerized ticker.  A value of zero indicates that the mob
    * is not mesmerized.
    * @return the mesmerized ticker.
    */
   int getMesmerizedTicker();

   /**
    * Sets the mesmerized ticker.  A value of zero indicates that the mob
    * is not mesmerized.
    * @param mesmerizedTicker the mesmerized ticker.
    */
   void setMesmerizedTicker(int mesmerizedTicker);

   /**
    * Gets the player that tamed this mob, if any.  Null otherwise.
    * @return the player that tamed this mob, if any.  Null otherwise.
    */
   Player getTamedBy();

   /**
    * Sets the player who tamed this mob.
    * @param tamedBy the player who tamed this mob.
    */
   void setTamedBy(Player tamedBy);

   /**
    * Finds a random hostile entity within the room.
    * @param room the room that the mob will find a random hostile entity in.
    * @return a random hostile entity within the room, or null if one was not found.
    */
   Entity findRandomHostileEntity(Room room);

   /**
    * Checks if the mob is fleeing.
    * @return true if the mob is fleeing.
    */
   boolean isFleeing();

   /**
    * Sets the mobs fleeing flag.
    * @param isFleeing the fleeing flag.
    */
   void setIsFleeing(boolean isFleeing);

   /**
    * Forces the mob to flee.
    */
   void flee();

   /**
    * Gets the player that the mob is chasing, or null.
    * @return the player that the mob is chasing, or null.
    */
   Player getChasing();

   /**
    * Sets the player that the mob will chase.
    * @param chasing the player that the mob is chasing, or null.
    */
   void setChasing(Player chasing);

   /**
    * Forces the mob to chase the given player.  If a player isn't defined,
    * then nothing will happen.
    */
   void chase();

   /**
    * Gets the mobs name with the 'a' or 'an' prefix.
    * @return the mobs name with the 'a' or 'an' prefix.
    */
   String getNameWithPrefix();

   /**
    * Sets it so that this mob can track a player with the order command.
    * @param canTrack the can tracking flag.
    */
   void setCanTrack(boolean canTrack);

   /**
    * Checks if this mob can track a player with the order command.
    * @return true if this mob can track a player with the order command.
    */
   boolean canTrack();

   /**
    * Checks if this mob is an npc.
    * @return true if this mob is an npc.
    */
   boolean isNpc();

   /**
    * Sets the npc flag of this mob.
    * @param npc the npc flag of this mob.
    */
   void setNpc(boolean npc);

   /**
    * Gets the mob variance.  Not really used, yet, but a means to make mobs slightly different.
    * @return the mob variance.
    */
   public float getVariance();

}
