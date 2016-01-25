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

package org.tdod.ether.taimpl.items.equipment;

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
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.items.equipment.EquipmentDatabase;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.taimpl.cosmos.enums.Town;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentType;
import org.tdod.ether.util.DataFileReader;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.InvalidFileException;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;

/**
 * The default implementation class of an equipment database.
 * @author Ron Kinney
 */
public class DefaultEquipmentDatabase extends DataFileReader implements EquipmentDatabase {

   private static Log _log = LogFactory.getLog(DefaultEquipmentDatabase.class);

   private static final int MAJOR_SPLIT_SIZE = 5;
   private static final int STAT_SPLIT_SIZE = 17;

   private static HashMap<Integer, Equipment> _equipmentList = new HashMap<Integer, Equipment>();

   /**
    * Creates a new DefaultEquipmentDatabase.
    */
   public DefaultEquipmentDatabase() {
   }

   //**********
   // Methods inherited from EquipmentDatabase.
   //**********

   /**
    * Initializes the database.
    * @throws InvalidFileException if the file is invalid.
    */
   public void initialize() throws InvalidFileException {
      String filename = PropertiesManager.getInstance().getProperty(PropertiesManager.EQUIPMENT_DATA_FILE);
      _log.info("Loading equipment file " + filename);
      readFile(filename);
      _log.info("Read in " + _equipmentList.size() + " pieces of equipment.");
   }


   /**
    * Gets a piece of equipment based on the vnum.
    * @param equipment the equipment vnum.
    * @return an equipment, or null if one was not found.
    */
   public Equipment getEquipment(int equipment) {
      return _equipmentList.get(equipment);
   }

   /**
    * Gets a piece of equipment based on the string value.  This does a partial compare
    * based on the name of the equipment.
    * @param equipment the partial name of the equipment.
    * @return an equipment, or null if one was not found.
    */
   public Equipment getEquipment(String equipment) {
      Collection<Equipment> c = _equipmentList.values();
      Iterator<Equipment> itr = c.iterator();
      while (itr.hasNext()) {
         Equipment eq = itr.next();
         if (MyStringUtil.contains(eq.getName(), equipment)) {
            return eq;
         }
      }

      return null;
   }

   /**
    * Gets the complete list of equipment that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of equipment that are sold in a specific shop.
    */
   public ArrayList<Item> getEquipmentForShop(Room room) {
      ArrayList<Item> resultList = new ArrayList<Item>();
      if (!GameUtil.isTown(room)) {
         return resultList;
      }

      Set<Integer> keys = _equipmentList.keySet();
      for (Integer key:keys) {
         Item item = _equipmentList.get(key);
         // Determine if the item is sold at a shop and at which town.
         if (item.getTown().getIndex() != 0 && 
             item.getTown().getIndex() <= (room.getTerrain().getIndex() - 10)) {
            
            if (RoomFlags.EQUIPMENT_SHOP.isSet(room.getRoomFlags())) {
               // Items sold at the equipment shop.
               if (((Equipment)item).getEquipmentType().equals(EquipmentType.SUPPLY) ||
                     ((Equipment)item).getEquipmentType().equals(EquipmentType.SUPPLY_WEAPON) ||
                     ((Equipment)item).getEquipmentType().equals(EquipmentType.AMMO_CONTAINER) ||
                     ((Equipment)item).getEquipmentType().equals(EquipmentType.THROWN_WEAPON_CONTAINER)) {
                    resultList.add(item);               
               }
            } else if (RoomFlags.MAGIC_SHOP.isSet(room.getRoomFlags())) {
               // Items sold at the magic shop.
               if (((Equipment)item).getEquipmentType().equals(EquipmentType.MINOR_MAGIC_ITEM) ||
                     ((Equipment)item).getEquipmentType().equals(EquipmentType.MAJOR_MAGIC_ITEM) ||
                     ((Equipment)item).getEquipmentType().equals(EquipmentType.POTION)) {
                    resultList.add(item);               
               }               
            }
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
      Equipment equipment = new DefaultEquipment();

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

      equipment.setVnum(Integer.valueOf(majorSplit[0]));
      equipment.setName(majorSplit[1]);
      equipment.setLongDescription(majorSplit[2]);
      equipment.setMessage(majorSplit[4]);

      equipment.setCost(new Integer(statSplit[0]));
      equipment.setWeight(new Integer(statSplit[1]));
      equipment.setV2(new Integer(statSplit[2]));
      equipment.setMinEquipmentEffect(new Integer(statSplit[3]));
      equipment.setMaxEquipmentEffect(new Integer(statSplit[4]));
      equipment.setEquipmentType(EquipmentType.getEquipmentType(new Integer(statSplit[5])));
      equipment.setV6(new Integer(statSplit[6]));
      equipment.setCharges(new Integer(statSplit[7]));
      equipment.setEquipmentSubType(EquipmentSubType.getEquipmentSubType(new Integer(statSplit[8])));
      equipment.setRange(new Integer(statSplit[9]));
      equipment.setV10(new Integer(statSplit[10]));
      equipment.setQuestStat(new Integer(statSplit[11]));
      equipment.setV12(new Integer(statSplit[12]));
      equipment.setV13(new Integer(statSplit[13]));
      equipment.setLevel(new Integer(statSplit[14]));
      equipment.setTown(Town.getTown(new Integer(statSplit[15])));
      equipment.setAllowableClasses(new Integer(statSplit[16]));

      _equipmentList.put(equipment.getVnum(), equipment);
   }
}
