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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.PlayerClassDatabase;
import org.tdod.ether.ta.player.RaceDatabase;
import org.tdod.ether.ta.player.StartingStats;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for starting stats.
 * @author rkinney
 */
public class DefaultStartingStats implements StartingStats {

   private static Log _log = LogFactory.getLog(DefaultStartingStats.class);

   private int                 _minVitality;
   private int                 _maxVitality;
   private BaseStats           _minBaseStats = new DefaultBaseStats();
   private BaseStats           _maxBaseStats = new DefaultBaseStats();
   private RaceDatabase        _raceDatabase = new DefaultRaceDatabase();
   private PlayerClassDatabase _playerClassDataBase = new DefaultPlayerClassDatabase();

   /**
    * Creates a new DefaultStartingStats.
    */
   public DefaultStartingStats() {
   }

   /**
    * Initializes the data.
    * @throws InvalidFileException is thrown if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      _raceDatabase.initialize();
      _playerClassDataBase.initialize();

      readStatRange();
   }

   /**
    * Gets the race data.
    * @return the race data.
    */
   public RaceDatabase getRaceDatabase() {
      return _raceDatabase;
   }

   /**
    * Gets the player class data.
    * @return the player class data.
    */
   public PlayerClassDatabase getPlayerClassDatabase() {
      return _playerClassDataBase;
   }

   /**
    * Gets the minimum starting vitality.
    * @return the minimum starting vitality.
    */
   public int getMinVitality() {
      return _minVitality;
   }

   /**
    * Gets the maximum starting vitality.
    * @return the maximum starting vitality.
    */
   public int getMaxVitality() {
      return _maxVitality;
   }

   /**
    * Gets the minimum starting stats.
    * @return the minimum starting stats.
    */
   public BaseStats getMinStats() {
      return _minBaseStats;
   }

   /**
    * Gets the maximum starting stats.
    * @return the maximum starting stats.
    */
   public BaseStats getMaxStats() {
      return _maxBaseStats;
   }

   /**
    * Reads in the stat ranges.
    * @throws InvalidFileException is thrown if the file is invalid.
    */
   private void readStatRange() throws InvalidFileException {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;

      int lineNumber = 0;
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.STAT_RANGE_FILE);
         _log.info("Loading stat rage data file " + filename);
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         readMin(bufferedReader.readLine());
         readMax(bufferedReader.readLine());
      } catch (Exception e) {
         _log.error("Error in class file on line " + lineNumber);
         throw new InvalidFileException(e);
      } finally {
         try {
            if (bufferedReader != null) {
               bufferedReader.close();
            }
            if (fileReader != null) {
               fileReader.close();
            }
         } catch (IOException e) {
            throw new InvalidFileException(e);
         }
      }
   }

   /**
    * Read the minimum line.
    * @param line the line of data.
    */
   private void readMin(String line) {
      if (line == null) {
         _log.error("line cannot be null!");
         return;
      }
      String[] statSplit = line.split(" ", 7);
      _minVitality = new Integer(statSplit[6]);
      assignBaseStats(_minBaseStats, statSplit);

   }

   /**
    * Read the maximum line.
    * @param line the line of data.
    */
   private void readMax(String line) {
      if (line == null) {
         _log.error("line cannot be null!");
         return;
      }
      String[] statSplit = line.split(" ", 7);
      _maxVitality = new Integer(statSplit[6]);
      assignBaseStats(_maxBaseStats, statSplit);
   }

   /**
    * Initializes the baseStats object with the array of Strings.
    * @param baseStats the BaseStats.
    * @param statSplit the array of string stats.
    */
   private void assignBaseStats(BaseStats baseStats, String[] statSplit) {
      baseStats.getIntellect().setValue(new Integer(statSplit[0]));
      baseStats.getKnowledge().setValue(new Integer(statSplit[1]));
      baseStats.getPhysique().setValue(new Integer(statSplit[2]));
      baseStats.getStamina().setValue(new Integer(statSplit[3]));
      baseStats.getAgility().setValue(new Integer(statSplit[4]));
      baseStats.getCharisma().setValue(new Integer(statSplit[5]));

   }
}
