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

package org.tdod.ether.taimpl.engine;

import org.tdod.ether.ta.engine.PlayerInputEvent;
import org.tdod.ether.ta.engine.PlayerInputEventId;

/**
 * Defines an player input event.
 */
public class PlayerInputEventImpl implements PlayerInputEvent {

   private static final long serialVersionUID = 5363344274277679263L;
   private PlayerInputEventId _eventId;
   private String             _playerInput;

   /**
    * Creates a new PlayerInputEventImpl.
    *
    * @param eventId The event id.
    * @param playerInput The player input.
    */
   public PlayerInputEventImpl(PlayerInputEventId eventId, String playerInput) {
      _eventId = eventId;
      _playerInput = playerInput;
   }

   //**********
   // Implementation of InternalEvent interface.
   //**********

   /**
    * Gets a text description of the event.
    *
    * @return a text description of the event
    */
   public String getDescription() {
      return getEventId().getDescription();
   }

   /**
    * Gets the player input.
    *
    * @return The player input.
    */
   public String getPlayerInput() {
      return _playerInput;
   }

   //**********
   // Methods overridden from Object
   //**********

   /**
    * Indicates whether some other object is "equal to" this one.
    *
    * @param   obj   the reference object with which to compare.
    *
    * @return  <code>true</code> if this object is the same as the obj
    *          argument; <code>false</code> otherwise.
    */
   public boolean equals(Object obj) {
      if (!(obj instanceof PlayerInputEventImpl)) {
         return false;
      }
      if (obj == this) {
         return true;
      }
      PlayerInputEventImpl otherObject = (PlayerInputEventImpl) obj;
      if (!getDescription().equals(otherObject.getDescription())) {
         return false;
      }
      return true;
   }

   /**
    * Returns a hash code value for the object. This method is
    * supported for the benefit of hashtables such as those provided by
    * <code>java.util.Hashtable</code>.
    *
    * @return  a hash code value for this object.
    * @see     java.lang.Object#equals(java.lang.Object)
    * @see     java.util.Hashtable
    */
   public int hashCode() {
      int result = 17;
      result = 37 * result + getEventId().hashCode();
      result = 37 * result + getDescription().hashCode();
      result = 37 * result + getPlayerInput().hashCode();
      return result;
   }

   /**
    * Returns a string representation of the object.
    *
    * @return a string representation of the object
    */
   public String toString() {
      StringBuffer result = new StringBuffer();
      result.append(getDescription());
      result.append(",");
      result.append(getPlayerInput());
      return result.toString();
   }

   //**********
   // Private methods
   //**********

   /**
    * Gets the event id.
    *
    * @return the event id
    */
   private PlayerInputEventId getEventId() {
      return _eventId;
   }

}
