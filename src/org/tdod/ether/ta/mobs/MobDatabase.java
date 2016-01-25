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

package org.tdod.ether.ta.mobs;

import java.util.ArrayList;

import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.InvalidFileException;

/**
 * This is an interface defining the API for all mobs.
 * @author Ron Kinney
 */
public interface MobDatabase {

   /**
    * Initializes the internal mob database.
    *
    * @throws InvalidFileException if the file could not be read.
    */
   void initialize() throws InvalidFileException;

   /**
    * Gets the complete list of mobs.
    *
    * @return the complete list of mobs.
    */
   ArrayList<Mob> getMobList();

   /**
    * Gets a list of mobs in the specified terrain.
    *
    * @param terrain The specified terain.
    * @return a list of mobs in the specified terrain.
    */
   ArrayList<Mob> getMobByTerrain(Terrain terrain);

   /**
    * Gets a single mob by vnum.
    *
    * @param mobVnum The vnum.
    *
    * @return a single mob by vnum.
    */
   Mob getMob(int mobVnum);

}
