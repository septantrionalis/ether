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

package org.tdod.ether.taimpl.mobs;

import org.tdod.ether.ta.mobs.GeneralAttack;
import org.tdod.ether.ta.mobs.enums.MobWeaponType;
import org.tdod.ether.taimpl.mobs.enums.AttackEffectEnum;

/**
 * The default implementation class for a general attack.
 * @author Ron Kinney
 */
public class DefaultGeneralAttack implements GeneralAttack {

   private int    _numAttacks;
   private String _weapon;
   private int    _minDamage;
   private int    _maxDamage;

   private AttackEffectEnum _attackEffect = AttackEffectEnum.INVALID;
   private int              _minAttackEffect;
   private int              _maxAttackEffect;

   private MobWeaponType _mobWeaponType = null;

   /**
    * Creates a new DefaultGeneralAttack.
    */
   public DefaultGeneralAttack() {

   }

   /**
    * Gets the total number of attacks.
    * @return the total number of attacks
    */
   public int getNumAttacks() {
      return _numAttacks;
   }

   /**
    * Sets the total number of attacks.
    * @param numAttacks the total number of attacks.
    */
   public void setNumAttacks(int numAttacks) {
      _numAttacks = numAttacks;
   }

   /**
    * Gets the weapon string.
    * @return the weapon string.
    */
   public String getWeapon() {
      return _weapon;
   }

   /**
    * Sets the weapon string.
    * @param weapon the weapon string.
    */
   public void setWeapon(String weapon) {
      _weapon = weapon;
   }

   /**
    * Gets the minimum damage.
    * @return the minimum damage.
    */
   public int getMinDamage() {
      return _minDamage;
   }

   /**
    * Sets the minimum damage.
    * @param minDamage the minimum damage.
    */
   public void setMinDamage(int minDamage) {
      _minDamage = minDamage;
   }

   /**
    * Gets the maximum damage.
    * @return the maximum damage.
    */
   public int getMaxDamage() {
      return _maxDamage;
   }

   /**
    * Sets the maximum damage.
    * @param maxDamage the maximum damage.
    */
   public void setMaxDamage(int maxDamage) {
      _maxDamage = maxDamage;
   }

   /**
    * Gets the attack effect.
    * @return the attack effect.
    */
   public AttackEffectEnum getAttackEffect() {
      return _attackEffect;
   }

   /**
    * Sets the attack effect.
    * @param attackEffect the attack effect.
    */
   public void setAttackEffect(AttackEffectEnum attackEffect) {
      _attackEffect = attackEffect;
   }

   /**
    * Gets the minimum attack effect.
    * @return the minimum attack effect.
    */
   public int getMinAttackEffect() {
      return _minAttackEffect;
   }

   /**
    * Sets the minimum attack effect.
    * @param minAttackEffect the minimum attack effect.
    */
   public void setMinAttackEffect(int minAttackEffect) {
      _minAttackEffect = minAttackEffect;
   }

   /**
    * Gets the maximum attack effect.
    * @return the maximum attack effect.
    */
   public int getMaxAttackEffect() {
      return _maxAttackEffect;
   }

   /**
    * Sets the maximum attack effect.
    * @param maxAttackEffect the maximum attack effect.
    */
   public void setMaxAttackEffect(int maxAttackEffect) {
      _maxAttackEffect = maxAttackEffect;
   }

   /**
    * Gets the mob weapon type.
    * @return the mob weapon type.
    */
   public MobWeaponType getMobWeaponType() {
      return _mobWeaponType;
   }

   /**
    * Sets the mob weapon type.
    * @param mobWeaponType the mob weapon type.
    */
   public void setMobWeaponType(MobWeaponType mobWeaponType) {
      _mobWeaponType = mobWeaponType;
   }

}
