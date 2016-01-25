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
 * A trigger result enumeration.
 * @author rkinney
 */
public enum TriggerResult {

   /**
    * No trigger result.
    */
   NOTHING(0),
   /**
    * Teleported.
    */
   TELEPORTED(1),
   /**
    * Death.
    */
   DEATH(2);

   private int _index;

   /**
    * Creates a new TriggerResult.
    * @param index the index of the trigger result.
    */
   private TriggerResult(int index) {
      _index = index;
   }

   /**
    * Gets the index of the trigger result.
    * @return the index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * This compares two trigger results and returns the more important one.
    * @param triggerResult a trigger result.
    * @return the more important of the two trigger results.
    */
   public TriggerResult getFinalTriggerResult(TriggerResult triggerResult) {
      if (triggerResult.getIndex() > getIndex()) {
         return triggerResult;
      }

      return this;
   }
}
