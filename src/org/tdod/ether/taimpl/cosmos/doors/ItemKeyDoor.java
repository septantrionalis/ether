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

package org.tdod.ether.taimpl.cosmos.doors;

import org.tdod.ether.ta.cosmos.Barrier;
import org.tdod.ether.ta.cosmos.enums.DoorType;
import org.tdod.ether.ta.cosmos.enums.MoveFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.util.PropertiesManager;

/**
 * This door requires a certain item to unlock.
 *
 * @author Ron Kinney
 */
public class ItemKeyDoor extends AbstractDoor {

   private static final int PIT_BARRIER_NUM = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PIT_BARRIER_NUM));

   /**
    * Gets the move fail code when a player attempts to pass through this door.
    *
    * @param player the player attempting to pass through this door.
    *
    * @return the move fail code.
    */
   public MoveFailCode getMoveFailCode(Player player) {
      // Determine if the player has the key.
      Item item = WorldManager.getItem(getV0());
      boolean hasKey = false;
      for (Item invItem : player.getInventory()) {
         if (invItem.isSimilar(item)) {
            hasKey = true;
            break;
         }
      }
      if (!hasKey) {
         // Determine if the player picks the lock.
         if (player.getPlayerClass().equals(PlayerClass.ROGUE)) {
            if (WorldManager.getGameMechanics().pickedLock(player, getV4())) {
               return MoveFailCode.PICKED_BARRIER;
            } else {
               return MoveFailCode.BARRIER;
            }
         } else {
            return MoveFailCode.BARRIER;
         }

      } else {
         Barrier barrier = WorldManager.getBarrier(getV3());

         if (barrier.getNumber() != PIT_BARRIER_NUM && getV4() == 1) {
            setIsUnlocked(true);
            player.removeItemFromInventory(item.getVnum());
         }
         return MoveFailCode.UNLOCK_BARRIER;
      }
   }

   /**
    * Gets the door type.
    *
    * @return the door type.
    */
   public DoorType getDoorType() {
      return DoorType.ITEM_KEY;
   }

   /**
    * Returns the debug string.
    *
    * @return The debug string.
    */
   public String toDebugString() {
      StringBuffer buffer = new StringBuffer();

      buffer.append("             ");
      buffer.append("Door Type: " + getDoorType());

      buffer.append("             ");
      buffer.append("Barrier: " + getV3());
      buffer.append("\n");

      int keyToUnlock = getV0();
      buffer.append("             Key: ");
      buffer.append(WorldManager.getEquipment(keyToUnlock).getName());
      if (getV4() == 0) {
         buffer.append(", consume=false");
      } else if (getV4() == 1) {
         buffer.append(", consume=true");
      } else {
         buffer.append(", consume=unknown");
      }
      buffer.append("\n");

      return buffer.toString();
   }

}
