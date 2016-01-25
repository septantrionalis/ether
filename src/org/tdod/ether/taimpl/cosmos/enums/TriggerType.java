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

package org.tdod.ether.taimpl.cosmos.enums;

/**
 * An enumeration of trigger types.
 * @author rkinney
 */
public enum TriggerType {

   /**
    * None.
    */
   NONE(0, "NONE"),
   /**
    * Treasure.
    */
   TREASURE(1, "Treasure"),
   /**
    * A trap.
    */
   TRAP(2, "Trap"),
   /**
    * A teleporter.
    */
   TELEPORT(3, "Teleport"),
   /**
    * A puzzle.
    */
   PUZZLE(4, "Puzzle"),
   /**
    * An invalid trigger type.
    */
   Invalid(-1, "Invalid");

   private int           _index;
   private String        _description;

   /**
    * Creates a new TriggerType.
    * @param index the index of the trigger type.
    * @param description a description of the trigger type.
    */
   TriggerType(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the index of the trigger.
    * @return the index of the trigger.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the description of the trigger.
    * @return the description of the trigger.
    */
   public String getDescription() {
      return _description;
   }

   /**
    * Gets the trigger type based on an index.
    * @param index the index of the trigger type.
    * @return the trigger type based on an index.
    */
   public static TriggerType getTriggerType(int index) {
      for (TriggerType triggerType : TriggerType.values()) {
         if (triggerType.getIndex() == index) {
            return triggerType;
         }
      }

      return TriggerType.Invalid;
   }

}
