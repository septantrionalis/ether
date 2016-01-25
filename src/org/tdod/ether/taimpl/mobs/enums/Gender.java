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


public enum Gender {
   
   NEUTER(  0,"Neuter", "It",      "Its",     "It"),
   MALE(    1,"Male",   "Him",     "His",     "He"),
   FEMALE(  2,"Female", "Her",     "Her",     "She"),   
   INVALID(-1,"Invalid","Invalid", "Invalid", "Invalid");

   private int           _index;
   private String        _description;
   private String        _objective;
   private String        _pronoun;
   private String        _noun;
   
   Gender(int index, String description, String objective, String pronoun, String noun) {
      _index = index;
      _description = description;
      _objective = objective;
      _pronoun = pronoun;
      _noun = noun ;
   }
   
   public int getIndex() {
      return _index;
   }
   
   public String getDescription() {
      return _description;
   }

   public String getObjective() {
      return _objective;
   }
   
   public String getPronoun() {
      return _pronoun;
   }
   
   public String getNoun() {
      return _noun;
   }
   
   public static Gender getGender(int index) {
      Gender[] gender = Gender.values();
      
      return gender[index] ; 
   }

}
