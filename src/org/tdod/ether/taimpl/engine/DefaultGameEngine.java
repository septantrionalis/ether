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

package org.tdod.ether.taimpl.engine;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.tdod.ether.factory.DefaultAppFactory;
import org.tdod.ether.ta.cosmos.Room;
import org.tdod.ether.ta.cosmos.World;
import org.tdod.ether.ta.engine.GameEngine;
import org.tdod.ether.ta.manager.WorldManager;
import org.tdod.ether.ta.player.Player;
import org.tdod.ether.ta.player.PlayerConnectedEvent;
import org.tdod.ether.ta.player.PlayerConnectedEventId;
import org.tdod.ether.ta.player.PlayerConnection;
import org.tdod.ether.ta.telnet.TelnetService;
import org.tdod.ether.taimpl.commands.DoDisband;
import org.tdod.ether.taimpl.engine.jobs.ItemEffectJob;
import org.tdod.ether.taimpl.engine.jobs.MobActivityJob;
import org.tdod.ether.taimpl.engine.jobs.OverheatingJob;
import org.tdod.ether.taimpl.engine.jobs.PlayerIdleJob;
import org.tdod.ether.taimpl.engine.jobs.PlayerSaveJob;
import org.tdod.ether.taimpl.engine.jobs.PopulateLairJob;
import org.tdod.ether.taimpl.engine.jobs.PopulateMobJob;
import org.tdod.ether.taimpl.engine.jobs.RegenerationJob;
import org.tdod.ether.taimpl.engine.jobs.RestJob;
import org.tdod.ether.taimpl.engine.jobs.SlowStatusJob;
import org.tdod.ether.taimpl.engine.jobs.StatisticsJob;
import org.tdod.ether.taimpl.engine.jobs.SustenanceJob;
import org.tdod.ether.taimpl.engine.jobs.WorldResetJob;
import org.tdod.ether.util.GameUtil;
import org.tdod.ether.util.PlayerConnectedManager;
import org.tdod.ether.util.TaMessageManager;

/**
 * The default game engine.  Most of the important game engine functionality goes here.
 *
 * @author Ron Kinney
 */
public class DefaultGameEngine implements GameEngine {

   private static Log _log = LogFactory.getLog(DefaultGameEngine.class);

   private TelnetService _telnetService = null;
   private Scheduler     _scheduler;
   private boolean       _shuttingDown = false;

   private static final String DEBUG_STRING = "|%-13s|%-14s|%-14s|%-14s|%6s|\n";

   /**
    * Creates a new DefaultGameEngine.
    */
   public DefaultGameEngine() {
   }

   /**
    * Initializes the game engine.
    *
    * @throws Exception if something goes wrong.  This is a serious error and a game shutdown
    * will usually follow if an exception occurs during initialization.
    */
   public void initialize() throws Exception {
      World world = DefaultAppFactory.createWorld();
      WorldManager.setWorld(world);
      WorldManager.setGameMechanics(DefaultAppFactory.createGameMechanics());
      world.bigBang();

      if (!_shuttingDown) {
         initializeJobs();

         _telnetService = TelnetService.createTelnetD();
         _telnetService.start();
      }
   }

   // **********
   // Methods inherited from PlayerConnectedListener.
   // **********

