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

import net.wimpi.telnetd.net.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.meyling.telnet.shell.ShellIo;

/**
 * Passes shell output stream to terminal.
 */
class OutputStreamGobbler extends Thread {

   private static Log   _log = LogFactory.getLog(OutputStreamGobbler.class);

   private Connection   _connection;
   private StringBuffer _errorBuffer;
   private ShellIo      _shellIo;
   
   /** Work on this stream. */
   private final InputStream _is;

   /**
    * Constructor.
    *
    * @param   is      Work on this stream.
    */
   OutputStreamGobbler(final InputStream is, Connection connection, StringBuffer errorBuffer, ShellIo shellIo) {
      _connection = connection ;
      _errorBuffer = errorBuffer ;
      _shellIo = shellIo ;
      _is = new BufferedInputStream(is);
   }

   public void run() {
       try {
           final StringBuffer outputBuffer = new StringBuffer();
           int c = '\0';
           while (-1 != (c = _is.read()) && _connection.isActive()) {
               if (_log.isDebugEnabled()) {
                  _log.debug("STDOUT>" + (char) c);
               }
               outputBuffer.append((char) c);
               if (0 == _is.available()) {
                   // if there are some error messages, write
                   // them first
                   synchronized (_errorBuffer) {
                       if (_errorBuffer.length() > 0) {
                           try {
                               _errorBuffer.wait();
                           } catch (InterruptedException e) {
                               _log.fatal(e, e);
                           }
                       }
                       _shellIo.write(outputBuffer.toString());
                       _shellIo.flush();
                       outputBuffer.setLength(0);
                   }
               }
           }
       } catch (IOException e) {
           _log.warn(e, e);
       }
   }
   
}
