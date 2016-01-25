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
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.cosmos.RoomFlags;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * de 10
 * You deposited 10 gold in your account.
 * &MSorry, you may only deposit or withdraw 30,000 gold per transaction.
 * &MSorry, you do not have that much gold on hand.
 * > no message to room
 */
public class DoDeposit extends Command {

   /**
    * Maximum amount of gold that the player can transfer.
    */
   private static final long MAX_VAULT_TRANSFER =
      Integer.valueOf(PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_VAULT_TRANSFER));

   /**
    * Executes the "deposit" command.
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

      String[] split = input.split(" ");
      if (split.length != 2) {
         return false;
      }

      if (!RoomFlags.VAULT.isSet(player.getRoom().getRoomFlags())) {
         return false;
      }

      int amount;

      // Sorry, you may only deposit or withdraw 30,000 gold per transaction.
      try {
         amount = Integer.valueOf(split[1]);
      } catch (NumberFormatException e) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.BNKTMC.getMessage(), MAX_VAULT_TRANSFER);
         player.print(messageToPlayer);
         return true;
      }
      if (amount < 1 || amount > MAX_VAULT_TRANSFER) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.BNKTMC.getMessage(), MAX_VAULT_TRANSFER);
         player.print(messageToPlayer);
         return true;
      }

      // Sorry, you do not have that much gold on hand.
      if (amount > player.getGold()) {
         player.print(TaMessageManager.BNKNHA.getMessage());
         return true;
      }

      player.subtractGold(amount);
      player.setVaultBalance(player.getVaultBalance() + amount);
      String messageToPlayer = MessageFormat.format(TaMessageManager.BNKDEP.getMessage(), "" + amount);
      player.print(messageToPlayer);

      return true;
   }

}
