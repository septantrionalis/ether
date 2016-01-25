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
import java.util.HashMap;

import org.tdod.ether.ta.player.Player;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the bones command.
 * @author rkinney
 */
public final class HandleBones {

   private static final int BONES_COST      = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.BONES_COST));
   private static final int LIMIT           = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.GAMES_OF_CHANCE_LIMIT));
   private static final int LOW_WINNINGS    = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.BONES_LOW_WINNINGS));
   private static final int HIGH_WINNINGS   = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.BONES_HIGH_WINNINGS));

   private static final int LOW_WIN_INDEX       = 4;
   private static final int HIGH_WIN_INDEX      = 7;

   private static int _globalWinnings = 0;

   private static final HashMap<Integer, String> _map = new HashMap<Integer, String>();

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleBones() {
   }

   /**
    * Executes this command.
    * @param player the player that executed the command.
    * @param parameter any parameters passed in by the player.
    * @return true if the command was successful.
    */
   public static boolean execute(Player player, String parameter) {
      initialize();

      String messageToPlayer;

      int cost = BONES_COST;

      if (cost > player.getGold()) {
         messageToPlayer = MessageFormat.format(TaMessageManager.DICLOW.getMessage(), BONES_COST);
         player.print(messageToPlayer);
         return true;
      }

      if (player.getGameOfChanceCount() >= LIMIT) {
         player.println(TaMessageManager.SLTOUT.getMessage());
         return true;
      }

      player.setGameOfChanceCount(player.getGameOfChanceCount() + 1);

      player.subtractGold(cost);
      _globalWinnings -= cost;

      String resultMessage = getResult(player);

      String costString = MessageFormat.format(TaMessageManager.ROLDIC.getMessage() + resultMessage, cost);
      player.print(costString);

      String messageToRoom = MessageFormat.format(TaMessageManager.OTHDIC.getMessage(),
            player.getName());
      player.getRoom().print(player, messageToRoom, false);

      return true;
   }

   /**
    * Gets the win/loss result.
    * @param player the player that issued the command.
    * @return the result string to display to the player.
    */
   private static String getResult(Player player) {
      int winAmount = 0;

      StringBuffer buffer = new StringBuffer();
      int pairIndex = Dice.roll(0, _map.size() - 1);
      buffer.append(_map.get(pairIndex));

      if (pairIndex == LOW_WIN_INDEX) {
         winAmount = LOW_WINNINGS;
         buffer.append(MessageFormat.format(TaMessageManager.GAMWIN.getMessage(), winAmount));
      } else if (pairIndex == HIGH_WIN_INDEX) {
         winAmount = HIGH_WINNINGS;
         buffer.append(MessageFormat.format(TaMessageManager.GAMWIN.getMessage(), winAmount));
      } else {
         buffer.append(TaMessageManager.GAMLOS.getMessage());
      }

      player.addGold(winAmount);

      _globalWinnings += winAmount;

      return buffer.toString();
   }

   /**
    * Gets the global winnings.
    * @return the global winnings.
    */
   public static int getGlobalWinnings() {
      return _globalWinnings;
   }

   /**
    * Initializes the map.
    */
   private static void initialize() {
      synchronized (_map) {
         if (_map.isEmpty()) {
            _map.put(Integer.valueOf(0), TaMessageManager.DICE00.getMessage());
            _map.put(Integer.valueOf(1), TaMessageManager.DICE01.getMessage());
            _map.put(Integer.valueOf(2), TaMessageManager.DICE02.getMessage());
            _map.put(Integer.valueOf(3), TaMessageManager.DICE03.getMessage());
            _map.put(Integer.valueOf(4), TaMessageManager.DICE04.getMessage());
            _map.put(Integer.valueOf(5), TaMessageManager.DICE05.getMessage());
            _map.put(Integer.valueOf(6), TaMessageManager.DICE06.getMessage());
            _map.put(Integer.valueOf(7), TaMessageManager.DICE07.getMessage());
            _map.put(Integer.valueOf(8), TaMessageManager.DICE08.getMessage());
            _map.put(Integer.valueOf(9), TaMessageManager.DICE09.getMessage());
            _map.put(Integer.valueOf(10), TaMessageManager.DICE10.getMessage());
         }
      }
   }
}
