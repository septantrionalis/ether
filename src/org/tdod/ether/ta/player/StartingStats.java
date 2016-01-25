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

package org.tdod.ether.ta.player;

import org.tdod.ether.util.InvalidFileException;

/**
 * Data for starting stats.
 * @author rkinney
 */
public interface StartingStats {

   /**
    * Initializes the data.
    * @throws InvalidFileException is thrown if the file is invalid.
    */
   void initialize() throws InvalidFileException;

   /**
    * Gets the race data.
    * @return the race data.
    */
   RaceDatabase getRaceDatabase();

   /**
    * Gets the player class data.
    * @return the player class data.
    */
   PlayerClassDatabase getPlayerClassDatabase();

   /**
    * Gets the minimum starting vitality.
    * @return the minimum starting vitality.
    */
   int getMinVitality();

   /**
    * Gets the maximum starting vitality.
    * @return the maximum starting vitality.
    */
   int getMaxVitality();

   /**
    * Gets the minimum starting stats.
    * @return the minimum starting stats.
    */
   BaseStats getMinStats();

   /**
    * Gets the maximum starting stats.
    * @return the maximum starting stats.
    */
   BaseStats getMaxStats();

}
