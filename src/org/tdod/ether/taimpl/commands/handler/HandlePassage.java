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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * b something
 * &MSorry, that item is not available here.
 *
 * b passage
 * Sorry, by decree of the guild masters, no one shall venture across the great     *
 * lake who does not bear a rune upon their brow.                                   *
 * Sorry, you cannot afford passage across the great lake!                          *
 * -> no message to room.
 *
 * You buy passage across the great lake and board a ship...                        *
 * Minex hands a small purse of coins to a ship's captain and boards a ship...      *
 * Minex has just arrived on a ship from across the great lake.                     *
 *
 * While aboard the ship your food was eaten by rats!                               *
 * While aboard the ship you seem to have been robbed!                              *
 * cost = 100 gold                                                                  *
 */
public final class HandlePassage {

   private static Log _log = LogFactory.getLog(HandlePassage.class);

   /**
    * Town 1 docks.
    */
   public static final int TOWN1_DOCKS   = -89;

   /**
    * Town 2 docks.
    */
   public static final int TOWN2_DOCKS   = -88;

   private static final String PASSAGE    = MessageManager.PASSAGE_STRING.getMessage();

   private static final int PASSAGE_COST  = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PASSAGE_COST));
   private static final int RAT_CHANCE    = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PASSAGE_RAT_CHANCE));
   private static final int ROBBED_CHANCE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PASSAGE_ROB_CHANCE));

   private static final int ROB_MIN       = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PASSAGE_ROB_MIN));
   private static final int ROB_MAX       = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PASSAGE_ROB_MAX));

   private static final int FOOD_VNUM     = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PASSAGE_RAT_CONSUME_VNUM));

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandlePassage() {
   }

   /**
    * Executes the passage command.
    * @param player the player issuing the command.
    * @param parameter any parameters the player passed in.
    * @return true of the command was successful.
    */
   public static boolean execute(Player player, String parameter) {
      if (!PASSAGE.equals(parameter.toLowerCase())) {
         player.print(TaMessageManager.NOSITM.getMessage());
         return true;
      }

      if (player.getRune().equals(Rune.NONE)) {
         player.print(TaMessageManager.NORUNE.getMessage());
         return true;
      }

      if (player.getGold() < PASSAGE_COST) {
         player.print(TaMessageManager.CNTVOY.getMessage());
         return true;
      }

      int arriveRoom;
      if (player.getRoom().getRoomNumber() == TOWN1_DOCKS) {
         arriveRoom = TOWN2_DOCKS;
      } else if (player.getRoom().getRoomNumber() == TOWN2_DOCKS) {
         arriveRoom = TOWN1_DOCKS;
      } else {
         _log.error("Handle passage was invoked from an unsupported room " + player.getRoom());
         arriveRoom = TOWN1_DOCKS;
      }

      player.subtractGold(PASSAGE_COST);
      player.print(TaMessageManager.YOUVOY.getMessage());

      String departRoomMessage = MessageFormat.format(TaMessageManager.OTHVOY.getMessage(), player.getName());
      WorldManager.getRoom(player.getRoom().getRoomNumber()).print(player, departRoomMessage, false);

      handleEvent(player);
      player.teleportToRoom(arriveRoom);

      String arriveRoomMessage = MessageFormat.format(TaMessageManager.OTHARR.getMessage(), player.getName());
      WorldManager.getRoom(arriveRoom).print(player, arriveRoomMessage, false);

      return true;
   }

   /**
    * Handles any events that might occur while buying passage across the great lake.
    * @param player the player
    */
   private static void handleEvent(Player player) {
      int roll = Dice.roll(0, 100);
      if (roll < RAT_CHANCE) {
         player.removeItemFromInventory(FOOD_VNUM);
         player.print(TaMessageManager.RATRAT.getMessage());
      } else if (roll < ROBBED_CHANCE + RAT_CHANCE) {
         player.subtractGold(Dice.roll(ROB_MIN, ROB_MAX));
         player.print(TaMessageManager.SHPSTL.getMessage());
      }
   }
}
