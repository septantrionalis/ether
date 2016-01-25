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

package org.tdod.ether.ta.player.enums;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The list of player classes.  This enumeration is a hack.  For one, I don't know how to calculate experience
 * using a single equation.  Thus, experience is currently hardcoded.  Secondly, I could probably move this data
 * into a data file.  However, I didn't want to spend too much time creating the framework for experience if I didn't
 * really understand how it works.
 * TODO move this into a data file.
 * @author rkinney
 */
public enum PlayerClass {

   /**
    * An invalid class.
    */
   INVALID(0, 0, "Invalid", null),

   /**
    * Warrior.
    */
   WARRIOR(1, 1, "Warrior", new int[] {
         0, 1125, 3240, 8025, 17890, 36000, 66300, 113400, 182600, 280200,
         413000, 588700, 815600, 1102800, 1460100, 1898300, 2428600, 3063100, 3814700, 4696900,
         5724000, 6911200, 8274200, 9829700, 11594700
   }),

   /**
    * Sorceror.
    */
   SORCEROR(2, 2, "Sorceror", new int[] {
         0, 1180, 3800, 10290, 24160, 50000, 93500, 161400, 261500, 402700,
         595000, 849600, 1178400, 1594900, 2113200, 2748800, 3518100, 4438700, 5529300, 6809500,
         8300000, 10022900, 12001000, 14258400, 16820200
   }),

   /**
    * Acolyte.
    */
   ACOLYTE(3, 4, "Acolyte", new int[] {
         0, 1150, 3490, 9025, 20640, 42200, 78200, 134300, 216900, 333500,
         492000, 701800, 972800, 1315900, 1742800, 2266200, 2899600, 3657600, 4555300, 5609100,
         6836000, 8254100, 9882100, 11739900, 13848000
   }),

   /**
    * Rogue.
    */
   ROGUE(4, 8, "Rogue", new int[] {
         0, 1120, 3200, 7860, 17440, 35000, 64400, 109900, 177000, 271500,
         400000, 570100, 789600, 1067600, 1413500, 1837500, 2350800, 2964800, 3692200, 4546000,
         5540000, 6689000, 8008000, 9513300, 11221500
   }),

   /**
    * Hunter.
    */
   HUNTER(5, 16, "Hunter", new int[] {
         0, 1125, 3240, 8025, 17890, 36000, 66300, 113400, 182600, 280200,
         413000, 588700, 815600, 1102800, 1460100, 1898300, 2428600, 3063100, 3814700, 4696900,
         5724000, 6911200, 8274200, 9829700, 11594700
   }),

   /**
    * Druid.
    */
   DRUID(6, 32, "Druid", new int[] {
         0, 1180, 3800, 10290, 24160, 50000, 93500, 161400, 261500, 402700,
         595000, 849600, 1178400, 1594900, 2113200, 2748800, 3518100, 4438700, 5529300, 6809500,
         8300000, 10022900, 12001000, 14258400, 16820200
   }),

   /**
    * Archer.
    */
   ARCHER(7, 64, "Archer", new int[] {
         0, 1125, 3240, 8025, 17890, 36000, 66300, 113400, 182600, 280200,
         413000, 588700, 815600, 1102800, 1460100, 1898300, 2428600, 3063100, 3814700, 4696900,
         5724000, 6911200, 8274200, 9829700, 11594700
   }),

   /**
    * Necrolyte.
    */
   NECROLYTE(8, 128, "Necrolyte", new int[] {
         0, 1150, 3490, 9025, 20640, 42200, 78200, 134300, 216900, 333500,
         492000, 701800, 972800, 1315900, 1742800, 2266200, 2899600, 3657600, 4555300, 5609100,
         6836000, 8254100, 9882100, 11739900, 13848000
   }),

   /**
    * Knight.
    */
   KNIGHT(9, 1, "Knight", new int[] {
         0, 72000, 140000, 251000, 412000, 633000, 926000, 1302000, 1774000, 2353000,
         3055000, 3893000, 4884000, 6042000, 7384000, 8929000, 10694000, 12698000, 14961000, 17504000,
         20347000, 23513000, 27024000, 30904000, 35176000, 39867000, 45001000, 50605000, 56706000, 63332000,
         70511000, 78274000, 86649000, 95669000, 105364000, 115767000, 126912000, 138831000, 151560000, 165133000,
         179588000, 194960000, 211288000, 228609000, 246964000, 266390000, 286930000, 308624000, 331515000, 355644000
   }),

