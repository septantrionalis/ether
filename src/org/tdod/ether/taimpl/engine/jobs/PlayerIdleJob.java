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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;

/**
 * Disconnects any idle players.
 * @author rkinney
 */
public class PlayerIdleJob implements Job {

   private static Log _log = LogFactory.getLog(PlayerIdleJob.class);

   private static final int IDLE_JOB_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.IDLE_JOB_WAKEUP)).intValue();
   private static final int IDLE_DISCONNECT_TIME = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.IDLE_DISCONNECT_TIME)).intValue();

   /**
    * Runs the job.
    * @param context the JobExecutionContext.
    */
   public void execute(JobExecutionContext context) {
      List<PlayerConnection> playersToDisconnect = new ArrayList<PlayerConnection>();
      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         try {
            long lastActivity = playerConn.getShell().getConnection().getConnectionData().getLastActivity();
            long currentTime = System.currentTimeMillis();
            long secondsIdle = (currentTime - lastActivity) / GameUtil.TIME_DIVISION;

            if (secondsIdle >= IDLE_DISCONNECT_TIME) {
               playersToDisconnect.add(playerConn);
            }
         } catch (Exception e) {
            _log.error(e);
         }
      }

      for (PlayerConnection playerConn : playersToDisconnect) {
         try {
            GameUtil.disconnectPlayer(playerConn.getPlayer());
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
      SimpleTrigger trigger = new SimpleTrigger("Player Idle",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            IDLE_JOB_WAKEUP * 1000L);

      return trigger;
   }

}
