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
import org.tdod.ether.ta.cosmos.Treasure;
import org.tdod.ether.ta.cosmos.TreasureDatabase;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for a treasure database.
 *
 * @author Ron Kinney
 */
public class DefaultTreasureDatabase extends DataFileReader implements TreasureDatabase {

   private static Log _log = LogFactory.getLog(DefaultTreasureDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 3;
   private static final int STAT_SPLIT_SIZE = 2;

   private static final int VNUM_INDEX = 0;
   private static final int MESSAGE_INDEX = 1;
   private static final int MIN_INDEX = 0;
   private static final int MAX_INDEX = 1;

   private static HashMap<Integer, Treasure> _treasures = new HashMap<Integer, Treasure>();

   /**
    * Gets a treasure.
    *
    * @param number the treasure number.,
    *
    * @return a treasure, or null if none was found.
    */
   public Treasure getTreasure(int number) {
      return _treasures.get(Integer.valueOf(number));
   }

   /**
    * Initializes the treasure database.
    *
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.TREASURES_DATA_FILE);
      _log.info("Loading treasures data file " + filename);
      readFile(filename);
      _log.info("Read in " + _treasures.size() + " treasures.");
   }

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Treasure treasure = new DefaultTreasure();

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

      treasure.setVnum(Integer.valueOf(majorSplit[VNUM_INDEX]));
      treasure.setMessage(majorSplit[MESSAGE_INDEX]);
      treasure.setMinValue(Integer.valueOf(statSplit[MIN_INDEX]));
      treasure.setMaxValue(Integer.valueOf(statSplit[MAX_INDEX]));

      _treasures.put(treasure.getVnum(), treasure);
   }
}
