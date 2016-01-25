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

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PropertiesManager;

/**
 * Resets the entire world.  This job should not be called very often.  Once a day should be good enough.
 * @author rkinney
 */
public class WorldResetJob implements Job {

   private static Log _log = LogFactory.getLog(WorldResetJob.class);

   private static final String DEFAULT_CRON_EXPRESSION = "0 0 23 * * ?";
   private static final String CRON_EXPRESSION = PropertiesManager.getInstance().getProperty(
         PropertiesManager.WORLD_RESET_CRON);

   /**
    * Runs the job.
    * @param context the JobExecutionContext.
    */
   public void execute(JobExecutionContext context) {
      _log.info("Resetting the world.");
      GameUtil.resetWorld();
      _log.info("Populating random item drops.");
      WorldManager.populateRandomItems();
   }

   /**
    * Gets the job trigger.
    * @return the job trigger.
    */
   public static Trigger getTrigger() {
      Trigger trigger = null;
      try {
         trigger = new CronTrigger("World Reset", Scheduler.DEFAULT_GROUP, CRON_EXPRESSION);
      } catch (ParseException e) {
         _log.error("Error parsing the cron expression for the world reset job: \""
               + CRON_EXPRESSION + "\"  Using default expression.");
         try {
            trigger = new CronTrigger("World Reset", Scheduler.DEFAULT_GROUP, DEFAULT_CRON_EXPRESSION);
         } catch (ParseException e2) {
            _log.error("Well thats not good.  The default cron expression doesn't work either: \""
                  + DEFAULT_CRON_EXPRESSION + "\"  Bye.");
            WorldManager.getGameEngine().shutdown();
         }
      }

      return trigger;
   }

}
