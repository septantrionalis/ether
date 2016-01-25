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
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerClassData;
import org.tdod.ether.ta.player.RaceData;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.MessageManager;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * &MYou are not ready for any further training, you must first prove
 * your skills in combat and gain more experience.
 *
 * &WAfter a rigorous mental and physical training session, you managed to blend
 * your personal experience and the new knowledge imparted to you by the guild
 * masters into a greater level of personal power!
 *
 * &WMinex is lead into another room by one of the guild's high ranking
 * members, and returns with a look of greater confidence.
 *
 * &MYou can't afford training session.
 *
 * 2=10
 * 3=15
 * 4=20
 * 5=25
 * 6=30
 * 7=35
 * 8=40
 * 9=45
 * 13=65
 * 20=100
 *
 * 25% chance stat increase
 */
public final class HandleTraining {

   private static final int TRAINING_COST_MODIFIER = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.LEVEL_TRAINING_COST));
   private static final int STAT_INCREASE_CHANCE = Integer.valueOf(
         PropertiesManager.getInstance().getProperty(PropertiesManager.STAT_INCREASE_CHANCE));

   /**
    * Private constructor to enforce the singleton pattern.
    */
   private HandleTraining() {
   }

   /**
    * Executes the training command.
    * @param player the player who issued the command.
    * @return true if the command was successful.
    */
   public static boolean execute(Player player) {
      int trainingCost = getTrainingCost(player.getLevel());
      if (trainingCost > player.getGold()) {
         String messageToPlayer = MessageFormat.format(TaMessageManager.CNTAFD.getMessage(),
               MessageManager.TRAINING_SESSION_STRING.getMessage());
         player.print(messageToPlayer);
         return true;
      }

      if (player.getPromotedClass().isMaxLevel(player.getPromotedLevel())) {
          player.print(TaMessageManager.NOTRDY.getMessage());
          return true;    	  
      }

      int expRequirement = player.getPromotedClass().getExpRequirement(player.getPromotedLevel() + 1, player.isPromoted());
      if (player.getExperience() < expRequirement) {
         player.print(TaMessageManager.NOTRDY.getMessage());
         return true;
      }
      
      // Your mind and body must be whole and untainted before you may train.
      if (GameUtil.isEntityDrained(player)) {
         player.print(TaMessageManager.NEDRST.getMessage());
         return true;
      }

      player.subtractGold(trainingCost);
      player.setLevel(player.getLevel() + 1);
      increaseVitality(player);
      increaseStat(player);
      increaseMana(player);

      player.print(TaMessageManager.GNDLEV.getMessage());

      String messageToRoom = MessageFormat.format(TaMessageManager.GOTTRN.getMessage(), player.getName());
      player.getRoom().print(player, messageToRoom, false);

      return true;
   }

   /**
    * Gets the training cost.
    * @param currentLevel the current level of the player.
    * @return the cost to train.
    */
   private static int getTrainingCost(int currentLevel) {
      return TRAINING_COST_MODIFIER * (currentLevel + 1);
   }

   /**
    * Increases a random stat.
    * @param player the lucky player who's stat will be raised.
    */
   private static void increaseStat(Player player) {
      int roll = Dice.roll(1, 100);
      if (roll <= STAT_INCREASE_CHANCE) {
         WorldManager.getGameMechanics().handlePleasedGods(player);
      }
   }

   /**
    * Increases the players mana.  This assumes that the player has already leveled.
    * @param player the player whos mana will increase.
    */
   private static void increaseMana(Player player) {
       player.getMana().setMaxMana(player.getLevel() * player.getPlayerClass().getManaIncreasePerLevel());
   }
   
   /**
    * Increases the players vitality.
    * @param player the player who's vitality will be increased.
    */
   public static void increaseVitality(Player player) {
      RaceData raceMod = PlayerFacade.getStartingStats().getRaceDatabase().getRaceData(player.getRace());
      PlayerClassData classMod = PlayerFacade.getStartingStats()
         .getPlayerClassDatabase().getPlayerClassData(player.getPlayerClass());

      int vitality = Dice.roll(PlayerFacade.getStartingStats().getMinVitality(),
            PlayerFacade.getStartingStats().getMaxVitality());
      vitality += raceMod.getVitality();
      vitality += classMod.getVitality();
      vitality += player.getStats().getStamina().getHpBonus();
      vitality += player.getVitality().getMaxVitality();
      player.getVitality().setCurVitality(vitality);
      player.getVitality().setMaxVitality(vitality);
   }

}
