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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.player.PlayerConnectedEvent;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.player.PlayerConnectedListener;
import org.tdod.ether.ta.telnet.TaShell;
import org.tdod.ether.taimpl.player.PlayerConnectedEventImpl;

/**
 * Handles player connections.
 * @author rkinney
 */
public final class PlayerConnectedManager {

   private static Log _log = LogFactory.getLog(PlayerConnectedManager.class);

   private static final PlayerConnectedManager _instance = new PlayerConnectedManager();
   private static LinkedList<PlayerConnectedListener> _playerConnectedListeners = new LinkedList<PlayerConnectedListener>();

   /**
    * Creates a new PlayerConnectedManager.
    */
   private PlayerConnectedManager() {
   }

   //**********
   // Methods overwritten from Object.
   //**********

   /**
    * Returns the string representation of this object.
    * @return the string representation of this object.
    */
   public String toString() {
      return "PlayerConnectedManager";
   }

   //**********
   // Public methods
   //**********

   /**
    * Gets the instance object for this Singleton.
    *
    * @return the instance object for this Singleton
    */
   public static PlayerConnectedManager getInstance() {
      return _instance;
   }

   /**
    * Removes a player input listener.
    * If  listener is null, no exception is thrown and no action is performed.
    *
    * @param listener a listener to be removed
    */
   public void removePlayerConnectedListener(PlayerConnectedListener listener) {
      if (listener == null) {
         return;
      }
      synchronized (_playerConnectedListeners) {
         _playerConnectedListeners.remove(listener);
      }
   }

   /**
    * Adds a player connected listener.
    * If listener is null, no exception is thrown and no action is performed.
    *
    * @param listener a listener to be added
    *
    * @see #removePlayerConnectedListener
    */
   public void addPlayerConnectedListener(PlayerConnectedListener listener) {
      if (listener == null) {
         return;
      }
      synchronized (_playerConnectedListeners) {
         if (!_playerConnectedListeners.contains(listener)) {
            _playerConnectedListeners.add(listener);
         }
      }
   }

   /**
    * Builds and distributes and internal event to all registered listeners.
    *
    * @param eventId     The event id
    * @param shell       The shell associated to the connection event.
    */
   public static void postPlayerConnectedEvent(PlayerConnectedEventId eventId, TaShell shell) {
      PlayerConnectedEvent playerConnectedEvent = new PlayerConnectedEventImpl(eventId, shell);
      Collection<?> listeners = (Collection<?>) _playerConnectedListeners.clone();
      Iterator<?> iterator = listeners.iterator();
      while (iterator.hasNext()) {
         Object o = iterator.next();
         if (o instanceof PlayerConnectedListener) {
            PlayerConnectedListener listener = (PlayerConnectedListener) o;
            listener.handleConnectionEvent(playerConnectedEvent);            
         } else {
            _log.error("Object is of type " + o.getClass() + " instead of PlayerConnectedListener.");
         }
      }
   }


}
