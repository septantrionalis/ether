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

package org.tdod.ether.taimpl.commands.debug;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.taimpl.player.DefaultAgility;
import org.tdod.ether.taimpl.player.DefaultCharisma;
import org.tdod.ether.taimpl.player.DefaultIntellect;
import org.tdod.ether.taimpl.player.DefaultKnowledge;
import org.tdod.ether.taimpl.player.DefaultPhysique;
import org.tdod.ether.taimpl.player.DefaultStamina;

/**
 * Increase the players level, stats, hp, etc for testing purposes.  Once this
 * command is issued, all of the old stats are wiped.
 *
 * @author Ron Kinney
 *
 */
public class DoGodMode extends SysopCommand {

   private static final int LEVEL = 20;
   private static final int VITALITY = 40000;
   private static final int STATS = 30;
   private static final int GOLD = 1000;

   /**
    * Executes the "godmode" command.
    *
    * @param player The player issuing the command.
    * @param input The player input.
    *
    * @return true if the command can be executed.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ", THREE_PARAMS);
      if (split.length != 1) {
         return false;
      }

      int level = LEVEL;
      player.setLevel(level);
      player.setGold(GOLD);
      player.getVitality().setCurVitality(VITALITY);
      player.getVitality().setMaxVitality(VITALITY);
      player.getMana().setMaxMana(level * 2);
      player.getMana().setCurMana(level * 2);
      player.getAttacks().reset();
      player.getStats().setAgility(new DefaultAgility(STATS));
      player.getStats().setCharisma(new DefaultCharisma(STATS));
      player.getStats().setIntellect(new DefaultIntellect(STATS));
      player.getStats().setKnowledge(new DefaultKnowledge(STATS));
      player.getStats().setPhysique(new DefaultPhysique(STATS));
      player.getStats().setStamina(new DefaultStamina(STATS));

      player.println("Done.");

      return true;
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: godmode");
   }

}
