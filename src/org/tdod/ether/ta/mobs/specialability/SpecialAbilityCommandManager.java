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

package org.tdod.ether.ta.mobs.specialability;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.taimpl.mobs.enums.SpecialAbilityEnum;
import org.tdod.ether.util.PropertiesManager;

/**
 * The special ability command manager.
 * @author Ron Kinney
 */
public final class SpecialAbilityCommandManager {

   private static Log _log = LogFactory.getLog(SpecialAbilityCommandManager.class);

   private static SpecialAbilityCommandManager _instance;

   private static HashMap<SpecialAbilityEnum, SpecialAbilityCommand> _specialAbilityCommands
      = new HashMap<SpecialAbilityEnum, SpecialAbilityCommand>();

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private SpecialAbilityCommandManager() {
      initialize();
   }

   /**
    * Gets a SpecialAbilityCommand based on the SpecialAbilityEnum.
    * @param specialAbilityEnum the SpecialAbilityEnum.
    * @return a SpecialAbilityCommand.
    */
   public SpecialAbilityCommand getSpecialAbilityCommand(SpecialAbilityEnum specialAbilityEnum) {
      return _specialAbilityCommands.get(specialAbilityEnum);
   }

   /**
    * Gets this instance.
    * @return this instance.
    */
   public static SpecialAbilityCommandManager getInstance() {
      synchronized (SpecialAbilityCommandManager.class) {
         if (_instance == null) {
            _instance = new SpecialAbilityCommandManager();
         }
      }
      return _instance;

   }

   /**
    * Initializes this manager.
    */
   private void initialize() {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_SPEC_ABILITY_DATA_FILE);
         _log.info("Loading mob special ability command file " + filename);
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
    * Processes a single line in the file.
    * @param line the line to process.
    */
   private void processLine(String line) {
      String[] split = line.split("=");

      try {
         // Class<SpecialAbilityCommand> specialAbilityCommandClass = (Class<SpecialAbilityCommand>) Class.forName(split[1]);
         Class<? extends SpecialAbilityCommand> specialAbilityCommandClass = Class.forName(split[1]).asSubclass(SpecialAbilityCommand.class);
         
         _specialAbilityCommands.put(SpecialAbilityEnum.getSpecialAbilityEnum(
               new Integer(split[0])), specialAbilityCommandClass.newInstance());
      } catch (Exception ex) {
         _log.fatal(ex, ex);
         System.exit(1);
      }
   }

}
