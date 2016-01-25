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

import java.util.ArrayList;

import org.tdod.ether.ta.items.Item;

/**
 * Various string handling utilities are handled by this class.
 * @author rkinney
 */
public final class MyStringUtil {

   /**
    * A private constructor to enforce the singleton pattern.
    */
   private MyStringUtil() {
   }

   /**
    * Capitalize the first letter of the name.
    * @param s The String to work on.
    * @return The same string with the first letter in caps.
    */
   public static String up1(String s) {
      return (s.length() > 0) ? Character.toUpperCase(s.charAt(0)) + s.substring(1) : s;
   }

   /**
    * Formats the item string so that it looks like.
    *
    * null
    * a torch
    * a torch, and a torch
    * a torch, a torch, and a torch
    *
    * @param items the list of items.
    * @return a formatted string of items.
    */
   public static String buildItemListToString(ArrayList<Item> items) {
      StringBuffer itemBuffer = new StringBuffer();
      if (items.isEmpty()) {
         return null;
      } else {
         int index = 0;
         if (items.size() == 1) {
            Item item = items.get(0);
            itemBuffer.append(item.getLongDescription());
         } else {
            for (Item item : items) {
               if (index == items.size() - 1) {
                  itemBuffer.append("and " + item.getLongDescription());
               } else {
                  itemBuffer.append(item.getLongDescription() + ", ");
               }
               index++;
            }
         }
      }
      return itemBuffer.toString();

   }

   /**
    * Generates a 'a' or 'an' prefix based on the string.
    * @param str the string.
    * @return the string prefix.
    */
   public static String generatePrefix(String str) {
      char c = str.toLowerCase().charAt(0);

      if (c == 'a' || c == 'e' || c == 'i' || c == 'o') {
         return "An";
      } else {
         return "A";
      }
   }

   /**
    * Returns true if str1 contains str2.
    *
    * @param str1 The string in question
    * @param str2 The string to compare.
    * @return true if str1 contains str2
    */
   public static boolean contains(String str1, String str2) {
      if (str1.toLowerCase().startsWith(str2.toLowerCase())) {
         return true;
      }

      String[] split = str1.split(" ");
      for (int count = 0; count < split.length; count++) {
         if (split[count].toLowerCase().startsWith(str2.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   /**
    * Checks if the players name is valid.
    * @param name the players name.
    * @return true if the name is valid.
    */
   public static boolean isValidName(String name) {
      if ("".equals(name)) {
         return false;
      }

      if (!isAlphaNumeric(name)) {
         return false;
      }

      return true;
   }

   /**
    * Checks if the string is alphanumeric.
    * @param str the string in question.
    * @return true if the string is alphanumeric.
    */
   private static boolean isAlphaNumeric(String str) {
      boolean blnAlpha = true;

      char[] chr = null;
      if (str != null) {
         chr = str.toCharArray();
      }

      if (chr == null) {
         return false;
      }

      for (int i = 0; i < chr.length; i++) {
         if (!((chr[i] >= 'A' && chr[i] <= 'Z') || (chr[i] >= 'a' && chr[i] <= 'z'))) {
            blnAlpha = false;
            break;
         }
      }
      return (blnAlpha);
   }

}
