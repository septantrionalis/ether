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

package org.tdod.ether.taimpl.mobs.specialability;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.SpecialAbility;
import org.tdod.ether.ta.mobs.specialability.SpecialAbilityCommand;

public class AttackAllTumble extends SpecialAbilityCommand {

   private static Log _log = LogFactory.getLog(AttackAllTumble.class);

   public void execute(Mob mob, Room room) {
      SpecialAbility ability = mob.getSpecialAbility();
      
      String messageToRoom = "&RThe " + mob.getName() + " " + ability.getSpecialAbilityDescription() + ".";
      room.println(null, messageToRoom, false) ;

      Entity victim = mob.findRandomHostileEntity(mob.getRoom());
      
      ArrayList<Entity> tumbledList = new ArrayList<Entity>();
      while (victim != null) {
         tumbleSingleEntity(mob, victim) ;
         victim = mob.findRandomHostileEntity(mob.getRoom());
         if (tumbledList.contains(victim)) {
            // Safety check to prevent an infinite loop.
            _log.debug(mob.getName() + " attempted to tumble " + victim.getName() + " twice in room " + room.getRoomNumber() + ".  Aborting.");
            return;
         }
         tumbledList.add(victim);
      }
   }
   
}
