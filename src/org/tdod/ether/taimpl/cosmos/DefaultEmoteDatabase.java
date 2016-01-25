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
import org.tdod.ether.ta.cosmos.Emote;
import org.tdod.ether.ta.cosmos.EmoteDatabase;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for the emote database.
 *
 * @author Ron Kinney
 */
public class DefaultEmoteDatabase extends DataFileReader implements EmoteDatabase {

   private static Log _log = LogFactory.getLog(DefaultEmoteDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 4;

   private static final int KEYWORD_INDEX = 0;
   private static final int TO_PLAYER_INDEX = 1;
   private static final int TO_TARGET_INDEX = 2;
   private static final int TO_ROOM_INDEX = 3;

   private static HashMap<String, Emote> _emoteMap = new HashMap<String, Emote>();

   /**
    * Creates a new DefaultEmoteDatabase.
    */
   public DefaultEmoteDatabase() {
   }

   /**
    * Gets the emote by the keyword.
    *
    * @param emote the keyword.
    *
    * @return an emote.
    */
   public Emote getEmote(String emote) {
      return _emoteMap.get(emote);
   }

   /**
    * Initializes the data.
    *
    * @throws InvalidFileException if something goes wrong in reading the file.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.EMOTE_DATA_FILE);
      _log.info("Loading emote data file " + filename);
      readFile(filename);
      _log.info("Read in " + _emoteMap.size() + " emotes.");
   }

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Emote emote = new DefaultEmote();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain "
               + MAJOR_SPLIT_SIZE + " subsections.  Got " + majorSplit.length);
      }

      if (!majorSplit[KEYWORD_INDEX].equals("")) {
         emote.setKeyword(majorSplit[KEYWORD_INDEX]);
      }
      if (!majorSplit[TO_PLAYER_INDEX].equals("")) {
         emote.setToPlayer(majorSplit[TO_PLAYER_INDEX]);
      }
      if (!majorSplit[TO_TARGET_INDEX].equals("")) {
         emote.setToTarget(majorSplit[TO_TARGET_INDEX]);
      }
      if (!majorSplit[TO_ROOM_INDEX].equals("")) {
         emote.setToRoom(majorSplit[TO_ROOM_INDEX]);
      }

      _emoteMap.put(emote.getKeyword(), emote);
   }
}
