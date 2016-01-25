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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.spells.AbstractSpellCommand;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.TaMessageManager;

public abstract class AbstractSpellAttack extends AbstractSpellCommand {
   
   private static Log _log = LogFactory.getLog(AbstractSpellAttack.class);

   protected void handleSingleTarget(Entity target) {
      // Can't cast combat spells on self.
      if (target.equals(_entity)) {
         handleUnsuccessfulSpell();
         return ;
      }
      
      // Can't cast combat spells in a safe room.
      if  (RoomFlags.SAFE.isSet(_room.getRoomFlags())) {
         _entity.print(TaMessageManager.NOTHER.getMessage());
         return;         
      }

      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }
      
      handleSingleTargetSpellAttack(target);
   }
   
   protected void handleAreaTarget(ArrayList<Entity> targets, boolean pvp) {
      if  (RoomFlags.SAFE.isSet(_room.getRoomFlags())) {
         _entity.print(TaMessageManager.NOTHER.getMessage());
         return;         
      }

      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }
      
      handleAreaSpellAttack(targets, pvp) ;
   }
   
   protected void handleNoTargets(String parameters) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");      
   }

   abstract protected void handleSingleTargetSpellAttack(Entity target) ;
   
   abstract protected void handleAreaSpellAttack(ArrayList<Entity> targets, boolean pvp) ;
}
