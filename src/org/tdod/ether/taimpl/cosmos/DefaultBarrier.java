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

package org.tdod.ether.taimpl.cosmos;

import org.tdod.ether.ta.cosmos.Barrier;

/**
 * The default implementation class for a barrier.
 *
 * @author Ron Kinney
 */
public class DefaultBarrier implements Barrier {

   private int         _number;
   private String      _lockedMessage;
   private String      _unlockedMessage;
   private String      _rogueMessage;
   private int         _value;

   /**
    * Gets the barrier number.
    *
    * @return the barrier associated with the number.
    */
   public int getNumber() {
      return _number;
   }

   /**
    * Sets the barrier number.
    *
    * @param number the barrier number.
    */
   public void setNumber(int number) {
      _number = number;
   }

   /**
    * Gets the message sent to the player when the barrier is locked.
    *
    * @return the message sent to the player when the barrier is locked.
    */
   public String getLockedMessage() {
      return _lockedMessage;
   }

   /**
    * Sets the locked message of the barrier.
    *
    * @param lockedMessage the locked message of the barrier.
    */
   public void setLockedMessage(String lockedMessage) {
      _lockedMessage = lockedMessage;
   }

   /**
    * Gets the message of the barrier when the player unlocks it.
    *
    * @return the message of the barrier when the player unlocks it.
    */
   public String getUnlockedMessage() {
      return _unlockedMessage;
   }

   /**
    * Sets the message of the barrier when the player unlocks it.
    *
    * @param unlockedMessage the message of the barrier when the player unlocks it.
    */
   public void setUnlockedMessage(String unlockedMessage) {
      _unlockedMessage = unlockedMessage;
   }

   /**
    * Gets the message sent to a rogue when he unlocks the barrier.
    *
    * @return the message sent to a rogue when he unlocks the barrier.
    */
   public String getRogueMessage() {
      return _rogueMessage;
   }

   /**
    * Sets the message sent to a rogue when he unlocks the barrier.
    *
    * @param rogueMessage the message sent to a rogue when he unlocks the barrier.
    */
   public void setRogueMessage(String rogueMessage) {
      _rogueMessage = rogueMessage;
   }

   /**
    * Gets the barrier value.
    *
    * @return the barrier value.
    */
   public int getValue() {
      return _value;
   }

   /**
    * Sets the barrier value.
    *
    * @param value the barrier value.
    */
   public void setValue(int value) {
      _value = value;
   }

}
