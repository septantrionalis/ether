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

import java.io.Serializable;

/**
 * The command trigger interface.
 * @author rkinney
 */
public interface CommandTrigger extends Serializable {

   /**
    * Gets the command.
    * @return the command.
    */
   String getCommand();

   /**
    * Sets the command.
    * @param command the command.
    */
   void setCommand(String command);

   /**
    * Gets the player message.
    * @return the player message.
    */
   String getPlayerMessage();

   /**
    * Sets the player message.
    * @param playerMessage the player message.
    */
   void setPlayerMessage(String playerMessage);

   /**
    * Gets the room message.
    * @return the room message.
    */
   String getRoomMessage();

   /**
    * Sets  the room message.
    * @param roomMessage the room message.
    */
   void setRoomMessage(String roomMessage);

}