   /**
    * Arch Magus.
    */
   ARCH_MAGUS(10, 2, "Arch Magus", new int[] {
         0, 92000, 192000, 351000, 584000, 905000, 1330000, 1876000, 2559000, 3400000,
         5635000, 7072000, 8753000, 10701000, 12943000, 15504000, 18413000, 21698000, 25388000,
         29515000, 34110000, 39207000, 44838000, 51040000, 57848000, 65301000, 73435000, 82291300, 91908000,
         102330000, 113598000, 125755000, 138848000, 152921000, 168022000, 184199000, 201500000, 219977000, 239680000,
         260663000, 282977000, 306780000, 331821000, 358464000, 386664000, 416479000, 447970000, 481198000, 516224000
   }),

   /**
    * High Priest.
    */
   HIGH_PRIEST(11, 4, "High Priest", new int[] {
         0, 80000, 163000, 294000, 486000, 751000, 1101000, 1550000, 2113000, 2805000,
         3644000, 4645000, 5828000, 7211000, 8814000, 10659000, 12767000, 15161000, 17864000, 20900000,
         24296000, 28077000, 32270000, 36903000, 42006000, 47607000, 53738000, 60430000, 67715000, 75627000,
         84201000, 93470000, 103471000, 114241000, 125818000, 138240000, 151547000, 165779000, 180977000, 197184000,
         214443000, 232798000, 252293000, 272975000, 294889000, 318084000, 342608000, 368510000, 395840000, 424649000
   }),

   /**
    * Black Guard.
    */
   BLACK_GUARD(12, 8, "Black Guard", new int[] {
         0, 70000, 137000, 244000, 399000, 614000, 897000, 1261000, 1717000, 2278000,
         2958000, 3769000, 4727000, 5848000, 7147000, 8642000, 10350000, 12290000, 14480000, 16940000,
         19692000, 22756000, 26154000, 29908000, 34043000, 38582000, 43551000, 48974000, 54878000, 61290000,
         68238000, 75751000, 83856000, 92585000, 101967000, 112035000, 122820000, 134354000, 146673000, 159808000,
         173797000, 188673000, 204474000, 221237000, 238999000, 257799000, 277677000, 298671000, 320823000, 344174000
   }),

   /**
    * Beast Master.
    */
   BEAST_MASTER(13, 16, "Beast Master", new int[] {
         0, 72000, 140000, 251000, 412000, 633000, 926000, 1302000, 1774000, 2353000,
         3055000, 3893000, 4884000, 6042000, 7384000, 8929000, 10694000, 12698000, 14961000, 17504000,
         20347000, 23513000, 27024000, 30904000, 35176000, 39867000, 45001000, 50605000, 56706000, 63332000,
         70511000, 78274000, 86649000, 95669000, 105364000, 115767000, 126912000, 138831000, 151560000, 165133000,
         179588000, 194960000, 211288000, 228609000, 246964000, 266390000, 286930000, 308624000, 331515000, 355644000
   }),

   /**
    * Arch Druid.
    */
   ARCH_DRUID(14, 32, "Arch Druid", new int[] {
         0, 92000, 192000, 351000, 584000, 905000, 1330000, 1876000, 2559000, 3400000,
         5635000, 7072000, 8753000, 10701000, 12943000, 15504000, 18413000, 21698000, 25388000,
         29515000, 34110000, 39207000, 44838000, 51040000, 57848000, 65301000, 73435000, 82291300, 91908000,
         102330000, 113598000, 125755000, 138848000, 152921000, 168022000, 184199000, 201500000, 219977000, 239680000,
         260663000, 282977000, 306780000, 331821000, 358464000, 386664000, 416479000, 447970000, 481198000, 516224000
   }),

