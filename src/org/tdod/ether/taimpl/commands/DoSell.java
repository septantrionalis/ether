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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the "sell" command.
 *
 * @author Ron Kinney
 */
public class DoSell extends Command {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoSell.class);

   /**
    * Executes the "sell" command.
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

      Room room = player.getRoom();

      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }

      if (RoomFlags.EQUIPMENT_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.WEAPON_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.ARMOR_SHOP.isSet(room.getRoomFlags())
            || RoomFlags.MAGIC_SHOP.isSet(room.getRoomFlags())) {
         String[] parsed = input.split(" ");

         // Find the first item beginning with what the player typed.
         ArrayList<Item> inventoryItemList = player.getInventory();
         Item itemForSale = null;
         for (Item item : inventoryItemList) {
            if (MyStringUtil.contains(item.getName(), parsed[1])) {
               itemForSale = item;
            }
         }

         // Item is not in the players inventory.
         if (itemForSale == null) {
            player.print(TaMessageManager.DNTHAV.getMessage());
            return true;
         }

         // The shop will only buy items he sells.
         ArrayList<Item> shopItemList = room.getShopItem();
         if (!isShopInterested(shopItemList, itemForSale)) {
            player.print(TaMessageManager.NOSHER.getMessage());
            return true;
         }

         // Sell it!
         int sellCost = itemForSale.getCost() / 2;

         player.addGold(sellCost);
         if (!player.removeItemFromInventory(itemForSale)) {
            _log.error("I paid " + player.getName() + " " + sellCost + " gold for " + itemForSale.getName()
                  + " but he didn't give me the item!");
         }
         String messageToPlayer = MessageFormat.format(TaMessageManager.SLDITM.getMessage(),
               itemForSale.getLongDescription(), sellCost);
         player.print(messageToPlayer);
         String messageToRoom = MessageFormat.format(TaMessageManager.SLDOTH.getMessage(),
               player.getName(), itemForSale.getLongDescription());
         room.print(player, messageToRoom, false);

         return true;
      }
      return false;
   }

   /**
    * Determines if the shop is interested in the item being sold.
    *
    * @param shopItemList The item list of the shop.
    * @param itemForSale The item being sold.
    *
    * @return true if the item can be sold to the shop.
    */
   private boolean isShopInterested(ArrayList<Item> shopItemList, Item itemForSale) {
      for (Item item : shopItemList) {
         if (item.isSimilar(itemForSale)) {
            return true;
         }
      }

      return false;
   }
}
