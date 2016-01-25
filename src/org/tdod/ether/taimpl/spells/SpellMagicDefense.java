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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.util.TaMessageManager;

/**
 * You cast an enchantment spell on A!
 * PLAYER(guess): &bYou cast an enchantment spell on A!
 * TARGET(guess): &bYou cast an enchantment spell on A!
 * ROOM(guess):   &bYou cast an enchantment spell on A!
 */
public class SpellMagicDefense extends AbstractUtilitySpell {

   public SpellMagicDefense() {
   }

   public SpellMagicDefense(Entity entity, Room room, Spell spell) {
      _entity = entity;
      _room = room;
      _spell = spell;
   }

   protected boolean handleSpellFunction(Entity target) {
      target.setMagicProtection(23) ;
      target.setMagicProtectionTimer(_spellResult.getEffectTime());
      return true;
   }
   
   protected void handleSingleTargetMessage(Entity target) {
      // This is a guess on messages.
      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLPRO.getMessage(), 
            _spell.getMessage(), _entity.getName());
      String messageToVictim = MessageFormat.format(TaMessageManager.PROYOU.getMessage(), 
            _entity.getName(), _spell.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.PROOTH.getMessage(), 
            _entity.getName(), _spell.getMessage(), _entity.getName());

      displayBeneficialMessages(messageToPlayer, messageToRoom, messageToVictim, (Entity)_entity);      
   }
   
   /**
    * PLAYER:(guess) &bYou discharged the spell at friendly people in the area!
    * ROOM:(guess)   &C% just discharged a bright yellow mist at friendly people in the area!
    */
   protected void handleAreaMessage() {
      String messageToPlayer = TaMessageManager.SPLMCR.getMessage();
      String messageToRoom = MessageFormat.format(TaMessageManager.SPLMHO.getMessage(),
            _entity.getName(), _spell.getMessage());
      
      displayBeneficialAreaEffectMessages(messageToPlayer, messageToRoom) ;
   }

}
