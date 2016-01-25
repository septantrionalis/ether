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

package org.tdod.ether.taimpl.mobs.enums;


public enum SpecialAbilityEnum {

   NONE(0,"None"),
   SUMMON(1,"Summon Aid"),
   STEAL(2,"Steal"),
   HEAL(3,"Heal"),
   UNKNOWN(4, "Unknown"),
   ATTACK_TUMBLE(5, "Attack and Tumble"),
   ATTACK_ALL_TUMBLE(6,"Attack All and Tumble"),
   SPELLS(7,"Spells"),
   INVALID(-1,"Invalid");

   private int           _index;
   private String        _description;

   SpecialAbilityEnum(int index, String description) {
      _index = index;
      _description = description;
   }
   
   public int getIndex() {
      return _index;
   }
   
   public String getDescription() {
      return _description;
   }

   public static SpecialAbilityEnum getSpecialAbilityEnum(int index) {
      SpecialAbilityEnum[] specialAbilityEnum = SpecialAbilityEnum.values();
      
      return specialAbilityEnum[index] ; 
   }

}
