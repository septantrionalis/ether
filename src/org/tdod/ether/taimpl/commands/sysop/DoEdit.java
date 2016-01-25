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

package org.tdod.ether.taimpl.commands.sysop;

import org.tdod.ether.ta.commands.SysopCommand;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.Rune;
import org.tdod.ether.util.MessageManager;

/**
 * The sysop edit command.
 * @author rkinney
 */
public class DoEdit extends SysopCommand {

   // Stats from stock TA.
   private static final String EDIT_EXP       = "experience";
   private static final String EDIT_GOLD      = "gold";
   private static final String EDIT_INTELLECT = "intellect";
   private static final String EDIT_KNOWLEDGE = "knowledge";
   private static final String EDIT_PHYSIQUE  = "physique";
   private static final String EDIT_STAMINA   = "stamina";
   private static final String EDIT_AGILITY   = "agility";
   private static final String EDIT_CHARISMA  = "charisma";
   private static final String EDIT_LEVEL     = "level";
   private static final String EDIT_RUNE      = "rune";
   private static final String EDIT_HITS      = "hits";
   private static final String EDIT_MAX_MANA  = "max_mana";
   private static final String EDIT_CUR_MANA  = "cur_mana";
   private static final String EDIT_MAX_VIT   = "max_vit";
   private static final String EDIT_CUR_VIT   = "cur_vit";
   private static final String EDIT_THIRST    = "thirst";
   private static final String EDIT_HUNGER    = "hunger";
   
   // Stats added from the Java port.
   private static final String EDIT_PROMOT    = "promot";

   /**
    * Executes the sysop command.
    * @param player the player issuing the command.
    * @param input the player input.
    * @return true if the command was successful.
    */
   public boolean executeSysopCommand(Player player, String input) {
      String[] split = input.split(" ");

      Player target = WorldManager.getPlayers().get(Integer.valueOf(split[1]))
            .getPlayer();
      if (target == null) {
         player.println("Player not found.");
         return true;
      }

      if (split[2].equals(EDIT_EXP)) {
         long value = Integer.valueOf(split[3]);
         target.setExperience(value);
      } else if (split[2].equals(EDIT_GOLD)) {
         int value = Integer.valueOf(split[3]);
         target.setGold(value);
      } else if (split[2].equals(EDIT_INTELLECT)) {
         int value = Integer.valueOf(split[3]);
         target.getStats().getIntellect().setValue(value);
      } else if (split[2].equals(EDIT_KNOWLEDGE)) {
         int value = Integer.valueOf(split[3]);
         target.getStats().getKnowledge().setValue(value);
      } else if (split[2].equals(EDIT_PHYSIQUE)) {
         int value = Integer.valueOf(split[3]);
         target.getStats().getPhysique().setValue(value);
      } else if (split[2].equals(EDIT_STAMINA)) {
         int value = Integer.valueOf(split[3]);
         target.getStats().getStamina().setValue(value);
      } else if (split[2].equals(EDIT_AGILITY)) {
         int value = Integer.valueOf(split[3]);
         target.getStats().getAgility().setValue(value);
      } else if (split[2].equals(EDIT_CHARISMA)) {
         int value = Integer.valueOf(split[3]);
         target.getStats().getCharisma().setValue(value);
      } else if (split[2].equals(EDIT_LEVEL)) {
         int value = Integer.valueOf(split[3]);
         target.setLevel(value);
      } else if (split[2].equals(EDIT_RUNE)) {
         int value = Integer.valueOf(split[3]);
         setRune(player, value);
      } else if (split[2].equals(EDIT_HITS)) {
         int value = Integer.valueOf(split[3]);
         target.getVitality().setCurVitality(value);
         target.getVitality().setMaxVitality(value);
      } else if (split[2].equals(EDIT_PROMOT)) {
         int value = Integer.valueOf(split[3]);

         if (value > 0) {
            player.setPromoted(true);
         } else {
            player.setPromoted(false);
         }
      } else if (split[2].equals(EDIT_MAX_MANA)) {
         int value = Integer.valueOf(split[3]);
         target.getMana().setMaxMana(value);
      } else if (split[2].equals(EDIT_CUR_MANA)) {
         int value = Integer.valueOf(split[3]);
         target.getMana().setCurMana(value);
      } else if (split[2].equals(EDIT_MAX_VIT)) {
         int value = Integer.valueOf(split[3]);
         target.getVitality().setMaxVitality(value);
      } else if (split[2].equals(EDIT_CUR_VIT)) {
         int value = Integer.valueOf(split[3]);
         target.getVitality().setCurVitality(value);
      } else if (split[2].equals(EDIT_THIRST)) {
         int value = Integer.valueOf(split[3]);
         target.setThirstTicker(value);
      } else if (split[2].equals(EDIT_HUNGER)) {
         int value = Integer.valueOf(split[3]);
         target.setHungerTicker(value);
      }
      player.println(MessageManager.SYSOP_EDIT.getMessage());

      return true;
   }

   /**
    * Sets the players rune.
    * @param player the player who's rune will be changed.
    * @param value the run value, as an index.
    */
   private void setRune(Player player, int value) {
      player.setRune(Rune.getRune(value));
   }

   /**
    * Displays the help line.
    *
    * @param player The player issuing the sysop command.
    */
   public final void displayHelp(Player player) {
      player.println("Syntax: edit pl# [field] [value]");
   }

}
