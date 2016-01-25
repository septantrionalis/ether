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
import org.tdod.ether.ta.cosmos.RoomDescDatabase;
import org.tdod.ether.ta.cosmos.RoomDescriptions;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The default implementation for room descriptions.
 *
 * @author Ron Kinney
 */
public class DefaultRoomDescDatabase implements RoomDescDatabase {

   private static Log _log = LogFactory.getLog(DefaultRoomDescDatabase.class);

   private XStream _xstream = new XStream(new DomDriver());

   private RoomDescriptions _roomDescription = new DefaultRoomDescriptions();

   /**
    * Creates a new DefaultRoomDescDatabase.
    */
   public DefaultRoomDescDatabase() {
   }

   /**
    * Gets the room description.
    *
    * @param index the index of the room description in question.
    *
    * @return the room description.
    */
   public String getEntry(int index) {
      return _roomDescription.getMap().get(Integer.valueOf(index));
   }

   /**
    * Initializes this room descriptions.
    *
    * @throws InvalidFileException if something goes wrong reading the data file.
    */
   public void initialize() throws InvalidFileException {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;

      try {
         String dir = PropertiesManager.getInstance().getProperty(PropertiesManager.AREA_DIR);
         String filename = dir + PropertiesManager.getInstance().getProperty(PropertiesManager.ROOM_DESC_FILE);
         _log.info("Loading room descriptions from file " + filename);
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         String line;
         while ((line = bufferedReader.readLine()) != null) {
            readRoomDescriptions(dir + line);
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
    * Reads the room description from the specified file.
    *
    * @param roomDescFile the room description file.
    *
    * @throws FileNotFoundException if the file cannot be found.
    */
   private void readRoomDescriptions(String roomDescFile) throws FileNotFoundException {
      _log.info("Loading " + roomDescFile);
      RoomDescriptions roomDescriptions = new DefaultRoomDescriptions();
      File file = new File(roomDescFile);
      FileInputStream fileInputStream = new FileInputStream(file);
      roomDescriptions = (DefaultRoomDescriptions) _xstream.fromXML(fileInputStream);

      Set<Integer> roomNumbers = roomDescriptions.getMap().keySet();
      for (Integer roomNumber : roomNumbers) {
          String s = roomDescriptions.getMap().get(roomNumber);
         if (_roomDescription.getMap().put(roomNumber, s) != null) {
            _log.error("Duplicate room number found: " + roomNumber);
         }
      }
   }

}
