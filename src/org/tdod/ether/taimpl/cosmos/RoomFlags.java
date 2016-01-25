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

package org.tdod.ether.taimpl.cosmos;

/**
 * An enumeration of room flags.
 * @author rkinney
 */
public enum RoomFlags {

   /**
    * No flag.
    */
   NONE(0, "none"),

   /**
    * A safe room.
    */
   SAFE(1, "safe"),

   /**
    * The room is dark.
    */
   DARK(2, "dark"),

   /**
    * An arena.
    */
   ARENA(4, "arena"),

   /**
    * A tavern.
    */
   TAVERN(8, "tavern"),

   /**
    * A private room.
    */
   PRIVATE(16, "private"),

   /**
    * The docks.
    */
   DOCKS(32, "docks"),

   /**
    * The guild hall.
    */
   GUILD_HALL(64, "guild hall"),

   /**
    * The vault.
    */
   VAULT(128, "vault"),

   /**
    * An equipment shop.
    */
   EQUIPMENT_SHOP(256, "equipment shop"),

   /**
    * A temple.
    */
   TEMPLE(512, "temple"),

   /**
    * An armor shop.
    */
   ARMOR_SHOP(1024, "armor shop"),

   /**
    * A weapon shop.
    */
   WEAPON_SHOP(2048, "weapon shop"),

   /**
    * A magic shop.
    */
   MAGIC_SHOP(4096, "magic shop"),

   /**
    * An inn.
    */
   INN(16384, "inn"),

   /**
    * The room is shrouded in permanent darkness.  Torches or a glowstone
    * will not work.
    */
   PERM_DARK(32768, "permanent darkness");

   private final int _bit;
   private final String _description;

   /**
    * Creates a new RoomFlags enum.
    * @param bit the bit vector.
    * @param description a description of the flag.
    */
   RoomFlags(int bit, String description) {
      _bit = bit;
      _description = description;
   }

   /**
    * Gets the room flag description.
    * @return the room flag description.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Creates a string representation of the room flag bit vector in the format of:
    * [n,e,s,w].
    * @param flags the flag bits.
    * @return a string representation of the flag.
    */
   public static String getFlagsString(int flags) {
      StringBuffer buffer = new StringBuffer();
      boolean firstFlag = true;

      buffer.append("[");
      for (RoomFlags roomFlag : RoomFlags.values()) {
         if (roomFlag.isSet(flags)) {
            if (firstFlag) {
               buffer.append(roomFlag.getDescription());
               firstFlag = false;
            } else {
               buffer.append(", ");
               buffer.append(roomFlag.getDescription());
            }
         }
      }
      buffer.append("]");

      return buffer.toString();
   }

   /**
    * Gets the bit vector.
    * @return the bit vector.
    */
   public int getBit() {
      return _bit;
   }

   /**
    * Checks if the flag is set.
    * @param flags the flag.
    * @return true if it is set.
    */
   public boolean isSet(int flags) {
      if ((flags & _bit) == _bit) {
         return true;
      }

      return false;
   }

}
