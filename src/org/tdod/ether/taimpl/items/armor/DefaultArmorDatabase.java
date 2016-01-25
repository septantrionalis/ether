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

package org.tdod.ether.taimpl.items.armor;

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
import org.tdod.ether.ta.items.armor.Armor;
import org.tdod.ether.ta.items.armor.ArmorDatabase;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class of an armor database.
 * @author Ron Kinney
 */
public class DefaultArmorDatabase extends DataFileReader implements ArmorDatabase {

   private static Log _log = LogFactory.getLog(DefaultArmorDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 4;
   private static final int STAT_SPLIT_SIZE = 17;

   private static HashMap<Integer, Armor> _armorList = new HashMap<Integer, Armor>();

   /**
    * Creates a new DefaultArmorDatabase.
    */
   public DefaultArmorDatabase() {
   }

   //**********
   // Methods inherited from ArmorDatabase.
   //**********

   /**
    * Initializes the database.
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.ARMOR_DATA_FILE);
      _log.info("Loading armor file " + filename);
      readFile(filename);
      _log.info("Read in " + _armorList.size() + " pieces of armor.");
   }

   /**
    * Gets a piece of armor based on the vnum.
    * @param armor the armor vnum.
    * @return a piece of armor.
    */
   public Armor getArmor(int armor) {
      return _armorList.get(armor);
   }

   /**
    * Gets a piece of armor based on the string value.  This does a partial compare
    * based on the name of the armor.
    * @param armor the partial name of the armor.
    * @return a piece of armor.
    */
   public Armor getArmor(String armor) {
      Collection<Armor> c = _armorList.values();
      Iterator<Armor> itr = c.iterator();
      while (itr.hasNext()) {
         Armor ar = itr.next();
         if (MyStringUtil.contains(ar.getName(), armor)) {
            return ar;
         }
      }

      return null;
   }

   /**
    * Gets the default armor.
    * @return the default armor.
    */
   public Armor getDefaultArmor() {
      int index = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.DEFAULT_ARMOR));
      return _armorList.get(index);
   }

   /**
    * Gets the complete list of weapons that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of weapons that are sold in a specific shop.
    */
   public ArrayList<Item> getArmorForShop(Room room) {
      ArrayList<Item> resultList = new ArrayList<Item>();
      if (!GameUtil.isTown(room)) {
         return resultList;
      }

      resultList.add(getDefaultArmor());
      
      Set<Integer> keys = _armorList.keySet();
      for (Integer key:keys) {
         Item item = _armorList.get(key);
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
      Armor armor = new DefaultArmor();

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

      armor.setVnum(Integer.valueOf(majorSplit[0]));
      armor.setName(majorSplit[1]);
      armor.setLongDescription(majorSplit[2]);

      armor.setCost(new Integer(statSplit[0]));
      armor.setWeight(new Integer(statSplit[1]));
      armor.setType(new Integer(statSplit[5]));
      armor.setArmorRating(new Integer(statSplit[6]));
      armor.setLevel(new Integer(statSplit[14]));
      armor.setTown(Town.getTown(new Integer(statSplit[15])));

      armor.setV2(new Integer(statSplit[2]));
      armor.setV3(new Integer(statSplit[3]));
      armor.setV4(new Integer(statSplit[4]));
      armor.setV7(new Integer(statSplit[7]));
      armor.setV8(new Integer(statSplit[8]));
      armor.setV9(new Integer(statSplit[9]));
      armor.setV10(new Integer(statSplit[10]));
      armor.setV11(new Integer(statSplit[11]));
      armor.setV12(new Integer(statSplit[12]));
      armor.setV13(new Integer(statSplit[13]));
      armor.setAllowableClasses(new Integer(statSplit[16]));

      _armorList.put(armor.getVnum(), armor);
   }


}
