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
import org.tdod.ether.taimpl.mobs.enums.Terrain;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * For places with the overheating flag, the character will take damage when this job wakes up.
 * @author rkinney
 */
public class OverheatingJob implements Job {

   private static Log _log = LogFactory.getLog(OverheatingJob.class);

   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.OVERHEATING_TIMER_WAKEUP)).intValue();
   private static final int MIN_DAMAGE = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.OVERHEATING_MIN_DAMAGE)).intValue();
   private static final int MAX_DAMAGE = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.OVERHEATING_MAX_DAMAGE)).intValue();

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

            if (playerConn.getPlayer().getRoom().getTerrain().equals(Terrain.FIRE2)) {
               playerConn.getPlayer().print(TaMessageManager.HOTENV.getMessage());
               int damage = Dice.roll(MIN_DAMAGE, MAX_DAMAGE);
               takeDamage(playerConn.getPlayer(), damage);
            }

         } catch (Exception e) {
            _log.error(e);
         }
      }
   }

   /**
    * Gets the job trigger.
    * @return the job trigger.
    */
   public static Trigger getTrigger() {
      SimpleTrigger trigger = new SimpleTrigger("Overheating",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            TIMER_WAKEUP * 1000L);

      return trigger;
   }

   /**
    * The player takes damage.
    * @param player the player taking the damage.
    * @param damage the amount of damage taken.
    */
   private void takeDamage(Player player, int damage) {
      String playerDeathMsg = TaMessageManager.YOUDED1.getMessage();
      if (player.takeDamage(damage)) {
         WorldManager.getGameMechanics().handlePlayerDeath(player, playerDeathMsg);
      }
   }

}
