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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.Area;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.RoomDatabase;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * .
 * room number (integer)
 * short description
 * long description
 * ~
 * terrain
 * exits
 * flags
 * S
 *
 * EXITS:
 * exitDir roomNum
 *
 * FLAGS:
 * 1 = Safe
 * 2 = Dark
 * 4 = Arena
 * 8 = Tavern
 * 16 = Private
 * 32 = Docks
 * 64 = Guild Hall
 * 128 = Vault
 * 256 = Equipment Shop
 * 512 = Temple
 * 1024 = Armor Shop
 * 2048 = Weapon Shop
 * 4096 = Magic Shop
 *
 * if Flags == 256 or 4096, list of items for sale  ie, 1 23 30 21
 * if Flags == 2048, list of weapons for sale
 * if Flags == 1024, list of armo for sale
 *
 * NPCS, -1 = none
 *
 * @author Ron Kinney
 */
public class DefaultRoomDatabase implements RoomDatabase {

   private static Log _log = LogFactory.getLog(DefaultRoomDatabase.class);

   private XStream _xstream = new XStream(new DomDriver());

   private Area _area = new DefaultArea();

   /**
    * Creates a new DefaultRoomDatabase.
    */
   public DefaultRoomDatabase() {
   }

   /**
    * Gets the area.
    *
    * @return the area.
    */
   public Area getArea() {
      return _area;
   }

   /**
    * Initializes the room database.
    *
    * @throws InvalidFileException if something goes wrong reading the data file.
    */
   public void initialize() throws InvalidFileException {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;

      try {
         String dir = PropertiesManager.getInstance().getProperty(PropertiesManager.AREA_DIR);
         String filename = dir + PropertiesManager.getInstance().getProperty(PropertiesManager.AREA_DATA_FILE);
         _log.info("Loading area file " + filename);
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         String line;
         while ((line = bufferedReader.readLine()) != null) {
            readArea(dir + line);
         }
      } catch (Exception e) {
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
    * Read in one xml file and append it to the central Area object.  This allows support for
    * multiple area files.
    *
    * @param areaFile The xml file to read.
    * @throws FileNotFoundException if the file cannot be found.
    */
   private void readArea(String areaFile) throws FileNotFoundException {
      _log.info("Loading " + areaFile);
      Area area = new DefaultArea();
      File file = new File(areaFile);
      FileInputStream fileInputStream = new FileInputStream(file);
      area = (DefaultArea) _xstream.fromXML(fileInputStream);
      area.initialize();

      Set<Integer> roomVnums = area.getRoomMap().keySet();
      for (Integer roomVnum : roomVnums) {
         Room room = area.getEntry(roomVnum);
         _area.getRoomMap().put(roomVnum, room);
      }
   }

}
