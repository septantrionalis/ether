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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.ta.engine.PlayerInputEvent;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnectedEvent;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.telnet.TaShell;
import org.tdod.ether.taimpl.player.state.PlayerStateContext;
import org.tdod.ether.util.PlayerInputManager;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for a player connection.
 *
 * @author Ron Kinney
 */
public class DefaultPlayerConnection implements PlayerConnection {

   private static Log _log = LogFactory.getLog(DefaultPlayerConnection.class);

   private Player             _player = new DefaultPlayer();
   private GameOutput         _output;
   private long               _connectionId;
   private PlayerStateContext _playerStateContext = new PlayerStateContext();
   private TaShell            _shell;

   /**
    * Creates a new DefaultPlayerConnection.
    *
    * @param e a PlayerConnectedEvent.
    */
   public DefaultPlayerConnection(PlayerConnectedEvent e) {
      _connectionId = e.getShell().getConnection().getId();
      _shell = e.getShell();
      PlayerInputManager.getInstance().addPlayerInputListener(_connectionId, this);
      _output = DefaultAppFactory.createShellOutput(e.getShell().getShellIo());
      _player.setOutput(_output);
      _playerStateContext.setPlayerConnection(this);
      _output.print(getMotd());
      _playerStateContext.execute("");
   }

   //**********
   // Methods inherited from PlayerConnection.
   //**********

   /**
    * Gets the TaShell.
    * @return the TaShell.
    */
   public TaShell getShell() {
      return _shell;
   }

   /**
    * Gets the game output.
    * @return the game output.
    */
   public GameOutput getOutput() {
      return _output;
   }

   /**
    * Gets the player.
    * @return the player.
    */
   public Player getPlayer() {
      return _player;
   }

   /**
    * Sets the player.
    *
    * @param player the player.
    */
   public void setPlayer(Player player) {
      _player = player;
   }

   /**
    * Cleans up the connection resources.
    */
   public void cleanup() {
      PlayerInputManager.getInstance().removePlayerInputListener(_connectionId);
      _player.cleanup();
      if (_player.getRoom() != null) {
         _player.getRoom().removePlayer(_player);
      }
      _shell.cleanup(_player.getName());
      WorldManager.getPlayers().remove(this);

      _player = null;
   }

   //**********
   // Methods inherited from PlayerInputListener.
   //**********

   /**
    * Called when a command was executed.
    *
    * @param playerInputEvent a PlayerInputEvent.
    */
   public void executeCommand(PlayerInputEvent playerInputEvent) {
      _log.debug("Got Player Input (" + playerInputEvent.getPlayerInput() + ")");
      _playerStateContext.execute(playerInputEvent.getPlayerInput());
   }

   /**
    * Gets the MOTD.  This method doesn't cache the file.  This allows the server admin to
    * change the contents of the file without having to reboot the server.
    *
    * @return the MOTD.
    */
   private String getMotd() {
      StringBuffer motd = new StringBuffer();
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.MOTD_FILE);
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;
      try {
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         String line;
         while ((line = bufferedReader.readLine()) != null) {
            motd.append(line + "\n");
         }
      } catch (Exception e) {
         _log.error(e);
      } finally {
         try {
            if (bufferedReader != null) {
               bufferedReader.close();
            }
            if (fileReader != null) {
               fileReader.close();
            }
         } catch (IOException e) {
            _log.error(e);
         }
      }

      String version = DefaultPlayerConnection.class.getPackage().getImplementationVersion();
      if (version == null) {
         version = "(undefined)";
      }
      
      motd.append("Java Tele-Arena engine v" + version + "\n");
 
      return motd.toString();
   }
}
