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

package org.tdod.ether.taimpl.items.equipment.enums;

/**
 * Types of equipment.
 * @author rkinney
 */
public enum EquipmentType {

   /**
    * Not an equipment.
    */
   NONE(0, "None"),

   /**
    * Miscellaneous.
    */
   MISC(14, "Misc"),

   /**
    * Supply equipment.
    */
   SUPPLY(21, "Supply"),

   /**
    * A weapon type sold by a supply store.
    */
   SUPPLY_WEAPON(22, "Supply Weapon"),

   /**
    * Non tele-arena enum.  Modified to get throw and fire working.
    * A weapon type that is thrown and used up.
    */
   THROWN_WEAPON_CONTAINER(23, "Thrown Weapon Container"),

   /**
    * Non tele-arena enum.  Modified to get throw and fire working.
    * Ammunition for ranged weapons.
    */
   RANGED_WEAPON_AMMO(24, "Ranged Weapon Ammo"),

   /**
    * Non tele-arena enum.  Modified to get throw and fire working.
    * Ammunition container.
    */
   AMMO_CONTAINER(25, "Ammo Container"),

   /**
    * A minor magic item.
    */
   MINOR_MAGIC_ITEM(31, "Minor Magic Item"),

   /**
    * A potion.
    */
   POTION(32, "Potion"),

   /**
    * A major magic item.
    */
   MAJOR_MAGIC_ITEM(33, "Major Magic Item"),

   /**
    * A key.
    */
   KEY(41, "Key"),

   /**
    * An invalid equipment type.
    */
   INVALID(-1, "Invalid");

   private int           _index;
   private String        _description;

   /**
    * Creates a new EquipmentType.
    * @param index the index of the EquipmentType.
    * @param description the description of the EquipmentType.
    */
   EquipmentType(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the index of the equipment type.
    * @return the index of the equipment type.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the equipment type description.
    * @return the equipment type description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets an equipment type based on the index.
    * @param index the index of the equipment type.
    * @return an EquipmentType.
    */
   public static EquipmentType getEquipmentType(int index) {
      for (EquipmentType equipmentType : EquipmentType.values()) {
         if (equipmentType.getIndex() == index) {
            return equipmentType;
         }
      }

      return EquipmentType.INVALID;
   }

}