   /**
    * Master Archer.
    */
   MASTER_ARCHER(15, 64, "Master Archer", new int[] {
         0, 72000, 140000, 251000, 412000, 633000, 926000, 1302000, 1774000, 2353000,
         3055000, 3893000, 4884000, 6042000, 7384000, 8929000, 10694000, 12698000, 14961000, 17504000,
         20347000, 23513000, 27024000, 30904000, 35176000, 39867000, 45001000, 50605000, 56706000, 63332000,
         70511000, 78274000, 86649000, 95669000, 105364000, 115767000, 126912000, 138831000, 151560000, 165133000,
         179588000, 194960000, 211288000, 228609000, 246964000, 266390000, 286930000, 308624000, 331515000, 355644000
   }),

   /**
    * Necromancer.
    */
   NECROMANCER(16, 128, "Necromancer", new int[] {
         0, 80000, 163000, 294000, 486000, 751000, 1101000, 1550000, 2113000, 2805000,
         3644000, 4645000, 5828000, 7211000, 8814000, 10659000, 12767000, 15161000, 17864000, 20900000,
         24296000, 28077000, 32270000, 36903000, 42006000, 47607000, 53738000, 60430000, 67715000, 75627000,
         84201000, 93470000, 103471000, 114241000, 125818000, 138240000, 151547000, 165779000, 180977000, 197184000,
         214443000, 232798000, 252293000, 272975000, 294889000, 318084000, 342608000, 368510000, 395840000, 424649000
   });

   private static Log _log = LogFactory.getLog(PlayerClass.class);

   private int           _index;
   private int           _bit;
   private String        _name;
   private int[]         _expTable;

   /**
    * The constructor.
    * @param index the index of the class.
    * @param bit the bit vector representing the class.
    * @param name the name of the class, in string format.
    * @param expTable the experience table.
    */
   PlayerClass(int index, int bit, String name, int[] expTable) {
      _index = index;
      _bit = bit;
      _name = name;
      _expTable = expTable;
   }

   /**
    * Gets the index of the class.
    * @return the index of the class.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the name of the class.
    * @return the name of the class.
    */
   public String getName() {
      return _name;
   }

   /**
    * Gets the bit vector for the class.
    * @return the bit vector for the class.
    */
   public int getBit() {
      return _bit;
   }

   /**
    * Gets the experience requirement for a particular class.
    * @param level the level of the class.
    * @param isPromoted determines if the player is promoted or not.
    * @return the experience requirement.
    */
   public int getExpRequirement(int level, boolean isPromoted) {
      // TODO I have no idea what the exp table is above 25.
      if (level > 25 && !isPromoted) {
         return level * level * level * _expTable[1];
      }
      int index = level - 1;

      if (index < 0 || index > _expTable.length - 1) {
         _log.error("level index out of bounds: " + level);
         return _expTable[_expTable.length - 1];
      }

      return _expTable[index];
   }

   public boolean isMaxLevel(int level) {
	   if (level >= _expTable.length) {
		   return true;
	   }
	   
	   return false;
   }
   
   /**
    * Gets the promoted class.
    * @return the promoted class.
    */
   public PlayerClass getPromotedClass() {
      PlayerClass[] playerClasses = PlayerClass.values();
      return playerClasses[getIndex() + PlayerClass.values().length / 2];
   }

   /**
    * Gets the players class based on index.
    * @param index the index of the class.
    * @return a PlayerClass.
    */
   public static PlayerClass getPlayerClass(int index) {
      PlayerClass[] playerClass = PlayerClass.values();

      return playerClass[index];
   }

   /**
    * Determines if this class is a spell caster.
    * @return true of the class is a spell caster.
    */
   public boolean isSpellCaster() {
      if (_index == 2 || _index == 3 || _index == 6 || _index == 8 ||
          _index == 10 || _index == 11 || _index == 14 || _index == 16) {
         return true;
      }
      return false;
   }

   /**
    * Gets the mana increase per level.  This is such a fucking hack right now.  I need to
    * pull this out into a data file.
    * @return the mana increase per level for this class.
    */
   public int getManaIncreasePerLevel() {
      if (_index == 3 || _index == 11 || _index == 6 || _index == 14) {
         // Acolytes, High Priests, Druids, or High Druids
         return 1;
      } else if (isSpellCaster()) {
         return 2;
      }
      return 0;
   }
}
