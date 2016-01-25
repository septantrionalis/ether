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

package org.tdod.ether.taimpl.commands;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * &WOk, you got a waterskin.
 * &WLimech just picked up a waterskin.
 *
 * get all
 * &MYou can't carry anything else.
 * &WOk, you got a torch, a torch, and a torch.
 * &WLimech just picked up some items.
 *
 * get asd
 * &MSorry, but no such item is here.
 * get water
 * &MYou can't carry anything else.
 */
public class DoGet extends Command {

   /**
    * Keyword suffix for the get all command.
    */
   private static final String ALL_KEYWORD = "all";

   /**
    * Executes the "get" command.
    *
    * @param entity The entity executing the command.
    * @param input The input string.
    *
    * @return true if the command can be executed, false otherwise.
    */
   public final boolean execute(Entity entity, String input) {
      if (!entity.getEntityType().equals(EntityType.PLAYER)) {
         return false;
      }
      Player player = (Player) entity;

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }
      String param = split[1].toLowerCase();

      // Drop all.
      if (param.toLowerCase().equals(ALL_KEYWORD)) {
         getAll(player);
         return true;
      } else {
         getSingleItem(player, param);
         return true;
      }
   }

   /**
    * Get a single item from the ground.
    *
    * @param player The player performing the get command.
    * @param param Any parameters passed in by the player.
    */
   private synchronized void getSingleItem(Player player, String param) {
      Room room = player.getRoom();
      ArrayList<Item> itemsOnGround = room.getItemsOnGround();

      // Find the item.
      Item itemToGet = null;
      for (Item item : itemsOnGround) {
         if (MyStringUtil.contains(item.getName(), param)) {
            itemToGet = item;
         }
      }

      // Item was not found.
      if (itemToGet == null) {
         player.print(TaMessageManager.NSIHER.getMessage());
         return;
      }

      InventoryFailCode failCode = player.placeItemInInventory(itemToGet, false);
      // Player does not have enough inventory space.
      if (failCode.equals(InventoryFailCode.SPACE)) {
         player.print(TaMessageManager.INVFUL.getMessage());
         return;
      }
      // Player is carrying to much weight.
      if (failCode.equals(InventoryFailCode.ENCUMBRANCE)) {
         player.print(TaMessageManager.TOOHVY.getMessage());
         return;
      }

      // Success.
      room.removeItemFromRoom(itemToGet);
      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUTAK.getMessage(), itemToGet.getLongDescription());
      player.print(messageToPlayer);
      String messageToRoom = MessageFormat.format(TaMessageManager.GETOTH.getMessage(),
            player.getName(), itemToGet.getLongDescription());
      room.print(player, messageToRoom, false);

      return;
   }

   /**
    * Get all items from the ground.
    *
    * @param player The player performing the get command.
    */
   private synchronized void getAll(Player player) {
      Room room = player.getRoom();
      ArrayList<Item> itemsOnGround = room.getItemsOnGround();

      // Attempt to get each item one by one.
      ArrayList<Item> itemsPickedUp = new ArrayList<Item>();
      InventoryFailCode code = InventoryFailCode.NONE;
      for (Item item : itemsOnGround) {
         code = player.placeItemInInventory(item, false);
         if (code.equals(InventoryFailCode.NONE)) {
            itemsPickedUp.add(item);
         } else {
            break;
         }
      }

      // Player did not have enough inventory space.
      if (code.equals(InventoryFailCode.SPACE)) {
         player.print(TaMessageManager.INVFUL.getMessage());
      }
      // Player is carrying to much weight.
      if (code.equals(InventoryFailCode.ENCUMBRANCE)) {
         player.print(TaMessageManager.TOOHVY.getMessage());
      }

      // No items were picked up.  Reason should have been given above.
      if (itemsPickedUp.size() < 1) {
         return;
      }

      // Remove all of the items from the room.
      for (Item item : itemsPickedUp) {
         room.removeItemFromRoom(item);
      }

      // Display the list items that were picked up.
      String str = MyStringUtil.buildItemListToString(itemsPickedUp);
      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUTAK.getMessage(), str);
      player.print(messageToPlayer);

      String messageToRoom = MessageFormat.format(TaMessageManager.GETOTH2.getMessage(), player.getName());
      room.print(player, messageToRoom, false);

   }
}
