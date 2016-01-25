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

package org.tdod.ether.taimpl.cosmos;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.CommandTrigger;
import org.tdod.ether.ta.cosmos.CommandTriggerDatabase;
import org.tdod.ether.ta.cosmos.CommandTriggers;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The default command trigger data store.
 * @author rkinney
 */
public class DefaultCommandTriggerDatabase implements CommandTriggerDatabase {

   private static Log _log = LogFactory.getLog(DefaultCommandTriggerDatabase.class);

   private XStream _xstream = new XStream(new DomDriver());

   private CommandTriggers _commandTriggers = new DefaultCommandTriggers();

   /**
    * Creates a new DefaultCommandTriggerDatabase.
    */
   public DefaultCommandTriggerDatabase() {
   }

   /**
    * Initializes the database.
    * @throws InvalidFileException is thrown if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.COMMAND_TRIGGER_DATA_FILE);
         _log.info("Loading " + filename);
         File file = new File(filename);
         FileInputStream fileInputStream = new FileInputStream(file);
         _commandTriggers = (CommandTriggers) _xstream.fromXML(fileInputStream);
      } catch (Exception e) {
         throw new InvalidFileException(e);
      }
   }

   /**
    * Gets the command trigger based on the command string.
    * @param command the command string.
    * @return a CommandTrigger object.
    */
   public CommandTrigger getCommandTrigger(String command) {
      Set<Integer> set = _commandTriggers.getMap().keySet();
      for (Integer key : set) {
         CommandTrigger commandTrigger = _commandTriggers.getMap().get(key);
         if (command.toLowerCase().equals(commandTrigger.getCommand().toLowerCase())) {
            return commandTrigger;
         }
      }

      return null;
   }

   /**
    * Gets the command trigger based on the index.
    * @param index the index of the command trigger.
    * @return the CommandTrigger object.
    */
   public CommandTrigger getCommandTrigger(int index) {
      return _commandTriggers.getMap().get(index);
   }
}
