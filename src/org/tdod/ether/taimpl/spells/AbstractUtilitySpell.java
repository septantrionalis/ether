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
import org.tdod.ether.ta.combat.SpellResult;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.spells.AbstractSpellCommand;

public abstract class AbstractUtilitySpell extends AbstractSpellCommand {

   private static Log _log = LogFactory.getLog(AbstractUtilitySpell.class);

   protected SpellResult _spellResult;

   protected void handleSingleTarget(Entity target) {

      // Can't cast utility spells on mobs.
      // if (target.getEntityType().equals(EntityType.MOB)) {
      //   handleUnsuccessfulSpell();
      //   return ;                  
      // }
      
      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }
      
      _spellResult = WorldManager.getGameMechanics().calculateUtilitySpellResult(_entity, target, _spell);

      if (handleSpellFunction(target)) {
         handleSingleTargetMessage(target);
         handleSpellSuccess() ;
      } else {
         handleFailedSpell();
      }
      
   }
   
   protected void handleAreaTarget(ArrayList<Entity> targets, boolean effectsPlayers) {
      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }

      _spellResult = WorldManager.getGameMechanics().calculateAreaUtilitySpellResult(_entity, _spell);

      handleAreaMessage();
      if (!targets.contains(_entity)) {
         targets.add(_entity);         
      }
      for (Entity target:targets) {
         handleSpellFunction(target);
      }
      handleSpellSuccess() ;
   }

   protected void handleNoTargets(String parameters) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");      
   }
   
   protected void handleSpellSuccess() {
      _entity.addExperience(WorldManager.getGameMechanics().getExpForUtilitySpells(_entity, _spell));
      _entity.getMana().subtractCurMana(_spell.getMana());
      WorldManager.getGameMechanics().handleMentalRestDelay(_entity, _spell);
   }

   /**
    * Handles the functionality of the spell only.  No player messages should go in here because the messages could be
    * determined by the outcome of this method call.
    * @param target The target.
    * @return true if the spell succeeded, false otherwise.
    */
   protected abstract boolean handleSpellFunction(Entity target) ;
   protected abstract void handleSingleTargetMessage(Entity target) ;
   protected abstract void handleAreaMessage() ;

}
