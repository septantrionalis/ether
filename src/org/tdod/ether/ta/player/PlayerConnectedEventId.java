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

package org.tdod.ether.ta.player;

import java.io.Serializable;

/**
 * A player connected event id.
 * @author rkinney
 */
public final class PlayerConnectedEventId  implements Serializable {

   private static final long serialVersionUID = -6879645165163766049L;

   /**
    * Creates a new PlayerConnectedEventId. The constructor is private. The only instances are the constants
    * instantiated in this module.
    *
    * @param eventCode    the numeric code for the event
    * @param description  a description of this internal event
    */
   private PlayerConnectedEventId(int eventCode, String description) {
      _eventCode = eventCode;
      _description = description;
   }

   //**********
   // Methods overridden from Object
   //**********

   /**
    * Returns a string representation of the object.
    *
    * @return string representation of the object.
    */
   public String toString() {
      return String.valueOf(getEventCode()) + ": " + getDescription();
   }

   //**********
   // Public methods
   //**********

   /**
    * Gets the event description.
    *
    * @return the event description
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets the event code.
    *
    * @return the event code
    */
   public int getEventCode() {
      return _eventCode;
   }

   //**********
   // Private methods
   //**********

   /**
    * Returns a substitute object during deserialization. The intention is to prevent duplicate
    * constants.
    *
    * @return a substitute object during deserialization.
    */
   private Object readResolve() {
      return _values[_ordinal];
   }

   //**********
   // Class attributes and constants
   //**********

   // The following are used to support Serialization. Together with the use of readResolve(),
   // this is intended to prevent duplicate constants from coexisting as a result of deserialization.
   // The idea was taken from the book "Effective Java Programming" by Joshua Bloch.
   // NOTE: The entries in the _values array must be in the same order as the declaration order of the
   // constants above.
   private static int   _nextOrdinal = 0;

   private String       _description;
   private int          _eventCode;
   private int          _ordinal = _nextOrdinal++;

   /**
    * Got a connected event.
    */
   public static final PlayerConnectedEventId Connected = new PlayerConnectedEventId(1, "Connected");

   /**
    * Got a disconnected event.
    */
   public static final PlayerConnectedEventId Disconnected = new PlayerConnectedEventId(1, "Disconnected");

   /**
    * Got a disconnecting event.
    */
   public static final PlayerConnectedEventId Disconnecting = new PlayerConnectedEventId(1, "Disconnecting");

   private static final PlayerConnectedEventId[] _values = {
      Connected,
      Disconnected,
      Disconnecting
   };

}
