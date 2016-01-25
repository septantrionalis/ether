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

package org.tdod.ether.ta.telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.io.terminal.TerminalManager;
import net.wimpi.telnetd.net.PortListener;
import net.wimpi.telnetd.shell.ShellManager;
import net.wimpi.telnetd.util.PropertiesLoader;
import net.wimpi.telnetd.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.meyling.telnet.startup.Loader;

/**
 * The telnet service.
 * @author rkinney
 */
public final class TelnetService {

   private static Log         _log = LogFactory.getLog(TelnetService.class);

   /** The one and only instance of this telnet server. */
   private static TelnetService  _instance = null;

   private ShellManager          _shellManager;

   /** All port listeners. */
   private List<PortListener>    _listeners;

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private TelnetService() {
      _instance = this;
      _listeners = new ArrayList<PortListener>(5);
   }

   /**
    * Factory method to create a TelnetD singleton instance, loading the
    * standard properties files from the location
    *   <code>config/telnetd.properties</code>.
    *
    * @return     TenetD instance that has been properly set up according to the
    *             passed in properties, and is ready to start serving.
    * @throws     BootException    Setup process failed.
    */
   public static TelnetService createTelnetD() throws BootException {
       try {
           // try to load properties from classpath
           URL url = Loader.getResource("telnetd.properties");
           _log.info("url=" + url);
           if (url == null) {
               throw new BootException("Failed to load configuration file.");
           }
           Properties properties = PropertiesLoader.loadProperties(url);
           return createTelnetD(properties);
       } catch (IOException e) {
          _log.fatal(e, e);
          throw new BootException("Failed to load configuration file.");
       }
   }

   /**
    * Factory method to create a TelnetD Instance.
    *
    * @param     main    Properties object with settings for the TelnetD.
    * @return     TenetD instance that has been properly set up according to
    *             the passed in properties, and is ready to start serving.
    * @throws    BootException    Setup process failed.
    */
   public static TelnetService createTelnetD(final Properties main) throws BootException {

       if (_instance == null) {
           final TelnetService td = new TelnetService();
           td.prepareShellManager(main);
           td.prepareTerminals(main);
           final String[] listnames = StringUtil.split(
               main.getProperty("listeners"), ",");
           for (int i = 0; i < listnames.length; i++) {
               td.prepareListener(listnames[i], main);
           }
           return td;
       } else {
           throw new BootException("Singleton already instantiated.");
       }

   }

   /**
    * Start this telnet daemon, respectively all configured listeners.
    */
   public void start() {
      _log.debug("start()");
       for (int i = 0; i < _listeners.size(); i++) {
           final PortListener plis = (PortListener) _listeners.get(i);
           plis.start();
       }
   }

   /**
    * Stops the telnet service.
    */
   public void stop() {
      _log.debug("stop()");
      for (int i = 0; i < _listeners.size(); i++) {
          final PortListener plis = (PortListener) _listeners.get(i);
          plis.stop();
      }
   }

   /**
    * Prepares the terminal services.
    * @param terminals the terminal properties.
    * @throws BootException is thrown if initialization fails.
    */
   private void prepareTerminals(final Properties terminals) throws BootException {
      TerminalManager.createTerminalManager(terminals);
   }

   /**
    * Method to prepare the ShellManager.<br>
    *
    * Creates and prepares a Singleton instance of the ShellManager, with
    * settings from the passed in Properties.
    *
    * @param settings
    *            Properties object that holds main settings.
    * @throws BootException
    *             if preparation fails.
    */
   private void prepareShellManager(Properties settings) throws BootException {
       // use factory method for creating mgr singleton
       _shellManager = ShellManager.createShellManager(settings);
       if (_shellManager == null) {
           _log.fatal("creation of shell manager failed");
           System.exit(1);
       }
   }

   /**
    * Method to prepare the PortListener.<br>
    *
    * Creates and prepares and runs a PortListener, with settings from the
    * passed in Properties. Yet the Listener will not accept any incoming
    * connections before startServing() has been called. this has the advantage
    * that whenever a TelnetD Singleton has been factorized, it WILL 99% not
    * fail any longer (e.g. serve its purpose).
    *
    * @param   name            Name of listner.
    * @param     settings        Properties object that holds main settings.
    * @throws     BootException    Preparation failed.
    */
   private void prepareListener(final String name, final Properties settings)
           throws BootException {

       int port = 0;
       try {
           port = Integer.parseInt(settings.getProperty(name + ".port"));
           ServerSocket socket = new ServerSocket(port);
           socket.close();
       } catch (NumberFormatException e) {
           _log.fatal(e, e);
           throw new BootException("Failure while parsing port number for \"" + name + ".port\": "
               + e.getMessage());
       } catch (IOException e) {
           _log.fatal(e, e);
           throw new BootException("Failure while starting listener for port number " + port + ": "
               + e.getMessage());
       }
       // factorize PortListener
       final PortListener listener = PortListener.createPortListener(name, settings);
       // start the Thread derived PortListener
       try {
           _listeners.add(listener);
       } catch (Exception e) {
           _log.fatal(e, e);
           throw new BootException("Failure while starting PortListener thread: "
               + e.getMessage());
       }

   }

}
