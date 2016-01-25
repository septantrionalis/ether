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

package org.tdod.ether.taimpl.commands.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;

/**
 * Displays all items in the game, along with how that item came into existance.  This
 * will hopefully help with cheating sysops and item memory leaks.
 *
 * @author Ron Kinney
 */
public class DoItems extends SysopCommand {
   
   private static final int PARAM_SIZE = 2;
   
   /**
    * Executes the "items" command.
    *
    * @param player The player issuing the command.
    * @param input The player input.
    *
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ", PARAM_SIZE);

      HashMap<Item, String> itemsMap = WorldManager.getItemsInExistance();

      Set<Item> itemSet = itemsMap.keySet();
      List<String> tempList = new ArrayList<String>();
      for (Item item : itemSet) {
         String display = item.getName() + ", " + itemsMap.get(item);
         if (split.length == PARAM_SIZE) {
            if (item.getName().contains(split[1])) {
               tempList.add(display);
            }
         } else {
            tempList.add(display);               
         }
      }

      if (tempList.size() == 0) {
         player.println("No items found.");
      } else {
         for (String string : tempList) {
            player.println(string);
         }
      }
      
      return true;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: items");
   }

}
