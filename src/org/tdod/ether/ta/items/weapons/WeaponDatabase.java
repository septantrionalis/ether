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

package org.tdod.ether.ta.items.weapons;

import java.util.ArrayList;

import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.util.InvalidFileException;

/**
 * The interface defining the weapon database API.
 * @author Ron Kinney
 */
public interface WeaponDatabase {

   /**
    * Initializes the database.
    * @throws InvalidFileException if the file is invalid.
    */
   void initialize() throws InvalidFileException;

   /**
    * Gets a weapon based on the vnum.
    * @param index the weapon vnum.
    * @return a weapon, or null if one was not found.
    */
   Weapon getWeapon(int index);

   /**
    * Gets a weapon based on the string value.  This does a partial compare
    * based on the name of the weapon.
    * @param weapon the partial name of the weapon.
    * @return a weapon, or null if one was not found.
    */
   Weapon getWeapon(String weapon);

   /**
    * Gets the default weapon.
    * @return the default weapon.
    */
   Weapon getDefaultWeapon();

   /**
    * Gets the complete list of weapons that are sold in a specific shop.
    * @param room the room that the shop belongs to.
    * @return the complete list of weapons that are sold in a specific shop.
    */
   ArrayList<Item> getWeaponsForShop(Room room);
}
