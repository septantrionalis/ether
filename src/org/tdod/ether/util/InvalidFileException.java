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

/**
 * An invalid file.
 * @author rkinney
 */
public class InvalidFileException extends Exception {

   private static final long serialVersionUID = 7732825140313332995L;

   /**
    * Creates an empty InvalidFileException.
    */
   public InvalidFileException() {
      super();
   }

   /**
    * Creates a new InvalidFileException with a message.
    * @param message the message.
    */
   public InvalidFileException(String message) {
      super(message);
   }

   /**
    * Creates a new InvalidFileException with a message and cause.
    * @param message the message.
    * @param cause a Throwable object.
    */
   public InvalidFileException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Creates a new InvalidFileException with the Throwable object defined.
    * @param cause a Throwable object.
    */
   public InvalidFileException(Throwable cause) {
      super(cause);
   }
}
