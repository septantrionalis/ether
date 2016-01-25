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

package org.tdod.ether.ta.cosmos;


/**
 * An interface that defines a barrier.
 *
 * @author Ron Kinney
 */
public interface Barrier {

   /**
    * Gets the barrier number.
    *
    * @return the barrier associated with the number.
    */
   int getNumber();

   /**
    * Sets the barrier number.
    *
    * @param number the barrier number.
    */
   void setNumber(int number);

   /**
    * Gets the message sent to the player when the barrier is locked.
    *
    * @return the message sent to the player when the barrier is locked.
    */
   String getLockedMessage();

   /**
    * Sets the locked message of the barrier.
    *
    * @param lockedMessage the locked message of the barrier.
    */
   void setLockedMessage(String lockedMessage);

   /**
    * Gets the message of the barrier when the player unlocks it.
    *
    * @return the message of the barrier when the player unlocks it.
    */
   String getUnlockedMessage();

   /**
    * Sets the message of the barrier when the player unlocks it.
    *
    * @param unlockedMessage the message of the barrier when the player unlocks it.
    */
   void setUnlockedMessage(String unlockedMessage);

   /**
    * Gets the message sent to a rogue when he unlocks the barrier.
    *
    * @return the message sent to a rogue when he unlocks the barrier.
    */
   String getRogueMessage();

   /**
    * Sets the message sent to a rogue when he unlocks the barrier.
    *
    * @param rogueMessage the message sent to a rogue when he unlocks the barrier.
    */
   void setRogueMessage(String rogueMessage);

   /**
    * Gets the barrier value.
    *
    * @return the barrier value.
    */
   int getValue();

   /**
    * Sets the barrier value.
    *
    * @param value the barrier value.
    */
   void setValue(int value);

}
