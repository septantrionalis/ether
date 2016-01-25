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

package org.tdod.ether.ta.combat;

/**
 * This is an enumeration of spell results.
 *
 * @author Ron Kinney
 */
public enum SpellResultEnum {

   /**
    * The spell was a success.
    */
   SUCCESS(0, "success"),

   /**
    * The spell was negated.
    */
   NEGATED(1, "negated");

   private int           _index;
   private String        _description;

   /**
    * Creates a new SpellResultEnum.
    *
    * @param index The enumeration index.
    * @param description The description of the spell result.
    */
   SpellResultEnum(int index, String description) {
      _index = index;
      _description = description;
   }

   /**
    * Gets the enumeration index.
    *
    * @return the enumeration index.
    */
   public int getIndex() {
      return _index;
   }

   /**
    * Gets the spell result description.
    *
    * @return the spell result description.
    */
   public String getDescription() {
      return _description;
   }

}
