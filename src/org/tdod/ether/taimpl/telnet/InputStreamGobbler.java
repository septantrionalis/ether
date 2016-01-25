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

package org.tdod.ether.taimpl.telnet;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.io.TerminalIO;
import net.wimpi.telnetd.io.terminal.BasicTerminal;
import net.wimpi.telnetd.net.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.engine.PlayerInputEventId;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.telnet.TaShell;
import org.tdod.ether.util.PlayerConnectedManager;
import org.tdod.ether.util.PlayerInputManager;

import com.meyling.telnet.shell.ShellIo;

/**
 * Passes input stream to shell.
 */
class InputStreamGobbler extends Thread {
   
   private static Log   _log = LogFactory.getLog(InputStreamGobbler.class);

   private TaShell      _taShell;
   private Connection   _connection;
   private ShellIo      _shellIo;

   private boolean      _hideInput = false; 
   
   /** ESC sequece for deleting a character. */
   private static final byte[] _deleteChar
       = new byte[] {BasicTerminal.ESC, BasicTerminal.LSB, '1', 'P'};

   /** ESC sequece for deleting a character. */
   private static final byte[] _insertChar
       = new byte[] {BasicTerminal.ESC, BasicTerminal.LSB, '1', '@'};

   /** ESC sequece for moving back a character. */
   // private static final byte[] _moveBack
   //    = new byte[] {BasicTerminal.ESC, BasicTerminal.LSB, '1', 'D'};

   /** ESC sequece for saving cursor position. */
   // private static final byte[] _saveCursor
   //    = new byte[] {BasicTerminal.ESC, BasicTerminal.LSB, 's'};

   /** ESC sequece for restoring cursor position. */
   // private static final byte[] _restoreCursor
   //    = new byte[] {BasicTerminal.ESC, BasicTerminal.LSB, 'u'};

   /**
    * Constructor.
    * 
    * @param os
    *           Work on this stream.
    */
   InputStreamGobbler(Connection connection, ShellIo shellIo, TaShell taShell) {
      _connection = connection ;
      _shellIo = shellIo ;      
      _taShell = taShell ;
   }

   public void run() {
      try {
         // list of previous entered lines
         final List<String> lines = new ArrayList<String>();
         // position within lines
         int lineNumber = -1;
         // line buffer
         final StringBuffer inputBuffer = new StringBuffer();
         // position within line buffer
         int cursor = 0;
         do {
            int c = _shellIo.read();
            if (_log.isDebugEnabled()) {
               _log.debug("STDIN> " + c + " " + (char) c);
            }
            switch (c) {
            case BasicTerminalIO.DELETE:
               /*_log.debug("STDIN> DELETE");
               if (cursor < inputBuffer.length()) {
                  inputBuffer.deleteCharAt(cursor);
                  _shellIo.write(_deleteChar);
               }
               break;*/
            case BasicTerminalIO.BACKSPACE:
               _log.debug("STDIN> BACKSPACE");
               if (cursor > 0) {
                  inputBuffer.deleteCharAt(cursor - 1);
                  cursor--;
                  _shellIo.write((char) TerminalIO.BS);
                  // shellIo.moveLeft(1);
                  _shellIo.write(_deleteChar);
               }
               break;
            case BasicTerminalIO.LEFT:
               _log.debug("STDIN> LEFT");
               if (cursor > 0) {
                  cursor--;
                  _shellIo.write((char) TerminalIO.BS);
               }
               break;
            case BasicTerminalIO.RIGHT:
               _log.debug("STDIN> RIGHT");
               if (cursor < inputBuffer.length()) {
                  _shellIo.moveRight(1);
                  // m_IO.write(inputBuffer.charAt(cursor));
                  cursor++;
               }
               break;
            case BasicTerminalIO.UP:
               _log.debug("STDIN> UP");
               if (lines.size() > 0 && lineNumber >= 0) {
                  if (inputBuffer.length() > cursor) {
                     // for (int i = cursor; i < inputBuffer.length(); i++) {
                     // shellIo.write(deleteChar);
                     // }
                     _shellIo.write(deleteChars(inputBuffer.length() - cursor));
                  }
                  for (int i = 0; i < cursor; i++) {
                     _shellIo.write((char) TerminalIO.BS);
                     _shellIo.write(_deleteChar);
                  }
                  final String line = (String) lines.get(lineNumber);
                  if (lineNumber > 0) {
                     lineNumber--;
                  }
                  _shellIo.write(line);
                  inputBuffer.setLength(0);
                  inputBuffer.append(line);
                  cursor = line.length();
               }
               break;
            case BasicTerminalIO.DOWN:
               _log.debug("STDIN> DOWN");
               if (lineNumber >= 0 && lineNumber < lines.size()) {
                  if (inputBuffer.length() > cursor) {
                     _shellIo.write(deleteChars(inputBuffer.length() - cursor));
                  }
                  for (int i = 0; i < cursor; i++) {
                     _shellIo.write((char) TerminalIO.BS);
                     _shellIo.write(_deleteChar);
                  }
                  final String line = (String) lines.get(lineNumber);
                  if (lineNumber + 1 < lines.size()) {
                     lineNumber++;
                  }
                  _shellIo.write(line);
                  inputBuffer.setLength(0);
                  inputBuffer.append(line);
                  cursor = line.length();
               }
               break;
            case BasicTerminalIO.ENTER:
               _log.debug("STDIN> ENTER");
               _shellIo.write(BasicTerminalIO.CRLF);
               cursor = 0;
               lines.add(inputBuffer.toString());               
               PlayerInputManager.postPlayerInputEvent(PlayerInputEventId.General, _connection.getId(), inputBuffer.toString()) ;
               lineNumber = lines.size() - 1;
               inputBuffer.setLength(0);
               break;
            default:
               if (c < 256 ) {
                  _log.debug("STDIN> no special char");
                  if (!_hideInput) {
                     _shellIo.write(_insertChar);
                     _shellIo.write((char) c);                     
                  }
                  inputBuffer.insert(cursor, (char) c);
                  cursor++;
               } else {
                  _log.debug("STDIN> unknown char");
               }
            }
         } while (_connection.isActive());
      } catch (SocketException e) {
         _log.warn("SocketException -- connection closed");
         PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnected, _taShell);
      } catch (IOException e) {
         _log.warn("IOException -- connection closed");
         PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnected, _taShell);
      }
   }

   public void setHideInput(boolean hideInput) {
      _hideInput = hideInput;
   }
   
   /**
    * Get ESC sequece for deleting <code>number</code> characters.
    * 
    *  @param  number  Delete this amount of characters.
    */
   private static final byte[] deleteChars(final int number) {
       final StringBuffer result = new StringBuffer();
       result.append((char) BasicTerminal.ESC);
       result.append((char) BasicTerminal.LSB);
       result.append(number);
       result.append('P');
       System.out.println(">>>" + result.toString());
       return result.toString().getBytes();
   }
   
}
