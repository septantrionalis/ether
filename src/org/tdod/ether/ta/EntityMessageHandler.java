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

package org.tdod.ether.ta;

import org.tdod.ether.ta.combat.MeleeResult;
import org.tdod.ether.taimpl.cosmos.ExitDirectionEnum;

/**
 * An interface that defines the message handler for an entity.  These are messages that are displayed to the entity
 * itself.
 *
 * @author Ron Kinney
 */
public interface EntityMessageHandler {

   /**
    * Message to the room when a mob misses an attack.
    * @param mobName The name of the mob.
    * @return the message to the room when a mob misses an attack.
    */
   String getMobMissRoomMessage(String mobName);

   /**
    * Message to the room when a mob hits with an attack.
    * @param mobName The name of the mob being hit.
    * @param result The melee result.
    * @return the message to the room when a mob hits with an attack.
    */
   String getMobHitRoomMessage(String mobName, MeleeResult result);

   /**
    * Message to the room when a mob dodges the attack.
    * @param mobName The name of the mob that dodged.
    * @return the message to the room when a mob dodges the attack.
    */
   String getMobDodgeRoomMessage(String mobName);

   /**
    * Message to the room when the attack glances of a mobs armor.
    * @param mobName The name of the mob that was being attacked.
    * @param result The melee result.
    * @return The message to the room when the attack glances of a mobs armor.
    */
   String getMobGlanceRoomMessage(String mobName, MeleeResult result);

   /**
    * Gets the "came back" message.
    * @return the "came back" message.
    */
   String getCameBackMessage();

   /**
    * Gets the string that is displayed when the entity enters a room.
    * @param exitDirection the exit direction.
    * @return the string that is displayed when the entity enters a room.
    */
   String getEnterMessage(ExitDirectionEnum exitDirection);

   /**
    * Gets the string that is displayed when the entity leaves a room.
    * @param exitDirection the exit direction.
    * @return the string that is displayed when the entity leaves a room.
    */
   String getLeaveMessage(ExitDirectionEnum exitDirection);

}
