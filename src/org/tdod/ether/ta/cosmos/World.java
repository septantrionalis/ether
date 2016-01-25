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

package org.tdod.ether.ta.cosmos;

import org.tdod.ether.ta.items.armor.ArmorDatabase;
import org.tdod.ether.ta.items.equipment.EquipmentDatabase;
import org.tdod.ether.ta.items.weapons.WeaponDatabase;
import org.tdod.ether.ta.mobs.MobDatabase;
import org.tdod.ether.ta.mobs.MobWeaponDatabase;
import org.tdod.ether.ta.spells.SpellDatabase;
import org.tdod.ether.util.InvalidFileException;

/**
 * An interface defining the API for a World.
 *
 * @author Ron Kinney
 */
public interface World {

   /**
    * BOOOOOOOOOOOOOOOOOOOOOOOOOM!!!!
    * Initializes the world.
    *
    * @throws InvalidFileException if any of the data files are invalid.
    */
   void bigBang() throws InvalidFileException;

   /**
    * Gets the help data.
    *
    * @return the help data.
    */
   HelpAssisant getHelpAssistant();

   /**
    * Gets the area.
    *
    * @return the area.
    */
   Area getArea();

   /**
    * Gets the room description database.
    *
    * @return the room description database.
    */
   RoomDescDatabase getRoomDescDatabase();

   /**
    * Gets the weapon database.
    *
    * @return the weapon database.
    */
   WeaponDatabase getWeaponDatabase();

   /**
    * Gets the armor database.
    *
    * @return the armor database.
    */
   ArmorDatabase getArmorDatabase();

   /**
    * Gets the mob database.
    *
    * @return the mob database.
    */
   MobDatabase getMobDatabase();

   /**
    * Gets the spell database.
    *
    * @return the spell database.
    */
   SpellDatabase getSpellDatabase();

   /**
    * Gets the equipment database.
    *
    * @return the equipment database.
    */
   EquipmentDatabase getEquipmentDatabase();

   /**
    * Gets the mob weapon database.
    *
    * @return the mob weapon database.
    */
   MobWeaponDatabase getMobWeaponDatabase();

   /**
    * Gets the npc database.
    *
    * @return the npc database.
    */
   NpcDatabase getNpcDatabase();

   /**
    * Gets the emote database.
    *
    * @return the emote database.
    */
   EmoteDatabase getEmoteDatabase();

   /**
    * Gets the barrier database.
    *
    * @return the barrier database.
    */
   BarrierDatabase getBarrierDatabase();

   /**
    * Gets the trap database.
    *
    * @return the trap database.
    */
   TrapDatabase getTrapDatabase();

   /**
    * Gets the teleporter database.
    *
    * @return the teleporter database.
    */
   TeleporterDatabase getTeleporterDatabase();

   /**
    * Gets the treasure database.
    *
    * @return the treasure database.
    */
   TreasureDatabase getTreasureDatabase();

   /**
    * Gets the command trigger database.
    *
    * @return the command trigger database.
    */
   CommandTriggerDatabase getCommandTriggerDatabase();

}
