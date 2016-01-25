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

package org.tdod.ether.ta.player;

import java.io.Serializable;

/**
 * An interface defining basic stats for a player.
 * @author Ron Kinney
 */
public interface BaseStats extends Serializable {

   /**
    * Gets the players Intellect.
    * @return the players Intellect.
    */
   Intellect getIntellect();

   /**
    * Sets the players Intellect.
    * @param intellect the players Intellect.
    */
   void setIntellect(Intellect intellect);

   /**
    * The players Knowledge.
    * @return the players knowledge.
    */
   Knowledge getKnowledge();

   /**
    * Sets the players knowledge.
    * @param knowledge the players knowledge.
    */
   void setKnowledge(Knowledge knowledge);

   /**
    * Gets the players physique.
    * @return the players physique.
    */
   Physique getPhysique();

   /**
    * Sets the players physique.
    * @param physique the players physique.
    */
   void setPhysique(Physique physique);

   /**
    * Gets the players stamina.
    * @return the players stamina.
    */
   Stamina getStamina();

   /**
    * Sets the players stamina.
    * @param stamina the players stamina.
    */
   void setStamina(Stamina stamina);

   /**
    * Gets the players agility.
    * @return the players agility.
    */
   Agility getAgility();

   /**
    * Sets the players agility.
    * @param agility the players agility.
    */
   void setAgility(Agility agility);

   /**
    * Gets the players charisma.
    * @return the players charisma.
    */
   Charisma getCharisma();

   /**
    * Sets the players charisma.
    * @param charisma the players charisma.
    */
   void setCharisma(Charisma charisma);

   /**
    * Adds the base stats to this base stats.
    * @param baseStats the base stats to add.
    */
   void add(BaseStats baseStats);

   /**
    * Decreases all of the enchantment timers for these base stats.
    * @return true if any timers are expired.
    */
   boolean decreaseTimers();

   /**
    * Resets all enchantment timers.
    */
   void resetEnchants();

   /**
    * Removes all drain timers.
    */
   void removeDrains();

   /**
    * Determines if any stats are drained.
    * @return true if the player is drain.
    */
   boolean isDrained();

}
