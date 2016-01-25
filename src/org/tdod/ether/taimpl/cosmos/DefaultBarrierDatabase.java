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
import org.tdod.ether.ta.cosmos.Barrier;
import org.tdod.ether.ta.cosmos.BarrierDatabase;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation of the barrier database.
 *
 * @author Ron Kinney
 */
public class DefaultBarrierDatabase extends DataFileReader implements BarrierDatabase {

   private static final int NUMBER_INDEX        = 0;
   private static final int LOCKED_INDEX        = 1;
   private static final int UNLOCKED_INDEX      = 2;
   private static final int ROGUE_MESSAGE_INDEX = 3;
   private static final int VALUE_INDEX         = 4;

   private static Log _log = LogFactory.getLog(DefaultBarrierDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 5;

   private static HashMap<Integer, Barrier> _barriers = new HashMap<Integer, Barrier>();

   /**
    * Creates a new DefaultBarrierDatabase.
    */
   public DefaultBarrierDatabase() {
   }

   /**
    * Gets a barrier from the given number.
    *
    * @param number The barrier number.
    *
    * @return a barrier.
    */
   public Barrier getBarrier(int number) {
      return _barriers.get(Integer.valueOf(number));
   }

   /**
    * Initializes the barrier data.
    *
    * @throws InvalidFileException if something went wrong reading the file.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.BARRIER_DATA_FILE);
      _log.info("Loading barrier data file " + filename);
      readFile(filename);
      _log.info("Read in " + _barriers.size() + " barriers.");

   }

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected final void parseLine(String line) throws InvalidFileException {
      Barrier barrier = new DefaultBarrier();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain "
               + MAJOR_SPLIT_SIZE + " subsections.  Got " + majorSplit.length);
      }

      barrier.setNumber(Integer.valueOf(majorSplit[NUMBER_INDEX]));
      barrier.setLockedMessage(majorSplit[LOCKED_INDEX]);
      barrier.setUnlockedMessage(majorSplit[UNLOCKED_INDEX]);
      barrier.setRogueMessage(majorSplit[ROGUE_MESSAGE_INDEX]);
      barrier.setValue(Integer.valueOf(majorSplit[VALUE_INDEX]));

      _barriers.put(barrier.getNumber(), barrier);
   }
}
