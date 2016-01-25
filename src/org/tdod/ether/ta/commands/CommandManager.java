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

package org.tdod.ether.ta.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.util.PropertiesManager;

/**
 * This class holds all commands that can be executed by the player.  Commands are defined in a separate
 * class outside of the source code and reflection is used to read those classes in.  All commands need to
 * extend from org.tdod.telearena.commands.Command.
 *
 * @author Ron Kinney
 */
public final class CommandManager {

   private static Log _log = LogFactory.getLog(CommandManager.class);

   private static CommandManager _instance;

   private static HashMap<String, Command> _commands = new HashMap<String, Command>();

   /**
    * Private constructor to force singleton pattern.
    */
   private CommandManager() {
      initialize();
   }

   /**
    * Initializes all of the commands listed in the properties file.
    */
   private void initialize() {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.COMMAND_DATA_FILE);
         _log.info("Loading command file " + filename);
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         String line;
         while ((line = bufferedReader.readLine()) != null) {
            processLine(line);
         }
      } catch (IOException e) {
         _log.fatal(e, e);
         System.exit(1);
      } finally {
         try {
            bufferedReader.close();
            fileReader.close();
         } catch (IOException e) {
            _log.fatal(e, e);
            System.exit(1);
         }
      }
   }

   /**
    * Process a single line in the properties file.
    *
    * @param line one line in the properties file.
    */
   private void processLine(String line) {
      String[] split = line.split("=");

      try {
         Class<? extends Command> commandClass = Class.forName(split[1]).asSubclass(Command.class);
         _commands.put(split[0], commandClass.newInstance());
      } catch (Exception ex) {
         _log.fatal(ex, ex);
         System.exit(1);
      }
   }

   /**
    * Gets a command.
    *
    * @param strCommand The command string.
    *
    * @return a command, or null if no command was found.
    */
   public Command getCommand(String strCommand) {
      return _commands.get(strCommand);
   }

   /**
    * Gets this instance.
    *
    * @return this instance.
    */
   public static CommandManager getInstance() {
      synchronized (CommandManager.class) {
         if (_instance == null) {
            _instance = new CommandManager();
         }
      }
      return _instance;

   }

}
