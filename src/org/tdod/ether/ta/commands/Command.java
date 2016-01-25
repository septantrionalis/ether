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

package org.tdod.ether.ta.commands;

import org.tdod.ether.ta.Entity;

/**
 * An abstract command class.  All player input commands need to extend this class.
 *
 * @author Ron Kinney
 */
public abstract class Command {

   /**
    * The input requires two parameters.
    */
   protected static final int TWO_PARAMS = 2;

   /**
    * The input requires three parameters.
    */
   protected static final int THREE_PARAMS = 3;

   /**
    * The input requires four parameters.
    */
   protected static final int FOUR_PARAMS = 4;

   /**
    * Executes the command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return True if the command can be executed, false otherwise.
    */
   public abstract boolean execute(Entity entity, String input);

   /**
    * Default every command to false.  Override if the command can be used while paralyzed.
    *
    * @return true if this command can be used paralyzed, false otherwise.
    */
   public boolean canUseParalyzed() {
      return false;
   }

}
