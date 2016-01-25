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

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.Trap;
import org.tdod.ether.ta.cosmos.TrapDatabase;
import org.tdod.ether.taimpl.cosmos.enums.TrapType;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class of a trap database.
 *
 * @author Ron Kinney
 */
public class DefaultTrapDatabase extends DataFileReader implements TrapDatabase {

   private static Log _log = LogFactory.getLog(DefaultTrapDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 3;
   private static final int STAT_SPLIT_SIZE  = 3;

   private static final int VNUM_INDEX       = 0;
   private static final int MESSAGE_INDEX    = 1;
   private static final int MIN_DAMAGE_INDEX = 0;
   private static final int MAX_DAMAGE_INDEX = 1;
   private static final int TRAP_TYPE_INDEX  = 2;

   private static HashMap<Integer, Trap> _traps = new HashMap<Integer, Trap>();

   /**
    * Creates a new DefaultTrapDatabase.
    */
   public DefaultTrapDatabase() {
   }

   /**
    * Gets a trap.
    *
    * @param number the trap number.
    *
    * @return a trap.
    */
   public Trap getTrap(int number) {
      return _traps.get(Integer.valueOf(number));
   }

   /**
    * Initializes the trap database.
    *
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.TRAPS_DATA_FILE);
      _log.info("Loading trap data file " + filename);
      readFile(filename);
      _log.info("Read in " + _traps.size() + " traps.");
   }

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Trap trap = new DefaultTrap();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain " + MAJOR_SPLIT_SIZE
               + " subsections.  Got " + majorSplit.length);
      }

      String[] statSplit = majorSplit[2].split(" ");
      if (statSplit.length != STAT_SPLIT_SIZE) {
         throw new InvalidFileException("Status section should contain " + STAT_SPLIT_SIZE
               + " numbers.  Got " + statSplit.length);
      }

      trap.setVnum(Integer.valueOf(majorSplit[VNUM_INDEX]));
      trap.setMessage(majorSplit[MESSAGE_INDEX]);

      trap.setMinDamage(Integer.valueOf(statSplit[MIN_DAMAGE_INDEX]));
      trap.setMaxDamage(Integer.valueOf(statSplit[MAX_DAMAGE_INDEX]));

      TrapType trapType = TrapType.getTrapType(Integer.valueOf(statSplit[TRAP_TYPE_INDEX]));
      if (trapType.equals(TrapType.Invalid)) {
         _log.error("Found an invalid trap type from line " + line);
      }
      trap.setTrapType(trapType);

      _traps.put(trap.getVnum(), trap);
   }

}
