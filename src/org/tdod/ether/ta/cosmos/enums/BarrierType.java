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

package org.tdod.ether.ta.cosmos.enums;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An enumeration of barrier types.
 *
 * @author Ron Kinney
 * @deprecated no longer used.
 */
public enum BarrierType {

   /**
    * A general barrier.
    */
   GENERAL1(0);

   private static Log _log = LogFactory.getLog(BarrierType.class);

   private int _index;

   /**
    * Constructor.
    * @param index the barrier index.
    */
   BarrierType(int index) {
      _index = index;
   }

   /**
    * Gets the index of the barrier.
    * @return the index of the barrier.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Returns the barrier in the given index.
    * @param index the barrier index.
    * @return a barrier, or "general", if one was not found.
    */
   public static BarrierType getBarrierType(int index) {
      BarrierType[] types = BarrierType.values();

      try {
         return types[index];
      } catch (Exception e) {
         _log.error("Barrier type of index " + index + " not supported.  Defaulting to 0.");
         return types[0];
      }
   }
}
