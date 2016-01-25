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

import org.tdod.ether.ta.player.Agility;
import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.Charisma;
import org.tdod.ether.ta.player.Intellect;
import org.tdod.ether.ta.player.Knowledge;
import org.tdod.ether.ta.player.Physique;
import org.tdod.ether.ta.player.Stamina;

/**
 * The default implementation of base stats.
 * @author Ron Kinney
 */
public class DefaultBaseStats implements BaseStats {

   private static final long serialVersionUID = 3924252222485702167L;

   private Intellect _intellect = new DefaultIntellect();
   private Knowledge _knowledge = new DefaultKnowledge();
   private Physique _physique = new DefaultPhysique();
   private Stamina _stamina = new DefaultStamina();
   private Agility _agility = new DefaultAgility();
   private Charisma _charisma = new DefaultCharisma();

   /**
    * Creates a new DefaultBaseStats.
    */
   public DefaultBaseStats() {
   }

   /**
    * Gets the players Intellect.
    * @return the players Intellect.
    */
   public Intellect getIntellect() {
      return _intellect;
   }

   /**
    * Sets the players Intellect.
    * @param intellect the players Intellect.
    */
   public void setIntellect(Intellect intellect) {
      _intellect = intellect;
   }

   /**
    * The players Knowledge.
    * @return the players knowledge.
    */
   public Knowledge getKnowledge() {
      return _knowledge;
   }

   /**
    * Sets the players knowledge.
    * @param knowledge the players knowledge.
    */
   public void setKnowledge(Knowledge knowledge) {
      _knowledge = knowledge;
   }

   /**
    * Gets the players physique.
    * @return the players physique.
    */
   public Physique getPhysique() {
      return _physique;
   }

   /**
    * Sets the players physique.
    * @param physique the players physique.
    */
   public void setPhysique(Physique physique) {
      _physique = physique;
   }

   /**
    * Gets the players stamina.
    * @return the players stamina.
    */
   public Stamina getStamina() {
      return _stamina;
   }

   /**
    * Sets the players stamina.
    * @param stamina the players stamina.
    */
   public void setStamina(Stamina stamina) {
      _stamina = stamina;
   }

   /**
    * Gets the players agility.
    * @return the players agility.
    */
   public Agility getAgility() {
      return _agility;
   }

   /**
    * Sets the players agility.
    * @param agility the players agility.
    */
   public void setAgility(Agility agility) {
      _agility = agility;
   }

   /**
    * Gets the players charisma.
    * @return the players charisma.
    */
   public Charisma getCharisma() {
      return _charisma;
   }

   /**
    * Sets the players charisma.
    * @param charisma the players charisma.
    */
   public void setCharisma(Charisma charisma) {
      _charisma = charisma;
   }

   /**
    * Adds the base stats to this base stats.
    * @param baseStats the base stats to add.
    */
   public void add(BaseStats baseStats) {
      _intellect.add(baseStats.getIntellect());
      _knowledge.add(baseStats.getKnowledge());
      _physique.add(baseStats.getPhysique());
      _stamina.add(baseStats.getStamina());
      _agility.add(baseStats.getAgility());
      _charisma.add(baseStats.getCharisma());

   }

   /**
    * Decreases all of the enchantment timers for these base stats.
    * @return true if any timers are expired.
    */
   public boolean decreaseTimers() {
      boolean intExpired = _intellect.decreaseTimers();
      boolean knoExpired = _knowledge.decreaseTimers();
      boolean phyExpired = _physique.decreaseTimers();
      boolean staExpired = _stamina.decreaseTimers();
      boolean agiExpired = _agility.decreaseTimers();
      boolean chaExpired = _charisma.decreaseTimers();

      return intExpired || knoExpired || phyExpired
         || staExpired || agiExpired || chaExpired;
   }

   /**
    * Resets all enchantment timers.
    */
   public void resetEnchants() {
      _intellect.resetEnchants();
      _knowledge.resetEnchants();
      _physique.resetEnchants();
      _stamina.resetEnchants();
      _agility.resetEnchants();
      _charisma.resetEnchants();
   }

   /**
    * Removes all drain timers.
    */
   public void removeDrains() {
      _intellect.removeDrain();
      _knowledge.removeDrain();
      _physique.removeDrain();
      _stamina.removeDrain();
      _agility.removeDrain();
      _charisma.removeDrain();
   }

   /**
    * Determines if any stats are drained.
    * @return true if the player is drain.
    */
   public boolean isDrained() {
      if (_intellect.getDrainTimer() > 0) {
         return true;
      }
      if (_knowledge.getDrainTimer() > 0) {
         return true;
      }
      if (_physique.getDrainTimer() > 0) {
         return true;
      }
      if (_stamina.getDrainTimer() > 0) {
         return true;
      }
      if (_agility.getDrainTimer() > 0) {
         return true;
      }
      if (_charisma.getDrainTimer() > 0) {
         return true;
      }

      return false;
   }
}
