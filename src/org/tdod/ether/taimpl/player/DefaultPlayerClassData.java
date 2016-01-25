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

import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.PlayerClassData;
import org.tdod.ether.ta.player.enums.PlayerClass;

/**
 * Implementation class for player class data.
 * @author rkinney
 */
public class DefaultPlayerClassData implements PlayerClassData {

   private int         _vitality;
   private PlayerClass _class = PlayerClass.INVALID;
   private BaseStats   _statModifiers = new DefaultBaseStats();
   private int         _weapon;
   private int         _armor;
   private int         _attacksPerLevel;
   private int         _maxBaseAttacks;
   private int         _minStartingGold;
   private int         _maxStartingGold;

   /**
    * Creates a new DefaultPlayerClassData.
    */
   public DefaultPlayerClassData() {
   }

   /**
    * Gets the player class.
    * @return the player class.
    */
   public PlayerClass getPlayerClass() {
      return _class;
   }

   /**
    * Sets the player class.
    * @param playerClass the player class.
    */
   public void setPlayerClass(PlayerClass playerClass) {
      _class = playerClass;
   }

   /**
    * Gets the vitality per level for this class.
    * @return the vitality per level for this class.
    */
   public int getVitality() {
      return _vitality;
   }

   /**
    * Sets the vitality per level for this class.
    * @param vitality the vitality per level for this class.
    */
   public void setVitality(int vitality) {
      _vitality = vitality;
   }

   /**
    * Gets the initial stat modifier.
    * @return the initial stat modifier.
    */
   public BaseStats getStatModifiers() {
      return _statModifiers;
   }

   /**
    * Gets the starting weapon for this class.
    * @return the starting weapon for this class.
    */
   public int getWeapon() {
      return _weapon;
   }

   /**
    * Gets the starting armor for this class.
    * @return the starting armor for this class.
    */
   public int getArmor() {
      return _armor;
   }

   /**
    * Sets the starting weapon for this class.
    * @param weapon the starting weapon for this class.
    */
   public void setWeapon(int weapon) {
      _weapon = weapon;
   }

   /**
    * Sets the starting armor for this class.
    * @param armor the starting armor for this class.
    */
   public void setArmor(int armor) {
      _armor = armor;
   }

   /**
    * Gets the number of attacks per level for this class.
    * @return the number of attacks per level for this class.
    */
   public int getAttacksPerLevel() {
      return _attacksPerLevel;
   }

   /**
    * Sets the number of attacks per level for this class.
    * @param value the number of attacks per level for this class.
    */
   public void setAttacksPerLevel(int value) {
      _attacksPerLevel = value;
   }

   /**
    * Gets the base number of attacks.
    * @return the base number of attacks.
    */
   public int getMaxBaseAttacks() {
      return _maxBaseAttacks;
   }

   /**
    * Sets the base number of attacks.
    * @param value the base number of attacks.
    */
   public void setMaxBaseAttacks(int value) {
      _maxBaseAttacks = value;
   }

   /**
    * Gets the minumum starting gold.
    * @return the minumum starting gold.
    */
   public int getMinStartingGold() {
      return _minStartingGold;
   }

   /**
    * Sets the minumum starting gold.
    * @param minGold the minumum starting gold.
    */
   public void setMinStartingGold(int minGold) {
      _minStartingGold = minGold;
   }

   /**
    * Gets the maximum starting gold.
    * @return the maximum starting gold.
    */
   public int getMaxStartingGold() {
      return _maxStartingGold;
   }

   /**
    * Sets the maximum starting gold.
    * @param maxGold the maximum starting gold.
    */
   public void setMaxStartingGold(int maxGold) {
      _maxStartingGold = maxGold;
   }

}
