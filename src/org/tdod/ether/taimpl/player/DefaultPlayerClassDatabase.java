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
import org.tdod.ether.ta.player.PlayerClassData;
import org.tdod.ether.ta.player.PlayerClassDatabase;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The implementation class for the player class database.
 * @author rkinney
 */
public class DefaultPlayerClassDatabase extends DataFileReader implements PlayerClassDatabase {

   private static Log _log = LogFactory.getLog(DefaultPlayerClassDatabase.class);

   private static final int STAT_SPLIT_SIZE = 14;

   private static HashMap<PlayerClass, PlayerClassData> _playerClassDataMap = new HashMap<PlayerClass, PlayerClassData>();

   /**
    * Creates a new DefaultPlayerClassDatabase.
    */
   public DefaultPlayerClassDatabase() {

   }

   /**
    * Gets the player class data for a specified class.
    * @param playerClass the player class.
    * @return the player class data.
    */
   public PlayerClassData getPlayerClassData(PlayerClass playerClass) {
      return _playerClassDataMap.get(playerClass);
   }

   /**
    * Initializes the data.
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.CLASS_DATA_FILE);
      _log.info("Loading class data file " + filename);
      readFile(filename);
      _log.info("Read in " + _playerClassDataMap.size() + " classes.");
   }

   /**
    * Parses a single data line and adds the information to the map.
    * @param line the line to parse.
    * @throws InvalidFileException if the line is invalid.
    */
   protected void parseLine(String line) throws InvalidFileException {
      PlayerClassData classData = new DefaultPlayerClassData();

      String[] statSplit = line.split(" ", STAT_SPLIT_SIZE);

      classData.setPlayerClass(PlayerClass.getPlayerClass(new Integer(statSplit[0])));
      classData.getStatModifiers().getIntellect().setValue(new Integer(statSplit[1]));
      classData.getStatModifiers().getKnowledge().setValue(new Integer(statSplit[2]));
      classData.getStatModifiers().getPhysique().setValue(new Integer(statSplit[3]));
      classData.getStatModifiers().getStamina().setValue(new Integer(statSplit[4]));
      classData.getStatModifiers().getAgility().setValue(new Integer(statSplit[5]));
      classData.getStatModifiers().getCharisma().setValue(new Integer(statSplit[6]));
      classData.setVitality(new Integer(statSplit[7]));
      classData.setWeapon(new Integer(statSplit[8]));
      classData.setArmor(new Integer(statSplit[9]));
      classData.setAttacksPerLevel(new Integer(statSplit[10]));
      classData.setMaxBaseAttacks(new Integer(statSplit[11]));
      classData.setMinStartingGold(new Integer(statSplit[12]));
      classData.setMaxStartingGold(new Integer(statSplit[13]));

      _playerClassDataMap.put(classData.getPlayerClass(), classData);
   }
}
