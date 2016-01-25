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

package org.tdod.ether.ta.player.enums;

/**
 * Player status.
 * @author rkinney
 */
public enum Status {

   /**
    * An invalid status.
    */
   INVALID(0, "Invalid"),

   /**
    * Healthy.
    */
   HEALTHY(1, "Healthy"),

   /**
    * Thirsty.
    */
   THIRSTY(2, "Thirsty"),

   /**
    * Hungry.
    */
   HUNGRY(3, "Hungry"),

   /**
    * Poisoned.
    */
   POISONED(4, "Poisoned"),

   /**
    * Paralysed.
    */
   PARALYSED(5, "Paralysed");

   private int           _index;
   private String        _name;

   /**
    * Creates a new Status.
    * @param index the index of the status.
    * @param name the name of the status.
    */
   Status(int index, String name) {
      _index = index;
      _name = name;
   }

   /**
    * Gets the index of the status.
    * @return the index of the status.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the name of the status.
    * @return the name of the status.
    */
   public String getName() {
      return _name;
   }

}
