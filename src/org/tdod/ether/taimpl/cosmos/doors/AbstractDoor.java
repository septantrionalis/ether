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

package org.tdod.ether.taimpl.cosmos.doors;

import org.tdod.ether.ta.cosmos.Door;

/**
 * Abstract class for shared attributes of a door.
 *
 * @author Ron Kinney
 */
public abstract class AbstractDoor implements Door {

   private int    _v0;
   private int    _v3;
   private int    _v4;
   private int    _v5;
   private int    _v6;

   private transient boolean _isUnlocked;

   /**
    * Gets the value at index 0.
    *
    * @return the value at index 0.
    */
   public int getV0() {
      return _v0;
   }

   /**
    * Sets the value for index 0.
    *
    * @param v0 the value for index 0.
    */
   public void setV0(int v0) {
      _v0 = v0;
   }

   /**
    * Gets the value at index 3.
    *
    * @return the value at index 3.
    */
   public int getV3() {
      return _v3;
   }

   /**
    * Sets the value for index 3.
    *
    * @param v3 the value for index 3.
    */
   public void setV3(int v3) {
      _v3 = v3;
   }

   /**
    * Gets the value at index 4.
    *
    * @return the value at index 4.
    */
   public int getV4() {
      return _v4;
   }

   /**
    * Sets the value for index 4.
    *
    * @param v4 the value for index 4.
    */
   public void setV4(int v4) {
      _v4 = v4;
   }

   /**
    * Gets the value at index 5.
    *
    * @return the value at index 5.
    */
   public int getV5() {
      return _v5;
   }

   /**
    * Sets the value for index 5.
    *
    * @param v5 the value for index 5.
    */
   public void setV5(int v5) {
      _v5 = v5;
   }

   /**
    * Gets the value at index 6.
    *
    * @return the value at index 6.
    */
   public int getV6() {
      return _v6;
   }

   /**
    * Sets the value for index 6.
    *
    * @param v6 the value for index 6;
    */
   public void setV6(int v6) {
      _v6 = v6;
   }

   /**
    * Checks if this door is unlocked.
    *
    * @return true if this door is unlocked.
    */
   public boolean isUnlocked() {
      return _isUnlocked;
   }

   /**
    * Sets the locked state of this door.
    *
    * @param isUnlocked The locked state of this door.
    */
   public void setIsUnlocked(boolean isUnlocked) {
      _isUnlocked = isUnlocked;
   }

   /**
    * Gets the "consumes key" value.
    *
    * @return the "consumes key" value.
    */
   public int consumesKey() {
      return _v4;
   }

   /**
    * Gets the barrier number.
    *
    * @return the barrier number.
    */
   public int getBarrier() {
      return _v3;
   }

}
