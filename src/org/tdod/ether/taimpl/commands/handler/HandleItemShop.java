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

package org.tdod.ether.taimpl.commands.handler;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles a command for an item shop.
 * @author rkinney
 */
public final class HandleItemShop {

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleItemShop() {
   }

   /**
    * Executes the shop command.
    * @param player the player issuing the command.
    * @param parameter any parameters passed in by the player.
    * @return true if the command was successful.
    */
   public static boolean execute(Player player, String parameter) {
      Room room = player.getRoom();

      // Find all items beginning with what the player typed.
      ArrayList<Item> shopItemList = room.getShopItem();
      ArrayList<Item> availableItems = new ArrayList<Item>();
      for (Item item : shopItemList) {
         if (MyStringUtil.contains(item.getName(), parameter.toLowerCase())) {
            availableItems.add(item);
         }
      }

      // No item exists.
      if (availableItems.size() == 0) {
         player.print(TaMessageManager.NSOHER.getMessage());
         return true;
      }

      // More than one item found.
      if (availableItems.size() > 1) {
         player.print(TaMessageManager.BMRSPC.getMessage());
         return true;
      }

      // Found one item.
      Item item = availableItems.get(0);

      // Check if the player can use the item.
      if (!item.canUse(player.getPlayerClass())) {
         if (item.getItemType().equals(ItemType.WEAPON)) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.NTTWEP.getMessage(),
                  player.getPlayerClass().getName().toLowerCase() + "s");
            player.print(messageToPlayer);
            return true;
         }
         if (item.getItemType().equals(ItemType.ARMOR)) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.NTTARM.getMessage(),
                  player.getPlayerClass().getName().toLowerCase() + "s");
            player.print(messageToPlayer);
            return true;
         }
      }

      // Check the level of the item.
      if (item.getLevel() > player.getLevel()) {
         player.print(TaMessageManager.TOOINX.getMessage());
         return true;
      }

      // Modify final cost of the item
      int variance = (Dice.roll(0, 20) - 10); // -10% to 10%
      float percentMarkup = (player.getStats().getCharisma().getBuyModifier() + variance) / 100f;
      float modCostFloat = (item.getCost() * percentMarkup) + item.getCost();
      int modCost = (int) modCostFloat;

      // Items sell for half of what the are worth, so we don't want an item being bought for that.
      if (modCost < (item.getCost() / 2)) {
         modCost = item.getCost();
      }

      // Sanity check... don't want a 0 cost item.
      if (modCost < 1) {
         modCost = 1;
      }

      // Player does not have enough gold.
      if (modCost > player.getGold()) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(), item.getLongDescription());
         player.print(messageToPlayer);
         return true;
      }

      Item clonedItem = item.clone("Purchased from " + player.getName());
      InventoryFailCode failCode = player.placeItemInInventory(clonedItem, false);
      // Player does not have enough inventory space.
      if (failCode.equals(InventoryFailCode.SPACE)) {
         player.print(TaMessageManager.INVFUL.getMessage());
         clonedItem.destroy();
         return true;
      }
      // Player is carrying to much weight.
      if (failCode.equals(InventoryFailCode.ENCUMBRANCE)) {
         player.print(TaMessageManager.TOOHVY.getMessage());
         clonedItem.destroy();
         return true;
      }

      // Finally!  Success!!
      player.subtractGold(modCost);
      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUGOT.getMessage(), item.getLongDescription(), modCost);
      player.print(messageToPlayer);
      String messageToRoom = MessageFormat.format(TaMessageManager.BUYOTH.getMessage(),
            player.getName(), item.getLongDescription());
      room.print(player, messageToRoom, false);

      return true;

   }
}
