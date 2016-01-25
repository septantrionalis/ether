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

import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles the shop spell command.
 * @author rkinney
 */
public final class HandleSpellShop {

   /**
    * The max number of spells a player can have scribed.
    */
   public static final int MAX_SPELLS = new Integer(PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_SPELLS));

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleSpellShop() {
   }

   /**
    * &MSorry, the guild doesn't offer that spell for sale.
    * &MSorry, warriors may not buy or cast spells.
    * &MSorry, sorcerors may not cast that spell.
    * &MYou already have that spell.
    * &MSorry, that spell is too powerful for you to control.
    * &MYou can't afford that spell.
    * @param player the player issuing the command.
    * @param spellName the spell name.
    * @return true if the command was successful.
    */
   public static boolean execute(Player player, String spellName) {
      Room room = player.getRoom();

      Spell spell = WorldManager.getSpell(spellName);

      // The spell does not exist.
      if (spell == null) {
         player.print(TaMessageManager.NOSSPL.getMessage());
         return true;
      }

      // Non-spellcasters can't buy spells.
      if ((player.getPlayerClass().equals(PlayerClass.ARCHER))
            || (player.getPlayerClass().equals(PlayerClass.HUNTER))
            || (player.getPlayerClass().equals(PlayerClass.ROGUE))
            || (player.getPlayerClass().equals(PlayerClass.WARRIOR))) {

         String messageToPlayer = MessageFormat.format(
               TaMessageManager.WARNSP.getMessage(), player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // Spell is of another practice.
      if (!spell.getSpellClass().equals(player.getPlayerClass())) {
         String messageToPlayer = MessageFormat.format(
               TaMessageManager.OUTRLM.getMessage(), player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      // Player already has the spell.
      if (player.getSpellbook().getSpells().contains(spell)) {
         String messageToPlayer = MessageFormat.format(
               TaMessageManager.ALRHVS.getMessage(), player.getPlayerClass().getName().toLowerCase());
         player.print(messageToPlayer);

         return true;
      }

      // The spell is too high of a level.
      if (player.getLevel() < spell.getLevel()) {
         player.print(TaMessageManager.TOOBIG.getMessage());
         return true;
      }

      // Player's spellbook cannot hold anymore.
      if (player.getSpellbook().getSpells().size() >= MAX_SPELLS) {
         player.print(TaMessageManager.BOKFUL.getMessage());
         return true;
      }

      // Modify final cost of the spell
      int variance = (Dice.roll(0, 20) - 10); // -10% to 10%
      float percentMarkup = (player.getStats().getCharisma().getBuyModifier() + variance) / 100f;
      float modCostFloat = (spell.getCost() * percentMarkup) + spell.getCost();
      int modCost = (int) modCostFloat;

      // Sanity check... don't want a 0 cost item.
      if (modCost < 1) {
         modCost = 1;
      }

      // Player does not have enough gold.
      if (modCost > player.getGold()) {
         player.print(TaMessageManager.CNTAFS.getMessage());
         return true;
      }

      // Finally!
      player.subtractGold(modCost);
      String messageToPlayer = MessageFormat.format(TaMessageManager.YOUGOT.getMessage(), spell.getName(), modCost);
      player.print(messageToPlayer);
      String messageToRoom = MessageFormat.format(TaMessageManager.BYSOTH.getMessage(),
            player.getName(), spell.getName());
      room.print(player, messageToRoom, false);
      player.getSpellbook().scribeSpell(spell);

      return true;
   }
}
