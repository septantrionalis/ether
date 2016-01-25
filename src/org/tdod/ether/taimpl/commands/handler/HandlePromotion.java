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

import org.tdod.ether.ta.manager.PlayerFacade;
import org.tdod.ether.ta.player.BaseStats;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * b pro
 * &MSorry, the guild doesn't offer that spell for sale.
 *
 * b promotion
 * &MYou can't afford promotion.
 *
 * b promotion
 * &MYou are not ready for a promotion, you must first achieve greater
 * power, and gain wider knowledge of your abilities.
 *
 * &WAfter a rigorous mental and physical training session, you managed to blend
 * your personal experience and the new knowledge imparted to you by the guild
 * masters into a greater level of personal power beyond anything you have
 * previously known. The guild bestows upon you the title of arch magus!
 * room:
 * &WRon Kinney is lead into another room by one of the guild's high ranking
 * members, and returns with a look of greater confidence and power.
 * -> 1000 gold
 *
 * Warriors->Knight        +5 physique, +5 stamina, +3 agility          991 vit
 * Sorceror->Arch Magus    +5 int, +5 know, +3 agility                  469 vit
 * Acolytes->High Priest   +3 int, +3 know, +2 phy, +2 sta, +3 agi      781 vit
 * Rogues->Blackguard      +1 int, +1 know, +3 phy, +3 sta, +5 agi      681 vit
 * Hunters->Beast Master   +1 int, +1 know, +4 phy, +4 sta, +3 agi      935 vit
 * Druids->Arch Druid      +4 int, +4 know, +1 phy, +1 sta, +3 agi      585 vit
 * Archers->Master Archer  +2 int, +2 know, +3 phy, +3 sta, +3 agi      889 vit
 * Necrolytes->Necromancer +5 int, +5 know, +3 agi                      470 vit
 *
 * @author rkinney
 */
public final class HandlePromotion {

   /**
    * The minimum level to promote.
    */
   public static final int PROMOTION_LEVEL = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PROMOTION_LEVEL));

   private static final int PROMOTION_COST = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.PROMOTION_COST));

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandlePromotion() {
   }

   /**
    * Executes the promotion command.
    * @param player the player attempting to promote.
    * @return true of the command was successful.
    */
   public static boolean execute(Player player) {
      String messageToPlayer;

      // You can't afford promotion.
      if (player.getGold() < PROMOTION_COST) {
         messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(),
               MessageManager.PROMOTION_STRING.getMessage());
         player.print(messageToPlayer);
         return true;
      }

      // You are not ready for a promotion
      if (player.getLevel() < PROMOTION_LEVEL || player.isPromoted()) {
         player.print(TaMessageManager.NOTRPR.getMessage());
         return true;
      }

      // Success
      PlayerClass newPlayerClass = player.getPlayerClass().getPromotedClass();
      BaseStats statMod = PlayerFacade.getStartingStats().getPlayerClassDatabase()
         .getPlayerClassData(newPlayerClass).getStatModifiers();
      player.getStats().add(statMod);

      player.setPromoted(true);
      player.subtractGold(PROMOTION_COST);
      player.setLevel(PROMOTION_LEVEL + 1);
      player.setExperience(0);
      setVitality(player);

      // Send messages
      messageToPlayer = MessageFormat.format(TaMessageManager.GNDPRM.getMessage(), newPlayerClass.getName().toLowerCase());
      player.print(messageToPlayer);
      String messageToRoom = MessageFormat.format(TaMessageManager.GOTPRM.getMessage(), player.getName());
      player.getRoom().print(player, messageToRoom, false);

      return true;
   }

   /**
    * Resets the players vitality.  This method recalculates the players health starting from level 1.
    * @param player the player to increase vitality through promotion.
    */
   private static void setVitality(Player player) {
      player.getVitality().setCurVitality(0);
      player.getVitality().setMaxVitality(0);
      for (int count = 0; count <= PROMOTION_LEVEL; count++) {
         HandleTraining.increaseVitality(player);
      }
   }
}
