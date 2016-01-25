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

import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.spells.enums.SpellType;

public enum MobSpellType {

   NONE(0,"None", PlayerClass.SORCEROR, SpellType.SORCEROR_ATTACK),
   SORCEROR(1,"Sorceror", PlayerClass.SORCEROR, SpellType.SORCEROR_ATTACK),
   NONE2(2,"Invalid", PlayerClass.SORCEROR, SpellType.SORCEROR_ATTACK),
   DRUID(3,"Druid", PlayerClass.DRUID, SpellType.DRUID_ATTACK),
   NECROLYTE(4,"Necrolyte", PlayerClass.NECROLYTE, SpellType.NECROLYTE_ATTACK),
   INVALID(-1,"Invalid", PlayerClass.SORCEROR, SpellType.SORCEROR_ATTACK);
   
   private int    _index;
   private String _description;
   private PlayerClass _playerClass;
   private SpellType _spellType;

   MobSpellType(int index, String description, PlayerClass playerClass, SpellType spellType) {
      _index = index;
      _description = description;
      _playerClass = playerClass;
      _spellType = spellType;
   }
   
   public int getIndex() {
      return _index;
   }
   
   public String getDescription() {
      return _description;
   }

   public SpellType getSpellType() {
      return _spellType;
   }
   
   public PlayerClass getPlayerClass() {
      return _playerClass;
   }
   
   public static MobSpellType getMobSpellType(int index) {
      MobSpellType[] mobSpellType = MobSpellType.values();
      
      return mobSpellType[index] ; 
   }

}
