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

import org.tdod.ether.ta.cosmos.Treasure;

/**
 * The default implementation class for treasure.
 *
 * @author Ron Kinney
 */
public class DefaultTreasure implements Treasure {

   private int      _vnum;
   private String   _message;
   private int      _minValue;
   private int      _maxValue;

   /**
    * Creates a new DefaultTreasure.
    */
   public DefaultTreasure() {
   }

   /**
    * Gets the treasure vnum.
    *
    * @return the treasure vnum.
    */
   public int getVnum() {
      return _vnum;
   }

   /**
    * Sets the treasure vnum.
    *
    * @param vnum the treasure vnum.
    */
   public void setVnum(int vnum) {
      _vnum = vnum;
   }

   /**
    * Gets the trap message.
    *
    * @return the trap message.
    */
   public String getMessage() {
      return _message;
   }

   /**
    * Sets the trap message.
    *
    * @param message the trap message.
    */
   public void setMessage(String message) {
      _message = message;
   }

   /**
    * Gets the trap min value.
    *
    * @return the trap min value.
    */
   public int getMinValue() {
      return _minValue;
   }

   /**
    * Sets the trap min value.
    *
    * @param minValue the trap min value.
    */
   public void setMinValue(int minValue) {
      _minValue = minValue;
   }

   /**
    * Gets the trap max value.
    *
    * @return the trap max value.
    */
   public int getMaxValue() {
      return _maxValue;
   }

   /**
    * Sets the trap max value.
    *
    * @param maxValue the trap max value.
    */
   public void setMaxValue(int maxValue) {
      _maxValue = maxValue;
   }

}
