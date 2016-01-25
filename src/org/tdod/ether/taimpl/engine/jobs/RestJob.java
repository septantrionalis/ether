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
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.util.PropertiesManager;

/**
 * Handles a players resting state -- both mental and physical.
 * @author rkinney
 */
public class RestJob implements Job {

   private static Log _log = LogFactory.getLog(RestJob.class);

   // Decrease timer to update more often.  May impact performance though.
   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.REST_TIMER_WAKEUP)).intValue();

   /**
    * This job is called very often, so don't put much into it!
    * @param context the JobExecutionContext.
    */
   public void execute(JobExecutionContext context) {
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         try {
            if (!playerConn.getPlayer().getState().equals(
                  PlayerStateEnum.PLAYING)) {
               continue;
            }
            playerConn.getPlayer().decreaseRestTicker();
            playerConn.getPlayer().decreaseCombatTicker();
            playerConn.getPlayer().decreaseMentalExhaustionTicker();
            if (playerConn.getPlayer().getAttacks().getAttacksLeft() == 0
                  && !playerConn.getPlayer().isResting()) {
               playerConn.getPlayer().getAttacks().reset();
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
      SimpleTrigger trigger = new SimpleTrigger("Rest",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            TIMER_WAKEUP * 1000L);

      return trigger;
   }

}
