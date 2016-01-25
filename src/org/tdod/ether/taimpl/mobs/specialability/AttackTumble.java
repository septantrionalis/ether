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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.mobs.SpecialAbility;
import org.tdod.ether.ta.mobs.specialability.SpecialAbilityCommand;

public class AttackTumble extends SpecialAbilityCommand {
   
   public void execute(Mob mob, Room room) {
      Entity victim = mob.findRandomHostileEntity(room);
      if (victim == null) {
         return;
      }

      SpecialAbility ability = mob.getSpecialAbility();

      String messageToVictim = "&RThe " + mob.getName() + " " + ability.getSpecialAbilityDescription() + " you.";
      victim.println(messageToVictim) ;
      String messageToRoom = "&MThe " + mob.getName() + " " + ability.getSpecialAbilityDescription() + " " + victim.getName() + ".";
      room.println(victim, messageToRoom, false) ;

      tumbleSingleEntity(mob, victim);
   }
   
}
