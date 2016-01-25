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
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.TaMessageManager;

/**
 * I've noticed that the shortcut, D, never actually worked in the original version.
 * D defaulted to down.
 *
 * &MSorry, that is not an appropriate command.                *
 * &MSorry, but you don't have that much gold.                 *
 *
 * donate a gold  :
 * donate -1 gold : &WDo you wish to anger the gods?           *
 *
 * &W%s just donated some gold coins to the temple priests.
 * &WThe Gods seem pleased by your donation...
 * &WThe priests gladly accept your donation.
 *
 * donate 1 gold
 * The priests gladly accept your donation.
 * donate 1 g
 * The priests gladly accept your donation.
 */
public class DoDonate extends Command {

   /**
    * Long suffix for gold keyword.
    */
   private static final String GOLD_KEYWORD_1 = "gold";

   /**
    * Short suffix for gold keyword.
    */
   private static final String GOLD_KEYWORD_2 = "g";

   /**
    * Executes the "donate" command.
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

      // Can only donate at the temple.
      if (!RoomFlags.TEMPLE.isSet(room.getRoomFlags())) {
         return false;
      }

      // Check if the player input three keywords.
      String[] split = input.split(" ");
      if (split.length != THREE_PARAMS) {
         return false;
      }

      // check if the 3rd keyword is "gold" or "g".
      if (!split[2].toLowerCase().equals(GOLD_KEYWORD_1) && !split[2].toLowerCase().equals(GOLD_KEYWORD_2)) {
         return false;
      }

      // Get the amount to donate.
      int donation;
      try {
         donation = Integer.valueOf(split[1]);
      } catch (NumberFormatException e) {
         player.print(TaMessageManager.DPLGDS.getMessage());
         return true;
      }

      // Check the donation value.
      if (donation <= 0) {
         player.print(TaMessageManager.DPLGDS.getMessage());
         return true;
      }

      // Check if the player has enough gold.
      if (donation > player.getGold()) {
         player.print(TaMessageManager.DNTHVG.getMessage());
         return true;
      }

      // Send the basic messages to the player.
      player.subtractGold(donation);
      player.print(TaMessageManager.YOUDON.getMessage());
      String messageToRoom = MessageFormat.format(TaMessageManager.JSTDON.getMessage(), player.getName());
      room.print(player, messageToRoom, false);

      // Check if the player pleased the gods.
      if (WorldManager.getGameMechanics().donate(player, donation)) {
         player.print(TaMessageManager.PLSGDS.getMessage());
         WorldManager.getGameMechanics().handlePleasedGods(player);
      }
      return true;
   }

}
