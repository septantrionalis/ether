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
 * An enumeration of equipment subtypes.
 * @author Ron Kinney
 */
public enum EquipmentSubType {

   /**
    * An undefined equipment subtype.
    */
   NONE(0, "None"),

   /**
    * Food.
    */
   FOOD(1, "Food"),

   /**
    * Water.
    */
   WATER(2, "Water"),

   /**
    * An item that heals.
    */
   HEAL(3, "Heal"),

   /**
    * An item that cures poison.
    */
   CURE_POISON(4, "Cure Poison"),

   /**
    * Boosts mana.
    */
   MINOR_MANA_BOOST(5, "Minor Mana Boost"),

   /**
    * Boosts physique.
    */
   PHYSIQUE_BOOST(6, "Physique Boost"),

   /**
    * Boosts agility.
    */
   AGILITY_BOOST(7, "Agiliy Boost"),

   /**
    * Turns the user invisible.
    */
   INVISIBILITY(8, "Invisibility"),

   /**
    * Recalls the player to the temple.
    */
   RECALL(9, "Recall"),

   /**
    * A larger boost to mana.
    */
   MAJOR_MANA_BOOST(10, "Major Mana Boost"),

   /**
    * This item subtype was defined in the data file, but I have
    * no idea what it does.
    */
   RESERVED(11, "Reserved"),

   /**
    * A wand.
    */
   WAND(12, "Wand"),

   /**
    * The runestaff.
    */
   RUNESTAFF(13, "Runestaff"),

   /**
    * The rod of power.
    */
   ROD_OF_POWER(14, "Rod of Power"),

   /**
    * A flame crystal.
    */
   FLAME_CRYSTAL(15, "Flame Crystal"),

   /**
    * This item provides temporary light.
    */
   LIGHT(16, "Light"),

   /**
    * This item provides eternal light.
    */
   ETERNAL_LIGHT(17, "Eternal Light"),

   /**
    * A soulstone.
    */
   SOULSTONE(18, "Soulstone"),

   /**
    * Ammo for ranged items.
    */
   AMMO(19, "Ammo"),

   /**
    * Another value defined in the data file, but who's functionality
    * is unknown.
    */
   MISC2(20, "Misc2"),

   /**
    * An invalid item subtype.
    */
   INVALID(-1, "Invalid");

   private int           _index;
   private String        _description;

   /**
    * Creates a new EquipmentSubType.
    * @param index the equipment subtype index.
    * @param description a description of the equipment subtype.
    */
   EquipmentSubType(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * The index of the equipment subtype.
    * @return the index of the equipment subtype.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * The equipment subtype description.
    * @return the equipment subtype description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Returns the EquipmentSubType enumeration based on the index.
    * @param index the index of the EquipmentSubType.
    * @return an EquipmentSubType.
    */
   public static EquipmentSubType getEquipmentSubType(int index) {
      EquipmentSubType[] equipmentSubType = EquipmentSubType.values();

      return equipmentSubType[index];
   }

}
