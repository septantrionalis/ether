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

package org.tdod.ether.util;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.engine.PlayerInputEvent;
import org.tdod.ether.ta.engine.PlayerInputEventId;
import org.tdod.ether.ta.engine.PlayerInputListener;
import org.tdod.ether.taimpl.engine.PlayerInputEventImpl;

/**
 * A utility class that contains an general listeners.
 */
public final class PlayerInputManager {

   private static Log _log = LogFactory.getLog(PlayerInputManager.class);

   private static final PlayerInputManager _instance = new PlayerInputManager();
   private static HashMap<Long, PlayerInputListener> _playerInputListeners = new HashMap<Long, PlayerInputListener>();

   /**
    * Creates a new PlayerInputManager.
    */
   private PlayerInputManager() {
   }

   //**********
   // Methods overwritten from Object.
   //**********

   /**
    * Returns the string representation of this object.
    * @return the string representation of this object.
    */
   public String toString() {
      return "PlayerInputManager";
   }

   //**********
   // Public methods
   //**********

   /**
    * Gets the instance object for this Singleton.
    *
    * @return the instance object for this Singleton
    */
   public static PlayerInputManager getInstance() {
      return _instance;
   }

   /**
    * Removes a player input listener.
    * If  listener is null, no exception is thrown and no action is performed.
    *
    * @param connectionId the connection id of the listener to be removed
    */
   public void removePlayerInputListener(long connectionId) {
      synchronized (_playerInputListeners) {
         PlayerInputListener listener = _playerInputListeners.remove(Long.valueOf(connectionId));
         if (listener == null) {
            _log.error("PlayerInputListener was null for connection id " + connectionId);
         } else {
            _log.info("Removed PlayerInputListener for connection id " + connectionId);
         }
      }
   }

   /**
    * Adds a player input listener.
    * If listener is null, no exception is thrown and no action is performed.
    *
    * @param connectionId the connection id.
    * @param listener a listener to be added
    *
    * @see #removePlayerInputListener
    */
   public void addPlayerInputListener(long connectionId, PlayerInputListener listener) {
      if (listener == null) {
         _log.info("listener was null for connectionId " + connectionId);
         return;
      }
      synchronized (_playerInputListeners) {
         _playerInputListeners.put(Long.valueOf(connectionId), listener);
      }
   }

   /**
    * Builds and distributes and internal event to all registered listeners.
    *
    * @param eventId     The event id
    * @param id          The id in the player input listener.
    * @param description The description of the event.
    */
   public static void postPlayerInputEvent(PlayerInputEventId eventId, long id, String description) {
      PlayerInputEvent playerInputEvent = new PlayerInputEventImpl(eventId, description);
      PlayerInputListener listener = _playerInputListeners.get(Long.valueOf(id));
      if (listener != null) {
         listener.executeCommand(playerInputEvent);
      }
   }
}
