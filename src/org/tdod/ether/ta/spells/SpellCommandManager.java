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

package org.tdod.ether.ta.spells;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.taimpl.spells.enums.SpellType;
import org.tdod.ether.util.PropertiesManager;

/**
 * The spell command manager.
 * @author rkinney
 */
public final class SpellCommandManager {

   private static Log _log = LogFactory.getLog(SpellCommandManager.class);

   private static SpellCommandManager _instance;

   private static HashMap<SpellType, AbstractSpellCommand> _spellCommands = new HashMap<SpellType, AbstractSpellCommand>();

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private SpellCommandManager() {
      initialize();
   }

   /**
    * Initializes this object.
    */
   private void initialize() {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.SPELL_COMMAND_DATA_FILE);
         _log.info("Loading spell command file " + filename);
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
    * Processes a single line of data.
    * @param line the line of data.
    */
   private void processLine(String line) {
      String[] split = line.split("=");

      try {
         // Class<AbstractSpellCommand> spellCommandClass = (Class<AbstractSpellCommand>) Class.forName(split[1]);
         Class<? extends AbstractSpellCommand> spellCommandClass = Class.forName(split[1]).asSubclass(AbstractSpellCommand.class);
         _spellCommands.put(SpellType.getSpellType(new Integer(split[0])), spellCommandClass.newInstance());
      } catch (Exception ex) {
         _log.fatal(ex, ex);
         System.exit(1);
      }
   }

   /**
    * Gets a particular spell command.
    * @param spellType the spell type.
    * @return a spell command.
    */
   public AbstractSpellCommand getSpellCommand(SpellType spellType) {
      return _spellCommands.get(spellType);
   }

   /**
    * Gets this instance of the spell command manager.
    * @return this instance.
    */
   public static SpellCommandManager getInstance() {
      synchronized (SpellCommandManager.class) {
         if (_instance == null) {
            _instance = new SpellCommandManager();
         }
      }
      return _instance;

   }

}
