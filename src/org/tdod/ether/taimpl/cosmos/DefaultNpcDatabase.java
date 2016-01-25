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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.NpcDatabase;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.taimpl.mobs.DefaultMob;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * A default implementation class of the npc database.
 *
 * @author Ron Kinney
 *
 */
public class DefaultNpcDatabase extends DataFileReader implements NpcDatabase {

   private static Log _log = LogFactory.getLog(DefaultNpcDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 4;

   private static final int VNUM_INDEX        = 0;
   private static final int NAME_INDEX        = 1;
   private static final int PLURAL_INDEX      = 2;
   private static final int DESCRIPTION_INDEX = 3;

   private static ArrayList<Mob> _npcList = new ArrayList<Mob>();

   /**
    * Creates a new DefaultNpcDatabase.
    */
   public DefaultNpcDatabase() {
   }

   /**
    * Gets the npc.
    *
    * @param index the npc vnum.
    *
    * @return an npc, or null if nothing was found.
    */
   public Mob getNpc(int index) {
      return _npcList.get(index);
   }

   /**
    * Initializes the npc database.
    *
    * @throws InvalidFileException if something goes wrong reading the data file.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.NPC_DATA_FILE);
      _log.info("Loading npc data file " + filename);
      readFile(filename);
      _log.info("Read in " + _npcList.size() + " npcs.");
   }

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      Mob npc = new DefaultMob();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain "
               + MAJOR_SPLIT_SIZE + " subsections.  Got " + majorSplit.length);
      }

      npc.setVnum(Integer.valueOf(majorSplit[VNUM_INDEX]));
      npc.setName(majorSplit[NAME_INDEX]);
      npc.setPlural(majorSplit[PLURAL_INDEX]);
      npc.setDescription(majorSplit[DESCRIPTION_INDEX]);
      npc.setNpc(true);

      _npcList.add(npc);
   }
}
