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
 * This class defines various spell types.  Each enumeration will be mapped to different classes that handle
 * the spell code.  This section is mapped to index 0 in the stats portion of the file.
 * 
 * @author minex
 */
public enum SpellType {

   NONE(0, "None"),
   SORCEROR_ATTACK(1, "Sorceror Attack"),
   ACOLYTE_ATTACK(2, "Acolyte Attack"),
   DRUID_ATTACK(3, "Druid Attack"),
   NECROLYTE_ATTACK(4, "Necrolyte Attack"),
   REMOVE_CONDITION(11, "Remove Condition"),
   SUSTENANCE(12, "Sustenance"),
   HEALING(21, "Healing"),
   REGENERATION(22, "Regeneration"),
   SUMMONING(31, "Summoning"),
   INVISIBILITY(32, "Invisibility"),
   STAT_MOD(33, "Stat Mod"),
   CHARM(41, "Charm");
   
   private int           _index;
   private String        _description;
   
   /**
    * Creates a new SpellType
    * 
    * @param index the enumeration index.
    * @param description the description of this enum.
    */
   SpellType(int index, String description) {
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
    * Gets the SpellType in the specified index.
    * 
    * @param index The index in question.
    * @return a SpellType.
    */
   public static SpellType getSpellType(int index) {      
      for (SpellType spellType:SpellType.values()) {
         if (spellType.getIndex() == index) {
            return spellType;
         }
      }
      
      return SpellType.NONE;
   }

}
