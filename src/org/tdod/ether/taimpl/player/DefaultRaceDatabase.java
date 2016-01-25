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

package org.tdod.ether.taimpl.player;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.player.RaceData;
import org.tdod.ether.ta.player.RaceDatabase;
import org.tdod.ether.ta.player.enums.RaceEnum;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for race data.
 * @author rkinney
 */
public class DefaultRaceDatabase extends DataFileReader implements RaceDatabase {

   private static Log _log = LogFactory.getLog(DefaultRaceDatabase.class);

   private static final int STAT_SPLIT_SIZE = 10;

   private static HashMap<RaceEnum, RaceData> _raceDataMap = new HashMap<RaceEnum, RaceData>();

   /**
    * Creates a new DefaultRaceDatabase.
    */
   public DefaultRaceDatabase() {
   }

   /**
    * Gets the race data for the specified race.
    * @param race the race.
    * @return the race data.
    */
   public RaceData getRaceData(RaceEnum race) {
      return _raceDataMap.get(race);
   }

   /**
    * Initializes the data.
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.RACE_DATA_FILE);
      _log.info("Loading race data file " + filename);
      readFile(filename);
      _log.info("Read in " + _raceDataMap.size() + " races.");

   }

   /**
    * Parses a single line and places the data into the map.
    * @param line the single line of data.
    * @throws InvalidFileException if the line is invalid.
    */
   protected void parseLine(String line) throws InvalidFileException {
      RaceData raceData = new DefaultRaceData();

      String[] statSplit = line.split(" ", STAT_SPLIT_SIZE);

      raceData.setRace(RaceEnum.getRace(new Integer(statSplit[0])));
      raceData.getStatModifiers().getIntellect().setValue(new Integer(statSplit[1]));
      raceData.getStatModifiers().getKnowledge().setValue(new Integer(statSplit[2]));
      raceData.getStatModifiers().getPhysique().setValue(new Integer(statSplit[3]));
      raceData.getStatModifiers().getStamina().setValue(new Integer(statSplit[4]));
      raceData.getStatModifiers().getAgility().setValue(new Integer(statSplit[5]));
      raceData.getStatModifiers().getCharisma().setValue(new Integer(statSplit[6]));
      raceData.setVitality(new Integer(statSplit[7]));
      raceData.setMinStartingGold(new Integer(statSplit[8]));
      raceData.setMaxStartingGold(new Integer(statSplit[9]));

      _raceDataMap.put(raceData.getRace(), raceData);
   }

}
