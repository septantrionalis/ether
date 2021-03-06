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
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.player.enums.SpellbookFailCode;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.util.TaMessageManager;

/**
 * &MSorry, warriors do not have spellbooks.
 * &MSorry, you know no such spell.
 * &WYou purged komiza from your spellbook.
 * &WLimech just purged a spell from his spellbook.
 */
public class DoPurge extends Command {

   /**
    * Executes the "purge" command.
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

      String[] parsed = input.toLowerCase().split(" ", 2);

      if (parsed.length < 2) {
         return false;
      }

      // Non-spellcasters do not have spellbooks.
      if ((player.getPlayerClass().equals(PlayerClass.ARCHER))
            || (player.getPlayerClass().equals(PlayerClass.HUNTER))
            || (player.getPlayerClass().equals(PlayerClass.ROGUE))
            || (player.getPlayerClass().equals(PlayerClass.WARRIOR))) {

         String messageToPlayer =
            MessageFormat.format(TaMessageManager.WARNBK.getMessage(), player.getPlayerClass().getName().toLowerCase() + "s");
         player.print(messageToPlayer);
         return true;
      }

      String spellName = parsed[1];
      Spell spell = WorldManager.getSpell(spellName);

      // Spell doesn't exist.
      if (spell == null) {
         player.print(TaMessageManager.NOSPEL.getMessage());
         return true;
      }

      SpellbookFailCode code = player.getSpellbook().purgeSpell(spell);

      if (code.equals(SpellbookFailCode.NOT_FOUND)) {
         // Player did not have the spell.
         player.print(TaMessageManager.NOSPEL.getMessage());
         return true;
      } else {
         // Purge the spell.
         String messageToPlayer = MessageFormat.format(TaMessageManager.PRGSPL.getMessage(), spell.getName());
         player.print(messageToPlayer);
         String messageToRoom = MessageFormat.format(TaMessageManager.PRGOTH.getMessage(),
               player.getName(), player.getGender().getPronoun().toLowerCase());
         room.print(player, messageToRoom, false);

      }

      return true;
   }

}
