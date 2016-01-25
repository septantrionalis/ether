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
 * This enumeration represents an exit direction.
 * @author rkinney
 */
public enum ExitDirectionEnum {

   /**
    * North.
    */
   NORTH("n", "north"),

   /**
    * East.
    */
   EAST("e", "east"),

   /**
    * South.
    */
   SOUTH("s", "south"),

   /**
    * West.
    */
   WEST("w", "west"),

   /**
    * North East.
    */
   NORTHEAST("ne", "northeast"),

   /**
    * North West.
    */
   NORTHWEST("nw", "northwest"),

   /**
    * South East.
    */
   SOUTHEAST("se", "southeast"),

   /**
    * South West.
    */
   SOUTHWEST("sw", "southwest"),

   /**
    * Up.
    */
   UP("u", "upward", "above", "up"),

   /**
    * Down.
    */
   DOWN("d", "downward", "below", "down"),

   /**
    * Unknown.
    */
   UNKNOWN("?" , "Unknown");

   private String _shortDescription;
   private String _longDescription;
   private String _altDescription;
   private String _alt2Description;

   /**
    * Creates a new ExitDirectionEnum.
    * @param shortDescription a short description of the exit.
    * @param longDescription the long description.
    */
   ExitDirectionEnum(String shortDescription, String longDescription) {
      _shortDescription = shortDescription;
      _longDescription = longDescription;
   }

   /**
    * Creates a new ExitDirectionEnum.
    * @param shortDescription a short description of the exit.
    * @param longDescription the long description.
    * @param altDescription alternate description 1.
    * @param alt2Description alternate description 2.
    */
   ExitDirectionEnum(String shortDescription, String longDescription, String altDescription, String alt2Description) {
      _shortDescription = shortDescription;
      _longDescription = longDescription;
      _altDescription = altDescription;
      _alt2Description = alt2Description;
   }

   /**
    * Gets the short description.
    * @return the short description.
    */
   public String getShortDescription() {
      return _shortDescription;
   }

   /**
    * Gets the long description.
    * @return the long description.
    */
   public String getLongDescription() {
      return _longDescription;
   }

   /**
    * Gets the alternate description 1.
    * @return the alternate description 1.
    */
   public String getAltDescription() {
      return _altDescription;
   }

   /**
    * Gets the alternate description 2.
    * @return the alternate description 1.
    */
   public String getAlt2Description() {
      return _alt2Description;
   }

   /**
    * Gets the exit direction enumeration based on the short description.
    * @param shortDescription the short description.
    * @return the corresponding exit direction, or unknown if one is not found.
    */
   public static ExitDirectionEnum getExit(String shortDescription) {
      for (ExitDirectionEnum exit : ExitDirectionEnum.values()) {
         if (exit.getShortDescription().equals(shortDescription)) {
            return exit;
         }
      }
      return ExitDirectionEnum.UNKNOWN;
   }

   /**
    * This method returns true if the keyword contains n,w,s,e,nw,ne,sw,se or the long version of them.
    * One example of this methods use is DoLook.
    *
    * @param keyword the keywork in question.
    * @return the exit corresponding to the keyword.  null if one is not found.
    */
   public static ExitDirectionEnum getExitKeyword(String keyword) {
      ExitDirectionEnum[] exits = ExitDirectionEnum.values();

      for (int index = 0; index < 8; index++) {
         if (exits[index].getShortDescription().equals(keyword.toLowerCase())
               || exits[index].getLongDescription().equals(keyword.toLowerCase())) {
            return exits[index];
         }
      }

      return null;
   }

   /**
    * Similar to getExitKeyword, but includes up and down.  This is needed to commands like DoDrag.
    * @param keyword the keyword in question.
    * @return the exit corresponding to the keyword.  null if one is not found.
    */
   public static ExitDirectionEnum getCompleteExitKeyword(String keyword) {
      ExitDirectionEnum exitDir = getExitKeyword(keyword);

      if (exitDir != null) {
         return exitDir;
      }

      if (keyword.toLowerCase().equals("u") || keyword.toLowerCase().equals("up")) {
         return ExitDirectionEnum.UP;
      }

      if (keyword.toLowerCase().equals("d") || keyword.toLowerCase().equals("down")) {
         return ExitDirectionEnum.DOWN;
      }

      return null;
   }
}
