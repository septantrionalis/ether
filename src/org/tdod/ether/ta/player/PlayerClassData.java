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

import org.tdod.ether.ta.player.enums.PlayerClass;

/**
 * Interface class for player class data.
 * @author rkinney
 */
public interface PlayerClassData {

   /**
    * Gets the player class.
    * @return the player class.
    */
   PlayerClass getPlayerClass();

   /**
    * Sets the player class.
    * @param playerClass the player class.
    */
   void setPlayerClass(PlayerClass playerClass);

   /**
    * Gets the vitality per level for this class.
    * @return the vitality per level for this class.
    */
   int getVitality();

   /**
    * Sets the vitality per level for this class.
    * @param vitality the vitality per level for this class.
    */
   void setVitality(int vitality);

   /**
    * Gets the initial stat modifier.
    * @return the initial stat modifier.
    */
   BaseStats getStatModifiers();

   /**
    * Gets the starting weapon for this class.
    * @return the starting weapon for this class.
    */
   int getWeapon();

   /**
    * Sets the starting weapon for this class.
    * @param weapon the starting weapon for this class.
    */
   void setWeapon(int weapon);

   /**
    * Gets the starting armor for this class.
    * @return the starting armor for this class.
    */
   int getArmor();

   /**
    * Sets the starting armor for this class.
    * @param armor the starting armor for this class.
    */
   void setArmor(int armor);

   /**
    * Gets the number of attacks per level for this class.
    * @return the number of attacks per level for this class.
    */
   int getAttacksPerLevel();

   /**
    * Sets the number of attacks per level for this class.
    * @param value the number of attacks per level for this class.
    */
   void setAttacksPerLevel(int value);

   /**
    * Gets the base number of attacks.
    * @return the base number of attacks.
    */
   int getMaxBaseAttacks();

   /**
    * Sets the base number of attacks.
    * @param value the base number of attacks.
    */
   void setMaxBaseAttacks(int value);

   /**
    * Gets the minumum starting gold.
    * @return the minumum starting gold.
    */
   int getMinStartingGold();

   /**
    * Sets the minumum starting gold.
    * @param minGold the minumum starting gold.
    */
   void setMinStartingGold(int minGold);

   /**
    * Gets the maximum starting gold.
    * @return the maximum starting gold.
    */
   int getMaxStartingGold();

   /**
    * Sets the maximum starting gold.
    * @param maxGold the maximum starting gold.
    */
   void setMaxStartingGold(int maxGold);

}
