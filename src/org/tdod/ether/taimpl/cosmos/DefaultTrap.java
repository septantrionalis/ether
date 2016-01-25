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

import org.tdod.ether.ta.cosmos.Trap;
import org.tdod.ether.taimpl.cosmos.enums.TrapType;

/**
 * The default implementation for a trap.
 *
 * @author Ron Kinney
 */
public class DefaultTrap implements Trap {

   private int      _vnum;
   private String   _message;
   private int      _minDamage;
   private int      _maxDamage;
   private TrapType _trapType;

   /**
    * Creates a new DefaultTrap.
    */
   public DefaultTrap() {
   }

   /**
    * Gets the trap vnum.
    *
    * @return a trap.
    */
   public int getVnum() {
      return _vnum;
   }

   /**
    * Sets the trap vnum.
    *
    * @param vnum the trap vnum.
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
    * Gets the minimum damage that the trap will do.
    *
    * @return the minimum damage that the trap will do.
    */
   public int getMinDamage() {
      return _minDamage;
   }

   /**
    * Sets the minimum damage that the trap will do.
    *
    * @param minDamage the minimum damage that the trap will do.
    */
   public void setMinDamage(int minDamage) {
      _minDamage = minDamage;
   }

   /**
    * Sets the maximum damage that the trap will do.
    *
    * @return the maximum damage that the trap will do.
    */
   public int getMaxDamage() {
      return _maxDamage;
   }

   /**
    * Sets the maximum damage that the trap will do.
    *
    * @param maxDamage the maximum damage that the trap will do.
    */
   public void setMaxDamage(int maxDamage) {
      _maxDamage = maxDamage;
   }

   /**
    * Gets the trap type.
    *
    * @return the trap type.
    */
   public TrapType getTrapType() {
      return _trapType;
   }

   /**
    * Sets the trap type.
    *
    * @param trapType the trap type.
    */
   public void setTrapType(TrapType trapType) {
      _trapType = trapType;
   }
}
