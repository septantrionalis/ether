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
import java.util.Formatter;
import java.util.Locale;
import java.util.TreeSet;

import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.enums.Slots;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handle slots.
 * @author rkinney
 */
public final class HandleSlots {

   // private static Log _log = LogFactory.getLog(HandleSlots.class);

   private static final int SLOTS_COST = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOTS_COST));
   private static final int JACKPOT = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOTS_JACKPOT_WINNINGS));
   private static final int TIER1 = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOTS_TIER1_WINNINGS));
   private static final int TIER2 = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOTS_TIER2_WINNINGS));
   private static final int TIER3 = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOTS_TIER3_WINNINGS));
   private static final int TIER4 = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.SLOTS_TIER4_WINNINGS));
   private static final int LIMIT = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.GAMES_OF_CHANCE_LIMIT));

   private static int _globalWinnings = 0;

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleSlots() {
   }

   /**
    * &MSorry, you need at least 1 gold crowns to play the slot machine!
    * &WMinex steps up to the slot machine, deposits some coins, and pulls the lever!
    * &WSorry, you may only play the games of chance 25 times per level!
    * @param player the player issuing the command.
    * @param parameter any parameters passed in by the player.
    * @return true if the command was successful.
    */
   public static boolean execute(Player player, String parameter) {
      String messageToPlayer;

      int cost = SLOTS_COST;

      if (cost > player.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.SLTLOW.getMessage(), SLOTS_COST);
         player.print(messageToPlayer);
         return true;
      }

      if (player.getGameOfChanceCount() >= LIMIT) {
         player.print(TaMessageManager.SLTOUT.getMessage());
         return true;
      }

      player.setGameOfChanceCount(player.getGameOfChanceCount() + 1);

      player.subtractGold(cost);
      _globalWinnings -= cost;

      Slots[] slots = {Slots.getRandomSlot(), Slots.getRandomSlot(), Slots.getRandomSlot()};
      String resultMessage = getResult(player, slots);

      // Complete hack, yes I know but fuck off.  It works.  Replace "{1}8s" generated by the data reader with "%-8s"
      String template = TaMessageManager.SLTPUL.getMessage().replace("{1}8s", "%-8s");
      template = template.replace("{2}8s", "%-8s");
      template = template.replace("{3}8s", "%-8s");
      template = MessageFormat.format(template, cost) + resultMessage;

      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb, Locale.US);
      formatter.format(template, slots[0].getDescription(), slots[1].getDescription(), slots[2].getDescription());
      player.print(sb.toString());

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHSLT.getMessage(),
            player.getName());
      player.getRoom().print(player, messageToRoom, false);

      return true;
   }

   /**
    * Gets the global winnings.
    * @return the global winnings.
    */
   public static int getGlobalWinnings() {
      return _globalWinnings;
   }

   /**
    * Gets the result of slots.
    * @param player the player playing.
    * @param slots the array of slots.
    * @return the result string.
    */
   private static String getResult(Player player, Slots[] slots) {
      String resultMessage = TaMessageManager.GAMLOS.getMessage();
      int winAmount = 0;
      if (isLemon(slots)) {
         resultMessage = TaMessageManager.GAMLOS.getMessage();
      } else if (isJackpot(slots)) {
         winAmount = JACKPOT;
         resultMessage = TaMessageManager.JACKPOT.getMessage();
      } else if (isTier1(slots)) {
         winAmount = TIER1;
         resultMessage = MessageFormat.format(TaMessageManager.GAMWIN.getMessage(), winAmount);
      } else if (isTier2(slots)) {
         winAmount = TIER2;
         resultMessage = MessageFormat.format(TaMessageManager.GAMWIN.getMessage(), winAmount);
      } else if (isTier4(slots)) {
         winAmount = TIER4;
         resultMessage = MessageFormat.format(TaMessageManager.GAMWIN.getMessage(), winAmount);
      } else if (isTier3(slots)) {
         winAmount = TIER3;
         resultMessage = MessageFormat.format(TaMessageManager.GAMWIN.getMessage(), winAmount);
      }
      player.addGold(winAmount);

      _globalWinnings += winAmount;

      return resultMessage;
   }

   /**
    * True if any of the slots contains a lemon.
    *
    * @param slots Array of slots.
    *
    * @return true if slots contains a lemon, false otherwise.
    */
   private static boolean isLemon(Slots[] slots) {
      for (int index = 0; index < slots.length; index++) {
         if (slots[index].equals(Slots.LEMON)) {
            return true;
         }
      }

      return false;
   }

   /**
    * Jackpot -- Crown in all three slots.
    *
    * @param slots Array of slots.
    *
    * @return true if win, false of lost.
    */
   private static boolean isJackpot(Slots[] slots) {
      for (int index = 0; index < slots.length; index++) {
         if (!slots[index].equals(Slots.CROWN)) {
            return false;
         }
      }

      return true;
   }

   /**
    * Crown in the first 2 slots.
    *
    * @param slots Array of slots.
    *
    * @return true if win, false of lost.
    */
   private static boolean isTier1(Slots[] slots) {
      if (slots[0].equals(Slots.CROWN) && slots[1].equals(Slots.CROWN)) {
         return true;
      }

      return false;
   }

   /**
    * Win if
    * Crown in [2,3]
    * Crown in [1,3]
    * else lose.
    *
    * @param slots Array of slots.
    *
    * @return true if win, false of lost.
    */
   private static boolean isTier2(Slots[] slots) {
      if (!slots[0].equals(Slots.CROWN) && slots[1].equals(Slots.CROWN) && slots[2].equals(Slots.CROWN)) {
         return true;
      } else if (slots[0].equals(Slots.CROWN) && !slots[1].equals(Slots.CROWN) && slots[2].equals(Slots.CROWN)) {
         return true;
      }

      return false;
   }

   /**
    * Two of the same fruits in any slot.
    * Crown in [2]
    * Crown in [3]
    *
    * @param slots Array of slots.
    *
    * @return true if win, false of lost.
    */
   private static boolean isTier3(Slots[] slots) {
      if (slots[1].equals(Slots.CROWN)) {
         return true;
      }

      if (slots[2].equals(Slots.CROWN)) {
         return true;
      }

      TreeSet<Slots> list = new TreeSet<Slots>();
      for (int index = 0; index < slots.length; index++) {
         if (!list.add(slots[0])) {
            // list already contains the item, so there must have been two.
            return true;
         }
      }

      return false;
   }

   /**
    * Two Apples in any slot is a win.
    *
    * @param slots Array of slots.
    *
    * @return true if win, false of lost.
    */
   private static boolean isTier4(Slots[] slots) {
      int appleCount = 0;
      for (int index = 0; index < slots.length; index++) {
         if (slots[index].equals(Slots.APPLE)) {
            appleCount++;
         }
      }

      if (appleCount == 2) {
         return true;
      }

      return false;
   }
}
