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

import java.io.ObjectStreamException;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.manager.PlayerFacade;
import org.tdod.ether.ta.player.Attacks;
import org.tdod.ether.util.PropertiesManager;

/**
 * The implementation class for an attack.
 * @author Ron Kinney
 */
public class DefaultAttacks implements Attacks {

   private static final long serialVersionUID = 8699730991982566792L;

   /**
    * The default number of attacks the player has.
    */
   private static final int STARTING_ATTACKS
      = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.STARTING_NUM_OF_ATTACKS)).intValue();

   private int      _attacksLeft;

   private transient Entity _entity;

   /**
    * Creates a new DefaultAttacks.
    * @param entity the entity whom this object belongs to.
    */
   public DefaultAttacks(Entity entity) {
      _entity = entity;
   }

   /**
    * Performs an attack.  This method does not check the number of attacks
    * left.  It just reduces the number of attacks by one.
    */
   public void attack() {
      _attacksLeft -= 1;
   }

   /**
    * Resets the players attack.
    */
   public void reset() {
      _attacksLeft = getMaxAttacks();
   }

   /**
    * Gets the number of attacks left.
    * @return the number of attacks left.
    */
   public int getAttacksLeft() {
      return _attacksLeft;
   }

   /**
    * Sets the number of attacks left.
    * @param attacksLeft the number of attacks left.
    */
   public void setAttacksLeft(int attacksLeft) {
      _attacksLeft = attacksLeft;
   }

   /**
    * Gets the maximum number of attacks the player can have.
    * @return the maximum number of attacks the player can have.
    */
   public int getMaxAttacks() {
      int levelBonus = _entity.getLevel() / PlayerFacade.getStartingStats().getPlayerClassDatabase().getPlayerClassData(
            _entity.getPlayerClass()).getAttacksPerLevel();
      int baseAttack = STARTING_ATTACKS + levelBonus;

      int maxBaseAttacks = PlayerFacade.getStartingStats().getPlayerClassDatabase().getPlayerClassData(
            _entity.getPlayerClass()).getMaxBaseAttacks();
      if (baseAttack > maxBaseAttacks) {
         baseAttack = maxBaseAttacks;
      }
      return baseAttack + _entity.getStats().getAgility().getAttackNumberBonus();
   }

   /**
    * Overrides the readResolve method.
    * @return an instance of this object.
    * @throws ObjectStreamException if something goes wrong.
    */
   private Object readResolve() throws ObjectStreamException {
      _entity = null;
      return this;
   }
}
