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
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.spells.AbstractSpellCommand;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.TaMessageManager;

public class SpellSummon extends AbstractSpellCommand {

   private static Log _log = LogFactory.getLog(SpellSummon.class);

   protected void handleSingleTarget(Entity target) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");
   }

   protected void handleAreaTarget(ArrayList<Entity> targets, boolean player) {
      _log.error(_entity.getName() + " cast the spell, " + _spell.getName() + ", but the function is not implemented.") ;
      _entity.println("Something went wrong!  Please contact an admin.");
   }

   /**
    * 
    * &MThat spell does not need to be cast at a specific person or creature.
    * arena/town
    * &PSorry, summoning spells are not permitted here.
    * 
    * PLAYER: &bYou intoned a summoning spell!
    * ROOM:   &bLimech just intoned a minor summoning spell!
    * 
    * Follow Up :
    * EVERYONE: &GA huge toad appears in a puff of reddish smoke!
    * 
    * if room full, spell success, just dont summon.
    * exp: 10*level
    */
   protected void handleNoTargets(String parameters) {
      if (parameters != null) {
         _entity.print(TaMessageManager.SDNTRG.getMessage());
         return;         
      }
      
      // Can't summon in certain rooms.
      if (RoomFlags.SAFE.isSet(_room.getRoomFlags()) ||
          RoomFlags.ARENA.isSet(_room.getRoomFlags())) {
         _entity.print(TaMessageManager.NSSHER.getMessage());
         return;
      }

      // Random failure chance.
      if (!WorldManager.getGameMechanics().isSpellSuccess(_entity, _spell)) {
         handleUnsuccessfulSpell();
         return ;         
      }

      // Find a mob based on the level given by the spell.
      // int mobLevel = Dice.roll(_spell.getMinSpellEffect(), _spell.getMaxSpellEffect()) ;
      int mobLevel = _spell.getMinSpellEffect();
      ArrayList<Mob> mobs = new ArrayList<Mob>() ;
      for (Mob mob:WorldManager.getMobList()){
         if (mob.getLevel() == mobLevel && !mob.getTerrain().equals(Terrain.SPECIAL)) {
            mobs.add(mob);
         }
      }         
      
      // Shouldn't happen, but you never know...
      if (mobs.size() < 1) {
         _entity.println("Something went wrong!  Please contact an admin.");
         _log.error("That was interesting.  No mobs want to be summoned.") ;
         return ;
      }
      
      // Randomly choose a mob.
      int index = Dice.roll(0, mobs.size() - 1);
      Mob summonedMob =  mobs.get(index).clone(_room);
      MoveFailCode code = _room.placeMob(summonedMob);
      
      // Place the summoned mob into the group.
      Entity groupLeader = _entity.getGroupLeader();
      if (groupLeader.equals(_entity)) {
         _entity.getGroupList().add(summonedMob) ;
         summonedMob.setGroupLeader(_entity) ;
      } else {
         groupLeader.getGroupList().add(summonedMob) ;
         summonedMob.setGroupLeader(groupLeader) ;
      }
      
      // Send messages.
      _entity.print(TaMessageManager.SPLSUM.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.SUMOTH.getMessage(),
            _entity.getName(), _spell.getMessage());
      _room.print(_entity, messageToRoom, false);
      
      if (code.equals(MoveFailCode.NONE)) {
         String summonMessage = MessageFormat.format(TaMessageManager.MONSUM.getMessage(),
               summonedMob.getPrefix(), summonedMob.getName());
         _room.print(null, summonMessage, true) ;         
      }
      
      // TODO Mobs are currently summoned hostile.  Will need to add them into a group.
      
      handleSpellSuccess();
   }
}
