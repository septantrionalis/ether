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

import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.EntityType;
import org.tdod.ether.ta.commands.Command;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.InventoryFailCode;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * give minex 1 gold
 * &MSorry, you can't give things to yourself.                 *
 */
public class DoGive extends Command {

   /**
    * The maximum amount of gold a player can transfer to another player.
    */
   private static final int MAX_GOLD_TRANSFER = 30000;

   /**
    * Level that a player can drop items.
    */
   private static final int LEVEL_TO_DROP_ITEMS =
      new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.LEVEL_TO_DROP_ITEMS)).intValue();

   /**
    * Keyword suffix for the give gold command.
    */
   private static final String GOLD_KEYWORD = "gold";

   /**
    * Executes the "withdraw" command.
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

      String[] split = input.split(" ", FOUR_PARAMS);
      if (split.length < THREE_PARAMS) {
         return false;
      }

      Room room = player.getRoom();
      Player target = room.getPlayerInRoom(split[1]);

      // No player found.
      if (target == null) {
         String message = MessageFormat.format(TaMessageManager.ARNNHR.getMessage(), split[1]);
         player.print(message);
         return true;
      }

      // Can't give items to yourself.
      if (player.equals(target)) {
         player.print(TaMessageManager.NOGSLF.getMessage());
         return true;
      }

      if (split.length == FOUR_PARAMS && split[THREE_PARAMS].toLowerCase().equals(GOLD_KEYWORD)) {
         return handleGold(player, target, split[2]);
      } else {
         return handleItem(player, target, split[2]);
      }

   }

   /**
    * give minex 1 gold                                           *
    * At your level your gold would be better spent elsewhere.    *
    *
    * give limech a gold                                          *
    * &MSorry, but you don't seem to have one.                    *
    *
    * give limech -1 gold                                         *
    * &MSorry, but you can only give away 30000 gold at a time.   *
    *
    * give limech 0 gold                                          *
    * &MSorry, but you don't seem to have one.                    *
    *
    * &MSorry, but you don't have that much gold.                 *
    *
    * give limech 1 gold                                          *
    * PLAYER : &WYou gave 1 gold coins to Limech.                 *
    * TARGET : &WMinex just gave you 1 gold coins.                *
    * ROOM   : &W%s just handed some gold coins to %s.            *
    *
    * give limech 1 gold                                          *
    * &MSorry, %s can't carry that much more gold.                *
    *
    * give minex 1 gold
    *
    * @param player The player issuing the command.
    * @param target The target player.
    * @param amountString The amount to give in string format.
    *
    * @return if the command is succesful.
    */
   private synchronized boolean handleGold(Player player, Player target, String amountString) {
      Room room = player.getRoom();
      String messageToPlayer;

      // Don't allow low level characters to give gold.
      if (player.getLevel() < LEVEL_TO_DROP_ITEMS) {
         player.print(TaMessageManager.CNTGVG.getMessage());
         return true;
      }

      int amount = 0;

      // Determine if the amount is a numeric value.
      try {
         amount = Integer.valueOf(amountString);
      } catch (NumberFormatException e) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      // Check for the amount given.
      if (amount == 0) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      } else if (amount < 0 || amount > MAX_GOLD_TRANSFER) {
         player.print(TaMessageManager.TMCHGP.getMessage());
         return true;
      }

      // Check if the player has that much gold.
      if (amount > player.getGold()) {
         player.print(TaMessageManager.DNTHVG.getMessage());
         return true;
      }

      int overflow = target.addGold(amount);
      // Player can't carry that much gold.  Take it all back and cancel the transaction.
      if (overflow > 0) {
         int amountGiven = amount - overflow;
         target.subtractGold(amountGiven);
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTGGP.getMessage(), target.getName());
         player.print(messageToPlayer);
         return true;
      }

      // Do it.
      player.subtractGold(amount);

      // Send the messages.
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGVG.getMessage(), amount, target.getName());
      String messageToTarget = MessageFormat.format(TaMessageManager.YOUGTG.getMessage(), player.getName(), amount);
      String messageToRoom = MessageFormat.format(TaMessageManager.JSTGVG.getMessage(), player.getName(), target.getName());

      player.print(messageToPlayer);
      target.print(messageToTarget);
      room.print(player, target, messageToRoom, false);

      return true;
   }

   /**
    * give minex to                                   *
    * &MAt your level you may need that item.         *
    *
    * give limech 1 asd                               *
    * Sorry, but you don't seem to have one.          *
    *
    * give limech to
    * PLAYER: &MSorry Limech can't carry anything else.                                *
    * TARGET: &MMinex tried to give you a torch, but you can't carry anything else.    *
    * ROOM:   &W%s just handed something to %s, who gave it right back.                *
    *
    * PLAYER: &M%s can't carry the additional weight.                                  *
    * TARGET: &M%s tried to give you a %s, but you can't carry the additional weight.  *
    * ROOM:   &W%s just handed something to %s, who gave it right back.                *
    *
    * PLAYER: &WYou gave your torch to Limech.                                         *
    * TARGET: &WMinex just gave you a torch.                                           *
    * ROOM:   &W%s just handed an item to %s.                                          *
    *
    * give minex item
    *
    * @param player The player issuing the command.
    * @param target The target player.
    * @param itemString The item to give in string format.
    *
    * @return if the command is succesfull.
    */
   private synchronized boolean handleItem(Player player, Player target, String itemString) {
      Room room = player.getRoom();
      // Don't allow low level characters to hand out items.
      if (player.getLevel() < LEVEL_TO_DROP_ITEMS) {
         player.print(TaMessageManager.CNTGVI.getMessage());
         return true;
      }

      Item itemToGive = player.getItem(itemString);

      // Item was not found.
      if (itemToGive == null) {
         player.print(TaMessageManager.DNTHAV.getMessage());
         return true;
      }

      String messageToPlayer;
      String messageToTarget;
      String messageToRoom;

      InventoryFailCode failCode = target.placeItemInInventory(itemToGive, false);
      // Player does not have enough inventory space.
      if (failCode.equals(InventoryFailCode.SPACE)) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTGIV.getMessage(), target.getName());
         messageToTarget = MessageFormat.format(TaMessageManager.TRDGIV.getMessage(), target.getName(), itemToGive.getName());
         messageToRoom = MessageFormat.format(TaMessageManager.DNTGIV.getMessage(), player.getName(), target.getName());
         player.print(messageToPlayer);
         target.print(messageToTarget);
         room.print(player, target, messageToRoom, false);
         return true;
      }
      // Player is carrying to much weight.
      if (failCode.equals(InventoryFailCode.ENCUMBRANCE)) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTHDL.getMessage(), target.getName());
         messageToTarget = MessageFormat.format(TaMessageManager.UCNHDL.getMessage(), target.getName(), itemToGive.getName());
         messageToRoom = MessageFormat.format(TaMessageManager.DNTGIV.getMessage(), player.getName(), target.getName());
         player.print(messageToPlayer);
         target.print(messageToTarget);
         room.print(player, target, messageToRoom, false);
         return true;
      }

      // Do it
      player.removeItemFromInventory(itemToGive);

      // Send messages
      messageToPlayer = MessageFormat.format(TaMessageManager.YOUGAV.getMessage(), itemToGive.getName(), target.getName());
      messageToTarget = MessageFormat.format(TaMessageManager.YOUGET.getMessage(),
            player.getName(), itemToGive.getLongDescription());
      messageToRoom = MessageFormat.format(TaMessageManager.JSTGAV.getMessage(), player.getName(), target.getName());

      player.print(messageToPlayer);
      target.print(messageToTarget);
      room.print(player, target, messageToRoom, false);

      return true;
   }

}
