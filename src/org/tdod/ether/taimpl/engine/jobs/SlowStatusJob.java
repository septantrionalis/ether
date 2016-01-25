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

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.tdod.ether.ta.Entity;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.mobs.Mob;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.ta.player.enums.Status;
import org.tdod.ether.util.Dice;
import org.tdod.ether.util.PropertiesManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * Handles any statuses that occur only occasionally.  Since this job isn't exected that often, we can
 * probably put a ton of shit in here.
 * @author rkinney
 */
public class SlowStatusJob implements Job {

   private static Log _log = LogFactory.getLog(SlowStatusJob.class);

   // Decrease timer to update more often.  May impact performance though.
   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(
               PropertiesManager.SLOW_STATUS_TIMER_WAKEUP)).intValue();
   private static final int MOB_REGEN = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_REGEN_PERCENT)).intValue();
   private static final int MOB_MIN_REGEN = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_MIN_REGEN)).intValue();
   private static final int MOB_MAX_REGEN = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_MAX_REGEN)).intValue();
   private static final int MOB_FLEE_RESET_CHECK = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.MOB_FLEE_RESET_CHECK)).intValue();

   /**
    * Runs the job.
    * @param context the JobExecutionContext.
    */
   public void execute(JobExecutionContext context) {
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         try {
            Player player = playerConn.getPlayer();

            if (!player.getState().equals(PlayerStateEnum.PLAYING)) {
               continue;
            }

            handleEffects(player);
            handleStatusMessages(player);
         } catch (Exception e) {
            _log.error(e);
         }
      }

      for (Mob mob : WorldManager.getMobsInExistance()) {
         try {
            handleEffects(mob);

            if (mob.getStatus().equals(Status.POISONED)) {
               mobTakeDamage(mob);
            } else if (mob.getStatus().equals(Status.PARALYSED)) {
               if (mob.decreaseParalysisTicker(TIMER_WAKEUP)) {
                  mob.setStatus(Status.HEALTHY);
               }
            } else if (Dice.roll(1, 100) < MOB_REGEN) {
               regenerateMob(mob);
            }
         } catch (Exception e) {
            _log.error(e);
         }
      }
   }

   /**
    * Displays the status message to the player.
    * @param player the player.
    * @param playerMsg the message to the player.
    * @param roomMsg the message to the room.
    */
   private void displayMessage(Player player, String playerMsg, String roomMsg) {
      Room room = player.getRoom();

      player.print(playerMsg);
      room.println(player, roomMsg, false);
   }

   /**
    * Moved here because the slow timer of sustenance checks seems to fit effects too.
    * @param entity the entity effected by the status.
    */
   private void handleEffects(Entity entity) {
      handleMagicRegeneration(entity);
      handleStatModifiers(entity);
   }

   /**
    * Handles magic regeneration.
    * @param entity the entity being affected.
    */
   private void handleMagicRegeneration(Entity entity) {
      if (entity.getRegenerationTicker() > 0) {
         if (Dice.roll(1, 20) > 15) {
            entity.reduceRegenerationTicker(1);
            entity.getVitality().addCurVitality(1);
         }
      }
   }

   /**
    * Handles stat modifiers.
    * @param entity the entity being affected.
    */
   private void handleStatModifiers(Entity entity) {
      boolean effectExpired = entity.decreaseEffectTimers();
      if (effectExpired) {
         entity.print(TaMessageManager.STA1.getMessage());
      }
   }

   /**
    * Handles status messages.
    * @param player the player who will receive the message.
    */
   private void handleStatusMessages(Player player) {
      if (player.getStatus().equals(Status.HUNGRY)) {
         String playerHungryMsg = TaMessageManager.YOUHNG.getMessage();
         String roomHungryMsg = MessageFormat.format(TaMessageManager.OTHHNG.getMessage(), player.getName());

         displayMessage(player, playerHungryMsg, roomHungryMsg);
      } else if (player.getStatus().equals(Status.THIRSTY)) {
         String playerThirstMsg = TaMessageManager.YOUTHR.getMessage();
         String roomThirstMsg = MessageFormat.format(TaMessageManager.OTHTHR.getMessage(), player.getName());

         displayMessage(player, playerThirstMsg, roomThirstMsg);
      } else if (player.getStatus().equals(Status.POISONED)) {
         String playerPoisonMsg = TaMessageManager.POISON.getMessage();
         String roomPoisonMsg = MessageFormat.format(TaMessageManager.OTHPSN.getMessage(), player.getName());

         displayMessage(player, playerPoisonMsg, roomPoisonMsg);
      }
   }

   /**
    * Mob takes damage.
    * @param mob the mob.
    */
   private void mobTakeDamage(Mob mob) {
      int damage = Dice.roll(0, mob.getPoisonDamage());
      damage *= 3; // Multiply by 3 because slow status thread is 3 times slower.
      if (mob.takeDamage(damage)) {
         WorldManager.getGameMechanics().handleMobDeath(null, mob);
      }
   }

   /**
    * Regenerate a mobs health.
    * @param mob the mob to regenerate.
    */
   private void regenerateMob(Mob mob) {
      // Don't regenerate dead mobs.
      if (mob.getVitality().getCurVitality() > 0) {
         int regeneration = Dice.roll(MOB_MIN_REGEN, MOB_MAX_REGEN);
         regeneration += mob.getRegeneration();
         mob.getVitality().addCurVitality(regeneration);

         if (mob.isFleeing()) {
            handleFleeStatus(mob);
         }
      }
   }

   /**
    * Handles a mobs fleeing status.
    * @param mob the mob that is fleeing.
    */
   private void handleFleeStatus(Mob mob) {
      if (mob.getVitality().getVitalityPercent() >= MOB_FLEE_RESET_CHECK) {
         int roll = Dice.roll(0, 100);
         if (roll >= mob.getMorale()) {
            mob.setIsFleeing(false);
         }
      }
   }

   /**
    * Gets the job trigger.
    * @return the job trigger.
    */
   public static Trigger getTrigger() {
      SimpleTrigger trigger = new SimpleTrigger("Slow Status",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            TIMER_WAKEUP * 1000L);

      return trigger;
   }
}