   /**
    * Called during a connection event.
    *
    * @param e a PlayerConnectedEvent
    */
   public void handleConnectionEvent(PlayerConnectedEvent e) {
      if (e.getEventId().equals(PlayerConnectedEventId.Connected)) {
         _log.info("Got connection from "
               + e.getShell().getConnection().getConnectionData().getHostName()
               + " "
               + e.getShell().getConnection().getConnectionData().getPort());
         PlayerConnection playerConnection = DefaultAppFactory.createDefaultPlayer(e);
         WorldManager.getPlayers().add(playerConnection);
      } else if (e.getEventId().equals(PlayerConnectedEventId.Disconnected)) {
         PlayerConnection disconnectedPlayerConn = null;

         for (PlayerConnection playerConn : WorldManager.getPlayers()) {
            if (e.getShell().getConnection().getId() == playerConn.getShell()
                  .getConnection().getId()) {
               disconnectedPlayerConn = playerConn;
               continue;
            }
         }

         if (disconnectedPlayerConn != null) {
            String message = MessageFormat.format(TaMessageManager.LEVGAM
                  .getMessage(), disconnectedPlayerConn.getPlayer().getName());
            _log.info(disconnectedPlayerConn.getPlayer().getName()
                  + " disconnected.  Cleaning resources.");
            Room room = disconnectedPlayerConn.getPlayer().getRoom();
            if (room != null) {
               room.print(disconnectedPlayerConn.getPlayer(), message, false);
            }
            cleanResources(disconnectedPlayerConn);
         }
      } else if (e.getEventId().equals(PlayerConnectedEventId.Disconnecting)) {
         ArrayList<PlayerConnection> disconnected = new ArrayList<PlayerConnection>();
         for (PlayerConnection playerConn : WorldManager.getPlayers()) {
            if (playerConn.getPlayer().isDisconnected()) {
               disconnected.add(playerConn);
            }
         }

         for (PlayerConnection playerConn : disconnected) {
            _log.info(playerConn.getPlayer().getName()
                  + " disconnecting.  Cleaning resources.");
            cleanResources(playerConn);
         }

      } else {
         _log.error("Got unknown event " + e.getDescription());
      }
   }

   /**
    * Cleans player resources when the player leaves the game.
    *
    * @param playerConn the player connection that left the game.
    */
   private void cleanResources(PlayerConnection playerConn) {
      playerConn.getPlayer().save();
      playerConn.getPlayer().cleanup();

      // Handle group related mechanics during disconnect.
      // TODO when the leader quits, does it disband and produce a message to other players?
      Player player = playerConn.getPlayer();
      player.getGroupLeader().getGroupList().remove(playerConn.getPlayer());
      if (player.getGroupLeader().equals(playerConn.getPlayer())) {
         DoDisband doDisband = new DoDisband();
         doDisband.disbandTheGroup(playerConn.getPlayer(), false);
      }

      playerConn.cleanup();
      playerConn = null;
   }

   /**
    * Gets the status of the quartz jobs.
    *
    * @return a string representation of the quartz job status.
    */
   public String getJobStatus() {
      StringBuffer buffer = new StringBuffer();
      try {

         String split = "+-------------+--------------+--------------+--------------+------+\n";
         buffer.append(split);
         StringBuffer sb = new StringBuffer();
         Formatter formatter = new Formatter(sb, Locale.US);
         formatter.format(DEBUG_STRING, "Trigger", "Final", "Next", "Previous", "Repeat");
         buffer.append(sb.toString());
         buffer.append(split);
         for (String triggerName : _scheduler.getTriggerNames(Scheduler.DEFAULT_GROUP)) {
            sb = new StringBuffer();
            formatter = new Formatter(sb, Locale.US);
            Trigger trigger = (Trigger) _scheduler.getTrigger(triggerName, Scheduler.DEFAULT_GROUP);

            formatter.format(DEBUG_STRING,
                  triggerName,
                  getDateString(trigger.getFinalFireTime()),
                  getDateString(trigger.getNextFireTime()),
                  getDateString(trigger.getPreviousFireTime()),
                  trigger.mayFireAgain()
            );
            buffer.append(sb.toString());
         }
         buffer.append(split);
      } catch (Exception e) {
         e.printStackTrace();
         _log.error(e);
      }
      return buffer.toString();
   }

   /**
    * Given a Date object, return a string representation of it.
    *
    * @param date The Date object to turn into a string.
    *
    * @return a string representation of the Date object.
    */
   private String getDateString(Date date) {
      try {
         if (date == null) {
            return "null";
         } else {
            DateFormat df = new SimpleDateFormat("MM.dd HH:mm:ss");
            return df.format(date);
         }
      } catch (Exception e) {
         return "error";
      }
   }

