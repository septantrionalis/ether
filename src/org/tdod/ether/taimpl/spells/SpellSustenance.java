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
import org.tdod.ether.util.TaMessageManager;

public class SpellSustenance extends AbstractUtilitySpell {

   protected boolean handleSpellFunction(Entity target) {
      target.addSustenance(_spellResult.getNumberEffect()) ;
      return true;
   }
   
   /**
    * PLAYER:        &bYou performed a restorative spell on Minex!
    * TARGET(guess): &CHilorex just cast a restorative spell on you!
    * ROOM(guess):   &bHilorex just cast a restorative spell on Minex!
    */
   protected void handleSingleTargetMessage(Entity target) {
      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLRES.getMessage(), 
            target.getName());
      String messageToVictim = MessageFormat.format(TaMessageManager.PROYOU.getMessage(), 
            _entity.getName(), _spell.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.PROOTH.getMessage(), 
            _entity.getName(), _spell.getMessage(), target.getName());

      displayBeneficialMessages(messageToPlayer, messageToRoom, messageToVictim, target);
   }
   
   /**
    * PLAYER:(guess) &bYou discharged the spell at friendly people in the area!
    * ROOM:(guess)   &C% just discharged %s at friendly people in the area!
    */
   protected void handleAreaMessage() {
      String messageToPlayer = TaMessageManager.SPLMCR.getMessage();
      String messageToRoom = MessageFormat.format(TaMessageManager.SPLMHO.getMessage(),
            _entity.getName(), _spell.getMessage());
      
      displayBeneficialAreaEffectMessages(messageToPlayer, messageToRoom) ;
   }

}
