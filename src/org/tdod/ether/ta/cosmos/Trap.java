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

package org.tdod.ether.ta.cosmos;

import org.tdod.ether.taimpl.cosmos.enums.TrapType;

/**
 * An interface defining the API for a trap.
 *
 * @author Ron Kinney
 */
public interface Trap {

   /**
    * Gets the trap vnum.
    *
    * @return a trap.
    */
   int getVnum();

   /**
    * Sets the trap vnum.
    *
    * @param vnum the trap vnum.
    */
   void setVnum(int vnum);

   /**
    * Gets the trap message.
    *
    * @return the trap message.
    */
   String getMessage();

   /**
    * Sets the trap message.
    *
    * @param message the trap message.
    */
   void setMessage(String message);

   /**
    * Gets the minimum damage that the trap will do.
    *
    * @return the minimum damage that the trap will do.
    */
   int getMinDamage();

   /**
    * Sets the minimum damage that the trap will do.
    *
    * @param minDamage the minimum damage that the trap will do.
    */
   void setMinDamage(int minDamage);

   /**
    * Sets the maximum damage that the trap will do.
    *
    * @return the maximum damage that the trap will do.
    */
   int getMaxDamage();

   /**
    * Sets the maximum damage that the trap will do.
    *
    * @param maxDamage the maximum damage that the trap will do.
    */
   void setMaxDamage(int maxDamage);

   /**
    * Gets the trap type.
    *
    * @return the trap type.
    */
   TrapType getTrapType();

   /**
    * Sets the trap type.
    *
    * @param trapType the trap type.
    */
   void setTrapType(TrapType trapType);

}
