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

package org.tdod.ether.taimpl.mobs;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.mobs.MobWeapon;
import org.tdod.ether.ta.mobs.MobWeaponDatabase;
import org.tdod.ether.ta.mobs.enums.MobWeaponType;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for the mob weapon database.
 * @author Ron Kinney
 */
public class DefaultMobWeaponDatabase extends DataFileReader implements MobWeaponDatabase {

   private static Log _log = LogFactory.getLog(DefaultMobWeaponDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 2;
   private static final int STAT_SPLIT_SIZE = 4;

   private static HashMap<MobWeaponType, ArrayList<MobWeapon>> _map = new HashMap<MobWeaponType, ArrayList<MobWeapon>>();

   /**
    * Creates a new DefaultMobWeaponDatabase.
    */
   public DefaultMobWeaponDatabase() {
   }

   //**********
   // Methods inherited from EquipmentDatabase.
   //**********

   /**
    * Reads in all of the weapons.
    * @throws InvalidFileException if the file could not be read imob n.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_WEAPONS_DATA_FILE);
      _log.info("Loading mob weapon file " + filename);
      readFile(filename);
      _log.info("Read in " + _map.size() + " mob weapons types.");
   }

   /**
    * Gets the list of mob weapons based on type.
    * @param mobWeaponType the mob weapon type.
    * @return a list of mob weapons
    */
   public ArrayList<MobWeapon> getWeapons(MobWeaponType mobWeaponType) {
      return _map.get(mobWeaponType);
   }

   //**********
   // Methods inherited from DataFileReader.
   //**********

   /**
    * parse a line of data.
    *
    * @param line the line to parse.
    * @throws InvalidFileException if something went wrong reading the file.
    */
   protected void parseLine(String line) throws InvalidFileException {
      MobWeapon mobWeapon = new DefaultMobWeapon();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain " + MAJOR_SPLIT_SIZE
               + " subsections.  Got " + majorSplit.length);
      }

      String[] statSplit = majorSplit[1].split(" ");
      if (statSplit.length != STAT_SPLIT_SIZE) {
         throw new InvalidFileException("Status section should contain " + STAT_SPLIT_SIZE
               + " numbers.  Got " + statSplit.length);
      }

      mobWeapon.setName(majorSplit[0]);
      mobWeapon.setMobWeaponType(MobWeaponType.getMobWeaponType(new Integer(statSplit[0]).intValue()));
      mobWeapon.setV1(new Integer(statSplit[1]));
      mobWeapon.setMinDamage(new Integer(statSplit[2]));
      mobWeapon.setMaxDamage(new Integer(statSplit[3]));

      ArrayList<MobWeapon> weaponList = _map.get(mobWeapon.getMobWeaponType());
      if (weaponList == null) {
         ArrayList<MobWeapon> newWeaponList = new ArrayList<MobWeapon>();
         newWeaponList.add(mobWeapon);
         _map.put(mobWeapon.getMobWeaponType(), newWeaponList);
      } else {
         weaponList.add(mobWeapon);
      }
   }

}
