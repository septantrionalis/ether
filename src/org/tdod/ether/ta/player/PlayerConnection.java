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

import org.tdod.ether.ta.engine.PlayerInputListener;
import org.tdod.ether.ta.output.GameOutput;
import org.tdod.ether.ta.telnet.TaShell;

/**
 * An interface defining the player connection API.
 *
 * @author Ron Kinney
 */
public interface PlayerConnection extends PlayerInputListener {

   /**
    * Gets the TaShell.
    * @return the TaShell.
    */
   TaShell getShell();

   /**
    * Gets the game output.
    * @return the game output.
    */
   GameOutput getOutput();

   /**
    * Gets the player.
    * @return the player.
    */
   Player getPlayer();

   /**
    * Sets the player.
    * @param player the player.
    */
   void setPlayer(Player player);

   /**
    * Cleans up the connection resources.
    */
   void cleanup();

}
