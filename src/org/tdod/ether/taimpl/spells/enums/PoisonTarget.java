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

package org.tdod.ether.taimpl.spells.enums;

/**
 * This enumeration is mapped to the 9th index of the stat portion of the data file.
 * The reserved enumerations were added so that the getPoisonTarget method could be simpler.
 * 
 * @author minex
 *
 */
public enum PoisonTarget {

   NONE(0, "None"),
   RESERVED1(1,"Reserved2"),
   MOB(2, "Mob Target"),
   RESERVED2(3,"Reserved3"),   
   PLAYER(4, "PVP Target"),
   RESERVED3(5,"Reserved4"),
   ROOM_MOB(6, "Room");

   private int           _index;
   private String        _description;
   
   /**
    * Creates a new PoisonTarget
    * 
    * @param index the enumeration index.
    * @param description the description of this enum.
    */
   PoisonTarget(int index, String description) {
      _index = index;
      _description = description;
   }
   
   /**
    * Gets the index associated with this enum.
    * 
    * @return the index associated with this enum.
    */
   public int getIndex() {
      return _index;
   }
   
   /**
    * Gets the description associated with this enum.
    * 
    * @return the description associated with this enum.
    */
   public String getDescription() {
      return _description;
   }
   
   /**
    * Gets the PoisonTarget in the specified index.
    * 
    * @param index The index in question.
    * @return a PoisonTarget.
    */
   public static PoisonTarget getPoisonTarget(int index) {
      PoisonTarget[] poisonTarget = PoisonTarget.values();
      
      return poisonTarget[index] ; 
   }

}
