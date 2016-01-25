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

package org.tdod.ether.taimpl.items.weapons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.weapons.Weapon;
import org.tdod.ether.ta.items.weapons.WeaponDatabase;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.taimpl.items.weapons.enums.SpecialWeaponFunction;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class for the weapon database.
 * @author Ron Kinney
 */
public class DefaultWeaponDatabase extends DataFileReader implements WeaponDatabase {

   private static Log _log = LogFactory.getLog(DefaultWeaponDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 5;
   private static final int STAT_SPLIT_SIZE = 17;

   private static HashMap<Integer, Weapon> _weaponList = new HashMap<Integer, Weapon>();

   /**
    * Creates a new DefaultWeaponDatabase.
    */
   public DefaultWeaponDatabase() {
   }

   //**********
   // Methods inherited from EquipmentDatabase.
   //**********

   /**
    * Initializes the database.
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.WEAPON_DATA_FILE);
      _log.info("Loading weapon file " + filename);
      readFile(filename);
      _log.info("Read in " + _weaponList.size() + " weapons.");

   }

   /**
    * Gets a weapon based on the vnum.
    * @param index the weapon vnum.
    * @return a weapon, or null if one was not found.
    */
   public Weapon getWeapon(int index) {
      return _weaponList.get(index);
   }

   /**
    * Gets a weapon based on the string value.  This does a partial compare
    * based on the name of the weapon.
    * @param weapon the partial name of the weapon.
    * @return a weapon, or null if one was not found.
    */
   public Weapon getWeapon(String weapon) {
      Collection<Weapon> c = _weaponList.values();
      Iterator<Weapon> itr = c.iterator();
      while (itr.hasNext()) {
         Weapon w = itr.next();
         if (MyStringUtil.contains(w.getName(), weapon)) {
            return w;
         }
      }

      return null;
   }

   /**
    * Gets the default weapon.
    * @return the default weapon.
    */
   public Weapon getDefaultWeapon() {
      int index = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.DEFAULT_WEAPON));
      return _weaponList.get(index);
   }

   /**
    * Gets the complete list of weapons that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of weapons that are sold in a specific shop.
    */
   public ArrayList<Item> getWeaponsForShop(Room room) {
      ArrayList<Item> resultList = new ArrayList<Item>();
      if (!GameUtil.isTown(room)) {
         return resultList;
      }

      resultList.add(getDefaultWeapon());
      
      Set<Integer> keys = _weaponList.keySet();
      for (Integer key:keys) {
         Item item = _weaponList.get(key);
         if (item.getTown().getIndex() != 0 && 
               item.getTown().getIndex() <= room.getServiceLevel()) {
              resultList.add(item);
           }
      }

      Collections.sort(resultList);

      return resultList;
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
      Weapon weapon = new DefaultWeapon();

      String[] majorSplit = line.split(":", MAJOR_SPLIT_SIZE);
      if (majorSplit.length != MAJOR_SPLIT_SIZE) {
         throw new InvalidFileException("Major section should contain " + MAJOR_SPLIT_SIZE
               + " subsections.  Got " + majorSplit.length);
      }

      String[] statSplit = majorSplit[3].split(" ");
      if (statSplit.length != STAT_SPLIT_SIZE) {
         throw new InvalidFileException("Status section should contain " + STAT_SPLIT_SIZE
               + " numbers.  Got " + statSplit.length);
      }

      weapon.setVnum(Integer.valueOf(majorSplit[0]));
      weapon.setName(majorSplit[1]);
      weapon.setLongDescription(majorSplit[2]);
      weapon.setMagicAttackMessage(majorSplit[4]);

      weapon.setCost(new Integer(statSplit[0]));
      weapon.setWeight(new Integer(statSplit[1]));
      weapon.setRange(new Integer(statSplit[2]));
      weapon.setMinDamage(new Integer(statSplit[3]));
      weapon.setMaxDamage(new Integer(statSplit[4]));
      weapon.setType(new Integer(statSplit[5]));
      weapon.setAmmoVnum(new Integer(statSplit[6]));
      weapon.setArmorRating(new Integer(statSplit[7]));
      weapon.setSpecFunction(SpecialWeaponFunction.getSpecialWeaponFunction(new Integer(statSplit[8])));
      weapon.setLevel(new Integer(statSplit[14]));
      weapon.setTown(Town.getTown(new Integer(statSplit[15])));

      weapon.setV9(new Integer(statSplit[9]));
      weapon.setV10(new Integer(statSplit[10]));
      weapon.setV11(new Integer(statSplit[11]));
      weapon.setV12(new Integer(statSplit[12]));
      weapon.setV13(new Integer(statSplit[13]));
      weapon.setAllowableClasses(new Integer(statSplit[16]));

      _weaponList.put(weapon.getVnum(), weapon);
   }

}
