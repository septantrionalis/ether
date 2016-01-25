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
 * This enumeration is mapped to the 6th index of the stat portion of the data file.
 * 
 * @TODO A value of 2 is a guess.  It seems as if only two spells use this value, and their target's appear
 * to be the room.
 * 
 * @author minex
 *
 */
public enum SpellTarget {

   SPECIFIED(0, "Specified"),
   ROOM_PLAYER(1, "Room Player"),
   ROOM_MOB(2, "Room Mob"),
   ROOM_MOB2(3, "Room Mob 2"),
   SUMMON(4, "Summon"),
   ROOM_PLAYER2(5, "Room Player 2");     // Added by me for spells that I think are PVP.
   
   private int           _index;
   private String        _description;

   /**
    * Creates a new SpellTarget
    * 
    * @param index the enumeration index.
    * @param description the description of this enum.
    */
   SpellTarget(int index, String description) {
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
    * Gets the SpellTarget in the specified index.
    * 
    * @param index The index in question.
    * @return a SpellTarget.
    */
   public static SpellTarget getSpellTarget(int index) {
      SpellTarget[] spellTarget = SpellTarget.values();
      
      return spellTarget[index] ; 
   }

}
