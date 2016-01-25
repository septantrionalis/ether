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

import java.io.Serializable;

/**
 * An interface defining a players attacks.
 * @author Ron Kinney
 */
public interface Attacks extends Serializable {

   /**
    * Performs an attack.  This method does not check the number of attacks
    * left.  It just reduces the number of attacks by one.
    */
   void attack();

   /**
    * Resets the players attack.
    */
   void reset();

   /**
    * Gets the number of attacks left.
    * @return the number of attacks left.
    */
   int getAttacksLeft();

   /**
    * Sets the number of attacks left.
    * @param attacksLeft the number of attacks left.
    */
   void setAttacksLeft(int attacksLeft);

   /**
    * Gets the maximum number of attacks the player can have.
    * @return the maximum number of attacks the player can have.
    */
   int getMaxAttacks();

}
