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

import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PlayerConnectedManager;

public class PromptPasswordState implements PlayerState {

   public void execute(PlayerStateContext stateContext, String input) {
      String inputHash = GameUtil.byteArrayToHexString(GameUtil.computeHash(input));
      if (inputHash.equals(stateContext.getPlayerConnection().getPlayer().getPassword())) {

         PlayerConnection connectedPlayerConn = getConnectedPlayer(stateContext.getPlayerConnection());
         if (connectedPlayerConn != null) {
            // The player is already connected.  Disconnect the old on and attach the new one.
            Player connectedPlayer = connectedPlayerConn.getPlayer();
            PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnected, connectedPlayerConn.getShell());
            connectedPlayer.setOutput(stateContext.getOutput());
            stateContext.getPlayerConnection().setPlayer(connectedPlayer);
         }
         stateContext.getPlayerConnection().getShell().setHideInput(false);         
         GameUtil.enterWorld(stateContext);                     
      } else {
         stateContext.getOutput().print("\n" + MessageManager.WRONG_PASSWORD.getMessage());
         stateContext.getPlayerConnection().getPlayer().setDisconnected(true) ;
         PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnecting, null);
      }
   }

   private PlayerConnection getConnectedPlayer(PlayerConnection originalConnection) {
      String name = originalConnection.getPlayer().getName();
      for (PlayerConnection conn:WorldManager.getPlayers()) {
         if (conn.getPlayer().getName() != null 
             && conn.getPlayer().getName().equals(name)
             && conn.getShell().getConnection().getId() != originalConnection.getShell().getConnection().getId()) {            
            return conn;
         }
      }
      return null;
   }
   

}
