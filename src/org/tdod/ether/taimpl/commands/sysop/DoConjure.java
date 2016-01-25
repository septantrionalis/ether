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

package org.tdod.ether.taimpl.commands.sysop;

import java.text.MessageFormat;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Conjures an item.
 * @author rkinney
 */
public class DoConjure extends SysopCommand {

   /**
    * Executes the sysop command.
    * @param player the player issuing the command.
    * @param input the player input.
    * @return true if the command was successful.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ", 2);

      if (split.length < 2) {
         return false;
      }

      Item item = WorldManager.getItem(split[1]);

      if (item == null) {
         player.println(MessageManager.CONJURE_FAIL.getMessage());
         return true;
      }

      Item clonedItem = item.clone("Conjured from " + player.getName());
      InventoryFailCode failCode = player.placeItemInInventory(clonedItem, false);

      if (failCode.equals(InventoryFailCode.NONE)) {
         String messageToPlayer = MessageFormat.format(MessageManager.CONJURE.getMessage(), item.getLongDescription());
         player.println(messageToPlayer);
      } else if (failCode.equals(InventoryFailCode.SPACE)) {
         player.print(TaMessageManager.INVFUL.getMessage());
         clonedItem.destroy();
      } else if (failCode.equals(InventoryFailCode.ENCUMBRANCE)) {
         player.print(TaMessageManager.TOOHVY.getMessage());
         clonedItem.destroy();
      }

      return true;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("conjure [item name]");
   }

}
