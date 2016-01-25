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

package org.tdod.ether.ta.items.armor;

import org.tdod.ether.ta.items.Item;
import org.tdod.ether.taimpl.cosmos.enums.Town;

/**
 * The armor interface.
 * @author Ron Kinney
 */
public interface Armor extends Item {

   /**
    * Gets the armor rating.
    * @return the armor rating.
    */
   int getArmorRating();

   /**
    * Sets the armor rating.
    * @param armorRating the armor rating.
    */
   void setArmorRating(int armorRating);

   /**
    * Gets the armor type.
    * @return the armor type.
    */
   int getType();

   /**
    * Sets the armor type.
    * @param type the armor type.
    */
   void setType(int type);

   /**
    * Gets the town that this item is sold in.
    * @return the town that this item is sold in.
    */
   Town getTown();

   /**
    * Sets the town this item is sold in.
    * @param town the town this item is sold in.
    */
   void setTown(Town town);

   /**
    * Sets the value at index 2.
    * @param v2 the value at index 2.
    */
   void setV2(int v2);

   /**
    * Gets the value at index 2.
    * @return the value at index 2.
    */
   int getV2();

   /**
    * Sets  the value at index 3.
    * @param v3 the value at index 3.
    */
   void setV3(int v3);

   /**
    * Gets the value at index 3.
    * @return the value at index 3.
    */
   int getV3();

   /**
    * Sets the value at index 4.
    * @param v4 the value at index 4.
    */
   void setV4(int v4);

   /**
    * Gets the value at index 4.
    * @return the value at index 4.
    */
   int getV4();

   /**
    * Sets the value at index 7.
    * @param v7 the value at index 7.
    */
   void setV7(int v7);

   /**
    * Gets the value at index 7.
    * @return the value at index 7.
    */
   int getV7();

   /**
    * Sets the value at index 8.
    * @param v8 the value at index 8.
    */
   void setV8(int v8);

   /**
    * Gets the value at index 8.
    * @return the value at index 8.
    */
   int getV8();

   /**
    * Sets the value at index 9.
    * @param v9 the value at index 9.
    */
   void setV9(int v9);

   /**
    * Gets the value at index 9.
    * @return the value at index 9.
    */
   int getV9();

   /**
    * Sets the value at index 10.
    * @param v10 the value at index 10.
    */
   void setV10(int v10);

   /**
    * Gets the value at index 10.
    * @return the value at index 10.
    */
   int getV10();

   /**
    * Sets the value at index 11.
    * @param v11 the value at index 11.
    */
   void setV11(int v11);

   /**
    * Gets the value at index 11.
    * @return the value at index 11.
    */
   int getV11();

   /**
    * Sets the value at index 12.
    * @param v12 the value at index 12.
    */
   void setV12(int v12);

   /**
    * Gets the value at index 12.
    * @return the value at index 12.
    */
   int getV12();

   /**
    * Sets the value at index 13.
    * @param v13 the value at index 13.
    */
   void setV13(int v13);

   /**
    * Gets the value at index 13.
    * @return the value at index 13.
    */
   int getV13();

}
