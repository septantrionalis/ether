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

/**
 * An interface defining treasure.
 *
 * @author Ron Kinney
 */
public interface Treasure {

   /**
    * Gets the treasure vnum.
    *
    * @return the treasure vnum.
    */
   int getVnum();

   /**
    * Sets the treasure vnum.
    *
    * @param vnum the treasure vnum.
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
    * Gets the trap min value.
    *
    * @return the trap min value.
    */
   int getMinValue();

   /**
    * Sets the trap min value.
    *
    * @param minValue the trap min value.
    */
   void setMinValue(int minValue);

   /**
    * Gets the trap max value.
    *
    * @return the trap max value.
    */
   int getMaxValue();

   /**
    * Sets the trap max value.
    *
    * @param maxValue the trap max value.
    */
   void setMaxValue(int maxValue);

}
