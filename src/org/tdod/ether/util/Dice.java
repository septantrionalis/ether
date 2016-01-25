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

package org.tdod.ether.util;

import java.util.Random;

/**
 * This class handles the generation of random numbers and rolls.
 *
 * @author Ron Kinney
 */
public final class Dice {

   /**
    * The minimum percentage value.
    */
   public static final int MIN_PERCENTAGE = 0;

   /**
    * The maximum percentage value.
    */
   public static final int MAX_PERCENTAGE = 100;

   /**
    * The random seed.
    */
   private static Random _random = new Random(System.currentTimeMillis());

   /**
    * The minimum variance value.
    */
   private static final float _minVariance = 0.90f;

   /**
    * The variance multiplier.
    */
   private static final float _varianceMultiplier = 0.4f;
   /**
    * A private constructor so that this class cannot be instantiated.
    */
   private Dice() {
   }

   /**
    * Generates a number between the specified value.
    *
    * @param min The minimum value.
    * @param max The maximum value.
    *
    * @return a value between the min and max.
    */
   public static int roll(int min, int max) {
      int difference = max - min + 1;
      int random = _random.nextInt(difference);
      int number = random + min;
      return number;
   }

   /**
    * Generates a number between .80 an 1.20.
    *
    * @return a number between .80 an 1.20
    */
   public static float generateNumberVariance() {
      float roll =  _minVariance + _random.nextFloat() * _varianceMultiplier;

      return roll;
   }
}
