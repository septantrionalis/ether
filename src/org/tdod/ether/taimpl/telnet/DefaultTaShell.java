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

package org.tdod.ether.taimpl.telnet;

import java.text.MessageFormat;

import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.telnet.TaShell;
import org.tdod.ether.util.PlayerConnectedManager;
import org.tdod.ether.util.TaMessageManager;

import com.meyling.telnet.shell.ShellIo;

public class DefaultTaShell implements TaShell {

   private static Log _log = LogFactory.getLog(DefaultTaShell.class);

   private Connection _connection;
   private ShellIo    _shellIo;
   private Process    _process;
   private Thread     _shellThread;

   // private boolean    _threadSuspended = true;

   private InputStreamGobbler _inputGobbler = null;

   //**********
   // Public methods
   //**********

   //**********
   // Methods overwritten from Shell.
   //**********

   public void run(Connection con) {
      _log.info("run()");
      _connection = con;
      _shellIo = new ShellIo(_connection);
      _connection.addConnectionListener(this);

      try {
         _shellIo.eraseScreen();
         _shellIo.homeCursor();
         _shellIo.setBold(true);
         _shellIo.flush();
         exec();
      } catch (Exception e) {
         _log.fatal(e, e);
      } catch (Error e) {
         _log.fatal(e, e);
         throw e;
      } catch (Throwable e) {
         _log.fatal(e, e);
      } finally {
         // close shell process if connection is closed
         if (_process != null) {
            _process.destroy();
         }
      }

   }

   //**********
   // Methods overwritten from ConnectionListener.
   //**********

   public void connectionIdle(ConnectionEvent ce) {
      _log.info("connectionIdle()");
   }

   public void connectionTimedOut(ConnectionEvent ce) {
      _log.info("Connection timed out for " + _connection.getConnectionData().getHostName() 
            + " " + _connection.getConnectionData().getPort());
      
      // I need to determine a better way to disconnect an idle player without coupling the Shell object
      // with the player object.
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         if (getConnection().getId() == playerConn.getShell()
               .getConnection().getId()) {
            playerConn.getPlayer().save();
            playerConn.getPlayer().setDisconnected(true) ;
            String message = MessageFormat.format(TaMessageManager.LEVGAM.getMessage(), playerConn.getPlayer().getName());
            playerConn.getPlayer().getRoom().print(playerConn.getPlayer(), message, false) ;
            
            PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnecting, null);
            return;
         }
      }
   }

   public void connectionLogoutRequest(ConnectionEvent ce) {
      _log.info("Connection logout request for " + _connection.getConnectionData().getHostName() 
            + " " + _connection.getConnectionData().getPort());
      PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Connected, this) ;
   }

   public void connectionSentBreak(ConnectionEvent ce) {
      _log.info("connectionSentBreak()");
   }

   public static Shell createShell() {
      return new DefaultTaShell();
   }

   //**********
   // Methods overwritten from TaShell.
   //**********

   public ShellIo getShellIo() {
      return _shellIo;
   }

   public Connection getConnection() {
      return _connection;
   }
   
   public void cleanup(String info) {
      _shellThread.resume();
      // _threadSuspended = false;
      // _shellThread.notify();
      _log.info("Cleanup for " + info);
   }
    
   public void setHideInput(boolean hideInput) {
      _inputGobbler.setHideInput(hideInput);
   }
   
   //**********
   // Private methods
   //**********

   private void exec() throws InterruptedException {
      _inputGobbler = new InputStreamGobbler(_connection, _shellIo, this);
      _inputGobbler.start();         
      _shellThread = Thread.currentThread();
      PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Connected, this);
      
      _shellThread.suspend();
      
      /* try {
         synchronized(_shellThread) {
            while (_threadSuspended) {
               _shellThread.wait();
            }
         }         
      } catch (InterruptedException e){
         _log.info("Shell thread interrupted.  Shutting down.");
      } */
   }

}
