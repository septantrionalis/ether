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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.meyling.telnet.shell.ShellIo;

public class ErrorStreamGobbler extends Thread {
   
   private static Log   _log = LogFactory.getLog(ErrorStreamGobbler.class);

   private Connection   _connection;
   private StringBuffer _errorBuffer;
   private ShellIo      _shellIo;

   /** Work on this stream. */
   final InputStream _is;

   /**
    * Constructor.
    *
    * @param   is      Work on this stream.
    */
   ErrorStreamGobbler(final InputStream is, Connection connection, StringBuffer errorBuffer, ShellIo shellIo) {
      _connection = connection ;
      _errorBuffer = errorBuffer ;
      _shellIo = shellIo ;      
      _is = new BufferedInputStream(is);
   }

   public void run() {
      try {
         int c = '\0';
         while (-1 != (c = _is.read()) && _connection.isActive()) {
             if (_log.isDebugEnabled()) {
                _log.debug("STDERR>" + (char) c);
             }
             _errorBuffer.append((char) c);
             if (0 == _is.available()) {
                 // we try to synchronize with the OutputStreamGobbler
                 synchronized (_errorBuffer) {
                    _shellIo.setForegroundColor(BasicTerminalIO.RED);
                    _shellIo.setBold(true);
                    _shellIo.write(_errorBuffer.toString());
                    _shellIo.setBold(false);
                    _shellIo.resetAttributes();
                    _shellIo.flush();
                    _errorBuffer.setLength(0);
                    _errorBuffer.notify();
                 }
             }
         }
      } catch (SocketException e) {
         _log.warn("connection closed");
      } catch (IOException e) {
         _log.warn(e, e);
      }
   }   
}
