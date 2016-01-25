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

package org.tdod.ether.taimpl.engine.jobs;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * This job deals with a players health.
 * @author rkinney
 */
public class RegenerationJob implements Job {

   private static Log _log = LogFactory.getLog(RegenerationJob.class);

   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.REGENERATION_TIMER_WAKEUP)).intValue();
   private static final int PLAYER_MANA_REGEN = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MANA_REGEN)).intValue();

   // Move the processing to outside the loop to help increase performance.
   private static final int MIN_SUSTENANCE_DAMAGE = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MIN_SUSTENANCE_DAMAGE)).intValue();
   private static final int MAX_SUSTENANCE_DAMAGE = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MAX_SUSTENANCE_DAMAGE)).intValue();

   /**
    * Runs the job.
    * @param context the JobExecutionContext.
    */
   public void execute(JobExecutionContext context) {
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         try {
            if (!playerConn.getPlayer().getState().equals(
                  PlayerStateEnum.PLAYING)) {
               continue;
            }

            regeneratePlayer(playerConn.getPlayer());
         } catch (Exception e) {
            _log.error(e);
         }

      }
   }

   /**
    * Regenerates a players health.
    * @param player the player to regenerate.
    */
   private void regeneratePlayer(Player player) {
      if (!player.getState().equals(PlayerStateEnum.PLAYING)) {
         return;
      }

      if (player.getStatus().equals(Status.HUNGRY)) {
         String playerHungerDeath = TaMessageManager.YOUDED2.getMessage();
         int damage = Dice.roll(MIN_SUSTENANCE_DAMAGE, MAX_SUSTENANCE_DAMAGE);
         takeDamage(player, playerHungerDeath, damage);
      } else if (player.getStatus().equals(Status.THIRSTY)) {
         String playerThirstDeath = TaMessageManager.YOUDED3.getMessage();
         int damage = Dice.roll(MIN_SUSTENANCE_DAMAGE, MAX_SUSTENANCE_DAMAGE);
         takeDamage(player, playerThirstDeath, damage);
      } else if (player.getStatus().equals(Status.POISONED)) {
         String playerPoisonDeath = TaMessageManager.YOUDED.getMessage();
         int damage = Dice.roll(0, player.getPoisonDamage());
         takeDamage(player, playerPoisonDeath, damage);
      } else if (player.getStatus().equals(Status.PARALYSED)) {
         if (player.decreaseParalysisTicker(TIMER_WAKEUP)) {
            player.setStatus(Status.HEALTHY);
            player.print(TaMessageManager.PAR1.getMessage());
         }
      } else if (Dice.roll(1, 100) < player.getStats().getStamina().gethpRegeneration()) {
         // Don't regenerate dead players.
         if (player.getVitality().getCurVitality() > 0) {
            player.getVitality().addCurVitality(1);
         }
      }

      if (Dice.roll(1, 100) < PLAYER_MANA_REGEN) {
         player.getMana().addCurMana(1);
      }
   }

   /**
    * The player takes damage.
    * @param player the player that is taking damage.
    * @param playerDeathMsg the death message, if applicable.
    * @param damage the amount of damage taken.
    */
   private void takeDamage(Player player, String playerDeathMsg, int damage) {
      if (player.takeDamage(damage)) {
         WorldManager.getGameMechanics().handlePlayerDeath(player, playerDeathMsg);
      }
   }

   /**
    * Gets the job trigger.
    * @return the job trigger.
    */
   public static Trigger getTrigger() {
      SimpleTrigger trigger = new SimpleTrigger("Regeneration",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            TIMER_WAKEUP * 1000L);

      return trigger;
   }

}
