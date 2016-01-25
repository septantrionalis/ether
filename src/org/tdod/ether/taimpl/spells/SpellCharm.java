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
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.spells.AbstractSpellCommand;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.TaMessageManager;

public class SpellCharm extends AbstractSpellCommand {

   private static Log _log = LogFactory.getLog(SpellCharm.class);
   
   /**
    * &bYou performed a charm spell on the %s!
    * Minex just performed a charm spell on the kobold!
    */
   protected void handleSingleTarget(Entity target) {
      // Can't charm arena mobs.
      if (RoomFlags.SAFE.isSet(_room.getRoomFlags()) ||
          RoomFlags.ARENA.isSet(_room.getRoomFlags())) {
         _entity.print(TaMessageManager.NCSHER.getMessage());
         return;
      }

      // Can't charm players, though that would be cool.
      if (target.getEntityType().equals(EntityType.PLAYER)) {
         _entity.print(TaMessageManager.NOCHCH.getMessage());
         return;
      }

      // Random failure chance.
      if (!WorldManager.getGameMechanics().isCharmSpellSuccess(_entity, (Mob)target, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }

      String messageToPlayer = MessageFormat.format(TaMessageManager.SPLCHM.getMessage(),
            target.getName());
      String messageToRoom = MessageFormat.format(TaMessageManager.CHMOTH.getMessage(),
            _entity.getName(), _spell.getMessage(), target.getName());
      
      _entity.print(messageToPlayer) ;
      _room.print(_entity, messageToRoom, false);

      // TODO - modify the mobs behavior here.
      // Place the summoned mob into the group.
      Entity groupLeader = _entity.getGroupLeader();
      if (groupLeader.equals(_entity)) {
         _entity.getGroupList().add(target) ;
         target.setGroupLeader(_entity) ;
      } else {
         groupLeader.getGroupList().add(target) ;
         target.setGroupLeader(groupLeader) ;
      }
      
      handleSpellSuccess();
   }

   protected void handleAreaTarget(ArrayList<Entity> targets, boolean pvp) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");      
   }

   protected void handleNoTargets(String parameters) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");      
   }

}
