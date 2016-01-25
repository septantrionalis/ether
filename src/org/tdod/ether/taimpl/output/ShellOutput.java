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

package org.tdod.ether.taimpl.output;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

import net.wimpi.telnetd.io.BasicTerminalIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.output.GameOutput;

import com.meyling.telnet.shell.ShellIo;

/**
 * A shell implementation of the output.
 * @author Ron Kinney
 */
public class ShellOutput implements GameOutput {

   private static Log _log = LogFactory.getLog(ShellOutput.class);

   private ShellIo _shellIo;

   /**
    * Creates a new ShellOutput.
    * @param shellIo the shell io.
    */
   public ShellOutput(ShellIo shellIo) {
      _shellIo = shellIo;
   }

   //**********
   // Public methods
   //**********

   //**********
   // Methods overwritten from GameOutput.
   //**********

   /**
    * Prints the specified output in color with a carriage return.
    * @param s the string to output.
    */
   public void println(String s) {
      writeColor(s + "\n");
   }

   /**
    * Prints the specified output in color with no carriage return.
    * @param s the string to output.
    */
   public void print(String s) {
      writeColor(s);

   }

   /**
    * Prints the specified output without color with a carriage return.
    * @param s the string to output.
    */
   public void printlnWithoutColor(String s) {
      try {
         _shellIo.setForegroundColor(BasicTerminalIO.WHITE);
      } catch (IOException e) {
         _log.error(e, e);
      }
      write(s + "\n");
   }

   /**
    * Prints the specified output without color with no carriage return.
    * @param s the string to output.
    */
   public void printWithoutColor(String s) {
      try {
         _shellIo.setForegroundColor(BasicTerminalIO.WHITE);
      } catch (IOException e) {
         _log.error(e, e);
      }
      write(s);
   }

   //**********
   // Private methods
   //**********

   /**
    * Writes the specified string to the shell in color.
    * @param str the string to output.
    */
   private void writeColor(String str) {
      try {
         _shellIo.setForegroundColor(BasicTerminalIO.WHITE);
      } catch (IOException e) {
         _log.error(e, e);
      }

      CharSequence charSequence = "&";
      if (!str.contains(charSequence)) {
         write(str);
         return;
      }

      ArrayList<Integer> colorList = new ArrayList<Integer>();
      ArrayList<String>  subStringList = new ArrayList<String>();

      CharacterIterator it = new StringCharacterIterator(str);

      StringBuffer buffer = new StringBuffer();
      Integer color = Integer.valueOf(37);
      for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
         if (ch == '&') {
            char ch2 = it.next();
            if (ch2 == '&') {
               buffer.append("&");
            } else {
               if (buffer.length() > 0) {
                  colorList.add(color);
                  subStringList.add(buffer.toString());
               }
               color = Integer.valueOf(getColor(ch2));
               buffer = new StringBuffer();
            }
         } else {
            buffer.append(ch);
         }
      }

      if (buffer.length() > 0) {
         colorList.add(color);
         subStringList.add(buffer.toString());
      }

      for (int count = 0; count < colorList.size(); count++) {
         Integer i = colorList.get(count);
         String subStr = subStringList.get(count);

         try {
            _shellIo.setForegroundColor(i);
         } catch (IOException e) {
            _log.info("Got exception \"" + e.getClass() + "\" during output with message \"" + e.getMessage() + "\".");
         }
         write(subStr);
      }

      try {
         _shellIo.setForegroundColor(BasicTerminalIO.WHITE);
      } catch (IOException e) {
         _log.error(e, e);
      }
   }

   /**
    * Gets the color based on the character.
    * @param c the character representing the color.
    * @return a color code as an int.
    */
   private int getColor(char c) {
      int color = BasicTerminalIO.WHITE;

      switch (c) {
      case 'B':
         color = BasicTerminalIO.BLACK;
         break;
      case 'R':
         color = BasicTerminalIO.RED;
         break;
      case 'G':
         color = BasicTerminalIO.GREEN;
         break;
      case 'Y':
         color = BasicTerminalIO.YELLOW;
         break;
      case 'b':
         color = BasicTerminalIO.BLUE;
         break;
      case 'M':
         color = BasicTerminalIO.MAGENTA;
         break;
      case 'C':
         color = BasicTerminalIO.CYAN;
         break;
      case 'W':
         color = BasicTerminalIO.WHITE;
         break;
      default:
         break;
      }

      return color;
   }

   /**
    * Writes the specified string to the shell without color.
    * @param s the string to output to the shell
    */
   private void write(String s) {
      if (_shellIo == null) {
         _log.fatal("ShellIo is null!");
         return;
      }

      try {
         _shellIo.write(s);
         _shellIo.flush();
      } catch (IOException e) {
         // Don't log this as severe since this exception probably occurs when the pipe is broken.
         _log.info("Got exception \"" + e.getClass() + "\" during output with message \"" + e.getMessage() + "\".");
      }
   }
}
