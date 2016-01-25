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
import org.tdod.ether.taimpl.spells.enums.CureCondition;
import org.tdod.ether.util.TaMessageManager;

public class SpellHeal extends AbstractUtilitySpell {

   /**
    * PLAYER: &bYou intoned the spell for Minex which healed 6 damage!
    * TARGET: &CHilorex just intoned a minor healing spell for you which healed 6 damage!
    * ROOM:   &bHilorex just intoned a minor healing spell for Hilorex! 
    */
   protected void handleSingleTargetMessage(Entity target) {
      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLHEL.getMessage(), 
            target.getName(), _spellResult.getNumberEffect());
      String messageToVictim = MessageFormat.format(TaMessageManager.HELYOU.getMessage(), 
            _entity.getName(), _spell.getMessage(), _spellResult.getNumberEffect());
      String messageToRoom = MessageFormat.format(TaMessageManager.HELOTH.getMessage(), 
            _entity.getName(), _spell.getMessage(), target.getName());

      displayBeneficialMessages(messageToPlayer, messageToRoom, messageToVictim, target);      
   }
   
   /**
    * PLAYER:(guess) &bYou discharged the spell at friendly people in the area, healing %d damage!
    * ROOM:(guess)   &C%s just discharged %s at friendly people in the area!
    */
   protected void handleAreaMessage() {
      if (!_spell.getCureCondition().equals(CureCondition.NONE)) {
         SpellCureCondition spellCureCondition = new SpellCureCondition(_entity, _room, _spell);
         spellCureCondition.handleAreaMessage();
      } else {
         String messageToPlayer = MessageFormat.format(TaMessageManager.SPLMHL.getMessage(), 
               _spellResult.getNumberEffect());
         String messageToRoom = MessageFormat.format(TaMessageManager.SPLMHO.getMessage(),
               _entity.getName(), _spell.getMessage());         
         displayBeneficialAreaEffectMessages(messageToPlayer, messageToRoom) ;
      }
      
   }
   
   protected boolean handleSpellFunction(Entity target) {
      if (!_spell.getCureCondition().equals(CureCondition.NONE)) {
         SpellCureCondition spellCureCondition = new SpellCureCondition(_entity, _room, _spell);
         return spellCureCondition.handleSpellFunction(target) ;
      } else {
         target.getVitality().addCurVitality(_spellResult.getNumberEffect()) ;
      }
      return true;
   }
   
}