   /**
    * Initializes all game related quartz jobs.
    *
    * @throws Exception if something goes wrong with initialization.  If a job doesn't start,
    * that's usually grounds for game shutdown.
    */
   private void initializeJobs() throws Exception {
      _scheduler = StdSchedulerFactory.getDefaultScheduler();
      _scheduler.start();

      JobDetail slowStatusJobDetail = new JobDetail("Slow Status Job", Scheduler.DEFAULT_GROUP, SlowStatusJob.class);
      _scheduler.scheduleJob(slowStatusJobDetail, SlowStatusJob.getTrigger());

      JobDetail sustenanceJobDetail = new JobDetail("Sustenance Job", Scheduler.DEFAULT_GROUP, SustenanceJob.class);
      _scheduler.scheduleJob(sustenanceJobDetail, SustenanceJob.getTrigger());

      JobDetail restJobDetail = new JobDetail("Rest Job", Scheduler.DEFAULT_GROUP, RestJob.class);
      _scheduler.scheduleJob(restJobDetail, RestJob.getTrigger());

      JobDetail mobActivityJobDetail = new JobDetail("Mob Activity Job", Scheduler.DEFAULT_GROUP, MobActivityJob.class);
      _scheduler.scheduleJob(mobActivityJobDetail, MobActivityJob.getTrigger());

      JobDetail regenerationJobDetail = new JobDetail("Regeneration Job", Scheduler.DEFAULT_GROUP, RegenerationJob.class);
      _scheduler.scheduleJob(regenerationJobDetail, RegenerationJob.getTrigger());

      JobDetail itemEffectJobDetail = new JobDetail("Item Effect Job", Scheduler.DEFAULT_GROUP, ItemEffectJob.class);
      _scheduler.scheduleJob(itemEffectJobDetail, ItemEffectJob.getTrigger());

      JobDetail populateLairJobDetail = new JobDetail("Populate Lair Job", Scheduler.DEFAULT_GROUP, PopulateLairJob.class);
      _scheduler.scheduleJob(populateLairJobDetail, PopulateLairJob.getTrigger());

      JobDetail populateMobJobDetail = new JobDetail("Populate Mob Job", Scheduler.DEFAULT_GROUP, PopulateMobJob.class);
      _scheduler.scheduleJob(populateMobJobDetail, PopulateMobJob.getTrigger());

      JobDetail playerSaveJobDetail = new JobDetail("Player Save Job", Scheduler.DEFAULT_GROUP, PlayerSaveJob.class);
      _scheduler.scheduleJob(playerSaveJobDetail, PlayerSaveJob.getTrigger());

      JobDetail statisticsJobDetail = new JobDetail("Statistics Job", Scheduler.DEFAULT_GROUP, StatisticsJob.class);
      _scheduler.scheduleJob(statisticsJobDetail, StatisticsJob.getTrigger());

      JobDetail overheatingJobDetail = new JobDetail("Overheating Job", Scheduler.DEFAULT_GROUP, OverheatingJob.class);
      _scheduler.scheduleJob(overheatingJobDetail, OverheatingJob.getTrigger());

      JobDetail worldResetJobDetail = new JobDetail("World Reset Job", Scheduler.DEFAULT_GROUP, WorldResetJob.class);
      _scheduler.scheduleJob(worldResetJobDetail, WorldResetJob.getTrigger());

      JobDetail playerIdleJob = new JobDetail("Player Idle Job", Scheduler.DEFAULT_GROUP, PlayerIdleJob.class);
      _scheduler.scheduleJob(playerIdleJob, PlayerIdleJob.getTrigger());

   }

   /**
    * Shuts down the game engine.
    */
   public void shutdown() {
      _log.info("Bye...");

      for (PlayerConnection playerConn : WorldManager.getPlayers()) {
         Player player = playerConn.getPlayer();
         player.save();
         player.print(WorldManager.getHelp(GameUtil.EXIT_HELP_KEYWORD));
         player.setDisconnected(true);

      }
      PlayerConnectedManager.postPlayerConnectedEvent(PlayerConnectedEventId.Disconnecting, null);

      _shuttingDown = true;
      _telnetService.stop();

      try {
         _scheduler.shutdown();
      } catch (SchedulerException e) {
         _log.error(e);
      }
   }
}
