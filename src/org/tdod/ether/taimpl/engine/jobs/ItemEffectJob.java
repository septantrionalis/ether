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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.tdod.ether.ta.items.Item;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.player.enums.PlayerStateEnum;
import org.tdod.ether.util.PropertiesManager;

/**
 * This job handles any item effects for a player.
 * @author rkinney
 */
public class ItemEffectJob implements Job {

   private static Log _log = LogFactory.getLog(ItemEffectJob.class);

   private static final int TIMER_WAKEUP = new Integer(
         PropertiesManager.getInstance().getProperty(PropertiesManager.ITEM_TIMER_WAKEUP)).intValue();

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

            ArrayList<Item> expiredItems = new ArrayList<Item>();
            for (Item item : player.getInventory()) {
               if (item.isActivated()) {
                  if (item.decreaseTimer(TIMER_WAKEUP)) {
                     expiredItems.add(item);
                  }
               }
            }

            for (Item item : expiredItems) {
               player.removeItemFromInventory(item);
               item.handleExpiredTimer(player, true);
               item.destroy();
               item = null;
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
      SimpleTrigger trigger = new SimpleTrigger("Item Effect",
            null,
            new Date(),
            null,
            SimpleTrigger.REPEAT_INDEFINITELY,
            TIMER_WAKEUP * 1000L);

      return trigger;
   }

}
