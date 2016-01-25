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

package org.tdod.ether.taimpl.cosmos;

import org.tdod.ether.ta.cosmos.CommandTrigger;

/**
 * This is the default implementation of a command trigger.
 * @author rkinney
 */
public class DefaultCommandTrigger implements CommandTrigger {

   private static final long serialVersionUID = 4017589289471464041L;

   private String _command;
   private String _playerMessage;
   private String _roomMessage;

   /**
    * Creates a new DefaultCommandTrigger.
    */
   public DefaultCommandTrigger() {
   }

   /**
    * Gets the command keyword.
    * @return the command keyword.
    */
   public String getCommand() {
      return _command;
   }

   /**
    * Sets the command keyword.
    * @param command the command keyword.
    */
   public void setCommand(String command) {
      _command = command;
   }

   /**
    * Gets the player message.
    * @return the player message.
    */
   public String getPlayerMessage() {
      return _playerMessage;
   }

   /**
    * Sets the player message.
    * @param playerMessage the player message.
    */
   public void setPlayerMessage(String playerMessage) {
      _playerMessage = playerMessage;
   }

    /**
     * Gets the room message.
     * @return the room message.
     */
   public String getRoomMessage() {
      return _roomMessage;
   }

   /**
    * Sets the room message.
    * @param roomMessage the room message.
    */
   public void setRoomMessage(String roomMessage) {
      _roomMessage = roomMessage;
   }

}
