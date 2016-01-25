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
import org.tdod.ether.ta.cosmos.enums.DropItemFailCode;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.items.ItemType;
import org.tdod.ether.ta.items.equipment.Equipment;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.items.equipment.enums.EquipmentSubType;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MyStringUtil;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * DONE
 * &MSorry, littering is not permitted here.
 * &MSorry, but you don't seem to have one.
 * &MSorry, but there is no more room here to drop items.
 * 8 items max on floor.
 * &WOk, you dropped your torch.
 * &WMinex just dropped a torch.
 *  * &WOk, you dropped a soulstone, a heartstone, a zarynthium potion, a zarynthium
 * potion, a hyssop potion, a rowan potion, a vervain potion, and an anemone
 * potion.
 * &MSorry, but there is no more room here to drop items.
 * &WMinex just dropped some items.
 */
public class DoDrop extends Command {

   /**
    * Logger.
    */
   private static Log _log = LogFactory.getLog(DoDrop.class);

   /**
    * Level that a player has to be in order to drop items.
    */
   private static final int LEVEL_TO_DROP_ITEMS =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.LEVEL_TO_DROP_ITEMS)).intValue();

   /**
    * Command suffix for the all keyword.
    */
   private static final String ALL_KEYWORD = "all";

   /**
    * Executes the "drop" command.
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

      Room room = entity.getRoom();
      String[] split = input.split(" ", 2);
      if (split.length < 2) {
         return false;
      }

      // Don't allow low level characters to hand out items.
      if (player.getLevel() < LEVEL_TO_DROP_ITEMS) {
         player.print(TaMessageManager.CNTGVI.getMessage());
         return true;
      }

      String param = split[1].toLowerCase();

      // Can't drop items in a town.
      if (GameUtil.isTown(room)) {
         player.print(TaMessageManager.NODHER.getMessage());
         return true;
      }

      // Drop all.
      if (param.toLowerCase().equals(ALL_KEYWORD)) {
         dropAll(player);
         return true;
      } else {
         dropSingleItem(player, param);
         return true;
      }

   }

   /**
    * Drop a single item.
    *
    * @param player The player dropping the item.
    * @param param An parameters the player passed into the command.
    */
   private synchronized void dropSingleItem(Player player, String param) {
      Room room = player.getRoom();
      Item itemToDrop = player.getItem(param);

      // Item was not found.
      if (itemToDrop == null) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return;
      }

      DropItemFailCode code = room.dropItemInRoom(itemToDrop);

      if (code.equals(DropItemFailCode.NO_SPACE_IN_ROOM)) {
         // No space on room floor.
         player.print(TaMessageManager.NDPITM.getMessage());
      } else if (code.equals(DropItemFailCode.NONE)) {
         // Looks good.  Send message to player, room, and remove the item.
         player.removeItemFromInventory(itemToDrop);

         if (!handleLitTorch(player, itemToDrop, true)) {
            String messageToPlayer = MessageFormat.format(TaMessageManager.DRPITM.getMessage(), itemToDrop.getName());
            player.print(messageToPlayer);

            String messageToRoom = MessageFormat.format(TaMessageManager.DRPOTH.getMessage(),
                  player.getName(), itemToDrop.getLongDescription());
            room.print(player, messageToRoom, false);
         }
      } else {
         // Got an unhandled code.
         _log.error("Attempted to drop " + itemToDrop.getName() + " but got return code " + code);
      }

      return;
   }

   /**
    * Drops all items in the players inventory.
    *
    * @param player The player issuing the command.
    */
   private synchronized void dropAll(Player player) {
      Room room = player.getRoom();
      ArrayList<Item> inventory = player.getInventory();

      // No item found.
      if (inventory.size() < 1) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return;
      }

      // Attempt to drop each item one by one.
      ArrayList<Item> droppedItems = new ArrayList<Item>();
      DropItemFailCode code = DropItemFailCode.NONE;
      for (Item item : inventory) {
         code = room.dropItemInRoom(item);
         if (code.equals(DropItemFailCode.NONE)) {
            droppedItems.add(item);
         } else {
            break;
         }
      }

      // Display the list of dropped items to the player.
      String str = MyStringUtil.buildItemListToString(droppedItems);
      String messageToPlayer = TaMessageManager.DRPITM2.getMessage() + " " + str + ".";
      player.println(messageToPlayer);

      // If the room filled up, tell the player so.
      if (code.equals(DropItemFailCode.NO_SPACE_IN_ROOM)) {
         player.print(TaMessageManager.NDPITM.getMessage());
      }

      // Tell the room that the player dropped all their items.
      String messageToRoom = MessageFormat.format(TaMessageManager.DRPOTH2.getMessage(), player.getName());
      room.print(player, messageToRoom, false);

      // Remove all of the items from the player.
      for (Item droppedItem : droppedItems) {
         player.removeItemFromInventory(droppedItem);

         handleLitTorch(player, droppedItem, false);
      }

      return;
   }

   /**
    * Handles a lit torch when it is dropped.
    *
    * @param player The player dropping the torch.
    * @param itemToDrop The torch that was dropped.
    * @param displayMessage True if a message should be displayed.
    *
    * @return True if the torch was dropped.
    */
   private boolean handleLitTorch(Player player, Item itemToDrop, boolean displayMessage) {
      Room room = player.getRoom();
      if (itemToDrop.getItemType().equals(ItemType.EQUIPMENT)) {
         Equipment eq = (Equipment) itemToDrop;
         if (eq.getEquipmentSubType().equals(EquipmentSubType.LIGHT)) {
            if (eq.isActivated()) {
               room.removeItemFromRoom(itemToDrop);
               itemToDrop.handleExpiredTimer(player, displayMessage);
               itemToDrop.destroy();
               DoDefault.displayRoomDescription(player);
               return true;
            }
         }
      }

      return false;
   }
}
