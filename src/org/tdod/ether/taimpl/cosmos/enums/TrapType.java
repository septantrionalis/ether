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

package org.tdod.ether.taimpl.cosmos.enums;

/**
 * A trap type enumeration.
 * @author rkinney
 */
public enum TrapType {

   /**
    * Trap 0.
    */
   TRAP0(0),
   /**
    * Poison.
    */
   TRAP1(10),
   /**
    * Trap 2.
    */
   TRAP2(20),
   /**
    * Trap 3.
    */
   TRAP3(30),
   /**
    * Trap 4.
    */
   TRAP4(40),
   /**
    * Trap 5.
    */
   TRAP5(50),
   /**
    * Trap 6.
    */
   TRAP6(60),
   /**
    * Invalid trap.
    */
   Invalid(-1);

   private int _number;

   /**
    * Creates a new trap type enum.
    * @param number the trap number.
    */
   private TrapType(int number) {
      _number = number;
   }

   /**
    * Gets the number of the trap.
    * @return the trap number.
    */
   public int getNumber() {
      return _number;
   }

   /**
    * Gets the TrapType based on the index.
    * @param index the index of the trap.
    * @return a TrapType.
    */
   public static TrapType getTrapType(int index) {
      for (TrapType trapType : TrapType.values()) {
         if (trapType.getNumber() == index) {
            return trapType;
         }
      }

      return TrapType.Invalid;
   }

}
