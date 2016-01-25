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

package org.tdod.ether.taimpl.player;

import org.tdod.ether.ta.player.PlayerConnectedEvent;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.telnet.TaShell;

/**
 * A player connected event.
 * @author rkinney
 */
public class PlayerConnectedEventImpl implements PlayerConnectedEvent {

   private static final long serialVersionUID = 3283946002322192976L;
   private PlayerConnectedEventId _eventId;
   private transient TaShell      _shell;

   /**
    * Creates a new PlayerConnectedEventImpl.
    * @param eventId The event id.
    * @param shell the TaShell.
    */
   public PlayerConnectedEventImpl(PlayerConnectedEventId eventId, TaShell shell) {
      _eventId = eventId;
      _shell = shell;
   }

   //**********
   // Implementation of InternalEvent interface.
   //**********

   /**
    * Gets the event id.
    *
    * @return the event id
    */
   public PlayerConnectedEventId getEventId() {
      return _eventId;
   }


   /**
    * Gets a text description of the event.
    *
    * @return a text description of the event
    */
   public String getDescription() {
      return getEventId().getDescription();
   }

   /**
    * Gets the Shell associated with this connection event.
    * @return the TaShell.
    */
   public TaShell getShell() {
      return _shell;
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
      if (!(obj instanceof PlayerConnectedEventImpl)) {
         return false;
      }
      if (obj == this) {
         return true;
      }
      PlayerConnectedEventImpl otherObject = (PlayerConnectedEventImpl) obj;
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
      return result.toString();
   }

   //**********
   // Private methods
   //**********


}
