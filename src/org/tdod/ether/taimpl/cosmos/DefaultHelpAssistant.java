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
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.CosmosConstants;
import org.tdod.ether.ta.cosmos.HelpAssisant;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * TODO this format can probably be worked on to make it more robust. The format is as follows :
 *
 * key~
 * body
 * ~
 * #$
 *
 * @author Ron Kinney
 */
public class DefaultHelpAssistant implements HelpAssisant {

   private static Log   _log = LogFactory.getLog(DefaultHelpAssistant.class);

   private static HashMap<String, String> _entries = new HashMap<String, String>();

   /**
    * Creates a new DefaultHelpAssistant.
    */
   public DefaultHelpAssistant() {
   }

   /**
    * Initializes the help data.
    *
    * @throws InvalidFileException if something goes wrong reading the data file.
    */
   public void initialize() throws InvalidFileException {
      readFile();
      _log.info("Number if help entries: " + _entries.size());
   }

   /**
    * Gets the help entry from assigned to the keyword.
    *
    * @param key the help keyword.
    *
    * @return a help entry.
    */
   public String getEntry(String key) {
      // This match needs to be CASE SENSITIVE or capital help entries will be
      // viewable by the player.
      return _entries.get(key);
   }

   /**
    * Read the file.
    *
    * @throws InvalidFileException if something goes wrong reading the data file.
    */
   private void readFile() throws InvalidFileException {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;
      try {
         String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.HELP_DATA_FILE);
         _log.info("Loading help file " + filename);
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         String key;
         do {
            key = getData(bufferedReader);
            if (key.equals(CosmosConstants.EOF)) {
               return;
            }
            String body = getData(bufferedReader);
            _entries.put(key, body);
         } while (true);
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
    * Reads one help entry.
    *
    * @param bufferedReader the buffered reader.
    * @return the help entry.
    * @throws IOException if an I/O exception of some sort has occurred.
    * @throws InvalidFileException if the file is invalid.
    */
   private String getData(BufferedReader bufferedReader) throws IOException, InvalidFileException {
      StringBuffer buffer = new StringBuffer();
      int intValue;
      while ((intValue = bufferedReader.read()) != -1) {
         char charValue = (char) intValue;
         if (charValue == '~') {
            bufferedReader.read(); // Carriage return.
            return buffer.toString();
         } else {
            buffer.append(charValue);
         }
      }
      if (buffer.toString().equals(CosmosConstants.EOF)) {
         return CosmosConstants.EOF;
      }
      throw new InvalidFileException("Invalid help file format.");
   }
}
