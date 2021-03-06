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
import org.tdod.ether.ta.cosmos.Teleporter;
import org.tdod.ether.ta.cosmos.TeleporterDatabase;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation for a teleporter database.
 *
 * @author Ron Kinney
 */
public class DefaultTeleporterDatabase extends DataFileReader implements TeleporterDatabase {

   private static Log _log = LogFactory.getLog(DefaultTeleporterDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 4;

   private static final int VNUM_INDEX = 0;
   private static final int VICTIM_MESSAGE_INDEX = 1;
   private static final int TO_ROOM_MESSAGE_INDEX = 2;
   private static final int FROM_ROOM_MESSAGE_INDEX = 3;

   private static HashMap<Integer, Teleporter> _teleporters = new HashMap<Integer, Teleporter>();

   /**
    * Creates a new DefaultTeleporterDatabase.
    */
   public DefaultTeleporterDatabase() {
   }

   /**
    * Gets a teleporter.
    *
    * @param number the teleporter number.
    *
    * @return a teleporter.
    */
   public Teleporter getTeleporter(int number) {
      return _teleporters.get(Integer.valueOf(number));
   }

   /**
    * Initializes the database.
    *
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.TELEPORTER_DATA_FILE);
      _log.info("Loading teleporter data file " + filename);
      readFile(filename);
      _log.info("Read in " + _teleporters.size() + " teleporters.");
   }

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Teleporter teleporter = new DefaultTeleporter();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain " + MAJOR_SPLIT_SIZE
               + " subsections.  Got " + majorSplit.length);
      }

      teleporter.setVnum(Integer.valueOf(majorSplit[VNUM_INDEX]));
      teleporter.setVictimMessage(majorSplit[VICTIM_MESSAGE_INDEX]);
      teleporter.setToRoomMessage(majorSplit[TO_ROOM_MESSAGE_INDEX]);
      teleporter.setFromRoomMessage(majorSplit[FROM_ROOM_MESSAGE_INDEX]);

      _teleporters.put(teleporter.getVnum(), teleporter);

   }
}
