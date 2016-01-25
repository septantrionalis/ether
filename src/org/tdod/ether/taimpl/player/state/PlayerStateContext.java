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

package org.tdod.ether.taimpl.player.state;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.player.PlayerConnection;

public class PlayerStateContext {
   
   private static Log _log = LogFactory.getLog(PlayerStateContext.class);

   private PlayerState      _playerState;
   private PlayerConnection _playerConnection ;
   
   public PlayerStateContext() {
      setState(new LoginPlayerState()) ;
   }
   
   public void setState(PlayerState playerState) {
      _playerState = playerState ;
   }
   
   public void execute(String input) {
      try {
         _playerState.execute(this, input) ;
      } catch (Exception e) {
         _playerConnection.getOutput().println("Something went wrong!  Please contact an admin.");
         _log.error(e, e);
      }
   }

   public PlayerConnection getPlayerConnection() {
      return _playerConnection;
   }

   public void setPlayerConnection(PlayerConnection playerConnection) {
      _playerConnection = playerConnection;
   }
   
   public GameOutput getOutput() {
      return _playerConnection.getOutput();
   }

}
