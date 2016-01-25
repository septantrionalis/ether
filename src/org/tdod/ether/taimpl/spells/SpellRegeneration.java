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

/**
 * PLAYER: &bYou intoned the spell for Hilorex!
 * TARGET: &CHilorex just intoned a regenerative spell for you which healed 8 damage!
 * ROOM:   &bHilorex just intoned a regenerative spell for Hilorex!
 */
public class SpellRegeneration extends AbstractUtilitySpell {

   protected boolean handleSpellFunction(Entity target) {
      target.getVitality().addCurVitality(_spellResult.getNumberEffect()) ;
      target.setRegenerationTicker(_spellResult.getNumberEffect());
      return true;
   }
   
   protected void handleSingleTargetMessage(Entity target) {
      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLRGN.getMessage(), 
            target.getName());
      String messageToVictim = MessageFormat.format(TaMessageManager.HELYOU.getMessage(), 
            _entity.getName(), _spell.getMessage(), _spellResult.getNumberEffect());
      String messageToRoom = MessageFormat.format(TaMessageManager.HELOTH.getMessage(), 
            _entity.getName(), _spell.getMessage(), target.getName());

      displayBeneficialMessages(messageToPlayer, messageToRoom, messageToVictim, target);
   }
   
   protected void handleAreaMessage() {
      _entity.println("You cast an area spell.") ;
      _room.println(_entity, _entity.getName() + " casts an area spell!.", false);

   }

}
