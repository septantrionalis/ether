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

package org.tdod.ether.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is a basic data file reader.  The parsing is delegated to the specific class.
 *
 * @author Ron Kinney
 */
public abstract class DataFileReader {

   private static Log _log = LogFactory.getLog(DataFileReader.class);

   /**
    * Reads in a single line from a data file.
    *
    * @param filename the file name.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   public void readFile(String filename) throws InvalidFileException {
      FileReader fileReader = null;
      BufferedReader bufferedReader = null;

      int lineNumber = 0;
      try {
         File file = new File(filename);
         fileReader = new FileReader(file);
         bufferedReader = new BufferedReader(fileReader);

         String line;
         while ((line = bufferedReader.readLine()) != null) {
            lineNumber++;
            if (line.equals("")) {
               continue;
            }
            if (line.startsWith("#")) {
               continue;
            }
            parseLine(line);
         }
      } catch (Exception e) {
         _log.error("Error in data file on line " + lineNumber);
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
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected abstract void parseLine(String line) throws InvalidFileException;

}
