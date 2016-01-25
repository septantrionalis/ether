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

package org.tdod.ether.taimpl.player;

import java.util.ArrayList;

import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Encumbrance;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.PropertiesManager;

/**
 * Defines how much a player can carry.
 * @author Ron Kinney
 */
public class DefaultEncumbrance implements Encumbrance {

   private static final long serialVersionUID = 8953377369898454948L;

   private static final float GOLD_WEIGHT
      = Float.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.GOLD_WEIGHT));

   private Player _player;

   /**
    * Creates a new DefaultEncumbrance.
    * @param player the player.
    */
   public DefaultEncumbrance(Player player) {
      _player = player;
   }

   /**
    * Gets the current encumbrance.
    * @return the current encumbrance.
    */
   public int getCurEncumbrance() {
      ArrayList<Item> items = _player.getInventory();

      int encumbrance = 0;
      for (Item item : items) {
         encumbrance += item.getWeight();
      }

      encumbrance += _player.getWeapon().getWeight();
      encumbrance += _player.getArmor().getWeight();
      encumbrance += _player.getGold() * GOLD_WEIGHT;
      return encumbrance;
   }

   /**
    * Gets the maximum encumbrance.
    * @return the maximum encumbrance.
    */
   public int getMaxEncumbrance() {
      return _player.getStats().getPhysique().getMaxEncumbrance();
   }

}
